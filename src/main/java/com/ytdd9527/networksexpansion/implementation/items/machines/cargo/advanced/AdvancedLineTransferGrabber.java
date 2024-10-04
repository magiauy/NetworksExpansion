package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.enums.TransportMode;
import com.ytdd9527.networksexpansion.core.items.machines.AdvancedDirectional;
import com.ytdd9527.networksexpansion.utils.DisplayGroupGenerators;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class AdvancedLineTransferGrabber extends AdvancedDirectional implements RecipeDisplayItem {
    private static final String KEY_UUID = "display-uuid";
    private static final int TRANSPORT_LIMIT = 3456;
    private static final int[] BACKGROUND_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 16, 17, 18, 19, 21, 23, 24, 25, 26, 28, 29, 21, 31, 32, 34, 35, 39, 40, 41, 42, 43, 44
    };
    private static final int TRANSPORT_MODE_SLOT = 27;
    private static final int MINUS_SLOT = 36;
    private static final int SHOW_SLOT = 37;
    private static final int ADD_SLOT = 38;
    private static final Map<Location, Integer> GRAB_TICKER_MAP = new HashMap<>();
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private int grabItemTick;
    private int maxDistance;

    public AdvancedLineTransferGrabber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String configKey) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER_GRABBER);
        loadConfigurations(configKey);
    }

    @Override
    public boolean comeMaxLimit(int currentNumber) {
        return currentNumber > TRANSPORT_LIMIT;
    }

    @Override
    public int getMaxLimit() {
        return TRANSPORT_LIMIT;
    }

    private void loadConfigurations(String configKey) {
        FileConfiguration config = Networks.getInstance().getConfig();

        int defaultMaxDistance = 64;
        int defaultGrabItemTick = 1;
        boolean defaultUseSpecialModel = false;

        this.maxDistance = config.getInt("items." + configKey + ".max-distance", defaultMaxDistance);
        this.grabItemTick = config.getInt("items." + configKey + ".grabitem-tick", defaultGrabItemTick);
        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", defaultUseSpecialModel);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("cloche", DisplayGroupGenerators::generateCloche);

        this.displayGroupGenerator = null;

        if (this.useSpecialModel) {
            String generatorKey = config.getString("items." + configKey + ".use-special-model.type");
            this.displayGroupGenerator = generatorMap.get(generatorKey);
            if (this.displayGroupGenerator == null) {
                Networks.getInstance().getLogger().warning("未知的展示组类型 '" + generatorKey + "', 特殊模型已禁用。");
                this.useSpecialModel = false;
            }
        }

    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        if (grabItemTick != 1) {
            final Location location = block.getLocation();
            int tickCounter = getTickCounter(location);
            tickCounter = (tickCounter + 1) % grabItemTick;
            if (tickCounter == 0) {
                tryGrabItem(blockMenu);
            }
            updateTickCounter(location, tickCounter);
        } else {
            tryGrabItem(blockMenu);
        }
    }

    private int getTickCounter(Location location) {
        final Integer tickCounter = GRAB_TICKER_MAP.get(location);
        if (tickCounter == null) {
            GRAB_TICKER_MAP.put(location, 0);
            return 0;
        } else {
            return tickCounter;
        }
    }

    private void updateTickCounter(Location location, int tickCounter) {
        GRAB_TICKER_MAP.put(location, tickCounter);
    }

    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final BlockFace direction = getCurrentDirection(blockMenu);
        if (direction == BlockFace.SELF) {
            return;
        }

        final NetworkRoot root = definition.getNode().getRoot();
        final int maxNumber = getCurrentNumber(blockMenu.getLocation());
        final TransportMode mode = getCurrentTransportMode(blockMenu.getLocation());

        Block currentBlock = blockMenu.getBlock().getRelative(direction);
        BlockMenu currentMenu;
        for (int i = 0; i <= maxDistance; i++) {
            currentMenu = StorageCacheUtils.getMenu(currentBlock.getLocation());

            if (currentMenu == null) {
                /*
                 * It means we found a slimefun block that has no menu, so we just continue.
                 */
                continue;
            }

            final int[] slots = currentMenu.getPreset().getSlotsAccessedByItemTransport(currentMenu, ItemTransportFlow.WITHDRAW, null);

            int limit = maxNumber;
            switch (mode) {
                case NONE, NONNULL_ONLY -> {
                    /*
                     * Grab all the items.
                     */
                    for (int slot : slots) {
                        final ItemStack item = currentMenu.getItemInSlot(slot);
                        if (item != null && !item.getType().isAir()) {
                            final int exceptedReceive = Math.min(item.getAmount(), limit);
                            final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                            root.addItemStack(clone);
                            item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                            limit -= exceptedReceive - clone.getAmount();
                            if (limit <= 0) {
                                break;
                            }
                        }
                    }
                }
                case NULL_ONLY -> {
                    /*
                     * Nothing to do.
                     */
                }
                case FIRST_ONLY -> {
                    /*
                     * Grab the first item only.
                     */
                    if (slots.length > 0) {
                        final ItemStack item = currentMenu.getItemInSlot(slots[0]);
                        if (item != null && !item.getType().isAir()) {
                            final int exceptedReceive = Math.min(item.getAmount(), limit);
                            final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                            root.addItemStack(clone);
                            item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                            limit -= exceptedReceive - clone.getAmount();
                            if (limit <= 0) {
                                break;
                            }
                        }
                    }
                }
                case LAST_ONLY -> {
                    /*
                     * Grab the last item only.
                     */
                    if (slots.length > 0) {
                        final ItemStack item = currentMenu.getItemInSlot(slots[slots.length - 1]);
                        if (item != null && !item.getType().isAir()) {
                            final int exceptedReceive = Math.min(item.getAmount(), limit);
                            final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                            root.addItemStack(clone);
                            item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                            limit -= exceptedReceive - clone.getAmount();
                            if (limit <= 0) {
                                break;
                            }
                        }
                    }
                }
                case FIRST_STOP -> {
                    /*
                     * Grab the first non-null item only.
                     */
                    for (int slot : slots) {
                        final ItemStack item = currentMenu.getItemInSlot(slot);
                        if (item != null && !item.getType().isAir()) {
                            final int exceptedReceive = Math.min(item.getAmount(), limit);
                            final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                            root.addItemStack(clone);
                            item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                            limit -= exceptedReceive - clone.getAmount();
                            break;
                        }
                    }
                }
                case LAZY -> {
                    /*
                     * When it's first item is non-null, we will grab all the items.
                     */
                    if (slots.length > 0) {
                        final ItemStack delta = currentMenu.getItemInSlot(slots[0]);
                        if (delta != null && !delta.getType().isAir()) {
                            for (int slot : slots) {
                                ItemStack item = currentMenu.getItemInSlot(slot);
                                if (item != null && !item.getType().isAir()) {
                                    final int exceptedReceive = Math.min(item.getAmount(), limit);
                                    final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                                    root.addItemStack(clone);
                                    item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                                    limit -= exceptedReceive - clone.getAmount();
                                    if (limit <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            currentBlock = currentBlock.getRelative(direction);
        }
    }


    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.LIME, 5);
    }

    @Override
    public void onPlace(BlockPlaceEvent e) {
        super.onPlace(e);
        if (useSpecialModel) {
            e.getBlock().setType(Material.BARRIER);
            setupDisplay(e.getBlock().getLocation());
        }
    }

    @Override
    public void postBreak(BlockBreakEvent e) {
        super.postBreak(e);
        Location location = e.getBlock().getLocation();
        removeDisplay(location);
        e.getBlock().setType(Material.AIR);
    }

    private void setupDisplay(@Nonnull Location location) {
        if (this.displayGroupGenerator != null) {
            DisplayGroup displayGroup = this.displayGroupGenerator.apply(location.clone().add(0.5, 0, 0.5));
            StorageCacheUtils.setData(location, KEY_UUID, displayGroup.getParentUUID().toString());
        }
    }

    private void removeDisplay(@Nonnull Location location) {
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            group.remove();
        }
    }

    @Nullable
    private UUID getDisplayGroupUUID(@Nonnull Location location) {
        String uuid = StorageCacheUtils.getData(location, KEY_UUID);
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
    }

    @Nullable
    private DisplayGroup getDisplayGroup(@Nonnull Location location) {
        UUID uuid = getDisplayGroupUUID(location);
        if (uuid == null) {
            return null;
        }
        return DisplayGroup.fromUUID(uuid);
    }

    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    protected int getMinusSlot() {
        return MINUS_SLOT;
    }

    protected int getShowSlot() {
        return SHOW_SLOT;
    }

    protected int getAddSlot() {
        return ADD_SLOT;
    }


    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aTransferring data",
                "",
                "&7[&aMaximum distance&7]&f: &6" + maxDistance + " blocks",
                "&7[&aGrab rate&7]&f:&7 Every &6" + grabItemTick + " SfTick &7Grab once"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aParameter",
                "&7Default mode: &6Unlimited",
                "&aChangable mode",
                "&7Default quantity: &63456",
                "&aChangable quantity"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aFeature",
                "",
                "&eLine grabber will grab items through",
                "&ea line of machines it facing."
        ));
        return displayRecipes;
    }

    @Override
    protected int getTransportModeSlot() {
        return TRANSPORT_MODE_SLOT;
    }
}
