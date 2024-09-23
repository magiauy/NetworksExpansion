package com.ytdd9527.networksexpansion.implementation.items.machines.unit;


import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.data.ItemContainer;
import com.ytdd9527.networksexpansion.api.data.StorageUnitData;
import com.ytdd9527.networksexpansion.api.enums.QuickTransferMode;
import com.ytdd9527.networksexpansion.api.enums.StorageUnitType;
import com.ytdd9527.networksexpansion.api.interfaces.Configurable;
import com.ytdd9527.networksexpansion.api.interfaces.ModelledItem;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.utils.DisplayGroupGenerators;
import com.ytdd9527.networksexpansion.utils.databases.DataStorage;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

//TODO 对于一些复杂的逻辑，需要重构
@SuppressWarnings({"deprecation", "unused"})
public class CargoStorageUnit extends SpecialSlimefunItem implements DistinctiveItem, ModelledItem, Configurable {
    private static final boolean DEFAULT_USE_SPECIAL_MODEL = false;
    private static final Map<Location, StorageUnitData> storages = new HashMap<>();
    private static final Map<Location, QuickTransferMode> quickTransferModes = new HashMap<>();
    private static final Set<Location> locked = new HashSet<>();
    private static final Set<Location> voidExcesses = new HashSet<>();
    private static final String KEY_UUID = "display-uuid";
    private static final int[] DISPLAY_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43, 46, 47, 48, 49, 50, 51, 52};
    private static final int STORAGE_INFO_SLOT = 4;
    private static final NamespacedKey idKey = new NamespacedKey(Networks.getInstance(), "CONTAINER_ID");
    private static final int QUANTUM_SLOT = 9;
    private static final int QUICK_TRANSFER_SLOT = 18;
    private static final int ITEM_CHOOSE_SLOT = 27;
    private static final ItemStack ERROR_BORDER = new CustomItemStack(Material.BARRIER, " ", " ", " ", " ");
    private final StorageUnitType sizeType;
    private final int[] BORDER = {0, 1, 2, 3, 5, 6, 17, 26, 35, 36, 44, 45, 53};
    private final int VOID_MODE_SLOT = 7;
    private final int LOCK_MODE_SLOT = 8;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private boolean useSpecialModel;

    public CargoStorageUnit(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, StorageUnitType sizeType) {
        super(itemGroup, item, recipeType, recipe);

        this.sizeType = sizeType;

        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                for (int slot : BORDER) {
                    addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(STORAGE_INFO_SLOT, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(LOCK_MODE_SLOT, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(VOID_MODE_SLOT, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                Location l = b.getLocation();
                requestData(l, getContainerId(l));
                // Restore mode
                SlimefunBlockData blockData = StorageCacheUtils.getBlock(l);
                String lock = null;
                String voidExcess = null;
                String quickModeStr = null;
                if (blockData != null) {
                    lock = blockData.getData("locked");
                    voidExcess = blockData.getData("voidExcess");
                    quickModeStr = blockData.getData("quickTransferMode");
                }
                QuickTransferMode quickTransferMode = quickModeStr == null ? QuickTransferMode.FROM_QUANTUM : QuickTransferMode.valueOf(quickModeStr);
                quickTransferModes.put(l, quickTransferMode);
                if (lock != null) {
                    locked.add(l);
                    menu.replaceExistingItem(LOCK_MODE_SLOT, getContentLockItem(true));
                } else {
                    menu.replaceExistingItem(LOCK_MODE_SLOT, getContentLockItem(false));
                }

                if (voidExcess != null) {
                    voidExcesses.add(l);
                    menu.replaceExistingItem(VOID_MODE_SLOT, getVoidExcessItem(true));
                } else {
                    menu.replaceExistingItem(VOID_MODE_SLOT, getVoidExcessItem(false));
                }

                menu.replaceExistingItem(QUICK_TRANSFER_SLOT, getQuickTransferItem(quickTransferMode));

                // Add lock mode switcher
                menu.addMenuClickHandler(LOCK_MODE_SLOT, (p, slot, item1, action) -> {
                    switchLock(menu, l);
                    return false;
                });

                menu.addMenuClickHandler(VOID_MODE_SLOT, (p, slot, item1, action) -> {
                    switchVoidExcess(menu, l);
                    return false;
                });

                menu.addMenuClickHandler(QUICK_TRANSFER_SLOT, (p, slot, item1, action) -> {
                    if (action.isRightClicked()) {
                        switchQuickTransferMode(menu, l);
                    } else {
                        quickTransfer(menu, l, p);
                    }
                    return false;
                });

                StorageUnitData data = storages.get(l);
                if (data != null) {
                    update(l, true);
                }
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || (canUse(p, false) && Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };

        if (item.getItemId().endsWith("MODEL")) {
            loadConfigurations();
        }
    }

    @Nullable
    public static StorageUnitData getStorageData(Location l) {
        return storages.get(l);
    }

    @Nonnull
    public static Map<Location, StorageUnitData> getAllStorageData() {
        return storages;
    }

    public static void setStorageData(Location l, StorageUnitData data) {
        storages.put(l, data);
    }

    public static void update(Location l, boolean force) {
        BlockMenu menu = StorageCacheUtils.getMenu(l);
        if (menu != null && (force || menu.hasViewer())) {

            StorageUnitData data = storages.get(l);
            if (data == null) {
                requestData(l, getContainerId(l));
                return;
            }
            StorageUnitType sizeType = data.getSizeType();
            int maxEach = sizeType.getEachMaxSize();

            // Update information
            menu.replaceExistingItem(STORAGE_INFO_SLOT, getStorageInfoItem(data.getId(), data.getStoredTypeCount(), sizeType.getMaxItemCount(), maxEach, isLocked(l), isVoidExcess(l)));

            // Update item display
            List<ItemContainer> itemStored = storages.get(l).getStoredItems();
            for (int i = 0; i < DISPLAY_SLOTS.length; i++) {
                if (i < itemStored.size()) {
                    ItemContainer each = itemStored.get(i);
                    menu.replaceExistingItem(DISPLAY_SLOTS[i], getDisplayItem(each.getSample(), each.getAmount(), maxEach));
                } else {
                    menu.replaceExistingItem(DISPLAY_SLOTS[i], ERROR_BORDER);
                }
            }
        }
    }

    public static boolean contains(Location l, ItemStack itemStack) {
        for (ItemContainer each : storages.get(l).getStoredItems()) {
            if (StackUtils.itemsMatch(each.getSample(), itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static int getBoundId(@Nonnull ItemStack item) {
        // Get meta
        final ItemMeta meta = item.getItemMeta();
        Integer id = null;
        // Check if meta has bound id
        if (meta != null && meta.getPersistentDataContainer().has(idKey, PersistentDataType.INTEGER)) {
            id = meta.getPersistentDataContainer().get(idKey, PersistentDataType.INTEGER);
        }
        if (id == null) {
            id = -1;
        }
        return id;
    }

    public static ItemStack bindId(@Nonnull ItemStack itemSample, int id) {
        final ItemStack item = itemSample.clone();
        final ItemMeta meta = item.getItemMeta();
        List<String> lore;
        if (meta != null) {
            lore = meta.getLore();
            if (lore != null) {
                lore.add(ChatColor.BLUE + "ID: " + ChatColor.YELLOW + id);
            } else {
                lore = new ArrayList<>();
                lore.add(ChatColor.BLUE + "ID: " + ChatColor.YELLOW + id);
            }
            meta.setLore(lore);
            meta.getPersistentDataContainer().set(idKey, PersistentDataType.INTEGER, id);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static void addBlockInfo(Location l, int id, boolean lock, boolean voidExcess) {
        // Save id
        StorageCacheUtils.setData(l, "containerId", String.valueOf(id));
        // Save mode
        setLock(l, lock);
        setVoidExcess(l, voidExcess);
    }

    public static boolean isLocked(Location l) {
        return locked.contains(l);
    }

    public static boolean isLocked(int containerId) {
        for (Location l : locked) {
            if (getContainerId(l) == containerId) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVoidExcess(Location l) {
        return voidExcesses.contains(l);
    }

    private static ItemStack getDisplayItem(ItemStack item, int amount, int max) {
        if (item == null) {
            return ERROR_BORDER;
        }
        try {
            return new CustomItemStack(item, (String) null, "", "&bStorage Quantity: &e" + amount + " &7/ &6" + max);
        } catch (NullPointerException e) {
            return item.clone();
        }
    }

    public static void requestData(Location l, int id) {
        if (id == -1) return;
        if (DataStorage.isContainerLoaded(id)) {
            DataStorage.getCachedStorageData(id).ifPresent(data -> storages.put(l, data));
        } else {
            DataStorage.requestStorageData(id);
        }
        addClickHandler(l);
    }

    private static void addClickHandler(Location l) {
        final BlockMenu blockMenu = StorageCacheUtils.getMenu(l);
        if (blockMenu == null) {
            return;
        }
        final StorageUnitData data = storages.get(l);
        // 遍历每一个显示槽
        for (int s : DISPLAY_SLOTS) {
            // 添加点击事件
            blockMenu.addMenuClickHandler(s, (player, slot, clickItem, action) -> {
                final ItemStack itemOnCursor = player.getItemOnCursor();
                if (StackUtils.itemsMatch(clickItem, ERROR_BORDER)) {
                    if (!itemOnCursor.getType().isAir()) {
                        data.depositItemStack(itemOnCursor, false, true);
                    }
                } else {
                    List<Integer> a = new ArrayList<>();
                    for (int i : DISPLAY_SLOTS) {
                        a.add(i);
                    }
                    int index = a.indexOf(slot);
                    final ItemStack take = storages.get(l).getStoredItems().get(index).getSample();

                    final ItemRequest itemRequest = new ItemRequest(take, 1);

                    if (!action.isShiftClicked() || !action.isRightClicked()) {
                        if (action.isRightClicked()) {
                            itemRequest.setAmount(take.getMaxStackSize());
                        } else if (action.isShiftClicked()) {
                            itemRequest.setAmount(take.getMaxStackSize() * 36);
                        }

                        final ItemStack requestedItemStack = data.requestItem(itemRequest);
                        if (requestedItemStack != null) {
                            do {
                                int max = Math.min(requestedItemStack.getAmount(), requestedItemStack.getMaxStackSize());
                                final ItemStack clone = StackUtils.getAsQuantity(requestedItemStack, max);
                                requestedItemStack.setAmount(requestedItemStack.getAmount() - max);
                                final HashMap<Integer, ItemStack> remnant = player.getInventory().addItem(clone);
                                remnant.values().stream().findFirst().ifPresent(leftOver -> data.depositItemStack(leftOver, false));
                            } while (requestedItemStack.getAmount() > 0);
                        }
                    } else {
                        for (ItemStack each : player.getInventory().getStorageContents()) {
                            if (StackUtils.itemsMatch(each, take)) {
                                data.depositItemStack(each, true);
                            }
                        }
                    }
                }
                return false;
            });
        }
    }

    private static ItemStack getStorageInfoItem(int id, int typeCount, int maxType, int maxEach, boolean locked, boolean voidExcess) {
        return new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "&cStorage info", "",
                "&bID: &a" + id,
                "&bItem Type: &e" + typeCount + " &7/ &6" + maxType,
                "&bCapacity Limit: &e" + maxType + " &7* &6" + maxEach,
                "&bLocked Mode: " + (locked ? (ChatColor.DARK_GREEN + "✔") : (ChatColor.DARK_RED + "✘")),
                "&bVoid Mode: " + (voidExcess ? (ChatColor.DARK_GREEN + "✔") : (ChatColor.DARK_RED + "✘"))
        );
    }

    private static void setLock(Location l, boolean lock) {
        if (lock) {
            locked.add(l);
            StorageCacheUtils.setData(l, "locked", "enable");
        }
    }

    private static void setVoidExcess(Location l, boolean voidExcess) {
        if (voidExcess) {
            voidExcesses.add(l);
            StorageCacheUtils.setData(l, "voidExcess", "enable");
        }
    }

    private static void switchQuickTransferMode(BlockMenu blockMenu, Location location) {
        QuickTransferMode mode = quickTransferModes.get(location);
        if (mode == null || mode == QuickTransferMode.TO_QUANTUM) {
            mode = QuickTransferMode.FROM_QUANTUM;
        } else {
            mode = QuickTransferMode.TO_QUANTUM;
        }
        quickTransferModes.put(location, mode);
        blockMenu.replaceExistingItem(QUICK_TRANSFER_SLOT, getQuickTransferItem(mode));
        StorageCacheUtils.setData(location, "quickTransferMode", mode.name());
    }

    private static void quickTransfer(BlockMenu blockMenu, Location location, Player player) {
        final ItemStack itemStack = blockMenu.getItemInSlot(QUANTUM_SLOT);
        if (itemStack == null || itemStack.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "Please put quantum storage in the quantum storage slot");
            return;
        }
        if (itemStack.getAmount() > 1) {
            player.sendMessage(ChatColor.RED + "Quantum storage slots can only hold one item!");
            return;
        }
        final ItemStack toTransfer = blockMenu.getItemInSlot(ITEM_CHOOSE_SLOT);
        if (toTransfer == null || toTransfer.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "Please place the items you want to transfer below");
            return;
        }
        final StorageUnitData thisStorage = storages.get(location);
        for (ItemContainer each : thisStorage.getStoredItems()) {
            final ItemStack sample = each.getSample();
            if (StackUtils.itemsMatch(sample, toTransfer)) {
                final SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);

                if (!(slimefunItem instanceof NetworkQuantumStorage)) {
                    player.sendMessage(ChatColor.RED + "It's not a quantum storage");
                    return;
                }

                final ItemMeta meta = itemStack.getItemMeta();
                QuantumCache quantumCache = DataTypeMethods.getCustom(
                        meta,
                        Keys.QUANTUM_STORAGE_INSTANCE,
                        PersistentQuantumStorageType.TYPE
                );

                final QuickTransferMode mode = quickTransferModes.get(location);
                switch (mode) {
                    case FROM_QUANTUM -> {
                        if (quantumCache == null || quantumCache.getItemStack() == null || quantumCache.getAmount() <= 0) {
                            player.sendMessage(ChatColor.RED + "Quantum storage without items or broken");
                            return;
                        }
                        if (!StackUtils.itemsMatch(quantumCache.getItemStack(), sample)) {
                            player.sendMessage(ChatColor.RED + "The item in quantum storage is different from the item in the storage");
                            return;
                        }
                        final long quantumAmount = quantumCache.getAmount();
                        final int canAdd = (int) Math.min(quantumAmount, thisStorage.getSizeType().getEachMaxSize() - each.getAmount());
                        if (canAdd <= 0) {
                            player.sendMessage(ChatColor.RED + "Not enough items in quantum storage or unable to deposit more items");
                            return;
                        }

                        final int left = (int) quantumAmount - canAdd;
                        if (left > 0) {
                            quantumCache.setAmount(left);
                            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
                            quantumCache.updateMetaLore(meta);
                            itemStack.setItemMeta(meta);
                        } else {
                            blockMenu.replaceExistingItem(QUANTUM_SLOT, slimefunItem.getItem());
                        }
                        final ItemStack clone = quantumCache.getItemStack().clone();
                        clone.setAmount(canAdd);
                        thisStorage.depositItemStack(clone, true);
                        player.sendMessage(ChatColor.GREEN + "Deposit from quantum storage is successful!");
                        return;
                    }

                    case TO_QUANTUM -> {
                        if (each.getAmount() == 0 && locked.contains(location)) {
                            player.sendMessage(ChatColor.RED + "Not enough items in this container to transfer to quantum storage");
                            return;
                        }

                        if (quantumCache == null) {
                            final NetworkQuantumStorage nqs = (NetworkQuantumStorage) slimefunItem;
                            final int quantumLimit = nqs.getMaxAmount();

                            final int unitAmount = each.getAmount();
                            final int canAdd = Math.min(unitAmount, quantumLimit);
                            if (canAdd <= 0) {
                                player.sendMessage(ChatColor.RED + "No more items to transfer or quantum storage is full");
                                return;
                            }
                            final ItemStack clone = sample.clone();

                            thisStorage.requestItem(new ItemRequest(clone, canAdd));
                            storages.put(location, thisStorage);

                            quantumCache = new QuantumCache(clone, canAdd, quantumLimit, false, false);
                            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
                            quantumCache.updateMetaLore(meta);
                            itemStack.setItemMeta(meta);

                            player.sendMessage(ChatColor.GREEN + "Withdraw to quantum storage is successful");
                            return;
                        } else if (StackUtils.itemsMatch(quantumCache.getItemStack(), sample)) {
                            final int quantumLimit = quantumCache.getLimit();
                            final int quantumAmount = (int) quantumCache.getAmount();
                            final int unitAmount = each.getAmount();
                            final int canAdd = Math.min(unitAmount, quantumLimit - quantumAmount);
                            if (canAdd <= 0) {
                                player.sendMessage(ChatColor.RED + "No more items to transfer or quantum storage is full");
                                return;
                            }
                            final ItemStack clone = sample.clone();

                            thisStorage.requestItem(new ItemRequest(clone, canAdd));
                            storages.put(location, thisStorage);

                            quantumCache = new QuantumCache(clone, quantumAmount + canAdd, quantumLimit, false, false);
                            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
                            quantumCache.updateMetaLore(meta);
                            itemStack.setItemMeta(meta);
                            player.sendMessage(ChatColor.GREEN + "Has been withdraw to quantum storage!");
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
        player.sendMessage(ChatColor.RED + "No items found" + toTransfer.getItemMeta().getDisplayName());
    }

    private static int getContainerId(Location l) {
        final String str = StorageCacheUtils.getData(l, "containerId");
        return str == null ? -1 : Integer.parseInt(str);
    }

    private static ItemStack getQuickTransferItem(QuickTransferMode mode) {
        return new CustomItemStack(
                mode == QuickTransferMode.FROM_QUANTUM ? Material.GREEN_CONCRETE_POWDER : Material.BLUE_CONCRETE_POWDER,
                "&6Fast transfer mode",
                "",
                "&bMode: " + (mode == QuickTransferMode.FROM_QUANTUM ? "&aTransfer from quantum storage" : "&cTransfer to quantum storage"),
                " ",
                "&ePut quantum storage above",
                "&ePlace the item to be transferred below",
                " ",
                "&eLeft-click to start the transfer",
                "&eRight-click to switch modes",
                " ",
                "&cThe Cargo Storage Unit must have this item to transfer it！"
        );
    }

    public void loadConfigurations() {
        final String configKey = this.getId();
        FileConfiguration config = Networks.getInstance().getConfig();

        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", DEFAULT_USE_SPECIAL_MODEL);

        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("1", DisplayGroupGenerators::generateStorageUnit_1);
        generatorMap.put("2", DisplayGroupGenerators::generateStorageUnit_2);
        generatorMap.put("3", DisplayGroupGenerators::generateStorageUnit_3);
        generatorMap.put("4", DisplayGroupGenerators::generateStorageUnit_4);
        generatorMap.put("5", DisplayGroupGenerators::generateStorageUnit_5);
        generatorMap.put("6", DisplayGroupGenerators::generateStorageUnit_6);
        generatorMap.put("7", DisplayGroupGenerators::generateStorageUnit_7);
        generatorMap.put("8", DisplayGroupGenerators::generateStorageUnit_8);
        generatorMap.put("9", DisplayGroupGenerators::generateStorageUnit_9);
        generatorMap.put("10", DisplayGroupGenerators::generateStorageUnit_10);
        generatorMap.put("11", DisplayGroupGenerators::generateStorageUnit_11);
        generatorMap.put("12", DisplayGroupGenerators::generateStorageUnit_12);
        generatorMap.put("13", DisplayGroupGenerators::generateStorageUnit_13);

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


    public void onPlace(@Nonnull BlockPlaceEvent e) {
        Location l = e.getBlock().getLocation();
        ItemStack itemInHand = e.getItemInHand();
        Player p = e.getPlayer();
        if (!(p.hasPermission("slimefun.inventory.bypass") || (canUse(p, false) && Slimefun.getProtectionManager().hasPermission(p, e.getBlock(), Interaction.INTERACT_BLOCK)))) {
            e.setCancelled(true);
            return;
        }
        boolean a = false;
        boolean b = false;
        int id = getBoundId(itemInHand);
        if (id != -1) {
            StorageUnitData data = DataStorage.getCachedStorageData(id).orElse(null);
            if (data != null && data.isPlaced() && !l.equals(data.getLastLocation())) {
                // This container already exists and placed in another location
                p.sendMessage(ChatColor.RED + "The container already exists in another location!");
                Location currLoc = data.getLastLocation();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + (currLoc.getWorld() == null ? "Unknown" : currLoc.getWorld().getName()) + " &7| &e" + currLoc.getBlockX() + "&7/&e" + currLoc.getBlockY() + "&7/&e" + currLoc.getBlockZ() + "&7;"));
                e.setCancelled(true);
                if (useSpecialModel) {
                    removeDisplay(l);
                }
                Slimefun.getDatabaseManager().getBlockDataController().removeBlock(l);
                return;
            }
            // Request data
            Location lastLoc;
            if (data != null) {
                lastLoc = data.getLastLocation();
                a = locked.contains(lastLoc);
                b = voidExcesses.contains(lastLoc);
                if (a) {
                    locked.remove(lastLoc);
                }
                if (b) {
                    voidExcesses.remove(lastLoc);
                }
            }
            requestData(l, id);
            // This prevents creative player from getting too many same id item
            if (p.getGameMode() == GameMode.CREATIVE) {
                itemInHand.setAmount(itemInHand.getAmount() - 1);
                p.getEquipment().setItem(e.getHand(), itemInHand);
            }
        } else {
            StorageUnitData data = DataStorage.createStorageUnitData(p, sizeType, l);
            id = data.getId();
            storages.put(l, data);
        }

        BlockMenu menu = StorageCacheUtils.getMenu(l);
        if (menu != null) {
            update(l, true);
            // 如果存在菜单，添加点击事件
            addClickHandler(l);
        }
        if (useSpecialModel) {
            // 如果为 true，执行特殊逻辑
            e.getBlock().setType(Material.BARRIER);
            setupDisplay(e.getBlock().getLocation());
        }
        // Save to block storage
        addBlockInfo(l, id, a, b);
    }

    public void onBreak(@Nonnull BlockBreakEvent e) {
        Block b = e.getBlock();
        Location l = b.getLocation();

        // 如果启用了特殊模型，移除展示组
        if (useSpecialModel) {
            removeDisplay(l);
        }

        // Fix display didn't remove when break
        e.setCancelled(true);
        BlockMenu menu = StorageCacheUtils.getMenu(l);
        if (menu != null) {
            menu.dropItems(l, QUANTUM_SLOT);
            menu.dropItems(l, ITEM_CHOOSE_SLOT);
        }

        // Remove data cache
        StorageUnitData data = storages.remove(l);
        b.setType(Material.AIR);
        // Drop custom item if data exists
        if (data != null) {
            data.setPlaced(false);
            b.getWorld().dropItemNaturally(l, bindId(getItem(), data.getId()));
        } else {
            // Data not loaded, just drop with the stored one.
            int id = getContainerId(l);
            if (id != -1) {
                DataStorage.setContainerStatus(id, false);
                b.getWorld().dropItemNaturally(l, bindId(getItem(), id));
            }
        }
        Slimefun.getDatabaseManager().getBlockDataController().removeBlock(l);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem item, Config conf) {
                onTick(block);
            }
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
                onPlace(blockPlaceEvent);
            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent blockBreakEvent, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {
                onBreak(blockBreakEvent);
            }
        });

    }

    private void onTick(@Nonnull Block block) {
        Location l = block.getLocation();
        StorageUnitData data = storages.get(l);
        if (data == null) {
            requestData(l, getContainerId(l));
            return;
        }
        if (!isLocked(l)) {
            for (ItemContainer each : data.getStoredItems()) {
                if (each.getAmount() == 0) {
                    data.removeItem(each.getId());
                }
            }
        }
        BlockMenu menu = StorageCacheUtils.getMenu(l);
        if (menu == null) {
            return;
        }
        if (!l.equals(data.getLastLocation())) {
            ItemStack itemInBorder = menu.getItemInSlot(0);
            if (data.isPlaced() && itemInBorder != null) {
                menu.replaceExistingItem(STORAGE_INFO_SLOT, getLocationErrorItem(data.getId(), data.getLastLocation()));

                for (int slot : BORDER) {
                    menu.replaceExistingItem(slot, ERROR_BORDER);
                }
                return;
            }
            // Not placed, update state
            data.setPlaced(true);
            data.setLastLocation(l);
        }
        if (menu.hasViewer()) {
            // Update display item
            update(l, false);
        }
    }

    private ItemStack getLocationErrorItem(int id, Location lastLoc) {
        if (lastLoc == null) {
            return new CustomItemStack(Material.REDSTONE_TORCH, "&cPositioning error", "",
                "&eThis container already exists in some other location",
                "&ePlease do not place containers with the same ID in multiple different locations",
                "&eeIf you think this is an issue, please contact an administrator to take care of it",
                " ",
                "&6Storage info:",
                "&bID: &a" + id,
                "&bWorld: &e Unknown",
                "&bLocation: &e Unknown"
            );
        }
        return new CustomItemStack(Material.REDSTONE_TORCH, "&cPositioning error", "",
                "&eThis container already exists in some other location",
                "&ePlease do not place containers with the same ID in multiple different locations",
                "&eeIf you think this is an issue, please contact an administrator to take care of it",
                " ",
                "&Storage info:",
                "&bID: &a" + id,
                "&bWorld: &e" + (lastLoc.getWorld() == null ? "Unknown" : lastLoc.getWorld().getName()),
                "&bLocation: &e" + lastLoc.getBlockX() + " &7/ &e" + lastLoc.getBlockY() + " &7/ &e" + lastLoc.getBlockZ()
        );
    }

    private void switchLock(BlockMenu menu, Location l) {
        if (locked.contains(l)) {
            StorageCacheUtils.removeData(l, "locked");
            locked.remove(l);
            menu.replaceExistingItem(LOCK_MODE_SLOT, getContentLockItem(false));
        } else {
            StorageCacheUtils.setData(l, "locked", "enable");
            locked.add(l);
            menu.replaceExistingItem(LOCK_MODE_SLOT, getContentLockItem(true));
        }
    }

    private void switchVoidExcess(BlockMenu menu, Location l) {
        if (voidExcesses.contains(l)) {
            StorageCacheUtils.removeData(l, "voidExcess");
            voidExcesses.remove(l);
            menu.replaceExistingItem(VOID_MODE_SLOT, getVoidExcessItem(false));
        } else {
            StorageCacheUtils.setData(l, "voidExcess", "enable");
            voidExcesses.add(l);
            menu.replaceExistingItem(VOID_MODE_SLOT, getVoidExcessItem(true));
        }
    }

    private ItemStack getContentLockItem(boolean locked) {
        return new CustomItemStack(
                locked ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                "&6Locked Mode",
                "",
                "&bStatus: " + (locked ? "&cLocked" : "&aUnlocked"),
                "",
                "&7When the container is locked，Will ONLY allow of currently-existing items",
                "&7And the output will leave at last one item",
                locked ? "&eClick on Disable" : "&eClick on Enable"
        );
    }

    private ItemStack getVoidExcessItem(boolean voidExcess) {
        return new CustomItemStack(
                voidExcess ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE,
                "&6Void Mode",
                "",
                "&bStatus: " + (voidExcess ? "&aEnable" : "&cDisable"),
                "",
                "&7Turned on to void items that can't be stored",
                voidExcess ? "&eClick on Disable" : "&eClick on Enable"
        );
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
    public boolean canStack(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getPersistentDataContainer().equals(meta2.getPersistentDataContainer());
    }
}
