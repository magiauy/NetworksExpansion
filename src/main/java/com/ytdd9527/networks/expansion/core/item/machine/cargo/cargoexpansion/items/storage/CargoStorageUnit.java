package com.ytdd9527.networks.expansion.core.item.machine.cargo.cargoexpansion.items.storage;


import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.core.item.machine.cargo.cargoexpansion.data.DataStorage;
import com.ytdd9527.networks.expansion.core.item.machine.cargo.cargoexpansion.objects.ItemContainer;
import com.ytdd9527.networks.expansion.setup.ExpansionItemStacks;
import com.ytdd9527.networks.expansion.util.DisplayGroupGenerators;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
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
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;

//!TODO 对于一些复杂的逻辑，需要重构
public class CargoStorageUnit extends NetworkObject {

    private static final Map<Location, StorageUnitData> storages = new HashMap<>();
    private static final Map<Location, TransportMode> transportModes = new HashMap<>();
    private static final Map<Location, QuickTransferMode> quickTransferModes = new HashMap<>();
    private static final Map<Location, CargoReceipt> cargoRecords = new HashMap<>();
    private static final Set<Location> locked = new HashSet<>();
    private static final Set<Location> voidExcesses = new HashSet<>();

    private Function<Location, DisplayGroup> displayGroupGenerator;
    private static final String KEY_UUID = "display-uuid";
    private boolean useSpecialModel;

