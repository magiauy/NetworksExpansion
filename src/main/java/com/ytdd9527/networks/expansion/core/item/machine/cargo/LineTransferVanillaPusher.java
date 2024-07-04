package com.ytdd9527.networks.expansion.core.item.machine.cargo;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class LineTransferVanillaPusher extends NetworkDirectional {

    private static final String TICK_COUNTER_KEY = "push_ticker";

    private static final int[] BACKGROUND_SLOTS = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 15, 16, 17, 18, 20, 22, 23, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int[] INPUT_SLOTS = new int[]{24, 25, 26};
    private static final int NORTH_SLOT = 11;
    private static final int SOUTH_SLOT = 29;
    private static final int EAST_SLOT = 21;
    private static final int WEST_SLOT = 19;
    private static final int UP_SLOT = 14;
    private static final int DOWN_SLOT = 32;

    private static int maxDistance;
    private static int pushItemTick;

    public LineTransferVanillaPusher(ItemGroup itemGroup,
                                      SlimefunItemStack item,
                                      RecipeType recipeType,
                                      ItemStack[] recipe,
                                      String configKey
    ) {
        super(itemGroup, item, recipeType, recipe, NodeType.PUSHER);
        for (int slot : getInputSlots()) {
            this.getSlotsToDrop().add(slot);
        }
        loadConfiguration(configKey);
    }

    private void loadConfiguration(String itemId) {
        FileConfiguration config = Networks.getInstance().getConfig();

        int defaultMaxDistance = 32;
        int defaultGrabItemTick = 5;

        this.maxDistance = config.getInt("items." + itemId + ".max-distance", defaultMaxDistance);
        this.pushItemTick = config.getInt("items." + itemId + ".grabitem-tick", defaultGrabItemTick);
    }


    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);

        // 初始化Tick计数器
        int tickCounter = getTickCounter(block);
        tickCounter = (tickCounter + 1) % pushItemTick;

        // 每10个Tick执行一次抓取操作
        if (tickCounter == 0) {
            performPushingOperation(blockMenu);
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

    private void performPushingOperation(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

            if (definition == null || definition.getNode() == null) {
                return;
            }

            final NetworkRoot root = definition.getNode().getRoot();
            tryPushItem(root, blockMenu);
        }
    }

    private void tryPushItem(@Nonnull NetworkRoot root, @Nonnull BlockMenu blockMenu) {

        final BlockFace direction = getCurrentDirection(blockMenu);

        // Fix for early vanilla pusher release
        final Block block = blockMenu.getBlock();
        final String ownerUUID = StorageCacheUtils.getData(block.getLocation(), OWNER_KEY);
        if (ownerUUID == null) {
            return;
        }
        final UUID uuid = UUID.fromString(ownerUUID);
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        // dirty fix
        Block targetBlock = block.getRelative(direction);
        for (int d = 0; d < maxDistance; d++) {
            // 如果方块是空气，退出
            if (targetBlock.getType() == Material.AIR) {
                break;
            }

            final BlockState blockState = targetBlock.getState();

            if (!(blockState instanceof InventoryHolder holder)) {
                return;
            }

            for (int slot : getInputSlots()) {
                final ItemStack stack = blockMenu.getItemInSlot(slot);

                if (stack == null || stack.getType() == Material.AIR) {
                    continue;
                }

                // dirty fix
                try {
                    if (!Slimefun.getProtectionManager().hasPermission(offlinePlayer, targetBlock, Interaction.INTERACT_BLOCK)) {
                        return;
                    }
                } catch (NullPointerException ex) {
                    return;
                }

                final Inventory inventory = holder.getInventory();

                boolean wildChests = Networks.getSupportedPluginManager().isWildChests();
                boolean isChest = wildChests && WildChestsAPI.getChest(targetBlock.getLocation()) != null;

                sendDebugMessage(block.getLocation(), "WildChests 已安装：" + wildChests);
                sendDebugMessage(block.getLocation(), "该方块是否被 WildChest 判断为方块：" + isChest);

                if (inventory instanceof FurnaceInventory furnace) {
                    handleFurnace(stack, furnace);
                } else if (inventory instanceof BrewerInventory brewer) {
                    handleBrewingStand(stack, brewer);
                } else if (wildChests && isChest) {
                    sendDebugMessage(block.getLocation(), "WildChest 测试失败！");
                    return;
                } else {
                    if (InvUtils.fits(holder.getInventory(), stack)) {
                        sendDebugMessage(block.getLocation(), "WildChest 测试成功。");
                        holder.getInventory().addItem(stack);
                        stack.setAmount(0);
                    } else {
                        HashMap<Integer, ItemStack> leftovers = holder.getInventory().addItem(stack);
                        if (!leftovers.isEmpty()) {
                            returnItems(root, leftovers.values().toArray(new ItemStack[0]));
                        }
                    }
                }
            }

            targetBlock = targetBlock.getRelative(direction);
        }
    }

    private void handleFurnace(@Nonnull ItemStack stack, @Nonnull FurnaceInventory furnace) {
        if (stack.getType().isFuel()
                && (furnace.getFuel() == null || furnace.getFuel().getType() == Material.AIR)
        ) {
            furnace.setFuel(stack.clone());
            stack.setAmount(0);
        } else if (!stack.getType().isFuel()
                && (furnace.getSmelting() == null || furnace.getSmelting().getType() == Material.AIR)
        ) {
            furnace.setSmelting(stack.clone());
            stack.setAmount(0);
        }
    }

    private void handleBrewingStand(@Nonnull ItemStack stack, @Nonnull BrewerInventory brewer) {
        if (stack.getType() == Material.BLAZE_POWDER) {
            if (brewer.getFuel() == null || brewer.getFuel().getType() == Material.AIR) {
                brewer.setFuel(stack.clone());
                stack.setAmount(0);
            }
        } else if (stack.getType() == Material.POTION) {
            for (int i = 0; i < 3; i++) {
                final ItemStack stackInSlot = brewer.getContents()[i];
                if (stackInSlot == null || stackInSlot.getType() == Material.AIR) {
                    final ItemStack[] contents = brewer.getContents();
                    contents[i] = stack.clone();
                    brewer.setContents(contents);
                    stack.setAmount(0);
                    return;
                }
            }
        } else if (brewer.getIngredient() == null || brewer.getIngredient().getType() == Material.AIR) {
            brewer.setIngredient(stack.clone());
            stack.setAmount(0);
        }
    }

    private void returnItems(@Nonnull NetworkRoot root, @Nonnull ItemStack[] inputs) {
        for (ItemStack input : inputs) {
            if (input != null) {
                root.addItemStack(input);
            }
        }
    }

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
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
    public boolean runSync() {
        return true;
    }

    @Override
    public int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.MAROON, 1);
    }
}
