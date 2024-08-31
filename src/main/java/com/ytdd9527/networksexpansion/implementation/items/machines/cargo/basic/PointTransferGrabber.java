package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.basic;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.utils.DisplayGroupGenerators;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
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


public class PointTransferGrabber extends NetworkDirectional implements RecipeDisplayItem {


    private static final String TICK_COUNTER_KEY = "tick_rate";
    private static final String KEY_UUID = "display-uuid";
    private static final ItemStack AIR = new CustomItemStack(Material.AIR);
    private static final int NORTH_SLOT = 1;
    private static final int SOUTH_SLOT = 19;
    private static final int EAST_SLOT = 11;
    private static final int WEST_SLOT = 9;
    private static final int UP_SLOT = 2;
    private static final int DOWN_SLOT = 20;
    private static final int[] BACKGROUND_SLOTS = {
            0, 3, 4, 5, 6, 7, 8,
            10, 12, 13, 14, 15, 16, 17,
            18, 21, 22, 23, 24, 25, 26,
    };
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private int grabItemTick;

    public PointTransferGrabber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String configKey) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER_GRABBER);
        loadConfigurations(configKey);
    }

    private void loadConfigurations(String configKey) {
        FileConfiguration config = Networks.getInstance().getConfig();

        int defaultGrabItemTick = 1;
        boolean defaultUseSpecialModel = false;

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

    private void performGrabbingOperation(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            tryGrabItem(blockMenu);
        }
    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);

        // 初始化Tick计数器
        int tickCounter = getTickCounter(block);
        tickCounter = (tickCounter + 1) % grabItemTick;

        // 每10个Tick执行一次抓取操作
        if (tickCounter == 0) {
            performGrabbingOperation(blockMenu);
        }

        // 更新Tick计数器
        updateTickCounter(block, tickCounter);
    }

    private int getTickCounter(Block block) {
        // 从BlockStorage中获取与TICK_COUNTER_KEY关联的值
        String tickCounterValue = BlockStorage.getLocationInfo(block.getLocation(), TICK_COUNTER_KEY);
        try {
            // 如果存在值，则尝试将其解析为整数
            return (tickCounterValue != null) ? Integer.parseInt(tickCounterValue) : 0;
        } catch (NumberFormatException e) {
            // 如果解析失败，则返回0
            return 0;
        }
    }

    private void updateTickCounter(Block block, int tickCounter) {
        // 将更新后的Tick计数器值存储到BlockStorage中
        BlockStorage.addBlockInfo(block.getLocation(), TICK_COUNTER_KEY, Integer.toString(tickCounter));
    }

    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        if (blockMenu == null) {
            return;
        }

        NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());
        if (definition == null || definition.getNode() == null) {
            return;
        }


        BlockFace direction = this.getCurrentDirection(blockMenu);

        Block currentBlock = blockMenu.getBlock().getRelative(direction);

        if (currentBlock != null && currentBlock.getType() != Material.AIR) {
            BlockMenu targetMenu = StorageCacheUtils.getMenu(currentBlock.getLocation());
            if (targetMenu != null) {
                int[] slots = targetMenu.getPreset().getSlotsAccessedByItemTransport(targetMenu, ItemTransportFlow.WITHDRAW, null);
                for (int slot : slots) {
                    ItemStack itemStack = targetMenu.getItemInSlot(slot);
                    if (itemStack != null && isItemTransferable(itemStack)) {
                        int before = itemStack.getAmount();
                        definition.getNode().getRoot().addItemStack(itemStack);
                        if (itemStack.getAmount() < before) {
                            showParticle(blockMenu.getLocation(), direction);
                            targetMenu.replaceExistingItem(slot, itemStack);
                        }
                    }
                }
            }
        }
    }

    private boolean isItemTransferable(@Nonnull ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
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

    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @NotNull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aTransfer rate",
                "",
                "&7[&aGrab rate&7]&f:&7 Every &6" + grabItemTick + " SfTick &7Grab once",
                "&7[&a1 SfTick=0.5s]",
                "",
                "&7 Simply put, it works like a normal grabber"
        ));
        displayRecipes.add(AIR);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aFeature",
                "",
                "&7[&aGrabber&7]&f:&7 Will grab all the item on the output slot",
                "&7 If there is no findable machine it faced, or ",
                "&7 no more item to grab, out of network space,",
                "&7 The Point Grabber will stop"
        ));
        displayRecipes.add(AIR);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aGuide",
                "",
                "&7Point Grabber effeciency recommendation：",
                "",
                "&7Please do not use it for machines with small output items",
                "&7Recommend to use it for large quantity output ",
                "",
                "&7Follow these tips and you will be able to maximize your",
                "&7performance while keep the server running smoothly."
        ));
        return displayRecipes;
    }
}
