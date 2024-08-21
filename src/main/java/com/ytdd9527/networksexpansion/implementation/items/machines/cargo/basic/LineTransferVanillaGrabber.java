package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.basic;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class LineTransferVanillaGrabber extends NetworkDirectional implements RecipeDisplayItem {
    private static final ItemStack AIR = new ItemStack(Material.AIR);

    private static final String TICK_COUNTER_KEY = "tick_rate";
    private static final int[] BACKGROUND_SLOTS = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 15, 16, 17, 18, 20, 22, 23, 24, 25, 26, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int NORTH_SLOT = 11;
    private static final int SOUTH_SLOT = 29;
    private static final int EAST_SLOT = 21;
    private static final int WEST_SLOT = 19;
    private static final int UP_SLOT = 14;
    private static final int DOWN_SLOT = 32;

    private static int maxDistance;
    private static int grabItemTick;
    private final HashMap<Location, Integer> TICKER_MAP = new HashMap<>();

    public LineTransferVanillaGrabber(ItemGroup itemGroup,
                                      SlimefunItemStack item,
                                      RecipeType recipeType,
                                      ItemStack[] recipe,
                                      String configKey
    ) {
        super(itemGroup, item, recipeType, recipe, NodeType.PUSHER);
        loadConfiguration(configKey);
    }

    private void loadConfiguration(String itemId) {
        FileConfiguration config = Networks.getInstance().getConfig();

        int defaultMaxDistance = 32;
        int defaultGrabItemTick = 1;

        maxDistance = config.getInt("items." + itemId + ".max-distance", defaultMaxDistance);
        grabItemTick = config.getInt("items." + itemId + ".grabitem-tick", defaultGrabItemTick);
    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);

        // 初始化Tick计数器
        final Location location = block.getLocation();
        int tickCounter = getTickCounter(location);
        tickCounter = (tickCounter + 1) % grabItemTick;

        // 每10个Tick执行一次抓取操作
        if (tickCounter == 0) {
            performGrabbingOperation(blockMenu);
        }

        // 更新Tick计数器
        updateTickCounter(location, tickCounter);
    }

    private int getTickCounter(Location location) {
        final Integer ticker = TICKER_MAP.get(location);
        if (ticker == null) {
            TICKER_MAP.put(location, 0);
            return 0;
        }
        return ticker;
    }

    private void updateTickCounter(Location location, int tickCounter) {
        TICKER_MAP.put(location, tickCounter);
    }

    private void performGrabbingOperation(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

            if (definition == null || definition.getNode() == null) {
                return;
            }

            final NetworkRoot root = definition.getNode().getRoot();
            tryGrabItem(root, blockMenu);
        }
    }

    private void tryGrabItem(@Nonnull NetworkRoot root, @Nonnull BlockMenu blockMenu) {
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
            try {
                if (!Slimefun.getProtectionManager().hasPermission(offlinePlayer, targetBlock, Interaction.INTERACT_BLOCK)) {
                    break;
                }
            } catch (NullPointerException ex) {
                break;
            }

            final BlockState blockState = targetBlock.getState();

            if (!(blockState instanceof InventoryHolder holder)) {
                return;
            }

            boolean wildChests = Networks.getSupportedPluginManager().isWildChests();
            boolean isChest = wildChests && WildChestsAPI.getChest(targetBlock.getLocation()) != null;

            if (wildChests && isChest) {
                continue;
            }

            final Inventory inventory = holder.getInventory();

            if (inventory instanceof FurnaceInventory furnaceInventory) {
                final ItemStack furnaceInventoryResult = furnaceInventory.getResult();
                final ItemStack furnaceInventoryFuel = furnaceInventory.getFuel();
                grabItem(root, blockMenu, furnaceInventoryResult);

                if (furnaceInventoryFuel != null && furnaceInventoryFuel.getType() == Material.BUCKET) {
                    grabItem(root, blockMenu, furnaceInventoryFuel);
                }

            } else if (inventory instanceof BrewerInventory brewerInventory) {
                for (int i = 0; i < 3; i++) {
                    final ItemStack stack = brewerInventory.getContents()[i];
                    if (stack != null && stack.getType() == Material.POTION) {
                        final PotionMeta potionMeta = (PotionMeta) stack.getItemMeta();
                        if (potionMeta.getBasePotionData().getType() != PotionType.WATER) {
                            grabItem(root, blockMenu, stack);
                            break;
                        }
                    }
                }
            } else {
                for (ItemStack stack : inventory.getContents()) {
                    if (grabItem(root, blockMenu, stack)) {
                        break;
                    }
                }
            }
            targetBlock = targetBlock.getRelative(direction);
        }
    }

    private boolean grabItem(@Nonnull NetworkRoot root, @Nonnull BlockMenu blockMenu, @Nullable ItemStack stack) {
        if (stack != null && !stack.getType().isAir()) {
            root.addItemStack(stack);
            return true;
        } else {
            return false;
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
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.MAROON, 1);
    }

    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩传输数据⇩",
                "",
                "&7[&a最大距离&7]&f:&6" + maxDistance + "方块",
                "&7[&a抓取频率&7]&f:&7 每 &6" + grabItemTick + " SfTick &7抓取一次"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩参数⇩",
                "&7默认运输模式: &6首位阻断",
                "&c不可调整运输模式",
                "&7默认运输数量: &664",
                "&c不可调整运输数量"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩功能⇩",
                "",
                "&e与链式不同的是，此机器&c只有连续抓取原版容器的功能",
                "&c而不是连续转移物品！"
        ));
        return displayRecipes;
    }
}
