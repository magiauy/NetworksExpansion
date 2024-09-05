package io.github.sefiraat.networks.commands;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.data.ItemContainer;
import com.ytdd9527.networksexpansion.api.data.StorageUnitData;
import com.ytdd9527.networksexpansion.implementation.items.ExpansionItemStacks;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.CraftingBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.machines.unit.CargoStorageUnit;
import com.ytdd9527.networksexpansion.utils.databases.DataSource;
import com.ytdd9527.networksexpansion.utils.databases.DataStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.stackcaches.BlueprintInstance;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentCraftingBlueprintType;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class NetworksMain implements TabExecutor {

    private static Location POS1 = null;
    private static Location POS2 = null;

    public static void restore(Player p) {
        Block target = p.getTargetBlockExact(5);
        if (target == null || target.getType().isAir()) {
            p.sendMessage(ChatColor.RED + "请指向一个失效的网络抽屉单元");
            return;
        }
        Location l = target.getLocation();
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(l);
        if (blockData != null) {
            String id = blockData.getData("containerId");
            if (id != null) {
                p.sendMessage(ChatColor.RED + "该单元的数据正常，无需恢复。");
                return;
            }
        } else {
            p.sendMessage(ChatColor.GREEN + "正在查询，请稍候...");
            DataStorage.restoreFromLocation(l, opData -> {
                if (opData.isPresent()) {
                    StorageUnitData data = opData.get();
                    String sfId = ExpansionItemStacks.getStorageItemFromType(data.getSizeType()).getItemId();

                    CargoStorageUnit.addBlockInfo(l, data.getId(), false, false);
                    Slimefun.getDatabaseManager().getBlockDataController().createBlock(l, sfId);
                    p.sendMessage(ChatColor.GREEN + "已成功恢复！");
                } else {
                    p.sendMessage(ChatColor.RED + "未找到数据。");
                    return;
                }
            });
        }
    }

    public static void setQuantum(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }

        Location targetLocation = targetBlock.getLocation();
        ItemStack clone = itemInHand.clone();
        if (slimefunItem instanceof NetworkQuantumStorage) {
            BlockMenu blockMenu = StorageCacheUtils.getMenu(targetLocation);
            if (blockMenu == null) {
                player.sendMessage(Theme.ERROR + "Cannot set item for air");
                return;
            }

            NetworkQuantumStorage.setItem(blockMenu, clone, amount);
            final QuantumCache cache = NetworkQuantumStorage.getCaches().get(blockMenu.getLocation());

            clone.setAmount(1);
            cache.setItemStack(clone);
            cache.setAmount(amount);
            NetworkQuantumStorage.updateDisplayItem(blockMenu, cache);
            NetworkQuantumStorage.syncBlock(blockMenu.getLocation(), cache);
            NetworkQuantumStorage.getCaches().put(blockMenu.getLocation(), cache);
        } else {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }
    }

    private static void addStorageItem(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (slimefunItem instanceof CargoStorageUnit) {
            Location targetLocation = targetBlock.getLocation();
            ItemStack clone = itemInHand.clone();
            StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

            if (data == null) {
                player.sendMessage(Theme.ERROR + "该网络抽屉不存在或已损坏!");
                return;
            }

            clone.setAmount(amount);
            data.depositItemStack(clone, false);
            CargoStorageUnit.setStorageData(targetLocation, data);
            player.sendMessage(ChatColor.GREEN + "已更新物品");
        } else {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }
    }

    private static void reduceStorageItem(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (slimefunItem instanceof CargoStorageUnit) {
            Location targetLocation = targetBlock.getLocation();
            ItemStack clone = itemInHand.clone();
            StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

            if (data == null) {
                player.sendMessage(Theme.ERROR + "该网络抽屉不存在或已损坏!");
                return;
            }

            clone.setAmount(1);
            data.requestItem(new ItemRequest(clone, amount));
            CargoStorageUnit.setStorageData(targetLocation, data);
            player.sendMessage(ChatColor.GREEN + "已更新物品");
        } else {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }
    }

    public static void setContainerId(Player player, int containerId) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (!(slimefunItem instanceof CargoStorageUnit)) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        Location location = targetBlock.getLocation();

        player.sendMessage(ChatColor.GREEN + "已请求数据，请稍候...");
        CargoStorageUnit.requestData(location, containerId);
        player.sendMessage(ChatColor.GREEN +
                "已设置位于为" + location.getWorld().getName()
                + " " + location.getBlockX()
                + " " + location.getBlockY()
                + " " + location.getBlockZ()
                + " 的网络抽屉的容器ID为" + containerId + ".");
    }

    public static void worldeditPos1(Player player) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        POS1 = targetBlock.getLocation();
        player.sendMessage(ChatColor.GREEN + "Set Pos1 to [World(" + POS1.getWorld().getName() + "), X(" + POS1.getBlockX() + "), Y(" + POS1.getBlockY() + "), Z(" + POS1.getBlockZ() + ")]");
    }

    public static void worldeditPos1(Location l) {
        POS1 = l;
    }

    public static void worldeditPos2(Location l) {
        POS2 = l;
    }

    public static void worldeditPos2(Player player) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        POS2 = targetBlock.getLocation();
        player.sendMessage(ChatColor.GREEN + "Set Pos2 to [World(" + POS2.getWorld().getName() + "), X(" + POS2.getBlockX() + "), Y(" + POS2.getBlockY() + "), Z(" + POS2.getBlockZ() + ")]");
    }

    public static void worldeditPaste(Player player, String sfid) {
        worldeditPaste(player, sfid, false);
    }

    public static void worldeditPaste(Player player, String sfid, boolean overrideData) {
        SlimefunItem sfItem = SlimefunItem.getById(sfid);

        if (sfItem == null) {
            player.sendMessage(ChatColor.RED + "这不是一个有效的粘液方块ID！");
            return;
        }

        if (!sfItem.getItem().getType().isBlock()) {
            player.sendMessage(ChatColor.RED + "这不是一个有效的粘液方块ID！");
            return;
        }

        if (sfItem.getItem().getType().isAir()) {
            player.sendMessage(ChatColor.RED + "不可放置的粘液方块！");
            return;
        }

        if (POS1 == null || POS2 == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (POS1.getWorld() != POS2.getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        int upX = Math.max(POS1.getBlockX(), POS2.getBlockX());
        int downX = Math.min(POS1.getBlockX(), POS2.getBlockX());
        int upY = Math.max(POS1.getBlockY(), POS2.getBlockY());
        int downY = Math.min(POS1.getBlockY(), POS2.getBlockY());
        int upZ = Math.max(POS1.getBlockZ(), POS2.getBlockZ());
        int downZ = Math.min(POS1.getBlockZ(), POS2.getBlockZ());

        player.sendMessage(ChatColor.GREEN + "Pasting blocks from " + POS1.toString() + " to " + POS2.toString());
        long currentMillSeconds = System.currentTimeMillis();

        int count = 0;
        Material t = sfItem.getItem().getType();
        ItemStack itemStack = sfItem.getItem();
        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    Block targetBlock = POS1.getWorld().getBlockAt(x, y, z);
                    sfItem.callItemHandler(BlockPlaceHandler.class, h -> {
                        h.onPlayerPlace(
                                new BlockPlaceEvent(
                                        targetBlock,
                                        targetBlock.getState(),
                                        targetBlock.getRelative(BlockFace.DOWN),
                                        itemStack,
                                        player,
                                        true
                                )
                        );
                    });
                    if (overrideData) {
                        targetBlock.setType(t);
                        BlockStorage.deleteLocationInfoUnsafely(targetBlock.getLocation(), true);
                    }
                    if (!BlockStorage.hasBlockInfo(targetBlock)) {
                        targetBlock.setType(t);
                        BlockStorage.store(targetBlock, sfid);
                    }
                    count += 1;
                }
            }
        }

        player.sendMessage("Paste " + count + " blocks done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditClear(Player player, boolean callHandler, boolean skipVanilla) {
        if (POS1 == null || POS2 == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (POS1.getWorld() != POS2.getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        int upX = Math.max(POS1.getBlockX(), POS2.getBlockX());
        int downX = Math.min(POS1.getBlockX(), POS2.getBlockX());
        int upY = Math.max(POS1.getBlockY(), POS2.getBlockY());
        int downY = Math.min(POS1.getBlockY(), POS2.getBlockY());
        int upZ = Math.max(POS1.getBlockZ(), POS2.getBlockZ());
        int downZ = Math.min(POS1.getBlockZ(), POS2.getBlockZ());

        player.sendMessage(ChatColor.GREEN + "Pasting blocks from " + POS1.toString() + " to " + POS2.toString());
        long currentMillSeconds = System.currentTimeMillis();

        int count = 0;
        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    Block targetBlock = POS1.getWorld().getBlockAt(x, y, z);
                    if (BlockStorage.hasBlockInfo(targetBlock)) {
                        SlimefunItem item = BlockStorage.check(targetBlock);
                        if (callHandler) {
                            item.callItemHandler(BlockBreakHandler.class, handler -> {
                                handler.onPlayerBreak(
                                        new BlockBreakEvent(targetBlock, player),
                                        new ItemStack(Material.AIR),
                                        new ArrayList<>()
                                );
                            });
                        }
                        targetBlock.setType(Material.AIR);
                    }
                    BlockStorage.deleteLocationInfoUnsafely(targetBlock.getLocation(), true);
                    if (!skipVanilla) {
                        targetBlock.setType(Material.AIR);
                    }
                    count += 1;
                }
            }
        }
        player.sendMessage("Clear " + count + " blocks done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditBlockMenuSetSlot(Player player, int slot) {
        if (POS1 == null || POS2 == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (POS1.getWorld() != POS2.getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        if (!(0 <= slot && slot <= 53)) {
            player.sendMessage(ChatColor.RED + "槽位号必须在0-53之间！");
            return;
        }
        ItemStack hand = player.getInventory().getItemInMainHand();

        int upX = Math.max(POS1.getBlockX(), POS2.getBlockX());
        int downX = Math.min(POS1.getBlockX(), POS2.getBlockX());
        int upY = Math.max(POS1.getBlockY(), POS2.getBlockY());
        int downY = Math.min(POS1.getBlockY(), POS2.getBlockY());
        int upZ = Math.max(POS1.getBlockZ(), POS2.getBlockZ());
        int downZ = Math.min(POS1.getBlockZ(), POS2.getBlockZ());

        player.sendMessage(ChatColor.GREEN + "Setting slot " + slot + " to " + ItemStackHelper.getDisplayName(hand));
        long currentMillSeconds = System.currentTimeMillis();

        int count = 0;
        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    BlockMenu menu = StorageCacheUtils.getMenu(new Location(POS1.getWorld(), x, y, z));
                    if (menu != null) {
                        menu.replaceExistingItem(slot, hand);
                    }
                    count += 1;
                }
            }
        }
        player.sendMessage("Set slot " + slot + " done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditBlockInfoAdd(Player player, String key, String value) {
        if (POS1 == null || POS2 == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (POS1.getWorld() != POS2.getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        int upX = Math.max(POS1.getBlockX(), POS2.getBlockX());
        int downX = Math.min(POS1.getBlockX(), POS2.getBlockX());
        int upY = Math.max(POS1.getBlockY(), POS2.getBlockY());
        int downY = Math.min(POS1.getBlockY(), POS2.getBlockY());
        int upZ = Math.max(POS1.getBlockZ(), POS2.getBlockZ());
        int downZ = Math.min(POS1.getBlockZ(), POS2.getBlockZ());

        player.sendMessage(ChatColor.GREEN + "Setting " + key + " to " + value);
        long currentMillSeconds = System.currentTimeMillis();

        int count = 0;
        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    Location location = new Location(POS1.getWorld(), x, y, z);
                    if (StorageCacheUtils.getBlock(location) != null) {
                        StorageCacheUtils.setData(location, key, value);
                    }
                    count += 1;
                }
            }
        }
        player.sendMessage("Set " + key + " done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditBlockInfoRemove(Player player, String key) {
        if (POS1 == null || POS2 == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (POS1.getWorld() != POS2.getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        int upX = Math.max(POS1.getBlockX(), POS2.getBlockX());
        int downX = Math.min(POS1.getBlockX(), POS2.getBlockX());
        int upY = Math.max(POS1.getBlockY(), POS2.getBlockY());
        int downY = Math.min(POS1.getBlockY(), POS2.getBlockY());
        int upZ = Math.max(POS1.getBlockZ(), POS2.getBlockZ());
        int downZ = Math.min(POS1.getBlockZ(), POS2.getBlockZ());

        player.sendMessage(ChatColor.GREEN + "Removing " + key);
        long currentMillSeconds = System.currentTimeMillis();

        int count = 0;
        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    Location location = new Location(POS1.getWorld(), x, y, z);
                    if (StorageCacheUtils.getBlock(location) != null) {
                        StorageCacheUtils.removeData(location, key);
                    }
                    count += 1;
                }
            }
        }
        player.sendMessage("Remove " + key + " done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    private static void updateItem(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        SlimefunItem slimefunItem = SlimefunItem.getByItem(itemInHand);
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "无法更新非粘液物品！");
            return;
        }

        String currentId = slimefunItem.getId();
        if (slimefunItem instanceof CargoStorageUnit) {
            player.sendMessage(ChatColor.RED + "暂不支持此物品的更新");
            return;
        } else if (slimefunItem instanceof NetworkQuantumStorage) {
            ItemMeta meta = itemInHand.getItemMeta();
            final QuantumCache quantumCache = DataTypeMethods.getCustom(
                    meta,
                    Keys.QUANTUM_STORAGE_INSTANCE,
                    PersistentQuantumStorageType.TYPE
            );

            if (quantumCache == null || quantumCache.getItemStack() == null) {
                itemInHand.setItemMeta(SlimefunItem.getById(currentId).getItem().getItemMeta());
                player.sendMessage(ChatColor.GREEN + "已更新物品！");
                return;
            }

            ItemStack stored = quantumCache.getItemStack();
            SlimefunItem sfi = SlimefunItem.getByItem(stored);
            if (sfi != null) {
                String quantumStoredId = sfi.getId();
                stored.setItemMeta(SlimefunItem.getById(quantumStoredId).getItem().getItemMeta());
                player.sendMessage(ChatColor.GREEN + "已更新存储内的物品！");
            }
            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
            quantumCache.updateMetaLore(meta);
            itemInHand.setItemMeta(meta);
            player.sendMessage(ChatColor.GREEN + "已更新物品！");
        } else {
            itemInHand.setItemMeta(SlimefunItem.getById(currentId).getItem().getItemMeta());
            player.sendMessage(ChatColor.GREEN + "已更新物品！");
        }
    }

    private static void findCachedStorages(CommandSender sender, String playerName) {
        DataSource dataSource = Networks.getDataSource();
        List<StorageUnitData> storageUnitData = new ArrayList<>();
        int id = 0;
        for (; ; ) {
            StorageUnitData data = dataSource.getStorageData(id);
            if (data == null) {
                break;
            }
            if (data.getOwner().getName().equals(playerName)) {
                storageUnitData.add(data);
            }
            id += 1;
        }
        sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has " + storageUnitData.size() + " storage units.");
        for (StorageUnitData data : storageUnitData) {
            if (data.isPlaced()) {
                sender.sendMessage(ChatColor.AQUA + "Loaded id: " + data.getId());
            } else {
                sender.sendMessage(ChatColor.RED + "Unloaded id: " + data.getId());
            }
        }
    }

    public static void getStorageItem(Player player, int slot) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (slimefunItem instanceof CargoStorageUnit) {
            Location targetLocation = targetBlock.getLocation();
            StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

            if (data == null) {
                player.sendMessage(Theme.ERROR + "该网络抽屉不存在或已损坏!");
                return;
            }

            List<ItemContainer> stored = data.getStoredItems();
            if (slot >= stored.size()) {
                player.sendMessage(Theme.ERROR + "槽位号必须在0-" + (stored.size() - 1) + "之间!");
                return;
            } else {
                ItemStack stack = stored.get(slot).getSample();
                if (stack == null || stack.getType().isAir()) {
                    player.sendMessage(Theme.ERROR + "该槽位没有物品!");
                    return;
                } else {
                    player.getInventory().addItem(StackUtils.getAsQuantity(stack, 1));
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }
    }

    public static void help(CommandSender sender, String mainCommand) {
        if (mainCommand == null) {
            sender.sendMessage(ChatColor.GOLD + "网络命令帮助:");
            sender.sendMessage(ChatColor.GOLD + "/networks help - 显示此帮助信息.");
            sender.sendMessage(ChatColor.GOLD + "/networks fillquantum <amount> - 填充手持量子存储物品的存储量.");
            sender.sendMessage(ChatColor.GOLD + "/networks fixblueprint <keyInMeta> - 修复手持合成蓝图.");
            sender.sendMessage(ChatColor.GOLD + "/networks restore - 恢复失效的网络抽屉单元.");
            sender.sendMessage(ChatColor.GOLD + "/networks addstorageitem <amount> - 使看向的网络抽屉中添加物品.");
            sender.sendMessage(ChatColor.GOLD + "/networks reducestorageitem <amount> - 使看向的网络抽屉中减少物品.");
            sender.sendMessage(ChatColor.GOLD + "/networks setquantum <amount> - 设置手持量子存储物品的存储量.");
            sender.sendMessage(ChatColor.GOLD + "/networks setcontainerid <containerId> - 设置网络抽屉的容器ID.");
            sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - 粘液创世神功能.");
            sender.sendMessage(ChatColor.GOLD + "/networks updateItem - 更新手持物品.");
            sender.sendMessage(ChatColor.GOLD + "/networks findCachedStorages <playerName> - 查找指定玩家放置的网络抽屉.");
            sender.sendMessage(ChatColor.GOLD + "/networks getstorageitem <slot> - 获取看向的网络抽屉指定槽位的物品.");
            return;
        }
        switch (mainCommand.toLowerCase(Locale.ROOT)) {
            case "help" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks help - 显示此帮助信息.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks help");
            }
            case "fillquantum" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks fillQuantum <amount> - 设置手持量子存储的存储量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks fillQuantum 1000");
            }
            case "fixblueprint" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks fixBlueprint <keyInMeta> - 修复手持合成蓝图.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks fixBlueprint networks-changed");
            }
            case "restore" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks restore - 恢复失效的网络抽屉单元.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks restore");
            }
            case "addstorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks addStorageItem <amount> - 向指向的货运存储中添加手中物品指定数量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks addstorageItem 1000");
            }
            case "reducestorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks reduceStorageItem <amount> - 向指向的货运存储中减少手中物品指定数量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks reduceStorageItem 1000");
            }
            case "setquantum" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks setQuantum <amount> - 设置指向的量子存储的物品为手上的物品，并设置存储指定数量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks setQuantum 1000");
            }
            case "setcontainerid" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks setContainerId <containerId> - 设置指向的货运存储的容器ID.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks setContainerId 6");
            }
            case "findcachedstorages" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks findCachedStorages <playerName> - 查找指定玩家放置的货运存储单元.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks findCachedStorages Notch");
            }
            case "getstorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks getStorageItem <slot> - 获取指向的货运存储指定槽位的物品.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks getStorageItem 0");
            }
            case "worldedit" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - 粘液创世神功能.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos1 - 选择第一个位置");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos2 - 选择第二个位置");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> - 粘贴粘液方块");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> override - 覆盖原本的数据");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> keep - 保留原本的数据");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit clear <callHandler> <skipVanilla> - 清除粘液方块");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockMenu setSlot <slot> - 修改选中区域的粘液方法的菜单的对应槽位为手上物品");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockInfo add <key> <value> - 增加粘液方块信息");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockInfo remove <value> - 移除粘液方块信息");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockInfo set <key> <value> - 设置粘液方块信息");
            }
            case "updateitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks updateItem - 更新手持物品.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks updateItem");
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "未知命令! 请使用 /networks help 查看帮助信息.");
            }
        }
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length == 0) {
            help(sender, null);
        }
        switch (args[0]) {
            case "fillquantum", "fixblueprint", "addstorageitem", "reducestorageitem", "setquantum", "restore", "setcontainerid" -> {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getErrorMessage(ERROR_TYPE.MUST_BE_PLAYER));
                    return false;
                }
            }
            case "help" -> {

            }
        }

        // 玩家或控制台皆可
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "help" -> {
                if (sender.isOp()) {
                    if (args.length >= 2) {
                        help(sender, args[1]);
                    } else {
                        help(sender, null);
                    }
                } else {
                    sender.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            }
            case "findcachedstorages" -> {
                if (sender.isOp() || sender.hasPermission("networks.admin") || sender.hasPermission("networks.commands.findcachedstorages")) {
                    if (args.length >= 2) {
                        findCachedStorages(sender, args[1]);
                    } else {
                        sender.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "playerName"));
                    }
                } else {
                    sender.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            }
        }

        // 仅玩家
        if (sender instanceof Player player) {
            if (args[0].equalsIgnoreCase("fillquantum")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.fillquantum")) && args.length >= 2) {
                    if (args.length >= 2) {
                        try {
                            int number = Integer.parseInt(args[1]);
                            fillQuantum(player, number);
                            return true;
                        } catch (NumberFormatException exception) {
                            player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "amount"));
                        }
                    } else {
                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "amount"));
                    }
                } else {
                    player.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("fixblueprint")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.fixblueprint"))) {
                    if (args.length >= 2) {
                        String before = args[1];
                        fixBlueprint(player, before);
                    } else {
                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "keyInMeta"));
                    }
                } else {
                    player.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("restore")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.restore"))) {
                    restore(player);
                } else {
                    player.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("setQuantum")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.setquantum"))) {
                    if (args.length >= 2) {
                        try {
                            setQuantum(player, Integer.parseInt(args[1]));
                        } catch (NumberFormatException e) {
                            player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "amount"));
                            return true;
                        }
                    } else {
                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "amount"));
                    }
                } else {
                    player.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("addstorageitem")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.addstorage"))) {
                    if (args.length >= 2) {
                        try {
                            int amount = Integer.parseInt(args[1]);
                            addStorageItem(player, amount);
                        } catch (NumberFormatException e) {
                            player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "amount"));
                            return true;
                        }
                    } else {
                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "amount"));
                    }
                } else {
                    player.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reducestorageitem")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.reducestorage"))) {
                    if (args.length >= 2) {
                        try {
                            int amount = Integer.parseInt(args[1]);
                            reduceStorageItem(player, amount);
                        } catch (NumberFormatException e) {
                            player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "amount"));
                            return true;
                        }
                    } else {
                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "amount"));
                    }
                } else {
                    player.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("setcontainerid")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.setcontainerid"))) {
                    if (args.length >= 2) {
                        String containerId = args[1];
                        try {
                            setContainerId(player, Integer.parseInt(containerId));
                        } catch (NumberFormatException e) {
                            player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "containerId"));
                        }
                    } else {
                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "containerId"));
                    }
                } else {
                    player.sendMessage(getErrorMessage(ERROR_TYPE.NO_PERMISSION));
                }
            } else if (args[0].equalsIgnoreCase("worldedit")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.worldedit.*"))) {
                    switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "pos1" -> {
                            worldeditPos1(player);
                        }
                        case "pos2" -> {
                            worldeditPos2(player);
                        }
                        case "clear" -> {
                            if (args.length == 4) {
                                try {
                                    boolean callHandler = Boolean.parseBoolean(args[2]);
                                    boolean skipVanilla = Boolean.parseBoolean(args[3]);
                                    worldeditClear(player, callHandler, skipVanilla);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "callHandler / skipVanilla"));
                                }
                            } else if (args.length == 3) {
                                try {
                                    boolean callHandler = Boolean.parseBoolean(args[2]);
                                    worldeditClear(player, callHandler, true);
                                } catch (NumberFormatException e) {
                                    player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "callHandler"));
                                }
                            } else {
                                worldeditClear(player, true, true);
                            }
                        }
                        case "paste" -> {
                            if (args.length >= 4) {
                                switch (args[3].toLowerCase(Locale.ROOT)) {
                                    case "override" -> {
                                        worldeditPaste(player, args[2], true);
                                    }
                                    case "keep" -> {
                                        worldeditPaste(player, args[2], false);
                                    }
                                    default -> {
                                        worldeditPaste(player, args[2]);
                                    }
                                }
                            } else if (args.length == 3) {
                                worldeditPaste(player, args[2]);
                            } else {
                                player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "sfId"));
                            }

                        }
                        case "blockmenu" -> {
                            if (args.length >= 3) {
                                switch (args[2].toLowerCase(Locale.ROOT)) {
                                    case "setslot" -> {
                                        if (args.length >= 4) {
                                            try {
                                                worldeditBlockMenuSetSlot(player, Integer.parseInt(args[3]));
                                            } catch (NumberFormatException e) {
                                                player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "slot"));
                                            }
                                        } else {
                                            player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "slot"));
                                        }
                                    }
                                    default -> {
                                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                                    }
                                }
                            } else {
                                player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                            }
                        }
                        case "blockinfo" -> {
                            if (args.length >= 3) {
                                switch (args[2].toLowerCase(Locale.ROOT)) {
                                    case "add", "set" -> {
                                        if (args.length == 3) {
                                            player.sendMessage(Theme.ERROR + getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "key"));
                                        }

                                        if (args.length == 4) {
                                            player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "value"));
                                        }

                                        if (args.length >= 5) {
                                            String key = args[3];
                                            String value = args[4];
                                            worldeditBlockInfoAdd(player, key, value);
                                        }
                                    }
                                    case "remove" -> {
                                        if (args.length == 3) {
                                            player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "key"));
                                        }
                                        if (args.length >= 4) {
                                            String key = args[3];
                                            worldeditBlockInfoRemove(player, key);
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                            }
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("updateItem")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.updateitem"))) {
                    updateItem(player);
                }
            } else if (args[0].equalsIgnoreCase("getstorageitem")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.getstorageitem"))) {
                    if (args.length >= 2) {
                        try {
                            int slot = Integer.parseInt(args[1]);
                            getStorageItem(player, slot);
                        } catch (NumberFormatException e) {
                            player.sendMessage(getErrorMessage(ERROR_TYPE.INVALID_REQUIRED_ARGUMENT, "slot"));
                        }
                    } else {
                        player.sendMessage(getErrorMessage(ERROR_TYPE.MISSING_REQUIRED_ARGUMENT, "slot"));
                    }
                }
            } else {
                help(sender, null);
            }
        } else {
            // 仅控制台时执行的操作
        }
        // We always return true, even if the command was not executed, so that the help message is not shown.
        return true;
    }

    public void fillQuantum(Player player, int amount) {
        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            player.sendMessage(Theme.ERROR + "你必须手持量子存储");
            return;
        }

        SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);

        if (!(slimefunItem instanceof NetworkQuantumStorage)) {
            player.sendMessage(Theme.ERROR + "你手中的物品必须为量子存储");
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        final QuantumCache quantumCache = DataTypeMethods.getCustom(
                meta,
                Keys.QUANTUM_STORAGE_INSTANCE,
                PersistentQuantumStorageType.TYPE
        );

        if (quantumCache == null || quantumCache.getItemStack() == null) {
            player.sendMessage(Theme.ERROR + "量子存储未指定物品或已损坏");
            return;
        }

        quantumCache.setAmount(amount);
        DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
        quantumCache.updateMetaLore(meta);
        itemStack.setItemMeta(meta);
        player.sendMessage(Theme.SUCCESS + "已更新物品");
    }

    public void fixBlueprint(Player player, String before) {
        ItemStack blueprint = player.getInventory().getItemInMainHand();
        if (blueprint.getType().isAir()) {
            player.sendMessage(Theme.ERROR + "你必须手持合成蓝图");
            return;
        }

        final SlimefunItem item = SlimefunItem.getByItem(blueprint);

        if (!(item instanceof CraftingBlueprint)) {
            player.sendMessage(Theme.ERROR + "你必须手持合成蓝图");
            return;
        }

        ItemMeta blueprintMeta = blueprint.getItemMeta();

        final Optional<BlueprintInstance> optional = DataTypeMethods.getOptionalCustom(
                blueprintMeta,
                new NamespacedKey(before, "ntw_blueprint"),
                PersistentCraftingBlueprintType.TYPE
        );

        if (optional.isEmpty()) {
            player.sendMessage(Theme.ERROR + "无法获取 instance");
            return;
        }

        BlueprintInstance instance = optional.get();

        ItemStack fix = NetworksSlimefunItemStacks.CRAFTING_BLUEPRINT.clone();
        CraftingBlueprint.setBlueprint(fix, instance.getRecipeItems(), instance.getItemStack());

        blueprint.setItemMeta(fix.getItemMeta());

        player.sendMessage(Theme.SUCCESS + "已修复蓝图");

        return;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> raw = onTabCompleteRaw(sender, args);
        return StringUtil.copyPartialMatches(args[args.length - 1], raw, new ArrayList<>());
    }

    public @NotNull List<String> onTabCompleteRaw(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of(
                    "addStorageItem",
                    "findCachedStorages",
                    "fillQuantum",
                    "fixBlueprint",
                    "getStorageItem",
                    "help",
                    "reduceStorageItem",
                    "restore",
                    "setContainerId",
                    "setQuantum",
                    "updateItem",
                    "worldedit"
            );
        } else if (args.length == 2) {
            return switch (args[0].toLowerCase(Locale.ROOT)) {
                // case "help", "restore", "updateitem" -> List.of();
                case "getstorageitem" -> List.of("<slot>");
                case "fillquantum", "addstorageitem", "reducestorageitem", "setquantum" -> List.of("<amount>");
                case "fixblueprint" -> List.of("<keyInMeta>");
                case "setcontainerid" -> List.of("<containerId>");
                case "findcachedstorages" -> List.of("<playerName>");
                case "worldedit" -> List.of("pos1", "pos2", "paste", "clear", "blockmenu", "blockinfo");
                default -> List.of();
            };
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("worldedit")) {
                return switch (args[1]) {
                    // case "pos1", "pos2" -> List.of();
                    case "paste" -> Slimefun.getRegistry().getAllSlimefunItems()
                            .stream()
                            .filter(sfItem -> sfItem.getItem().getType().isBlock())
                            .map(SlimefunItem::getId)
                            .toList();
                    case "blockinfo" -> List.of("add", "remove", "set");
                    case "blockmenu" -> List.of("setSlot");
                    case "clear" -> List.of("true", "false");
                    default -> List.of();
                };
            } else {
                return List.of();
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("worldedit")) {
                return switch (args[1].toLowerCase(Locale.ROOT)) {
                    case "paste" -> List.of("override", "keep");
                    case "blockmenu" -> switch (args[2].toLowerCase(Locale.ROOT)) {
                        case "setslot" ->
                                List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53");
                        default -> List.of();
                    };
                    case "clear" -> List.of("true", "false");
                    default -> List.of();
                };
            }
        }
        return new ArrayList<>();
    }

    public String getErrorMessage(ERROR_TYPE errorType) {
        return getErrorMessage(errorType, null);
    }

    public String getErrorMessage(ERROR_TYPE errorType, String argument) {
        return switch (errorType) {
            case NO_PERMISSION -> "你没有权限执行此命令! ";
            case NO_ITEM_IN_HAND -> "你必须在手上持有物品! ";
            case MISSING_REQUIRED_ARGUMENT -> "缺少必要参数: <" + argument + ">";
            case INVALID_REQUIRED_ARGUMENT -> "无效的参数: <" + argument + ">";
            case MUST_BE_PLAYER -> "此命令只能由玩家执行! ";
            default -> "未知错误! ";
        };
    }

    public enum ERROR_TYPE {
        NO_PERMISSION,
        NO_ITEM_IN_HAND,
        MISSING_REQUIRED_ARGUMENT,
        INVALID_REQUIRED_ARGUMENT,
        MUST_BE_PLAYER,
        UNKNOWN_ERROR
    }
}
