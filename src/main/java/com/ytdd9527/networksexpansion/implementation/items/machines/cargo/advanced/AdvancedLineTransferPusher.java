package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.enums.TransportMode;
import com.ytdd9527.networksexpansion.core.items.machines.AdvancedDirectional;
import com.ytdd9527.networksexpansion.utils.DisplayGroupGenerators;
import com.ytdd9527.networksexpansion.utils.itemstacks.BlockMenuUtil;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.Theme;
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

public class AdvancedLineTransferPusher extends AdvancedDirectional implements RecipeDisplayItem {
    public static final CustomItemStack TEMPLATE_BACKGROUND_STACK = new CustomItemStack(
            Material.BLUE_STAINED_GLASS_PANE, Theme.PASSIVE + "Push Item Matching"
    );
    private static final String KEY_UUID = "display-uuid";
    private static final int TRANSPORT_LIMIT = 3456;
    private static final int TRANSPORT_MODE_SLOT = 27;
    private static final int MINUS_SLOT = 36;
    private static final int SHOW_SLOT = 37;
    private static final int ADD_SLOT = 38;
    private static final int[] BACKGROUND_SLOTS = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 15, 17, 18, 20, 22, 23, 27, 28, 30, 31, 33, 34, 35, 39, 40, 41, 42, 43, 44
    };
    private static final int[] TEMPLATE_BACKGROUND = new int[]{16};
    private static final int[] TEMPLATE_SLOTS = new int[]{24, 25, 26};
    private static final int NORTH_SLOT = 11;
    private static final int SOUTH_SLOT = 29;
    private static final int EAST_SLOT = 21;
    private static final int WEST_SLOT = 19;
    private static final int UP_SLOT = 14;
    private static final int DOWN_SLOT = 32;
    private static final Map<Location, Integer> PUSH_TICKER_MAP = new HashMap<>();
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private int pushItemTick;
    private int maxDistance;

    public AdvancedLineTransferPusher(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String configKey) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER_PUSHER);
        for (int slot : TEMPLATE_SLOTS) {
            this.getSlotsToDrop().add(slot);
        }
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
        int defaultMaxDistance = 64;
        int defaultPushItemTick = 1;
        boolean defaultUseSpecialModel = false;

        FileConfiguration config = Networks.getInstance().getConfig();

        this.maxDistance = config.getInt("items." + configKey + ".max-distance", defaultMaxDistance);
        this.pushItemTick = config.getInt("items." + configKey + ".pushitem-tick", defaultPushItemTick);
        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", defaultUseSpecialModel);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("cloche", DisplayGroupGenerators::generateCloche);
        generatorMap.put("cell", DisplayGroupGenerators::generateCell);

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

    private void performPushItemOperation(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            tryPushItem(blockMenu);
        }
    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        final Location location = block.getLocation();
        if (pushItemTick != 1) {
            int tickCounter = getTickCounter(location);
            tickCounter = (tickCounter + 1) % pushItemTick;
            if (tickCounter == 0) {
                performPushItemOperation(blockMenu);
            }
            updateTickCounter(location, tickCounter);
        } else {
            performPushItemOperation(blockMenu);
        }
    }

    private int getTickCounter(Location location) {
        final Integer tickCounter = PUSH_TICKER_MAP.get(location);
        if (tickCounter == null) {
            PUSH_TICKER_MAP.put(location, 0);
            return 0;
        } else {
            return tickCounter;
        }
    }

    private void updateTickCounter(Location location, int tickCounter) {
        PUSH_TICKER_MAP.put(location, tickCounter);
    }

    private void tryPushItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }
        final NetworkRoot root = definition.getNode().getRoot();
        final BlockFace direction = this.getCurrentDirection(blockMenu);
        if (direction == BlockFace.SELF) {
            return;
        }

        final TransportMode currentTransportMode = getCurrentTransportMode(blockMenu.getLocation());
        final int currentLimit = getCurrentNumber(blockMenu.getLocation());

        List<ItemStack> templates = new ArrayList<>();
        for (int slot : this.getItemSlots()) {
            final ItemStack template = blockMenu.getItemInSlot(slot);
            if (template != null && !template.getType().isAir()) {
                templates.add(StackUtils.getAsQuantity(template, 1));
            }
        }

        Block targetBlock = blockMenu.getBlock().getRelative(direction);
        BlockMenu targetMenu;
        for (int i = 0; i <= maxDistance; i++) {
            targetMenu = StorageCacheUtils.getMenu(targetBlock.getLocation());
            if (targetMenu == null) {
                break;
            }

            for (ItemStack clone : templates) {
                final ItemRequest itemRequest = new ItemRequest(clone, clone.getMaxStackSize());

                final int[] slots = targetMenu.getPreset().getSlotsAccessedByItemTransport(targetMenu, ItemTransportFlow.INSERT, clone);
                switch (currentTransportMode) {
                    case NONE -> {
                        int freeSpace = 0;
                        for (int slot : slots) {
                            final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                            if (itemStack == null || itemStack.getType().isAir()) {
                                freeSpace += clone.getMaxStackSize();
                            } else {
                                if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                                    continue;
                                }
                                if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                                    final int availableSpace = itemStack.getMaxStackSize() - itemStack.getAmount();
                                    if (availableSpace > 0) {
                                        freeSpace += availableSpace;
                                    }
                                }
                            }
                        }
                        if (freeSpace <= 0) {
                            continue;
                        }
                        itemRequest.setAmount(Math.min(freeSpace, currentLimit));

                        final ItemStack retrieved = root.getItemStack(itemRequest);
                        if (retrieved != null && !retrieved.getType().isAir()) {
                            BlockMenuUtil.pushItem(targetMenu, retrieved, slots);
                        }
                    }

                    case NULL_ONLY -> {
                        int free = currentLimit;
                        for (int slot : slots) {
                            final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                            if (itemStack == null || itemStack.getType().isAir()) {
                                itemRequest.setAmount(clone.getMaxStackSize());
                            } else {
                                continue;
                            }
                            itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                            final ItemStack retrieved = root.getItemStack(itemRequest);
                            if (retrieved != null && !retrieved.getType().isAir()) {
                                free -= retrieved.getAmount();
                                targetMenu.pushItem(retrieved, slot);
                                if (free <= 0) {
                                    break;
                                }
                            }
                        }
                    }

                    case NONNULL_ONLY -> {
                        int free = currentLimit;
                        for (int slot : slots) {
                            final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                            if (itemStack == null || itemStack.getType().isAir()) {
                                continue;
                            }
                            if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                                continue;
                            }
                            if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                                final int space = itemStack.getMaxStackSize() - itemStack.getAmount();
                                if (space > 0) {
                                    itemRequest.setAmount(space);
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                            itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                            final ItemStack retrieved = root.getItemStack(itemRequest);
                            if (retrieved != null && !retrieved.getType().isAir()) {
                                free -= retrieved.getAmount();
                                targetMenu.pushItem(retrieved, slot);
                                if (free <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                    case FIRST_ONLY -> {
                        int free = currentLimit;
                        if (slots.length == 0) {
                            break;
                        }
                        final int slot = slots[0];
                        final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                        if (itemStack == null || itemStack.getType().isAir()) {
                            itemRequest.setAmount(clone.getMaxStackSize());
                        } else {
                            if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                                continue;
                            }
                            if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                                final int space = itemStack.getMaxStackSize() - itemStack.getAmount();
                                if (space > 0) {
                                    itemRequest.setAmount(space);
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }
                        itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                        final ItemStack retrieved = root.getItemStack(itemRequest);
                        if (retrieved != null && !retrieved.getType().isAir()) {
                            free -= retrieved.getAmount();
                            targetMenu.pushItem(retrieved, slot);
                            if (free <= 0) {
                                break;
                            }
                        }
                    }
                    case LAST_ONLY -> {
                        int free = currentLimit;
                        if (slots.length == 0) {
                            break;
                        }
                        final int slot = slots[slots.length - 1];
                        final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                        if (itemStack == null || itemStack.getType().isAir()) {
                            itemRequest.setAmount(clone.getMaxStackSize());
                        } else {
                            if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                                continue;
                            }
                            if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                                final int space = itemStack.getMaxStackSize() - itemStack.getAmount();
                                if (space > 0) {
                                    itemRequest.setAmount(space);
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        }
                        itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                        final ItemStack retrieved = root.getItemStack(itemRequest);
                        if (retrieved != null && !retrieved.getType().isAir()) {
                            free -= retrieved.getAmount();
                            targetMenu.pushItem(retrieved, slot);
                            if (free <= 0) {
                                break;
                            }
                        }
                    }
                    case FIRST_STOP -> {
                        int freeSpace = 0;
                        for (int slot : slots) {
                            final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                            if (itemStack == null || itemStack.getType().isAir()) {
                                freeSpace += clone.getMaxStackSize();
                                break;
                            } else {
                                if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                                    continue;
                                }
                                if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                                    final int availableSpace = itemStack.getMaxStackSize() - itemStack.getAmount();
                                    if (availableSpace > 0) {
                                        freeSpace += availableSpace;
                                    }
                                }
                                break;
                            }
                        }
                        if (freeSpace <= 0) {
                            continue;
                        }
                        itemRequest.setAmount(Math.min(freeSpace, currentLimit));

                        final ItemStack retrieved = root.getItemStack(itemRequest);
                        if (retrieved != null && !retrieved.getType().isAir()) {
                            BlockMenuUtil.pushItem(targetMenu, retrieved, slots);
                        }
                    }
                    case LAZY -> {
                        if (slots.length > 0) {
                            final ItemStack delta = targetMenu.getItemInSlot(slots[0]);
                            if (delta == null || delta.getType().isAir()) {
                                int freeSpace = 0;
                                for (int slot : slots) {
                                    final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                                    if (itemStack == null || itemStack.getType().isAir()) {
                                        freeSpace += clone.getMaxStackSize();
                                    } else {
                                        if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                                            continue;
                                        }
                                        if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                                            final int availableSpace = itemStack.getMaxStackSize() - itemStack.getAmount();
                                            if (availableSpace > 0) {
                                                freeSpace += availableSpace;
                                            }
                                        }
                                    }
                                }
                                if (freeSpace <= 0) {
                                    continue;
                                }
                                itemRequest.setAmount(Math.min(freeSpace, currentLimit));

                                final ItemStack retrieved = root.getItemStack(itemRequest);
                                if (retrieved != null && !retrieved.getType().isAir()) {
                                    BlockMenuUtil.pushItem(targetMenu, retrieved, slots);
                                }
                            }
                        }
                    }
                }
            }
            targetBlock = targetBlock.getRelative(direction);
        }
    }

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @Nullable
    @Override
    protected int[] getOtherBackgroundSlots() {
        return TEMPLATE_BACKGROUND;
    }

    @Nullable
    @Override
    protected CustomItemStack getOtherBackgroundStack() {
        return TEMPLATE_BACKGROUND_STACK;
    }

    @Override
    public int getNorthSlot() {
        return NORTH_SLOT;
    }

    @Override
    public int getSouthSlot() {
        return SOUTH_SLOT;
    }

    @Override
    public int getEastSlot() {
        return EAST_SLOT;
    }

    @Override
    public int getWestSlot() {
        return WEST_SLOT;
    }

    @Override
    public int getUpSlot() {
        return UP_SLOT;
    }

    @Override
    public int getDownSlot() {
        return DOWN_SLOT;
    }

    @Override
    public int[] getItemSlots() {
        return TEMPLATE_SLOTS;
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.BLUE, 2);
    }

    @Override
    public void onPlace(@Nonnull BlockPlaceEvent e) {
        super.onPlace(e);
        if (useSpecialModel) {
            e.getBlock().setType(Material.BARRIER);
            setupDisplay(e.getBlock().getLocation());
        }
    }

    @Override
    public void postBreak(@Nonnull BlockBreakEvent e) {
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
                "&7[&aPush Rate&7]&f:&7 Every &6" + pushItemTick + " SfTick &7Push once"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aParameter",
                "&7Default mode: &6First Stop",
                "&aChangable mode",
                "&7Default quantity: &63456",
                "&aChangable quantity"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
        "&aFeature",
        "",
        "&eLine pusher will push an item through",
        "&ea line of machines it facing."
        ));
        return displayRecipes;
    }

    @Override
    protected int getTransportModeSlot() {
        return TRANSPORT_MODE_SLOT;
    }
}
