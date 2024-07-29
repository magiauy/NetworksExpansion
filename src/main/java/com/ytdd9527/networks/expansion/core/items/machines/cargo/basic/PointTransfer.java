package com.ytdd9527.networks.expansion.core.items.machines.cargo.basic;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.utils.DisplayGroupGenerators;
import com.ytdd9527.networks.expansion.utils.TextUtil;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


public class PointTransfer extends NetworkDirectional implements RecipeDisplayItem {


    public static final CustomItemStack TEMPLATE_BACKGROUND_STACK = new CustomItemStack(
            Material.BLUE_STAINED_GLASS_PANE, TextUtil.colorRandomString("指定需要推送的物品")
    );
    private static final String TICK_COUNTER_KEY = "tick_rate";
    private static final String KEY_UUID = "display-uuid";
    private static final ItemStack AIR = new CustomItemStack(Material.AIR);
    private static final int NORTH_SLOT = 1;
    private static final int SOUTH_SLOT = 19;
    private static final int EAST_SLOT = 11;
    private static final int WEST_SLOT = 9;
    private static final int UP_SLOT = 2;
    private static final int DOWN_SLOT = 20;
    private static final int[] TEMPLATE_BACKGROUND = new int[]{
            4, 8,
            13, 17,
            22, 26
    };
    private static final int[] BACKGROUND_SLOTS = {
            0, 3,
            10, 12,
            18, 21,
    };
    private static final int[] TEMPLATE_SLOTS = new int[]{
            5, 6, 7,
            14, 15, 16,
            23, 24, 25
    };
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;

    public PointTransfer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String configKey) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER_GRABBER);
        loadConfigurations(configKey);
    }

    private void loadConfigurations(String configKey) {
        FileConfiguration config = Networks.getInstance().getConfig();
        int defaultPushItemTick = 1;
        int defaultGrabItemTick = 1;
        boolean defaultUseSpecialModel = false;

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
        if (blockMenu != null) {
            tryPushItem(blockMenu);
            tryGrabItem(blockMenu);
        }
    }

    private void tryPushItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final BlockFace direction = getCurrentDirection(blockMenu);
        final BlockMenu targetMenu = StorageCacheUtils.getMenu(blockMenu.getBlock().getRelative(direction).getLocation());

        if (targetMenu == null) {
            return;
        }

        for (int itemSlot : this.getItemSlots()) {
            final ItemStack testItem = blockMenu.getItemInSlot(itemSlot);

            if (testItem == null || testItem.getType() == Material.AIR) {
                continue;
            }

            final ItemStack clone = testItem.clone();
            clone.setAmount(1);
            final ItemRequest itemRequest = new ItemRequest(clone, clone.getMaxStackSize());

            int[] slots = targetMenu.getPreset().getSlotsAccessedByItemTransport(targetMenu, ItemTransportFlow.INSERT, clone);

            for (int slot : slots) {
                final ItemStack itemStack = targetMenu.getItemInSlot(slot);

                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    final int space = itemStack.getMaxStackSize() - itemStack.getAmount();
                    if (space > 0 && StackUtils.itemsMatch(itemRequest, itemStack, true)) {
                        itemRequest.setAmount(space);
                    } else {
                        continue;
                    }
                }

                ItemStack retrieved = definition.getNode().getRoot().getItemStack(itemRequest);
                if (retrieved != null) {
                    targetMenu.pushItem(retrieved, slots);
                    if (definition.getNode().getRoot().isDisplayParticles()) {
                        showParticle(blockMenu.getLocation(), direction);
                    }
                }
                break;
            }
        }
    }

    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final BlockFace direction = this.getCurrentDirection(blockMenu);
        final BlockMenu targetMenu = StorageCacheUtils.getMenu(blockMenu.getBlock().getRelative(direction).getLocation());

        if (targetMenu == null) {
            return;
        }

        int[] slots = targetMenu.getPreset().getSlotsAccessedByItemTransport(targetMenu, ItemTransportFlow.WITHDRAW, null);

        for (int slot : slots) {
            final ItemStack itemStack = targetMenu.getItemInSlot(slot);

            if (itemStack != null && itemStack.getType() != Material.AIR) {
                int before = itemStack.getAmount();
                definition.getNode().getRoot().addItemStack(itemStack);
                if (definition.getNode().getRoot().isDisplayParticles() && itemStack.getAmount() < before) {
                    showParticle(blockMenu.getLocation(), direction);
                }
                break;
            }
        }
    }

    private int getTickCounter(Block block) {
        String tickCounterValue = BlockStorage.getLocationInfo(block.getLocation(), TICK_COUNTER_KEY);
        try {
            return (tickCounterValue != null) ? Integer.parseInt(tickCounterValue) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateTickCounter(Block block, int tickCounter) {
        // 将更新后的Tick计数器值存储到BlockStorage中
        BlockStorage.addBlockInfo(block.getLocation(), TICK_COUNTER_KEY, Integer.toString(tickCounter));
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.ORANGE, 1);
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

    @Nonnull
    @Override
    public int[] getOtherBackgroundSlots() {
        return TEMPLATE_BACKGROUND;
    }

    @Nullable
    @Override
    protected CustomItemStack getOtherBackgroundStack() {
        return TEMPLATE_BACKGROUND_STACK;
    }

    @Nonnull
    @Override
    public int[] getItemSlots() {
        return TEMPLATE_SLOTS;
    }

    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @NotNull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(6);
        displayRecipes.add(AIR);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩功能⇩",
                "",
                "&e逻辑&f:",
                "&f-&7[&a推送物品&7]&f:&7将设置的推送物品推送至机器的输入槽中",
                "&f-&7[&a抓取物品&7]&f:&7将输出槽上的物品全部抓取进网络中"
        ));
        return displayRecipes;
    }
}
