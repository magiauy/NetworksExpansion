package com.ytdd9527.networks.expansion.core.item.machine.cargo.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.util.DisplayGroupGenerators;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.utils.BlockMenuUtils;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class AdvancedLineTransfer extends AdvancedDirectional implements RecipeDisplayItem {
    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private static BukkitTask transferTask;
    private static final int[] BACKGROUND_SLOTS = new int[]{
            0,
            10,
            18,
            27,28,29,
            36,37,38
    };
    private static final int[] TEMPLATE_SLOTS = new int[]{
            3, 4, 5, 6, 7, 8,
            12, 13, 14, 15, 16, 17,
            21, 22, 23, 24, 25, 26,
            30, 31, 32, 33, 34, 35,
            39, 40, 41, 42, 43, 44,
            48, 49, 50, 51, 52, 53,
    };
    private static final int NORTH_SLOT = 1;
    private static final int SOUTH_SLOT = 19;
    private static final int EAST_SLOT = 11;
    private static final int WEST_SLOT = 9;
    private static final int UP_SLOT = 2;
    private static final int DOWN_SLOT = 20;
    private static final int MINUS_SLOT = 45;
    private static final int SHOW_SLOT = 46;
    private static final int ADD_SLOT = 47;
    private static final int TRANSPORT_MODE_SLOT = 36;

    public static final CustomItemStack TEMPLATE_BACKGROUND_STACK = new CustomItemStack(
        Material.BLUE_STAINED_GLASS_PANE, Theme.PASSIVE + "指定需要推送的物品"
    );
    private static final String KEY_UUID = "display-uuid";
    private static final int TRANSPORT_LIMIT = 3456;
    private int maxDistance;
    private int pushItemTick;
    private int grabItemTick;
    private int requiredPower;
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;

    private static final HashMap<Location, Integer> PUSH_TICKER_MAP = new HashMap<>();
    private static final HashMap<Location, Integer> GRAB_TICKER_MAP = new HashMap<>();

    public AdvancedLineTransfer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String configKey) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER);
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
        int defaultGrabItemTick = 1;
        int defaultRequiredPower = 5000;
        boolean defaultUseSpecialModel = false;

        FileConfiguration config = Networks.getInstance().getConfig();

        // 读取配置值
        this.maxDistance = config.getInt("items." + configKey + ".max-distance", defaultMaxDistance);
        this.pushItemTick = config.getInt("items." + configKey + ".pushitem-tick", defaultPushItemTick);
        this.grabItemTick = config.getInt("items." + configKey + ".grabitem-tick", defaultGrabItemTick);
        this.requiredPower = config.getInt("items." + configKey + ".required-power", defaultRequiredPower);
        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", defaultUseSpecialModel);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("cloche", DisplayGroupGenerators::generateCloche);
        generatorMap.put("cell", DisplayGroupGenerators::generateCell);

        this.displayGroupGenerator = null;

        if (this.useSpecialModel) {
            String generatorKey = config.getString("items." + configKey + ".use-special-model.type");
            this.displayGroupGenerator = generatorMap.get(generatorKey);
            if (this.displayGroupGenerator == null) {
                Networks.getInstance().getLogger().warning("未知类型 '" + generatorKey + "', 模型已禁用。");
                this.useSpecialModel = false;
            }
        }
    }
    private void performPushItemOperation(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            tryPushItem(blockMenu);
        }
    }

    public static void cancelTransferTask() {
        if (transferTask != null && !transferTask.isCancelled()) {
            transferTask.cancel();
        }
    }
    private void performGrabItemOperationAsync(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            transferTask = new BukkitRunnable() {
                @Override
                public void run() {
                    tryGrabItem(blockMenu);
                }
            }.runTaskAsynchronously(Networks.getInstance());
        }
    }
    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        final Location location = block.getLocation();

        int currentPushTick = getPushTickCounter(location);
        if (currentPushTick == 0) {
            performPushItemOperation(blockMenu);
        }
        currentPushTick = (currentPushTick + 1) % pushItemTick;
        updatePushTickCounter(location, currentPushTick);

        int currentGrabTick = getGrabTickCounter(location);
        if (currentGrabTick == 0) {
            performGrabItemOperationAsync(blockMenu);
        }
        currentGrabTick = (currentGrabTick + 1) % grabItemTick;
        updateGrabTickCounter(location, currentGrabTick);
    }
    private int getPushTickCounter(Location location) {
        final Integer ticker = PUSH_TICKER_MAP.get(location);
        if (ticker != null) {
            return ticker;
        } else {
            PUSH_TICKER_MAP.put(location, 0);
            return 0;
        }
    }
    private int getGrabTickCounter(Location location) {
        final Integer ticker = GRAB_TICKER_MAP.get(location);
        if (ticker != null) {
            return ticker;
        } else {
            GRAB_TICKER_MAP.put(location, 0);
            return 0;
        }
    }
    private void updatePushTickCounter(Location location, int pushTick) {
        PUSH_TICKER_MAP.put(location, pushTick);
    }

    private void updateGrabTickCounter(Location location, int grabTick) {
        GRAB_TICKER_MAP.put(location, grabTick);
    }

    private void tryPushItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }
        final NetworkRoot root = definition.getNode().getRoot();
        final BlockFace direction = this.getCurrentDirection(blockMenu);

        Block targetBlock = blockMenu.getBlock().getRelative(direction);
        String currentTransportMode = getCurrentTransportMode(blockMenu.getLocation());
        int currentLimit = getCurrentNumber(blockMenu.getLocation());

        for (int i = 0; i <= maxDistance; i++) {
            final BlockMenu targetMenu = StorageCacheUtils.getMenu(targetBlock.getLocation());

            if (targetMenu == null) {
                break;
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

                switch (currentTransportMode) {
                    case TRANSPORT_MODE_NONE -> {
                        int freeSpace = 0;
                        for (int slot : slots) {
                            final ItemStack itemStack = targetMenu.getItemInSlot(slot);
                            if (StackUtils.itemsMatch(itemRequest, itemStack, true)) {
                                final int availableSpace = itemStack.getMaxStackSize() - itemStack.getAmount();
                                if (availableSpace > 0) {
                                    freeSpace += availableSpace;
                                }
                            } else {
                                freeSpace += clone.getMaxStackSize();
                            }
                        }
                        if (freeSpace <= 0) {
                            continue;
                        }
                        itemRequest.setAmount(Math.min(freeSpace, currentLimit));

                        ItemStack retrieved = root.getItemStack(itemRequest);
                        if (retrieved != null && !retrieved.getType().isAir()) {
                            BlockMenuUtils.pushItem(targetMenu, retrieved, slots);
                        }
                    }

                    case TRANSPORT_MODE_NULL_ONLY -> {
                        int free = currentLimit;
                        for (int slot: slots) {
                            ItemStack itemStack = targetMenu.getItemInSlot(slot);
                            if (itemStack == null || itemStack.getType().isAir()) {
                                itemRequest.setAmount(clone.getMaxStackSize());
                            } else {
                                continue;
                            }
                            itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                            ItemStack retrieved = root.getItemStack(itemRequest);
                            if (retrieved != null && !retrieved.getType().isAir()) {
                                free -= retrieved.getAmount();
                                targetMenu.pushItem(retrieved, slot);
                                if (free <= 0) {
                                    break;
                                }
                            }
                        }
                    }

                    case TRANSPORT_MODE_NONNULL_ONLY -> {
                        int free = currentLimit;
                        for (int slot: slots) {
                            ItemStack itemStack = targetMenu.getItemInSlot(slot);
                            if (StackUtils.itemsMatch(testItem, itemStack)) {
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

                            ItemStack retrieved = root.getItemStack(itemRequest);
                            if (retrieved != null && !retrieved.getType().isAir()) {
                                free -= retrieved.getAmount();
                                targetMenu.pushItem(retrieved, slot);
                                if (free <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            targetBlock = targetBlock.getRelative(direction);
        }
    }
    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }
        final NetworkRoot root = definition.getNode().getRoot();

        final BlockFace direction = this.getCurrentDirection(blockMenu);
        Block currentBlock = blockMenu.getBlock().getRelative(direction);

        for (int i = 0; i < maxDistance; i++) {
            // 如果方块是空气，退出
            if (currentBlock.getType().isAir()) {
                break;
            }

            BlockMenu targetMenu = StorageCacheUtils.getMenu(currentBlock.getLocation());
            // 如果没有blockMenu，退出
            if (targetMenu == null) {
                break;
            }
            // 获取输出槽
            int[] slots = targetMenu.getPreset().getSlotsAccessedByItemTransport(targetMenu, ItemTransportFlow.WITHDRAW, null);
            int free = getCurrentNumber(blockMenu.getLocation());
            for (int slot : slots) {
                ItemStack itemStack = targetMenu.getItemInSlot(slot);
                if (itemStack != null && !itemStack.getType().isAir()) {
                    int canConsume = Math.min(itemStack.getAmount(), free);
                    ItemStack clone = itemStack.clone();
                    clone.setAmount(canConsume);
                    root.addItemStack(clone);
                    itemStack.setAmount(itemStack.getAmount() - canConsume);
                    free -= canConsume;
                    if (free <= 0) {
                        break;
                    }
                }
            }
            currentBlock = currentBlock.getRelative(direction);
        }
    }

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
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
    @NotNull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes  = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩传输数据⇩",
                "",
                "&7[&a最大距离&7]&f:&6" + maxDistance + "方块",
                "&7[&a推送频率&7]&f:&7 每 &6" + pushItemTick + " SfTick &7推送一次",
                "&7[&a抓取频率&7]&f:&7 每 &6" + grabItemTick + " SfTick &7抓取一次",
                "&7[&a运输耗电&7]&f:&7 每次运输消耗 &6" + requiredPower + " J 网络电力"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩参数⇩",
                "&7默认运输模式: &6无限制",
                "&a可调整运输模式",
                "&7默认运输数量: &63456",
                "&a可调整运输数量"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩功能⇩",
                "",
                "&e与链式不同的是，此机器&c只有连续推送和抓取的功能",
                "&c而不是连续转移物品！"
        ));
        return displayRecipes ;
    }

    @Override
    protected int getMinusSlot() {
        return MINUS_SLOT;
    }

    @Override
    protected int getShowSlot() {
        return SHOW_SLOT;
    }

    @Override
    protected int getAddSlot() {
        return ADD_SLOT;
    }

    @Override
    protected int getTransportModeSlot() {
        return TRANSPORT_MODE_SLOT;
    }
}