    private static final int[] displaySlots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43,46,47,48,49,50,51,52};
    private static final int storageInfoSlot = 4;
    private static final NamespacedKey idKey = new NamespacedKey(Networks.getInstance(),"CONTAINER_ID");
    private final StorageUnitType sizeType;
    private final int[] border = {0,1,2,3,5,6,17,26,35,36,44,45,53};
    private final int voidModeSlot = 7;
    private final int lockModeSlot = 8;
    private static final int quantumSlot = 9;
    private static final int quickTransferSlot = 18;
    private static final int itemChooseSlot = 27;
    private static final ItemStack errorBorder = new CustomItemStack(Material.BARRIER, " ", " ", " ", " ");

    public CargoStorageUnit(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, StorageUnitType sizeType) {
        super(itemGroup, item, recipeType, recipe, NodeType.MODEL);

        this.sizeType = sizeType;

        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                for (int slot : border) {
                    addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(storageInfoSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(lockModeSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(voidModeSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Block b) {
                Location l = b.getLocation();
                requestData(l, getContainerId(l));
                // Restore mode
                SlimefunBlockData blockData = StorageCacheUtils.getBlock(l);
                String modeStr = null;
                String lock = null;
                String voidExcess = null;
                String quickModeStr = null;
                if (blockData != null) {
                    modeStr = blockData.getData("transportMode");
                    lock = blockData.getData("locked");
                    voidExcess = blockData.getData("voidExcess");
                    quickModeStr = blockData.getData("quickTransferMode");
                }
                TransportMode mode = modeStr == null ? TransportMode.REJECT : TransportMode.valueOf(modeStr);
                transportModes.put(l, mode);
                QuickTransferMode quickTransferMode = quickModeStr == null ? QuickTransferMode.FROM_QUANTUM : QuickTransferMode.valueOf(quickModeStr);
                quickTransferModes.put(l, quickTransferMode);
                if(lock != null) {
                    locked.add(l);
                    menu.replaceExistingItem(lockModeSlot, getContentLockItem(true));
                } else {
                    menu.replaceExistingItem(lockModeSlot, getContentLockItem(false));
                }

                if(voidExcess != null) {
                    voidExcesses.add(l);
                    menu.replaceExistingItem(voidModeSlot, getVoidExcessItem(true));
                } else {
                    menu.replaceExistingItem(voidModeSlot, getVoidExcessItem(false));
                }

                menu.replaceExistingItem(quickTransferSlot, getQuickTransferItem(quickTransferMode));

                // Add lock mode switcher
                menu.addMenuClickHandler(lockModeSlot, (p, slot, item1, action) -> {
                    switchLock(menu, l);
                    return false;
                });

                menu.addMenuClickHandler(voidModeSlot, (p, slot, item1, action) -> {
                    switchVoidExcess(menu, l);
                    return false;
                });

                menu.addMenuClickHandler(quickTransferSlot, (p, slot, item1, action) -> {
                    if (action.isRightClicked()) {
                        switchQuickTransferMode(menu, l);
                    } else {
                        quickTransfer(menu, l, p);
                    }
                    return false;
                });

                StorageUnitData data = storages.get(l);
                if (data != null) {
                    CargoReceipt receipt = cargoRecords.get(l);
                    if (receipt != null) {
                        update(l, receipt, true);
                    } else {
                        update(l, new CargoReceipt(data.getId(), 0, 0, data.getTotalAmount(), data.getStoredTypeCount(), data.getSizeType()), true);
                    }
                }
            }

            @Override
            public boolean canOpen(@NotNull Block b, @NotNull Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || (canUse(p, false) && Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };

    }
    public CargoStorageUnit(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, StorageUnitType sizeType, String itemId) {
        super(itemGroup, item, recipeType, recipe, NodeType.CELL);

        this.sizeType = sizeType;

        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                for (int slot : border) {
                    addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(storageInfoSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(lockModeSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(voidModeSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(quickTransferSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Block b) {
                Location l = b.getLocation();
                requestData(l, getContainerId(l));
                // Restore mode
                SlimefunBlockData blockData = StorageCacheUtils.getBlock(l);
                String modeStr = null;
                String lock = null;
                String voidExcess = null;
                String quickModeStr = null;
                if (blockData != null) {
                    modeStr = blockData.getData("transportMode");
                    lock = blockData.getData("locked");
                    voidExcess = blockData.getData("voidExcess");
                    quickModeStr = blockData.getData("quickTransferMode");
                }
                TransportMode mode = modeStr == null ? TransportMode.REJECT : TransportMode.valueOf(modeStr);
                transportModes.put(l, mode);
                QuickTransferMode quickTransferMode = quickModeStr == null ? QuickTransferMode.FROM_QUANTUM : QuickTransferMode.valueOf(quickModeStr);
                quickTransferModes.put(l, quickTransferMode);
                if(lock != null) {
                    locked.add(l);
                    menu.replaceExistingItem(lockModeSlot, getContentLockItem(true));
                } else {
                    menu.replaceExistingItem(lockModeSlot, getContentLockItem(false));
                }

                if(voidExcess != null) {
                    voidExcesses.add(l);
                    menu.replaceExistingItem(voidModeSlot, getVoidExcessItem(true));
                } else {
                    menu.replaceExistingItem(voidModeSlot, getVoidExcessItem(false));
                }

                menu.replaceExistingItem(quickTransferSlot, getQuickTransferItem(quickTransferMode));

                // Add lock mode switcher
                menu.addMenuClickHandler(lockModeSlot, (p, slot, item1, action) -> {
                    switchLock(menu, l);
                    return false;
                });

                menu.addMenuClickHandler(voidModeSlot, (p, slot, item1, action) -> {
                    switchVoidExcess(menu, l);
                    return false;
                });

                menu.addMenuClickHandler(quickTransferSlot, (p, slot, item1, action) -> {
                    if (action.isRightClicked()) {
                        switchQuickTransferMode(menu, l);
                    } else {
                        quickTransfer(menu, l, p);
                    }
                    return false;
                });

                StorageUnitData data = storages.get(l);
                if (data != null) {
                    CargoReceipt receipt = cargoRecords.get(l);
                    if (receipt != null) {
                        update(l, receipt, true);
                    } else {
                        update(l, new CargoReceipt(data.getId(), 0, 0, data.getTotalAmount(), data.getStoredTypeCount(), data.getSizeType()), true);
                    }
                }
            }

            @Override
            public boolean canOpen(@NotNull Block b, @NotNull Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || (canUse(p, false) && Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
        loadConfigurations(itemId);
    }

    private void loadConfigurations(String itemId) {
        FileConfiguration config = Networks.getInstance().getConfig();


        boolean defaultUseSpecialModel = false;
        this.useSpecialModel = config.getBoolean("items." + itemId + ".use-special-model.enable", defaultUseSpecialModel);


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
            String generatorKey = config.getString("items." + itemId + ".use-special-model.type");
            this.displayGroupGenerator = generatorMap.get(generatorKey);
            if (this.displayGroupGenerator == null) {
                Networks.getInstance().getLogger().warning("未知的展示组类型 '" + generatorKey + "', 特殊模型已禁用。");
                this.useSpecialModel = false;
            }
        }

    }
    @Nullable
    public static StorageUnitData getStorageData(Location l) {
        return storages.get(l);
    }

    @Nullable
    public static Map<Location, StorageUnitData> getAllStorageData() {
        return storages;
    }

    @Nullable
    public static void setStorageData(Location l, StorageUnitData data) {
        storages.put(l, data);
    }

    @NotNull
    public static TransportMode getTransportMode(Location l) {
        return transportModes.getOrDefault(l, TransportMode.REJECT);
    }

    public static void update(Location l, CargoReceipt receipt, boolean force) {
        BlockMenu menu = StorageCacheUtils.getMenu(l);
        if (menu != null && (force||menu.hasViewer())) {

            int maxEach = receipt.getSizeType().getEachMaxSize();

            // Update information
            menu.replaceExistingItem(storageInfoSlot, getStorageInfoItem(receipt.getContainerId(), receipt.getTypeCount(),receipt.getSizeType().getMaxItemCount(),maxEach, isLocked(l), isVoidExcess(l)));

            // Update item display
            List<ItemContainer> itemStored = storages.get(l).getStoredItems();
            for (int i = 0; i < displaySlots.length; i++) {
                if (i < itemStored.size()) {
                    ItemContainer each = itemStored.get(i);
                    menu.replaceExistingItem(displaySlots[i], getDisplayItem(each.getSample(), each.getAmount(), maxEach));
                } else {
                    menu.replaceExistingItem(displaySlots[i], errorBorder);
                }
            }
        }
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent e) {
                Location l = e.getBlock().getLocation();
                ItemStack itemInHand = e.getItemInHand();
                Player p = e.getPlayer();
                if (!canUse(p, true) || !Slimefun.getProtectionManager().hasPermission(p, l, Interaction.INTERACT_BLOCK)) {
                    return;
                }
                boolean a = false;
                boolean b = false;
                int id = getBoundId(itemInHand);
                if (id != -1) {
                    StorageUnitData data = DataStorage.getCachedStorageData(id).orElse(null);
                    if (data != null && data.isPlaced() && !l.equals(data.getLastLocation())) {
                        // This container already exists and placed in another location
                        p.sendMessage(ChatColor.RED + "该容器已在其它位置存在！");
                        Location currLoc = data.getLastLocation();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + (currLoc.getWorld() == null ? "Unknown" : currLoc.getWorld().getName()) + " &7| &e" + currLoc.getBlockX() + "&7/&e" + currLoc.getBlockY() + "&7/&e" + currLoc.getBlockZ() + "&7;"));
                        e.setCancelled(true);
                        Slimefun.getDatabaseManager().getBlockDataController().removeBlock(l);
                        if (useSpecialModel) {
                            removeDisplay(l);
                        }
                        return;
                    }
                    // Request data
                    Location lastLoc = null;
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
                        itemInHand.setAmount(itemInHand.getAmount()-1);
                        p.getEquipment().setItem(e.getHand(), itemInHand);
                    }
                } else {
                    StorageUnitData data = DataStorage.createStorageUnitData(p, sizeType, l);
                    id = data.getId();
                    storages.put(l, data);
                }

                StorageUnitData cache = storages.get(l);
                BlockMenu menu = StorageCacheUtils.getMenu(l);
                if (menu != null) {
                    update(l, new CargoReceipt(id, 0, 0, cache.getTotalAmount(), cache.getStoredTypeCount(), cache.getSizeType()), true);
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
        });


        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                e.setCancelled(true);
                Block b = e.getBlock();
                Location l = b.getLocation();

                // 如果启用了特殊模型，移除展示组
                if (useSpecialModel) {
                    removeDisplay(l);
                }

                // Remove data cache
                StorageUnitData data = storages.remove(l);

                // Remove block
                Slimefun.getDatabaseManager().getBlockDataController().removeBlock(l);
                b.setType(Material.AIR);

                // Drop custom item if data exists
                if(data != null) {
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

                // Remove cache
                transportModes.remove(l);
                cargoRecords.remove(l);
            }
        });

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

    }

    private void onTick(@Nonnull Block block) {
        Location l = block.getLocation();
        StorageUnitData data = storages.get(l);
        if (data == null) {
            requestData(l, getContainerId(l));
            return;
        }
        for (ItemContainer each : data.getStoredItems()) {
            if (each.getAmount() == 0) {
                data.removeItem(each.getId());
            }
        }
        BlockMenu menu = StorageCacheUtils.getMenu(l);
        if (menu == null) {
            return;
        }
        if (!l.equals(data.getLastLocation())) {
            ItemStack itemInBorder = menu.getItemInSlot(0);
            if (data.isPlaced() && itemInBorder != null) {
                menu.replaceExistingItem(storageInfoSlot, getLocationErrorItem(data.getId(), data.getLastLocation()));

                for (int slot : border) {
                    menu.replaceExistingItem(slot, errorBorder);
                }
                return;
            }
            // Not placed, update state
            data.setPlaced(true);
            data.setLastLocation(l);
        }
        if(menu.hasViewer()) {
            // Update display item
            CargoReceipt receipt = cargoRecords.get(l);
            if(receipt != null) {
                CargoStorageUnit.update(l, receipt, false);
            } else {
                CargoStorageUnit.update(l, new CargoReceipt(data.getId(), 0, 0, data.getTotalAmount(), data.getStoredTypeCount(), data.getSizeType()), false);
            }
        }
    }

    public static boolean canAddMoreType(Location l, ItemStack itemStack) {
        return storages.get(l).getStoredItems().size() < storages.get(l).getSizeType().getMaxItemCount() && !isLocked(l) && !CargoStorageUnit.contains(l, itemStack);
    }

    public static boolean contains(Location l, ItemStack itemStack) {
        for (ItemContainer each : storages.get(l).getStoredItems()) {
            if (StackUtils.itemsMatch(each.getSample(), itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static int getBoundId(@NotNull ItemStack item) {
        // Get meta
        ItemMeta meta = item.getItemMeta();
        int id = -1;
        // Check if meta has bound id
        if (meta != null && meta.getPersistentDataContainer().has(idKey, PersistentDataType.INTEGER)) {
            id = meta.getPersistentDataContainer().get(idKey, PersistentDataType.INTEGER);
        }
        return id;
    }

    public static ItemStack bindId(@NotNull ItemStack itemSample, int id) {
        ItemStack item = itemSample.clone();
        ItemMeta meta = item.getItemMeta();
        List<String> lore;
        if (meta != null) {
            lore = meta.getLore();
            if (lore != null) {
                lore.add(ChatColor.BLUE + "已绑定容器ID: " + ChatColor.YELLOW + id);
            } else {
                lore = new ArrayList<>();
                lore.add(ChatColor.BLUE + "已绑定容器ID: " + ChatColor.YELLOW + id);
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
        setMode(l);
        setLock(l, lock);
        setVoidExcess(l, voidExcess);
    }

    public static boolean isLocked(Location l) {
        return locked.contains(l);
    }

    public static boolean isVoidExcess(Location l) {
        return voidExcesses.contains(l);
    }

    private static ItemStack getDisplayItem(ItemStack item, int amount, int max) {
        if (item == null) {
            return errorBorder;
        }
        try {
            return new CustomItemStack(item, (String) null, "", "&b存储数量: &e" + amount + " &7/ &6" + max);
        } catch (NullPointerException e) {
            return item.clone();
        }
    }

    public static void requestData(Location l, int id) {
        if (id == -1) return;
        if (DataStorage.isContainerLoaded(id)) {
            DataStorage.getCachedStorageData(id).ifPresent(data -> {
                storages.put(l, data);
            });
        } else {
            DataStorage.requestStorageData(id);
        }
        addClickHandler(l);
    }

    private static void addClickHandler(Location l) {
        BlockMenu blockMenu = StorageCacheUtils.getMenu(l);
        if (blockMenu == null) {
            return;
        }
        StorageUnitData data = storages.get(l);
        // 遍历每一个显示槽
        for (int s : displaySlots) {
            // 添加点击事件
            blockMenu.addMenuClickHandler(s, (player, slot, clickItem, action) -> {
                ItemStack itemOnCursor = player.getItemOnCursor();
                if (StackUtils.itemsMatch(clickItem, errorBorder)) {
                    // 如果点击的是空白
                    if (itemOnCursor.getType() != Material.AIR) {
                        ItemStack clone = itemOnCursor.clone();
                        data.depositItemStack(clone, false, true);
                        if (clone.getAmount() != 0) {
                            // 如果存储可以增加更多类型
                            if (canAddMoreType(l, clone)) {
                                // 存储光标上的物品
                                data.depositItemStack(clone, true);
                            }
                        }
                        // 更新玩家光标上的物品
                        player.setItemOnCursor(clone);
                        // 数据更新
                        CargoStorageUnit.update(l, new CargoReceipt(data.getId(), itemOnCursor.getAmount(), 0, data.getTotalAmount(), data.getStoredTypeCount(), data.getSizeType()), true);
                    }
                } else {
                    // 如果点击的不是空白
                    int camount = 1;
                    if (action.isRightClicked()) {
                        camount = 64;
                    }

                    // 获取实际物品
                    int index = -1;
                    for (int i = 0; i < displaySlots.length; i++) {
                        if (displaySlots[i] == slot) {
                            index = i;
                            break;
                        }
                    }
                    if (index == -1) {
                        return false;
                    }
                    ItemStack clone = storages.get(l).getStoredItems().get(index).getSample().clone();


                    camount = Math.min(camount, clone.getMaxStackSize());
                    if (action.isShiftClicked()) {
                        // 如果Shift，加到背包里

                        // 取出物品
                        ItemStack requestingStack = data.requestItem(new ItemRequest(clone, camount));
                        if (requestingStack == null) {
                            return false;
                        }

                        HashMap<Integer, ItemStack> remnant = player.getInventory().addItem(requestingStack);
                        requestingStack = remnant.values().stream().findFirst().orElse(null);
                        if (requestingStack != null) {
                            data.depositItemStack(requestingStack, false);
                        }
                    } else {
                        // 没有Shift，放到光标上

                        // 取出物品
                        ItemStack requestingStack = data.requestItem(new ItemRequest(clone, camount));
                        if (requestingStack == null) {
                            return false;
                        }

                        if (itemOnCursor.getType() == Material.AIR) {
                            // 如果光标是空气，直接替换就行
                            player.setItemOnCursor(requestingStack.clone());
                        } else if (StackUtils.itemsMatch(requestingStack, itemOnCursor)) {
                            // 如果不是空气并且物品相同
                            ItemStack cursorClone = itemOnCursor.clone();
                            int cursorAmount = cursorClone.getAmount();
                            int takeAmount = requestingStack.getAmount();
                            if (cursorAmount + takeAmount <= cursorClone.getMaxStackSize()) {
                                // 可以直接存储
                                cursorClone.setAmount(cursorAmount + takeAmount);
                            } else {
                                // 超过最大容量，只取最大容量
                                cursorClone.setAmount(cursorClone.getMaxStackSize());
                                // 设置剩余的数量
                                requestingStack.setAmount(takeAmount - (cursorClone.getMaxStackSize() - cursorAmount));
                                // 数据更新
                                data.depositItemStack(requestingStack, false);
                            }
                            // 更新玩家光标上的物品
                            player.setItemOnCursor(cursorClone);
                        }
                    }
                }
                return false;
            });
        }
    }

    private static ItemStack getStorageInfoItem(int id, int typeCount, int maxType, int maxEach, boolean locked, boolean voidExcess) {
        return new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "&c存储信息", "",
                "&b容器ID: &a"+id,
                "&b物品种类: &e"+typeCount+" &7/ &6"+maxType,
                "&b容量上限: &e"+maxType+" &7* &6"+maxEach,
                "&b内容锁定模式: " + (locked ? (ChatColor.DARK_GREEN + "✔") : (ChatColor.DARK_RED + "✘")),
                "&b满载清空模式: " + (voidExcess ? (ChatColor.DARK_GREEN + "✔") : (ChatColor.DARK_RED + "✘"))
        );
    }

    private static void setMode(Location l) {
        transportModes.put(l, TransportMode.ACCEPT);
        StorageCacheUtils.setData(l, "transportMode", TransportMode.ACCEPT.name());
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
        blockMenu.replaceExistingItem(quickTransferSlot, getQuickTransferItem(mode));
        StorageCacheUtils.setData(location, "quickTransferMode", mode.name());
    }

    private static void quickTransfer(BlockMenu blockMenu, Location location, Player player) {
        ItemStack itemStack = blockMenu.getItemInSlot(quantumSlot);
        if (itemStack.getAmount() > 1) {
            player.sendMessage(ChatColor.RED + "量子存储槽只能放入一个物品！");
            return;
        }
        ItemStack toTransfer = blockMenu.getItemInSlot(itemChooseSlot);
        if (toTransfer == null || toTransfer.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "请在下方放入你要传输的物品");
            return;
        }
        StorageUnitData thisStorage = storages.get(location);
        for (ItemContainer each : thisStorage.getStoredItems()) {
            ItemStack sample = each.getSample();
            if (StackUtils.itemsMatch(sample, toTransfer)) {
                SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);

                if (!(slimefunItem instanceof NetworkQuantumStorage)) {
                    player.sendMessage(ChatColor.RED + "这不是一个量子存储");
                    return;
                }

                ItemMeta meta = itemStack.getItemMeta();
                QuantumCache quantumCache = DataTypeMethods.getCustom(
                        meta,
                        Keys.QUANTUM_STORAGE_INSTANCE,
                        PersistentQuantumStorageType.TYPE
                );

                QuickTransferMode mode = quickTransferModes.get(location);
                switch (mode) {
                    case FROM_QUANTUM -> {
                        if (quantumCache == null || quantumCache.getItemStack() == null || quantumCache.getAmount() <= 0) {
                            player.sendMessage(ChatColor.RED + "量子存储无物品或已损坏");
                            return;
                        }
                        if (!StackUtils.itemsMatch(quantumCache.getItemStack(), sample)) {
                            player.sendMessage(ChatColor.RED + "量子存储中的物品与要传输的物品不同");
                            return;
                        }
                        long quantumAmount = quantumCache.getAmount();
                        int canAdd = (int) Math.min(quantumAmount, thisStorage.getSizeType().getEachMaxSize() - each.getAmount());
                        if (canAdd <= 0) {
                            player.sendMessage(ChatColor.RED + "量子存储中没有足够的物品或无法存入更多的物品");
                            return;
                        }

                        quantumCache.setAmount((int)quantumAmount - canAdd);
                        DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
                        quantumCache.updateMetaLore(meta);
                        itemStack.setItemMeta(meta);

                        ItemStack clone = quantumCache.getItemStack().clone();
                        clone.setAmount(canAdd);
                        thisStorage.depositItemStack(clone, true);
                        player.sendMessage(ChatColor.GREEN + "已存入物品！");
                        return;
                    }

                    case TO_QUANTUM -> {
                        if (each.getAmount() == 1 && locked.contains(location)) {
                            player.sendMessage(ChatColor.RED + "此容器物品不足，无法转移至量子存储");
                            return;
                        }

                        if (quantumCache == null) {
                            Map<SlimefunItemStack, Integer> quantumMap = new HashMap<>();
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_0, 64);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_1, 4096);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_2, 32768);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_3, 262144);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_4, 2097152);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_5, 16777216);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_6, 134217728);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_7, 1073741824);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_8, Integer.MAX_VALUE);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_9, 256);
                            quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_10, 1024);
                            quantumMap.put(ExpansionItemStacks.ADVANCED_QUANTUM_STORAGE, Integer.MAX_VALUE);

                            Integer quantumLimit = quantumMap.get(slimefunItem.getItem());
                            if (quantumLimit == null) {
                                player.sendMessage(ChatColor.RED + "该量子存储不支持快速转移");
                                return;
                            }
                            int unitAmount = each.getAmount();
                            if (locked.contains(location)) {
                                unitAmount -= 1;
                            }
                            int canAdd = Math.min(unitAmount, quantumLimit);
                            if (canAdd <= 0) {
                                player.sendMessage(ChatColor.RED + "没有更多物品可以转移或量子存储已满");
                                return;
                            }
                            ItemStack clone = sample.clone();

                            thisStorage.requestItem(new ItemRequest(clone, canAdd));
                            storages.put(location, thisStorage);

                            quantumCache = new QuantumCache(clone, canAdd, quantumLimit, false, false);
                            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
                            quantumCache.updateMetaLore(meta);
                            itemStack.setItemMeta(meta);

                            player.sendMessage(ChatColor.GREEN + "已转移至量子存储！");
                            return;
                        } else if (StackUtils.itemsMatch(quantumCache.getItemStack(), sample)) {
                            int quantumLimit = quantumCache.getLimit();
                            int quantumAmount = (int) quantumCache.getAmount();
                            int unitAmount = each.getAmount();
                            if (locked.contains(location)) {
                                unitAmount -= 1;
                            }
                            int canAdd = Math.min(unitAmount, quantumLimit - quantumAmount);
                            if (canAdd <= 0) {
                                player.sendMessage(ChatColor.RED + "没有更多物品可以转移或量子存储已满");
                                return;
                            }
                            ItemStack clone = sample.clone();

                            thisStorage.requestItem(new ItemRequest(clone, canAdd));
                            storages.put(location, thisStorage);

                            quantumCache = new QuantumCache(clone, quantumAmount + canAdd, quantumLimit, false, false);
                            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
                            quantumCache.updateMetaLore(meta);
                            itemStack.setItemMeta(meta);
                            player.sendMessage(ChatColor.GREEN + "已转移至量子存储！");
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
        }
        player.sendMessage(ChatColor.RED + "未找到物品" + ItemStackHelper.getDisplayName(toTransfer));
    }

    private ItemStack getLocationErrorItem(int id, Location lastLoc) {
        return new CustomItemStack(Material.REDSTONE_TORCH, "&c位置错误", "",
                "&e这个容器已在其它位置存在",
                "&e请不要将同ID的容器放在多个不同的位置",
                "&e如果您认为这是个意外，请联系管理员处理",
                " ",
                "&6容器信息:",
                "&b容器ID: &a" + id,
                "&b所在世界: &e" + (lastLoc.getWorld() == null ? "Unknown" : lastLoc.getWorld().getName()),
                "&b所在坐标: &e" + lastLoc.getBlockX() + " &7/ &e" + lastLoc.getBlockY() + " &7/ &e" + lastLoc.getBlockZ()
        );
    }

    private int getContainerId(Location l) {
        String str = StorageCacheUtils.getData(l, "containerId");
        return str == null ? -1 : Integer.parseInt(str);
    }

    private void switchLock(BlockMenu menu, Location l) {
        if (locked.contains(l)) {
            StorageCacheUtils.removeData(l, "locked");
            locked.remove(l);
            menu.replaceExistingItem(lockModeSlot, getContentLockItem(false));
        } else {
            StorageCacheUtils.setData(l, "locked", "enable");
            locked.add(l);
            menu.replaceExistingItem(lockModeSlot, getContentLockItem(true));
        }
    }

    private void switchVoidExcess(BlockMenu menu, Location l) {
        if (voidExcesses.contains(l)) {
            StorageCacheUtils.removeData(l, "voidExcess");
            voidExcesses.remove(l);
            menu.replaceExistingItem(voidModeSlot, getVoidExcessItem(false));
        } else {
            StorageCacheUtils.setData(l, "voidExcess", "enable");
            voidExcesses.add(l);
            menu.replaceExistingItem(voidModeSlot, getVoidExcessItem(true));
        }
    }

    private ItemStack getContentLockItem(boolean locked) {
        return new CustomItemStack(
                locked ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                "&6内容锁定模式",
                "",
                "&b状态: " + (locked ? "&c已锁定" : "&a未锁定"),
                "",
                "&7当容器锁定后，将仅允许当前存在的物品输入",
                "&7并且输出时将会留下至少一个物品",
                locked ? "&e点击禁用" : "&e点击启用"
        );
    }

    private ItemStack getVoidExcessItem(boolean voidExcess) {
        return new CustomItemStack(
                voidExcess ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE,
                "&6满载清空模式",
                "",
                "&b状态: " + (voidExcess ? "&a已开启" : "&c未开启"),
                "",
                "&7开启此模式后，超过存储上限的物品的数量不会再增加，但仍能存入",
                voidExcess ? "&e点击禁用" : "&e点击启用"
        );
    }

    private static ItemStack getQuickTransferItem(QuickTransferMode mode) {
        return new CustomItemStack(
                mode == QuickTransferMode.FROM_QUANTUM ? Material.GREEN_CONCRETE_POWDER : Material.BLUE_CONCRETE_POWDER,
                "&6快速转移模式",
                "",
                "&b状态: " + (mode == QuickTransferMode.FROM_QUANTUM ? "&a从量子存储转移" : "&c转移至量子存储"),
                " ",
                "&e在上方放入量子存储",
                "&e在下方放入要转移的物品",
                " ",
                "&e点击左键开始转移",
                "&e点击右键切换模式",
                " ",
                "&c需要货运单元中存在此物品才能运输！"
        );
    }

    public static void putRecord(Location l, CargoReceipt receipt) {
        cargoRecords.put(l, receipt);
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
    @javax.annotation.Nullable
    private UUID getDisplayGroupUUID(@Nonnull Location location) {
        String uuid = StorageCacheUtils.getData(location, KEY_UUID);
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
    }
    @javax.annotation.Nullable
    private DisplayGroup getDisplayGroup(@Nonnull Location location) {
        UUID uuid = getDisplayGroupUUID(location);
        if (uuid == null) {
            return null;
        }
        return DisplayGroup.fromUUID(uuid);
    }

}
