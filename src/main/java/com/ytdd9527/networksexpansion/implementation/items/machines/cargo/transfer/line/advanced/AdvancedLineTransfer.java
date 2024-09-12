package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.advanced;

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
import org.bukkit.Location;
import org.bukkit.Material;
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

// TODO: 需要重构
public class AdvancedLineTransfer extends AdvancedDirectional implements RecipeDisplayItem {
    private static final int DEFAULT_MAX_DISTANCE = 64;
    private static final int DEFAULT_PUSH_ITEM_TICK = 1;
    private static final int DEFAULT_GRAB_ITEM_TICK = 1;
    private static final int DEFAULT_REQUIRED_POWER = 5000;
    private static final boolean DEFAULT_USE_SPECIAL_MODEL = false;
    public static final CustomItemStack TEMPLATE_BACKGROUND_STACK = new CustomItemStack(
            Material.BLUE_STAINED_GLASS_PANE, Theme.PASSIVE + "指定需要推送的物品"
    );
    private static final int[] BACKGROUND_SLOTS = new int[]{
            0,
            10,
            18,
            27, 28, 29,
            36, 37, 38
    };
    private static final int[] TEMPLATE_BACKGROUND = new int[]{
            3,
            12,
            21,
            30,
            39,
            48
    };
    private static final int[] TEMPLATE_SLOTS = new int[]{
            4, 5, 6, 7, 8,
            13, 14, 15, 16, 17,
            22, 23, 24, 25, 26,
            31, 32, 33, 34, 35,
            40, 41, 42, 43, 44,
            49, 50, 51, 52, 53,
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
    private static final String KEY_UUID = "display-uuid";
    private static final int TRANSPORT_LIMIT = 3456;
    private static final HashMap<Location, Integer> PUSH_TICKER_MAP = new HashMap<>();
    private static final HashMap<Location, Integer> GRAB_TICKER_MAP = new HashMap<>();
    private int maxDistance;
    private int pushItemTick;
    private int grabItemTick;
    private int requiredPower;
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;

    public AdvancedLineTransfer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String configKey) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER);
        for (int slot : TEMPLATE_SLOTS) {
            this.getSlotsToDrop().add(slot);
        }
        loadConfigurations();
    }

    @Override
    public boolean comeMaxLimit(int currentNumber) {
        return currentNumber > TRANSPORT_LIMIT;
    }

    @Override
    public int getMaxLimit() {
        return TRANSPORT_LIMIT;
    }

    private void loadConfigurations() {
        String configKey = getId();
        FileConfiguration config = Networks.getInstance().getConfig();

        // 读取配置值
        this.maxDistance = config.getInt("items." + getId() + ".max-distance", DEFAULT_MAX_DISTANCE);
        this.pushItemTick = config.getInt("items." + configKey + ".pushitem-tick", DEFAULT_PUSH_ITEM_TICK);
        this.grabItemTick = config.getInt("items." + configKey + ".grabitem-tick", DEFAULT_GRAB_ITEM_TICK);
        this.requiredPower = config.getInt("items." + configKey + ".required-power", DEFAULT_REQUIRED_POWER);
        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", DEFAULT_USE_SPECIAL_MODEL);


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

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        final Location location = block.getLocation();

        if (blockMenu == null) {
            return;
        }

        if (pushItemTick != 1) {
            int currentPushTick = getPushTickCounter(location);
            if (currentPushTick == 0) {
                tryPushItem(blockMenu);
            }
            currentPushTick = (currentPushTick + 1) % pushItemTick;
            updatePushTickCounter(location, currentPushTick);
        } else {
            tryPushItem(blockMenu);
        }

        if (grabItemTick != 1) {
            int currentGrabTick = getGrabTickCounter(location);
            if (currentGrabTick == 0) {
                tryGrabItem(blockMenu);
            }
            currentGrabTick = (currentGrabTick + 1) % grabItemTick;
            updateGrabTickCounter(location, currentGrabTick);
        } else {
            tryGrabItem(blockMenu);
        }
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

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(6);
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
                "&7默认运输模式: &6首位阻断",
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
        return displayRecipes;
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
