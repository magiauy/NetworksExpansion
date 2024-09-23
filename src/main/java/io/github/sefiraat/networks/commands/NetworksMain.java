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

        int downX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int upX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int downY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int upY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int downZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int upZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        return (long) (Math.abs(upX - downX) + 1) * (Math.abs(upY - downY) + 1) * (Math.abs(upZ - downZ) + 1);
    }

    private static void doWorldEdit(Location pos1, Location pos2, Consumer<Location> consumer) {
        if (pos1 == null || pos2 == null) {
            return;
        }
        int downX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int upX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int downY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int upY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int downZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int upZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        for (int x = downX; x <= upX; x++) {
            for (int y = downY; y <= upY; y++) {
                for (int z = downZ; z <= upZ; z++) {
                    consumer.accept(new Location(pos1.getWorld(), x, y, z));
                }
            }
        }
    }

    public static void restore(Player p) {
        Block target = p.getTargetBlockExact(5);
        if (target == null || target.getType().isAir()) {
            p.sendMessage(ChatColor.RED + "Please point to a network storage unit");
            return;
        }
        Location l = target.getLocation();
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(l);
        if (blockData != null) {
            String id = blockData.getData("containerId");
            if (id != null) {
                p.sendMessage(ChatColor.RED + "The data in this unit is normal and does not need to be restored.");
                return;
            }
        } else {
            p.sendMessage(ChatColor.GREEN + "In progress, please wait...");
            DataStorage.restoreFromLocation(l, opData -> {
                if (opData.isPresent()) {
                    StorageUnitData data = opData.get();
                    String sfId = ExpansionItemStacks.getStorageItemFromType(data.getSizeType()).getItemId();

                    CargoStorageUnit.addBlockInfo(l, data.getId(), false, false);
                    Slimefun.getDatabaseManager().getBlockDataController().createBlock(l, sfId);
                    p.sendMessage(ChatColor.GREEN + "Restore Successfully");
                } else {
                    p.sendMessage(ChatColor.RED + "No data found.");
                    return;
                }
            });
        }
    }

    public static void setQuantum(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must hold the item to execute the command!");
            return;
        }

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        Location targetLocation = targetBlock.getLocation();
        ItemStack clone = itemInHand.clone();
        if (slimefunItem instanceof NetworkQuantumStorage) {
            BlockMenu blockMenu = StorageCacheUtils.getMenu(targetLocation);
            if (blockMenu == null) {
                player.sendMessage(Theme.ERROR + "Cannot set item to air");
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
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }
    }

    private static void addStorageItem(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must hold the item to execute the command!");
            return;
        }

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        if (slimefunItem instanceof CargoStorageUnit) {
            Location targetLocation = targetBlock.getLocation();
            ItemStack clone = itemInHand.clone();
            StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

            if (data == null) {
                player.sendMessage(Theme.ERROR + "This network Quantum does not exist or its corrupted!");
                return;
            }

            clone.setAmount(amount);
            data.depositItemStack(clone, false);
            CargoStorageUnit.setStorageData(targetLocation, data);
            player.sendMessage(ChatColor.GREEN + "Item updated");
        } else {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }
    }

    private static void reduceStorageItem(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must hold the item to execute the command!");
            return;
        }

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        if (slimefunItem instanceof CargoStorageUnit) {
            Location targetLocation = targetBlock.getLocation();
            ItemStack clone = itemInHand.clone();
            StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

            if (data == null) {
                player.sendMessage(Theme.ERROR + "This network Quantum does not exist or its corrupted!\"");
                return;
            }

            clone.setAmount(1);
            data.requestItem(new ItemRequest(clone, amount));
            CargoStorageUnit.setStorageData(targetLocation, data);
            player.sendMessage(ChatColor.GREEN + "Item updated");
        } else {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }
    }

    public static void setContainerId(Player player, int containerId) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        if (!(slimefunItem instanceof CargoStorageUnit)) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        Location location = targetBlock.getLocation();

        player.sendMessage(ChatColor.GREEN + "Data requested, please wait...");
        CargoStorageUnit.requestData(location, containerId);
        player.sendMessage(ChatColor.GREEN +
                "has been set to be located at" + location.getWorld().getName()
                + " " + location.getBlockX()
                + " " + location.getBlockY()
                + " " + location.getBlockZ()
                + " The network quantum ID:" + containerId + ".");
    }


    public static void worldeditPos1(Player player) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null) {
            targetBlock = player.getLocation().getBlock();
        }
        setPos1(player, targetBlock.getLocation());
        if (getPos2(player) == null) {
            player.sendMessage(ChatColor.GREEN + "Set Pos1 to " + locationToString(getPos1(player)));
        } else {
            player.sendMessage(ChatColor.GREEN + "Set Pos1 to " + locationToString(getPos1(player)) + "(" + locationRange(getPos1(player), getPos2(player)) + " Blocks)");
        }
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
        setPos2(player, targetBlock.getLocation());
        if (getPos1(player) == null) {
            player.sendMessage(ChatColor.GREEN + "Set Pos2 to " + locationToString(getPos2(player)));
        } else {
            player.sendMessage(ChatColor.GREEN + "Set Pos2 to " + locationToString(getPos2(player)) + "(" + locationRange(getPos1(player), getPos2(player)) + " Blocks)");
        }
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
        SlimefunItem sfItem = SlimefunItem.getById(sfid);

        if (getPos1(player) == null || getPos2(player) == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
            return;
        }

        if (getPos1(player).getWorld() != getPos2(player).getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
            return;
        }

        if (sfItem == null) {
            player.sendMessage(ChatColor.RED + "This is not a valid slimefun ID!");
            return;
        }

        if (!sfItem.getItem().getType().isBlock()) {
            player.sendMessage(ChatColor.RED + "This is not a valid slimefun ID!");
            return;
        }

        if (sfItem.getItem().getType().isAir()) {
            player.sendMessage(ChatColor.RED + "Cannot be placed!");
            return;
        }

        if (!force && sfItem instanceof NotPlaceable) {
            player.sendMessage(ChatColor.RED + "不可放置的粘液方块！");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "Pasting blocks from " + getPos1(player).toString() + " to " + getPos2(player).toString());
        long currentMillSeconds = System.currentTimeMillis();

        AtomicInteger count = new AtomicInteger();
        Material t = sfItem.getItem().getType();
        ItemStack itemStack = sfItem.getItem();
        final PlayerSkin skin;
        final boolean isHead;
        if (itemStack.getType() == Material.PLAYER_HEAD || itemStack.getType() == Material.PLAYER_WALL_HEAD) {
            if (itemStack instanceof SlimefunItemStack sfis) {
                Optional<String> texture = sfis.getSkullTexture();
                if (texture.isPresent()) {
                    skin = PlayerSkin.fromBase64(texture.get());
                    isHead = true;
                } else {
                    skin = null;
                    isHead = false;
                }
            } else {
                skin = null;
                isHead = false;
            }
        } else {
            skin = null;
            isHead = false;
        }
        // java's operation ↑
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            Block targetBlock = getPos1(player).getWorld().getBlockAt(location);
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
        long currentMillSeconds = System.currentTimeMillis();

        AtomicInteger count = new AtomicInteger();
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            Block targetBlock = getPos1(player).getWorld().getBlockAt(location);
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
            player.sendMessage(ChatColor.RED + "The slot number must be between 0 and 53!");
            return;
        }
        ItemStack hand = player.getInventory().getItemInMainHand();

        player.sendMessage(ChatColor.GREEN + "Setting slot " + slot + " to " + hand.getItemMeta().getDisplayName());
        long currentMillSeconds = System.currentTimeMillis();

        AtomicInteger count = new AtomicInteger();
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            BlockMenu menu = StorageCacheUtils.getMenu(location);
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
        long currentMillSeconds = System.currentTimeMillis();

        AtomicInteger count = new AtomicInteger();
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
        long currentMillSeconds = System.currentTimeMillis();

        AtomicInteger count = new AtomicInteger();
        doWorldEdit(getPos1(player), getPos2(player), (location -> {
            if (StorageCacheUtils.getBlock(location) != null) {
                StorageCacheUtils.removeData(location, key);
                count.addAndGet(1);
            }
        }));
        player.sendMessage("Remove " + key + " done in " + (System.currentTimeMillis() - currentMillSeconds) + "ms");
    }

    private static void updateItem(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        SlimefunItem slimefunItem = SlimefunItem.getByItem(itemInHand);
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "Can't update non-Slimefun items!");
            return;
        }

        String currentId = slimefunItem.getId();
        if (slimefunItem instanceof CargoStorageUnit) {
            player.sendMessage(ChatColor.RED + "Unsupported!");
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
                player.sendMessage(ChatColor.GREEN + "Item has been updated!");
                return;
            }

            ItemStack stored = quantumCache.getItemStack();
            SlimefunItem sfi = SlimefunItem.getByItem(stored);
            if (sfi != null) {
                String quantumStoredId = sfi.getId();
                stored.setItemMeta(SlimefunItem.getById(quantumStoredId).getItem().getItemMeta());
                player.sendMessage(ChatColor.GREEN + "Item in network quantum has been updated!");
            }
            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
            quantumCache.updateMetaLore(meta);
            itemInHand.setItemMeta(meta);
            player.sendMessage(ChatColor.GREEN + "Item has been updated!");
        } else {
            itemInHand.setItemMeta(SlimefunItem.getById(currentId).getItem().getItemMeta());
            player.sendMessage(ChatColor.GREEN + "Item has been updated!");
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
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(targetBlock.getLocation());
        if (blockData == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(targetBlock.getLocation());
        if (slimefunItem == null) {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }

        if (slimefunItem instanceof CargoStorageUnit) {
            Location targetLocation = targetBlock.getLocation();
            StorageUnitData data = CargoStorageUnit.getStorageData(targetLocation);

            if (data == null) {
                player.sendMessage(Theme.ERROR + "This network quantum does not exist or its corrupted");
                return;
            }

            List<ItemContainer> stored = data.getStoredItems();
            if (slot >= stored.size()) {
                player.sendMessage(Theme.ERROR + "The slot number must be between 0-" + (stored.size() - 1) + "!");
                return;
            } else {
                ItemStack stack = stored.get(slot).getSample();
                if (stack == null || stack.getType().isAir()) {
                    player.sendMessage(Theme.ERROR + "There is no item in that slot!");
                    return;
                } else {
                    player.getInventory().addItem(StackUtils.getAsQuantity(stack, 1));
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You must point to a network Quantum to execute the command!");
            return;
        }
    }

    public static void help(CommandSender sender, String mainCommand) {
        if (mainCommand == null) {
            sender.sendMessage(ChatColor.GOLD + "Network Command Help:");
            sender.sendMessage(ChatColor.GOLD + "/networks help - Show help message.");
            sender.sendMessage(ChatColor.GOLD + "/networks fillquantum <amount> - Filling the storage of handheld quantum storage items.");
            sender.sendMessage(ChatColor.GOLD + "/networks fixblueprint <keyInMeta> - Fix held Crafting Blueprints.");
            sender.sendMessage(ChatColor.GOLD + "/networks restore - Recovery of error network drawer units.");
            sender.sendMessage(ChatColor.GOLD + "/networks addstorageitem <amount> - Add items to the network storage of handheld items.");
            sender.sendMessage(ChatColor.GOLD + "/networks reducestorageitem <amount> - Reduce items from the network storage of held items.");
            sender.sendMessage(ChatColor.GOLD + "/networks setquantum <amount> - Setting the storage capacity of held quantum storage items.");
            sender.sendMessage(ChatColor.GOLD + "/networks setcontainerid <Id> - Setting the ID of the network storage.");
            sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - SlimeFun God of Creation Features.");
            sender.sendMessage(ChatColor.GOLD + "/networks updateItem - Update held items.");
            sender.sendMessage(ChatColor.GOLD + "/networks findCachedStorages <playerName> - Locates Network Storages placed by a given player.");
            return;
        }
        switch (mainCommand.toLowerCase(Locale.ROOT)) {
            case "help" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks help - Show help message.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks help");
            }
            case "fillquantum" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks fillQuantum <amount> - Set the storage capacity of the held quantum storage");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks fillQuantum 1000");
            }
            case "fixblueprint" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks fixBlueprint <keyInMeta> - Fix held crafting blueprint.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks fixBlueprint networks-changed");
            }
            case "restore" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks restore - Recovery of error network drawer units.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks restore");
            }
            case "addstorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks addStorageItem <amount> - Add items to the network storage of handheld items.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks addstorageItem 1000");
            }
            case "reducestorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks reduceStorageItem <amount> - Add items to the network storage of handheld items.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks reduceStorageItem 1000");
            }
            case "setquantum" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks setQuantum <amount> - Reduce items from the network storage of held items.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks setQuantum 1000");
            }
            case "setcontainerid" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks setContainerId <containerId> - Setting the ID of the network storage.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks setContainerId 6");
            }
            case "findcachedstorages" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks findCachedStorages <playerName> - SlimeFun God of Creation Features.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks findCachedStorages Notch");
            }
            case "getstorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks getStorageItem <slot> - Locates Network Storages placed by a given player.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks getStorageItem 0");
            }
            case "worldedit" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - Slimefun features.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos1 - Select the 1st position.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos2 - Select the 2nd position.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> - Paste Slimefun machines.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> override - Overwrite the original data.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> keep - Keep the original data.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit clear <callHandler> - Remove Slimefun machines.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockMenu setSlot <slot> - Change the slot inside the selected machines to held items.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockInfo add <key> <value> - Change Slimefun machine info");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockInfo remove <value> - Remove Slimefun machine info");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockInfo set <key> <value> - Set Slimefun machine info");
            }
            case "updateitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks updateItem - Update held items.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks updateItem");
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Unknown command! Please use /networks help for information.");
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
                            if (args.length >= 5) {
                                switch (args[3].toLowerCase(Locale.ROOT)) {
                                    case "override" -> {
                                        worldeditPaste(player, args[2], true, Boolean.parseBoolean(args[4]));
                                    }
                                    case "keep" -> {
                                        worldeditPaste(player, args[2], false, Boolean.parseBoolean(args[4]));
                                    }
                                    default -> {
                                        worldeditPaste(player, args[2], false, Boolean.parseBoolean(args[4]));
                                    }
                                }
                            } else if (args.length >= 4) {
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
            player.sendMessage(Theme.ERROR + "You must hold the quantum storage.");
            return;
        }

        SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);

        if (!(slimefunItem instanceof NetworkQuantumStorage)) {
            player.sendMessage(Theme.ERROR + "The item in your hand must be for quantum storage.");
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        final QuantumCache quantumCache = DataTypeMethods.getCustom(
                meta,
                Keys.QUANTUM_STORAGE_INSTANCE,
                PersistentQuantumStorageType.TYPE
        );

        if (quantumCache == null || quantumCache.getItemStack() == null) {
            player.sendMessage(Theme.ERROR + "Quantum storage unspecified item or damaged.");
            return;
        }

        quantumCache.setAmount(amount);
        DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
        quantumCache.updateMetaLore(meta);
        itemStack.setItemMeta(meta);
        player.sendMessage(Theme.SUCCESS + "Item Updated.");
    }

    public void fixBlueprint(Player player, String before) {
        ItemStack blueprint = player.getInventory().getItemInMainHand();
        if (blueprint.getType().isAir()) {
            player.sendMessage(Theme.ERROR + "You must hold the blueprint.");
            return;
        }

        final SlimefunItem item = SlimefunItem.getByItem(blueprint);

        if (!(item instanceof CraftingBlueprint)) {
            player.sendMessage(Theme.ERROR + "You must hold the blueprint.");
            return;
        }

        ItemMeta blueprintMeta = blueprint.getItemMeta();

        final Optional<BlueprintInstance> optional = DataTypeMethods.getOptionalCustom(
                blueprintMeta,
                new NamespacedKey(before, "ntw_blueprint"),
                PersistentCraftingBlueprintType.TYPE
        );

        if (optional.isEmpty()) {
            player.sendMessage(Theme.ERROR + "Inaccesible instance");
            return;
        }

        BlueprintInstance instance = optional.get();

        ItemStack fix = NetworksSlimefunItemStacks.CRAFTING_BLUEPRINT.clone();
        CraftingBlueprint.setBlueprint(fix, instance.getRecipeItems(), instance.getItemStack());

        blueprint.setItemMeta(fix.getItemMeta());

        player.sendMessage(Theme.SUCCESS + "Blueprint fixed.");

    }

    @Override
    public @Nullable List<String> onTabComplete(
            @Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> raw = onTabCompleteRaw(sender, args);
        return StringUtil.copyPartialMatches(args[args.length - 1], raw, new ArrayList<>());
    }

    public @Nonnull List<String> onTabCompleteRaw(@Nonnull CommandSender sender, @Nonnull String[] args) {
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
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("worldedit")) {
                if (args[1].equalsIgnoreCase("paste")) {
                    return List.of("true", "false");
                }
            }
        }
        return new ArrayList<>();
    }

    public String getErrorMessage(ERROR_TYPE errorType) {
        return getErrorMessage(errorType, null);
    }

    public String getErrorMessage(ERROR_TYPE errorType, String argument) {
        return switch (errorType) {
            case NO_PERMISSION -> "You do not have permission to execute this command! ";
            case NO_ITEM_IN_HAND -> "You must hold the items! ";
            case MISSING_REQUIRED_ARGUMENT -> "Missing required argument: <" + argument + ">";
            case INVALID_REQUIRED_ARGUMENT -> "Invalid required argument: <" + argument + ">";
            case MUST_BE_PLAYER -> "This command can only be executed by the player! ";
            default -> "Unknown Error! ";
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
