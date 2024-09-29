package io.github.sefiraat.networks.commands;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.data.ItemContainer;
import com.ytdd9527.networksexpansion.api.data.StorageUnitData;
import com.ytdd9527.networksexpansion.api.enums.ErrorType;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.CraftingBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.machines.unit.CargoStorageUnit;
import com.ytdd9527.networksexpansion.utils.databases.DataSource;
import io.github.bakedlibs.dough.collections.Pair;
import io.github.bakedlibs.dough.skins.PlayerHead;
import io.github.bakedlibs.dough.skins.PlayerSkin;
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
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class NetworksMain implements TabExecutor {
    private static final Map<String, Pair<Location, Location>> SELECTED_POS = new HashMap<>();

    public static Location getPos1(Player p) {
        if (SELECTED_POS.get(p.getName()) == null) {
            return null;
        }
        return SELECTED_POS.get(p.getName()).getFirstValue();
    }

    public static Location getPos2(Player p) {
        if (SELECTED_POS.get(p.getName()) == null) {
            return null;
        }
        return SELECTED_POS.get(p.getName()).getSecondValue();
    }

    public static void setPos1(Player p, Location pos) {
        SELECTED_POS.put(p.getName(), new Pair<>(pos, getPos2(p)));
    }

    public static void setPos2(Player p, Location pos) {
        SELECTED_POS.put(p.getName(), new Pair<>(getPos1(p), pos));
    }

    public static String locationToString(Location l) {
        if (l == null) {
            return "Unknown";
        }
        if (l.getWorld() == null) {
            return "Unknown";
        }
        return l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
    }

    public static long locationRange(Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null) {
            return 0;
        }

        final int downX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        final int upX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        final int downY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        final int upY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        final int downZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        final int upZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        return (long) (Math.abs(upX - downX) + 1) * (Math.abs(upY - downY) + 1) * (Math.abs(upZ - downZ) + 1);
    }

    private static void doWorldEdit(Location pos1, Location pos2, Consumer<Location> consumer) {
        if (pos1 == null || pos2 == null) {
            return;
        }
        final int downX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        final int upX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        final int downY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        final int upY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        final int downZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        final int upZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    consumer.accept(new Location(pos1.getWorld(), x, y, z));
                }
            }
        }
    }

    public static void setQuantum(Player player, int amount) {
        final Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }
        
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        final SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }

        final SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }

        final Location targetLocation = targetBlock.getLocation();
        final ItemStack clone = itemInHand.clone();
        if (!(slimefunItem instanceof NetworkQuantumStorage)) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络存储才能执行该指令!");
            return;
        }

        final BlockMenu blockMenu = StorageCacheUtils.getMenu(targetLocation);
        if (blockMenu == null) {
            player.sendMessage(Theme.ERROR + "这是一个无效的网络存储");
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
    }

    private static void addStorageItem(Player player, int amount) {
        final Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        final SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        final SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (!(slimefunItem instanceof CargoStorageUnit)) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
        }
        
        final Location targetLocation = targetBlock.getLocation();
        final ItemStack clone = itemInHand.clone();
        final StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

        if (data == null) {
            player.sendMessage(Theme.ERROR + "该网络抽屉不存在或已损坏!");
            return;
        }

        clone.setAmount(amount);
        data.depositItemStack(clone, false);
        CargoStorageUnit.setStorageData(targetLocation, data);
        player.sendMessage(ChatColor.GREEN + "已更新物品");
    }

    private static void reduceStorageItem(Player player, int amount) {
        final Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }
        
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        final SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        final SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (!(slimefunItem instanceof CargoStorageUnit)) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
        }

        final Location targetLocation = targetBlock.getLocation();
        final ItemStack clone = itemInHand.clone();
        final StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

        if (data == null) {
            player.sendMessage(Theme.ERROR + "该网络抽屉不存在或已损坏!");
            return;
        }

        clone.setAmount(1);
        data.requestItem(new ItemRequest(clone, amount));
        CargoStorageUnit.setStorageData(targetLocation, data);
        player.sendMessage(ChatColor.GREEN + "已更新物品");
    }

    public static void setContainerId(Player player, int containerId) {
        final Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        final SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (!(slimefunItem instanceof CargoStorageUnit)) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        final Location location = targetBlock.getLocation();

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
        if (targetBlock == null) {
            targetBlock = player.getLocation().getBlock();
        }
        
        worldeditPos1(player, targetBlock.getLocation());
    }

    public static void worldeditPos1(Player player, Location location) {
        setPos1(player, location);
        if (getPos2(player) == null) {
            player.sendMessage(ChatColor.GREEN + "Set Pos1 to " + locationToString(getPos1(player)));
        } else {
            player.sendMessage(ChatColor.GREEN + "Set Pos1 to " + locationToString(getPos1(player)) + "(" + locationRange(getPos1(player), getPos2(player)) + " Blocks)");
        }
    }

    public static void worldeditPos2(Player player) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null) {
            targetBlock = player.getLocation().getBlock();
        }
        
        worldeditPos2(player, targetBlock.getLocation());
    }

    public static void worldeditPos2(Player player, Location location) {
        setPos2(player, location);
        if (getPos1(player) == null) {
            player.sendMessage(ChatColor.GREEN + "Set Pos2 to " + locationToString(getPos2(player)));
        } else {
            player.sendMessage(ChatColor.GREEN + "Set Pos2 to " + locationToString(getPos2(player)) + "(" + locationRange(getPos1(player), getPos2(player)) + " Blocks)");
        }
    }

    public static void worldeditPaste(Player player, String sfid) {
        worldeditPaste(player, sfid, false, false);
    }

    public static void worldeditPaste(Player player, String sfid, boolean overrideData) {
        worldeditPaste(player, sfid, overrideData, false);
    }

    public static void worldeditPaste(Player player, String sfid, boolean overrideData, boolean force) {
        final SlimefunItem sfItem = SlimefunItem.getById(sfid);

        if (getPos1(player) == null || getPos2(player) == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (getPos1(player).getWorld() != getPos2(player).getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        if (sfItem == null) {
            player.sendMessage(ChatColor.RED + "这不是一个有效的粘液方块ID！");
            return;
        }

        if (!sfItem.getItem().getType().isBlock()) {
            player.sendMessage(ChatColor.RED + "这不是一个有效的粘液方块ID！");
            return;
        }

        if (sfItem.getItem().getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "不可放置的粘液方块！");
            return;
        }

        if (!force && sfItem instanceof NotPlaceable) {
            player.sendMessage(ChatColor.RED + "不可放置的粘液方块！");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "Pasting blocks from " + locationToString(getPos1(player)) + " to " + locationToString(getPos2(player)));
        final long currentMillSeconds = System.currentTimeMillis();

        final AtomicInteger count = new AtomicInteger();
        final Material t = sfItem.getItem().getType();
        final ItemStack itemStack = sfItem.getItem();
        final PlayerSkin skin;
        final boolean isHead;
        if (itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD) {
            if (itemStack instanceof SlimefunItemStack sfis) {
                Optional<String> texture = sfis.getSkullTexture();
                if (texture.isPresent()) {
                    skin = PlayerSkin.fromBase64(texture.get());
                    isHead = true;
                } else {
                    isHead = false;
                    skin = null;
                }
            } else {
                isHead = false;
                skin = null;
            }
        } else {
            isHead = false;
            skin = null;
        }
        // java's operation ↑
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            final Block targetBlock = getPos1(player).getWorld().getBlockAt(location);
            sfItem.callItemHandler(BlockPlaceHandler.class, h -> h.onPlayerPlace(
                    new BlockPlaceEvent(
                            targetBlock,
                            targetBlock.getState(),
                            targetBlock.getRelative(BlockFace.DOWN),
                            itemStack,
                            player,
                            true
                    )
            ));
            if (overrideData) {
                Slimefun.getDatabaseManager().getBlockDataController().removeBlock(location);
            }
            if (!StorageCacheUtils.hasBlock(location)) {
                targetBlock.setType(t);
                if (isHead) {
                    PlayerHead.setSkin(targetBlock, skin, false);
                }
                Slimefun.getDatabaseManager().getBlockDataController().createBlock(location, sfid);
            }
            count.addAndGet(1);
        }));

        player.sendMessage("Paste " + count + " blocks done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditClear(Player player, boolean callHandler, boolean skipVanilla) {
        if (getPos1(player) == null || getPos2(player) == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (getPos1(player).getWorld() != getPos2(player).getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "Pasting blocks from " + getPos1(player).toString() + " to " + getPos2(player).toString());
        final long currentMillSeconds = System.currentTimeMillis();

        final AtomicInteger count = new AtomicInteger();
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            final Block targetBlock = getPos1(player).getWorld().getBlockAt(location);
            if (StorageCacheUtils.hasBlock(location)) {
                SlimefunItem item = StorageCacheUtils.getSfItem(location);
                if (callHandler) {
                    item.callItemHandler(BlockBreakHandler.class, handler -> handler.onPlayerBreak(
                            new BlockBreakEvent(targetBlock, player),
                            new ItemStack(Material.AIR),
                            new ArrayList<>()
                    ));
                }
                targetBlock.setType(Material.AIR);
            }
            Slimefun.getDatabaseManager().getBlockDataController().removeBlock(location);
            if (!skipVanilla) {
                targetBlock.setType(Material.AIR);
            }
            count.addAndGet(1);
        }));

        player.sendMessage("Clear " + count + " blocks done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditBlockMenuSetSlot(Player player, int slot) {
        if (getPos1(player) == null || getPos2(player) == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (getPos1(player).getWorld() != getPos2(player).getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        if (!(0 <= slot && slot <= 53)) {
            player.sendMessage(ChatColor.RED + "槽位号必须在0-53之间！");
            return;
        }
        
        final ItemStack hand = player.getInventory().getItemInMainHand();

        player.sendMessage(ChatColor.GREEN + "Setting slot " + slot + " to " + ItemStackHelper.getDisplayName(hand));
        final long currentMillSeconds = System.currentTimeMillis();

        final AtomicInteger count = new AtomicInteger();
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            final BlockMenu menu = StorageCacheUtils.getMenu(location);
            if (menu != null) {
                menu.replaceExistingItem(slot, hand);
            }
            count.addAndGet(1);
        }));

        player.sendMessage("Set slot " + slot + " done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditBlockInfoAdd(Player player, String key, String value) {
        if (getPos1(player) == null || getPos2(player) == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (getPos1(player).getWorld() != getPos2(player).getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "Setting " + key + " to " + value);
        final long currentMillSeconds = System.currentTimeMillis();

        final AtomicInteger count = new AtomicInteger();
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            if (StorageCacheUtils.getBlock(location) != null) {
                StorageCacheUtils.setData(location, key, value);
                count.addAndGet(1);
            }
        }));

        player.sendMessage("Set " + key + " done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    public static void worldeditBlockInfoRemove(Player player, String key) {
        if (getPos1(player) == null || getPos2(player) == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (getPos1(player).getWorld() != getPos2(player).getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "Removing " + key);
        final long currentMillSeconds = System.currentTimeMillis();

        final AtomicInteger count = new AtomicInteger();
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            if (StorageCacheUtils.getBlock(location) != null) {
                StorageCacheUtils.removeData(location, key);
                count.addAndGet(1);
            }
        }));
        player.sendMessage("Remove " + key + " done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    private static void updateItem(Player player) {
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        final SlimefunItem slimefunItem = SlimefunItem.getByItem(itemInHand);
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "无法更新非粘液物品！");
            return;
        }

        final String currentId = slimefunItem.getId();
        if (slimefunItem instanceof CargoStorageUnit) {
            player.sendMessage(ChatColor.RED + "暂不支持此物品的更新");
        } else if (slimefunItem instanceof NetworkQuantumStorage) {
            final ItemMeta meta = itemInHand.getItemMeta();
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

            final ItemStack stored = quantumCache.getItemStack();
            final SlimefunItem sfi = SlimefunItem.getByItem(stored);
            if (sfi != null) {
                final String quantumStoredId = sfi.getId();
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

    public static void getStorageItem(Player player, int slot) {
        final Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        final SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        final SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }

        if (!(slimefunItem instanceof CargoStorageUnit)) {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
        }
        
        final Location targetLocation = targetBlock.getLocation();
        final StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

        if (data == null) {
            player.sendMessage(Theme.ERROR + "该网络抽屉不存在或已损坏!");
            return;
        }

        final List<ItemContainer> stored = data.getStoredItems();
        if (slot >= stored.size()) {
            player.sendMessage(Theme.ERROR + "槽位号必须在0-" + (stored.size() - 1) + "之间!");
        } else {
            final ItemStack stack = stored.get(slot).getSample();
            if (stack == null || stack.getType() == Material.AIR) {
                player.sendMessage(Theme.ERROR + "该槽位没有物品!");
                return;
            }
            
            player.getInventory().addItem(StackUtils.getAsQuantity(stack, 1));
        }
    }

    public static void help(CommandSender sender, String mainCommand) {
        if (mainCommand == null) {
            sender.sendMessage(ChatColor.GOLD + "网络命令帮助:");
            sender.sendMessage(ChatColor.GOLD + "/networks help - 显示此帮助信息.");
            sender.sendMessage(ChatColor.GOLD + "/networks fillquantum <amount> - 填充手持量子存储物品的存储量.");
            sender.sendMessage(ChatColor.GOLD + "/networks fixblueprint <keyInMeta> - 修复手持合成蓝图.");
            sender.sendMessage(ChatColor.GOLD + "/networks addstorageitem <amount> - 使看向的网络抽屉中添加物品.");
            sender.sendMessage(ChatColor.GOLD + "/networks reducestorageitem <amount> - 使看向的网络抽屉中减少物品.");
            sender.sendMessage(ChatColor.GOLD + "/networks setquantum <amount> - 设置手持量子存储物品的存储量.");
            sender.sendMessage(ChatColor.GOLD + "/networks setcontainerid <containerId> - 设置网络抽屉的容器ID.");
            sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - 粘液创世神功能.");
            sender.sendMessage(ChatColor.GOLD + "/networks updateItem - 更新手持物品.");
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
            case "getstorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks getStorageItem <slot> - 获取指向的货运存储指定槽位的物品.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks getStorageItem 0");
            }
            case "worldedit" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - 粘液创世神功能.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos1 - 选择第一个位置");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos2 - 选择第二个位置");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> <handler> <force> - 粘贴粘液方块");
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
            return true;
        }
        switch (args[0]) {
            case "fillquantum", "fixblueprint", "addstorageitem", "reducestorageitem", "setquantum", "setcontainerid" -> {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getErrorMessage(ErrorType.MUST_BE_PLAYER));
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
                    sender.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                }
                return true;
            }
        }

        // 仅玩家
        if (sender instanceof Player player) {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "fillquantum" -> {
                    if (!player.hasPermission("networks.admin") &&!player.hasPermission("networks.commands.fillquantum")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 2) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "amount"));
                        return true;
                    }

                    try {
                        int amount = Integer.parseInt(args[1]);
                        fillQuantum(player, amount);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "amount"));
                    }

                    return true;
                }

                case "fixblueprint" -> {
                    if (!player.hasPermission("networks.admin") &&!player.hasPermission("networks.commands.fixblueprint")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 2) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "keyInMeta"));
                        return true;
                    }

                    String before = args[1];
                    fixBlueprint(player, before);
                    return true;
                }

                case "setquantum" -> {
                    if (!player.hasPermission("networks.admin") &&!player.hasPermission("networks.commands.setquantum")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 2) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "amount"));
                        return true;
                    }

                    try {
                        int amount = Integer.parseInt(args[1]);
                        setQuantum(player, amount);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "amount"));
                    }

                    return true;
                }
                case "addstorageitem" -> {
                    if (!player.hasPermission("networks.admin") &&!player.hasPermission("networks.commands.addstorageitem")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 2) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "amount"));
                        return true;
                    }

                    try {
                        int amount = Integer.parseInt(args[1]);
                        addStorageItem(player, amount);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "amount"));
                    }

                    return true;
                }

                case "reducestorageitem" -> {
                    if (!player.hasPermission("networks.admin") &&!player.hasPermission("networks.commands.reducestorageitem")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 2) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "amount"));
                        return true;
                    }

                    try {
                        int amount = Integer.parseInt(args[1]);
                        reduceStorageItem(player, amount);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "amount"));
                    }

                    return true;
                }

                case "setcontainerid" -> {
                    if (!player.hasPermission("networks.admin") &&!player.hasPermission("networks.commands.setcontainerid")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 2) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "containerId"));
                        return true;
                    }

                    try {
                        int containerId = Integer.parseInt(args[1]);
                        setContainerId(player, containerId);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "containerId"));
                    }

                    return true;
                }

                case "worldedit" -> {
                    if (!player.hasPermission("networks.admin") && !player.hasPermission("networks.commands.worldedit.*")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 1) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                        return true;
                    }

                    switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "pos1" -> {
                            worldeditPos1(player);
                        }
                        case "pos2" -> {
                            worldeditPos2(player);
                        }

                        case "clear" -> {
                            switch (args.length) {
                                case 4 -> {
                                    try {
                                            boolean callHandler = Boolean.parseBoolean(args[2]);
                                            boolean skipVanilla = Boolean.parseBoolean(args[3]);
                                            worldeditClear(player, callHandler, skipVanilla);
                                    } catch (NumberFormatException e) {
                                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "callHandler / skipVanilla"));
                                    }
                                }
                                case 3 -> {
                                    try {
                                        boolean callHandler = Boolean.parseBoolean(args[2]);
                                        worldeditClear(player, callHandler, true);
                                    } catch (NumberFormatException e) {
                                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "callHandler"));
                                    }
                                }
                                default -> {
                                    worldeditClear(player, true, true);
                                }
                            }
                        }

                        case "paste" -> {
                            if (args.length <= 2) {
                                player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "sfId"));
                                return true;
                            }
                            boolean overrideData = false;
                            boolean force = false;
                            switch (args.length) {
                                case 5 -> {
                                    if (args[3].toLowerCase(Locale.ROOT).equals("override")) {
                                        overrideData = true;
                                    }
                                    force = Boolean.parseBoolean(args[4]);
                                }
                                case 4 -> {
                                    if (args[3].toLowerCase(Locale.ROOT).equals("override")) {
                                        overrideData = true;
                                    }
                                }
                            }
                            worldeditPaste(player, args[2], overrideData, force);
                        }

                        case "blockmenu" -> {
                            if (args.length <= 2) {
                                player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                                return true;
                            }

                            switch (args[2].toLowerCase(Locale.ROOT)) {
                                case "setslot" -> {
                                    if (args.length <= 3) {
                                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "slot"));
                                        return true;
                                    }

                                    try {
                                        int slot = Integer.parseInt(args[3]);
                                        worldeditBlockMenuSetSlot(player, slot);
                                    } catch (NumberFormatException e) {
                                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "slot"));
                                    }
                                }

                                default -> {
                                    player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                                }
                            }
                        }

                        case "blockinfo" -> {
                            if (args.length <= 2) {
                                player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                                return true;
                            }

                            switch (args[2].toLowerCase(Locale.ROOT)) {
                                case "add", "set" -> {
                                    switch (args.length) {
                                        case 3 -> {
                                            player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "key"));
                                        }
                                        case 4 -> {
                                            player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "value"));
                                        }
                                        case 5 -> {
                                            String key = args[3];
                                            String value = args[4];
                                            worldeditBlockInfoAdd(player, key, value);
                                        }
                                    }
                                }
                                case "remove" -> {
                                    if (args.length <= 3) {
                                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "value"));
                                        return true;
                                    }

                                    String value = args[3];
                                    worldeditBlockInfoRemove(player, value);
                                }

                                default -> {
                                    player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "subCommand"));
                                }
                            }
                        }
                    }
                }
                
                case "updateitem" -> {
                    if (!player.hasPermission("networks.admin") && !player.hasPermission("networks.commands.updateitem")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    updateItem(player);
                    return true;
                }
                
                case "getstorageitem" -> {
                    if (!player.hasPermission("networks.admin") && !player.hasPermission("networks.commands.getstorageitem")) {
                        player.sendMessage(getErrorMessage(ErrorType.NO_PERMISSION));
                        return true;
                    }

                    if (args.length <= 2) {
                        player.sendMessage(getErrorMessage(ErrorType.MISSING_REQUIRED_ARGUMENT, "slot"));
                        return true;
                    }

                    try {
                        int slot = Integer.parseInt(args[1]);
                        getStorageItem(player, slot);
                    } catch (NumberFormatException e) {
                        player.sendMessage(getErrorMessage(ErrorType.INVALID_REQUIRED_ARGUMENT, "slot"));
                    }

                    return true;
                }
                
                default -> {
                    help(player, null);
                }
            }
        }
        // We always return true, even if the command was not executed, so that the help message is not shown.
        return true;
    }

    public void fillQuantum(Player player, int amount) {
        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) {
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
        if (blueprint.getType() == Material.AIR) {
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

    }

    @Override
    public @Nullable List<String> onTabComplete(
            @Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> raw = onTabCompleteRaw(sender, args);
        return StringUtil.copyPartialMatches(args[args.length - 1], raw, new ArrayList<>());
    }

    public @Nonnull List<String> onTabCompleteRaw(@Nonnull CommandSender sender, @Nonnull String[] args) {
        switch (args.length) {
            case 1 -> {
                return List.of(
                        "addStorageItem",
                        "fillQuantum",
                        "fixBlueprint",
                        "getStorageItem",
                        "help",
                        "reduceStorageItem",
                        "setContainerId",
                        "setQuantum",
                        "updateItem",
                        "worldedit"
                );
            }
            case 2 -> {
                return switch (args[0].toLowerCase(Locale.ROOT)) {
                    // case "help", "updateitem" -> List.of();
                    case "getstorageitem" -> List.of("<slot>");
                    case "fillquantum", "addstorageitem", "reducestorageitem", "setquantum" -> List.of("<amount>");
                    case "fixblueprint" -> List.of("<keyInMeta>");
                    case "setcontainerid" -> List.of("<containerId>");
                    case "worldedit" -> List.of("pos1", "pos2", "paste", "clear", "blockmenu", "blockinfo");
                    default -> List.of();
                };
            }
            case 3 -> {
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
                }
            }
            case 4 -> {
                if (args[0].equalsIgnoreCase("worldedit")) {
                    return switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "paste" -> List.of("override", "keep");
                        case "blockmenu" -> switch (args[2].toLowerCase(Locale.ROOT)) {
                            case "setslot" -> List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53");
                            default -> List.of();
                        };
                        case "clear" -> List.of("true", "false");
                        default -> List.of();
                    };
                }
            }
            case 5 -> {
                if (args[0].equalsIgnoreCase("worldedit")) {
                    if (args[1].equalsIgnoreCase("paste")) {
                        return List.of("true", "false");
                    }
                }
            }
        }
        
        return new ArrayList<>();
    }

    public String getErrorMessage(ErrorType errorType) {
        return getErrorMessage(errorType, null);
    }

    public String getErrorMessage(ErrorType errorType, String argument) {
        return switch (errorType) {
            case NO_PERMISSION -> "你没有权限执行此命令! ";
            case NO_ITEM_IN_HAND -> "你必须在手上持有物品! ";
            case MISSING_REQUIRED_ARGUMENT -> "缺少必要参数: <" + argument + ">";
            case INVALID_REQUIRED_ARGUMENT -> "无效的参数: <" + argument + ">";
            case MUST_BE_PLAYER -> "此命令只能由玩家执行! ";
            default -> "未知错误! ";
        };
    }
}
