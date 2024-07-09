package io.github.sefiraat.networks.commands;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.core.item.machine.cargo.cargoexpansion.data.DataStorage;
import com.ytdd9527.networks.expansion.core.item.machine.cargo.cargoexpansion.items.storage.CargoStorageUnit;
import com.ytdd9527.networks.expansion.core.item.machine.cargo.cargoexpansion.items.storage.StorageUnitData;
import com.ytdd9527.networks.expansion.setup.ExpansionItemStacks;
import io.github.sefiraat.networks.network.stackcaches.BlueprintInstance;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import io.github.sefiraat.networks.slimefun.tools.CraftingBlueprint;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.Theme;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentCraftingBlueprintType;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Placeable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.*;
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
import java.util.*;

//!TODO 调整过于复杂的逻辑，需要重构

public class NetworksMain implements TabExecutor {

    private static final Map<Integer, NetworkQuantumStorage> QUANTUM_REPLACEMENT_MAP = new HashMap<>();

    static {
        QUANTUM_REPLACEMENT_MAP.put(4096, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_1);
        QUANTUM_REPLACEMENT_MAP.put(32768, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_2);
        QUANTUM_REPLACEMENT_MAP.put(262144, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_3);
        QUANTUM_REPLACEMENT_MAP.put(2097152, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_4);
        QUANTUM_REPLACEMENT_MAP.put(16777216, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_5);
        QUANTUM_REPLACEMENT_MAP.put(134217728, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_6);
        QUANTUM_REPLACEMENT_MAP.put(1073741824, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_7);
        QUANTUM_REPLACEMENT_MAP.put(Integer.MAX_VALUE, NetworkSlimefunItems.NETWORK_QUANTUM_STORAGE_8);
    }

    private static final Map<String, String> ID_UPDATE_MAP = new HashMap<>();

    static {
        ID_UPDATE_MAP.put("NE_EXPANSION_WORKBENCH", "NTW_EXPANSION_WORKBENCH");
        ID_UPDATE_MAP.put("NE_COORDINATE_CONFIGURATOR", "NTW_EXPANSION_COORDINATE_CONFIGURATOR");
        ID_UPDATE_MAP.put("NE_ADVANCED_IMPORT", "NTW_EXPANSION_ADVANCED_IMPORT");
        ID_UPDATE_MAP.put("NE_ADVANCED_EXPORT", "NTW_EXPANSION_ADVANCED_EXPORT");
        ID_UPDATE_MAP.put("NE_ADVANCED_PURGER", "NTW_EXPANSION_ADVANCED_PURGER");
        ID_UPDATE_MAP.put("NE_ADVANCED_GREEDY_BLOCK", "NTW_EXPANSION_ADVANCED_GREEDY_BLOCK");
        ID_UPDATE_MAP.put("NTW_CAPACITOR_5", "NTW_EXPANSION_CAPACITOR_5");
        ID_UPDATE_MAP.put("NE_ADVANCED_QUANTUM_STORAGE", "NTW_EXPANSION_ADVANCED_QUANTUM_STORAGE");
        ID_UPDATE_MAP.put("NE_CHAING_PUSHER", "NTW_EXPANSION_LINE_TRANSFER_PUSHER");
        ID_UPDATE_MAP.put("NE_EXPANSION_GRABBER_1", "NTW_EXPANSION_LINE_TRANSFER_GRABBER");
        ID_UPDATE_MAP.put("NE_CHAIN_DISPATCHER", "NTW_EXPANSION_LINE_TRANSFER");
        ID_UPDATE_MAP.put("NE_CHAING_PUSHER_PLUS", "NTW_EXPANSION_LINE_TRANSFER_PLUS_PUSHER");
        ID_UPDATE_MAP.put("NE_EXPANSION_GRABBER_1_PLUS", "NTW_EXPANSION_LINE_TRANSFER_PLUS_GRABBER");
        ID_UPDATE_MAP.put("NE_CHAIN_DISPATCHER_PLUS", "NTW_EXPANSION_LINE_TRANSFER_PLUS");
        ID_UPDATE_MAP.put("NE_ADVANCED_CHAING_PUSHER", "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PUSHER");
        ID_UPDATE_MAP.put("NE_ADVANCED_EXPANSION_GRABBER_1", "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_GRABBER");
        ID_UPDATE_MAP.put("NE_ADVANCED_CHAIN_DISPATCHER", "NTW_EXPANSION_ADVANCED_LINE_TRANSFER");
        ID_UPDATE_MAP.put("NE_ADVANCED_CHAING_PUSHER_PLUS", "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS_PUSHER");
        ID_UPDATE_MAP.put("NE_ADVANCED_EXPANSION_GRABBER_1_PLUS", "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS_GRABBER");
        ID_UPDATE_MAP.put("NE_ADVANCED_CHAIN_DISPATCHER_PLUS", "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS");

        /*
        ID_UPDATE_MAP.put("NE_MAGIC_WORKBENCH_BLUEPRINT", "NTW_EXPANSION_MAGIC_WORKBENCH_BLUEPRINT");
        ID_UPDATE_MAP.put("NE_ARMOR_FORGE_BLUEPRINT", "NTW_EXPANSION_ARMOR_FORGE_BLUEPRINT");
        ID_UPDATE_MAP.put("NE_SMELTERY_BLUEPRINT", "NTW_EXPANSION_SMELTERY_BLUEPRINT");
        ID_UPDATE_MAP.put("NE_QUANTUM_WORKBENCH_BLUEPRINT", "NTW_EXPANSION_QUANTUM_WORKBENCH_BLUEPRINT");
        ID_UPDATE_MAP.put("NE_ANCIENT_ALTAR_BLUEPRINT", "NTW_EXPANSION_ANCIENT_ALTAR_BLUEPRINT");
        ID_UPDATE_MAP.put("NE_EXPANSION_WORKBENCH_BLUEPRINT", "NTW_EXPANSION_WORKBENCH_BLUEPRINT");
        */

        ID_UPDATE_MAP.put("NE_MAGIC_WORKBENCH_RECIPE_ENCODER", "NTW_EXPANSION_MAGIC_WORKBENCH_RECIPE_ENCODER");
        ID_UPDATE_MAP.put("NE_ARMOR_FORGE_RECIPE_ENCODER", "NTW_EXPANSION_ARMOR_FORGE_RECIPE_ENCODER");
        ID_UPDATE_MAP.put("NE_SMELTERY_RECIPE_ENCODER", "NTW_EXPANSION_SMELTERY_RECIPE_ENCODER");
        ID_UPDATE_MAP.put("NE_QUANTUM_WORKBENCH_RECIPE_ENCODER", "NTW_EXPANSION_QUANTUM_WORKBENCH_RECIPE_ENCODER");
        ID_UPDATE_MAP.put("NE_ANCIENT_ALTAR_RECIPE_ENCODER", "NTW_EXPANSION_ANCIENT_ALTAR_RECIPE_ENCODER");
        ID_UPDATE_MAP.put("NE_EXPANSION_WORKBENCH_RECIPE_ENCODER", "NTW_EXPANSION_WORKBENCH_RECIPE_ENCODER");

        ID_UPDATE_MAP.put("NE_AUTO_MAGIC_WORKBENCH", "NTW_EXPANSION_AUTO_MAGIC_WORKBENCH");
        ID_UPDATE_MAP.put("NE_AUTO_ARMOR_FORGE", "NTW_EXPANSION_AUTO_ARMOR_FORGE");
        ID_UPDATE_MAP.put("NE_AUTO_SMELTERY", "NTW_EXPANSION_AUTO_SMELTERY");
        ID_UPDATE_MAP.put("NE_AUTO_QUANTUM_WORKBENCH", "NTW_EXPANSION_AUTO_QUANTUM_WORKBENCH");
        ID_UPDATE_MAP.put("NE_AUTO_ANCIENT_ALTAR", "NTW_EXPANSION_AUTO_ANCIENT_ALTAR");
        ID_UPDATE_MAP.put("NE_AUTO_EXPANSION_WORKBENCH", "NTW_EXPANSION_AUTO_EXPANSION_WORKBENCH");

        ID_UPDATE_MAP.put("NE_AUTO_MAGIC_WORKBENCH_WITHHOLDING", "NTW_EXPANSION_AUTO_MAGIC_WORKBENCH_WITHHOLDING");
        ID_UPDATE_MAP.put("NE_AUTO_ARMOR_FORGE_WITHHOLDING", "NTW_EXPANSION_AUTO_ARMOR_FORGE_WITHHOLDING");
        ID_UPDATE_MAP.put("NE_AUTO_SMELTERY_WITHHOLDING", "NTW_EXPANSION_AUTO_SMELTERY_WITHHOLDING");
        ID_UPDATE_MAP.put("NE_AUTO_QUANTUM_WORKBENCH_WITHHOLDING", "NTW_EXPANSION_AUTO_QUANTUM_WORKBENCH_WITHHOLDING");
        ID_UPDATE_MAP.put("NE_AUTO_ANCIENT_ALTAR_WITHHOLDING", "NTW_EXPANSION_AUTO_ANCIENT_ALTAR_WITHHOLDING");
        ID_UPDATE_MAP.put("NE_AUTO_EXPANSION_WORKBENCH_WITHHOLDING", "NTW_EXPANSION_AUTO_EXPANSION_WORKBENCH_WITHHOLDING");

        ID_UPDATE_MAP.put("NEA_AUTO_MAGIC_WORKBENCH", "NTW_EXPANSION_ADVANCED_AUTO_MAGIC_WORKBENCH");
        ID_UPDATE_MAP.put("NEA_AUTO_ARMOR_FORGE", "NTW_EXPANSION_ADVANCED_AUTO_ARMOR_FORGE");
        ID_UPDATE_MAP.put("NEA_AUTO_SMELTERY", "NTW_EXPANSION_ADVANCED_AUTO_SMELTERY");
        ID_UPDATE_MAP.put("NEA_AUTO_QUANTUM_WORKBENCH", "NTW_EXPANSION_ADVANCED_AUTO_QUANTUM_WORKBENCH");
        ID_UPDATE_MAP.put("NEA_AUTO_ANCIENT_ALTAR", "NTW_EXPANSION_ADVANCED_AUTO_ANCIENT_ALTAR");
        ID_UPDATE_MAP.put("NEA_AUTO_EXPANSION_WORKBENCH", "NTW_EXPANSION_ADVANCED_AUTO_EXPANSION_WORKBENCH");

        ID_UPDATE_MAP.put("NEA_AUTO_MAGIC_WORKBENCH_WITHHOLDING", "NTW_EXPANSION_ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING");
        ID_UPDATE_MAP.put("NEA_AUTO_ARMOR_FORGE_WITHHOLDING", "NTW_EXPANSION_ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING");
        ID_UPDATE_MAP.put("NEA_AUTO_SMELTERY_WITHHOLDING", "NTW_EXPANSION_ADVANCED_AUTO_SMELTERY_WITHHOLDING");
        ID_UPDATE_MAP.put("NEA_AUTO_QUANTUM_WORKBENCH_WITHHOLDING", "NTW_EXPANSION_ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING");
        ID_UPDATE_MAP.put("NEA_AUTO_ANCIENT_ALTAR_WITHHOLDING", "NTW_EXPANSION_ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING");
        ID_UPDATE_MAP.put("NEA_AUTO_EXPANSION_WORKBENCH_WITHHOLDING", "NTW_EXPANSION_ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING");

        ID_UPDATE_MAP.put("NE_BRIDGE_WHITE", "NTW_EXPANSION_BRIDGE_WHITE");
        ID_UPDATE_MAP.put("NE_BRIDGE_ORANGE", "NTW_EXPANSION_BRIDGE_ORANGE");
        ID_UPDATE_MAP.put("NE_BRIDGE_MAGENTA", "NTW_EXPANSION_BRIDGE_MAGENTA");
        ID_UPDATE_MAP.put("NE_BRIDGE_LIGHT_BLUE", "NTW_EXPANSION_BRIDGE_LIGHT_BLUE");
        ID_UPDATE_MAP.put("NE_BRIDGE_YELLOW", "NTW_EXPANSION_BRIDGE_YELLOW");
        ID_UPDATE_MAP.put("NE_BRIDGE_LIME", "NTW_EXPANSION_BRIDGE_LIME");
        ID_UPDATE_MAP.put("NE_BRIDGE_PINK", "NTW_EXPANSION_BRIDGE_PINK");
        ID_UPDATE_MAP.put("NE_BRIDGE_GRAY", "NTW_EXPANSION_BRIDGE_GRAY");
        ID_UPDATE_MAP.put("NE_BRIDGE_LIGHT_GRAY", "NTW_EXPANSION_BRIDGE_LIGHT_GRAY");
        ID_UPDATE_MAP.put("NE_BRIDGE_CYAN", "NTW_EXPANSION_BRIDGE_CYAN");
        ID_UPDATE_MAP.put("NE_BRIDGE_PURPLE", "NTW_EXPANSION_BRIDGE_PURPLE");
        ID_UPDATE_MAP.put("NE_BRIDGE_BLUE", "NTW_EXPANSION_BRIDGE_BLUE");
        ID_UPDATE_MAP.put("NE_BRIDGE_BROWN", "NTW_EXPANSION_BRIDGE_BROWN");
        ID_UPDATE_MAP.put("NE_BRIDGE_GREEN", "NTW_EXPANSION_BRIDGE_GREEN");
        ID_UPDATE_MAP.put("NE_BRIDGE_RED", "NTW_EXPANSION_BRIDGE_RED");
        ID_UPDATE_MAP.put("NE_BRIDGE_BLACK", "NTW_EXPANSION_BRIDGE_BLACK");

    }

    private static Location POS1 = null;
    private static Location POS2 = null;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length == 0) {
            help(sender, null);
        }
        switch (args[0]) {
            case "fillquantum", "fixblueprint", "addstorageitem", "reducestorageitem", "setquantum", "restore", "setcontainerid" -> {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Theme.ERROR + "只有玩家才能执行该命令");
                    return false;
                }
            }
            case "help" -> {

            }
        }

        // 玩家或控制台皆可
        if (args[0].equalsIgnoreCase("help")) {
            if (sender.isOp()) {
                if (args.length >= 2) {
                    help(sender, args[1]);
                } else {
                    help(sender, null);
                }
            } else {
                sender.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                return true;
            }
        }

        // 如果是玩家
        if (sender instanceof Player player) {
            if (args[0].equalsIgnoreCase("fillquantum")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.fillquantum")) && args.length >= 2) {
                    if (args.length >= 2) {
                        try {
                            int number = Integer.parseInt(args[1]);
                            fillQuantum(player, number);
                            return true;
                        } catch (NumberFormatException exception) {
                            player.sendMessage(Theme.ERROR + "Wrong argument: <amount>. Range: 0 ~ 2147483647");
                        }
                    } else {
                        player.sendMessage(Theme.ERROR + "Missing argument: <amount>");
                    }
                } else {
                    player.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("fixblueprint")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.fixblueprint"))) {
                    if (args.length >= 2) {
                        String before = args[1];
                        fixBlueprint(player, before);
                    } else {
                        player.sendMessage(Theme.ERROR + "Missing argument: <keyInMeta>");
                    }
                } else {
                    player.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("restore")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.restore"))) {
                    restore(player);
                } else {
                    player.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("setQuantum")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.setquantum"))) {
                    if (args.length >= 2) {
                        setQuantum(player, Integer.parseInt(args[1]));
                    } else {
                        player.sendMessage(Theme.ERROR + "Missing argument: <amount>");
                    }
                } else {
                    player.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("addstorage")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.addstorage"))) {
                    if (args.length >= 2) {
                        int amount = Integer.parseInt(args[1]);
                        addStorageItem(player, amount);
                    } else {
                        player.sendMessage(Theme.ERROR + "Missing argument: <amount>");
                    }
                } else {
                    player.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("reducestorage")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.reducestorage"))) {
                    if (args.length >= 2) {
                        int amount = Integer.parseInt(args[1]);
                        reduceStorageItem(player, amount);
                    } else {
                        player.sendMessage(Theme.ERROR + "Missing argument: <amount>");
                    }
                } else {
                    player.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("setcontainerid")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.setcontainerid"))) {
                    if (args.length >= 2) {
                        String containerId = args[1];
                        try {
                            setContainerId(player, Integer.parseInt(containerId));
                        } catch (NumberFormatException e) {
                            player.sendMessage(Theme.ERROR + "Invalid container ID: " + containerId);
                        }
                    } else {
                        player.sendMessage(Theme.ERROR + "Missing argument: <containerId>");
                    }
                } else {
                    player.sendMessage(Theme.ERROR + "你没有权限执行该命令");
                }
            }

            if (args[0].equalsIgnoreCase("worldedit")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.worldedit.*"))) {
                    switch (args[1].toLowerCase(Locale.ROOT)) {
                        case "pos1" -> {
                            worldeditPos1(player);
                        }
                        case "pos2" -> {
                            worldeditPos2(player);
                        }
                        case "clear" -> {
                            worldeditClear(player);
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
                                player.sendMessage(Theme.ERROR + "Missing argument: <sfId>");
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
                                                player.sendMessage(Theme.ERROR + "Invalid slot: " + args[3]);
                                            }
                                        } else {
                                            player.sendMessage(Theme.ERROR + "Missing argument: <slot>");
                                        }
                                    }
                                    default -> {
                                        player.sendMessage(Theme.ERROR + "Invalid sub-command: " + args[2]);
                                    }
                                }
                            } else {
                                player.sendMessage(Theme.ERROR + "Missing argument: <subCommand>");
                            }
                        }
                        case "blockinfo" -> {
                            if (args.length >= 3) {
                                switch (args[2].toLowerCase(Locale.ROOT)) {
                                    case "add", "set" -> {
                                        if (args.length == 3) {
                                            player.sendMessage(Theme.ERROR + "Missing argument: <key>");
                                        }

                                        if (args.length == 4) {
                                            player.sendMessage(Theme.ERROR + "Missing argument: <value>");
                                        }

                                        if (args.length >= 5) {
                                            String key = args[3];
                                            String value = args[4];
                                            worldeditBlockInfoAdd(player, key, value);
                                        }
                                    }
                                    case "remove" -> {
                                        if (args.length == 3) {
                                            player.sendMessage(Theme.ERROR + "Missing argument: <key>");
                                        }
                                        if (args.length >= 4) {
                                            String key = args[3];
                                            worldeditBlockInfoRemove(player, key);
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(Theme.ERROR + "Missing argument: <subCommand>");
                            }
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("updateItem")) {
                if ((player.isOp() || player.hasPermission("networks.admin") || player.hasPermission("networks.commands.updateitem"))) {
                    updateItem(player);
                }
            }
        } else {
            // 仅控制台时执行的操作
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
        if (blueprint == null || blueprint.getType() == Material.AIR) {
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

    public static void restore(Player p) {
        Block target = p.getTargetBlockExact(5);
        if (target == null || target.getType().isAir()) {
            p.sendMessage(ChatColor.RED+"请指向一个失效的网络抽屉单元");
        }
        Location l = target.getLocation();
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(l);
        if (blockData != null) {
            String id = blockData.getData("containerId");
            if(id != null) {
                p.sendMessage(ChatColor.RED+"该单元的数据正常，无需恢复。");
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
                }
            });
        }
    }

    public static void setQuantum(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
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
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
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
        } else {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }
    }

    private static void reduceStorageItem(Player player, int amount) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "你必须手持物品才能执行该指令!");
            return;
        }

        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
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
        } else {
            player.sendMessage(ChatColor.RED + "你必须指着一个网络抽屉才能执行该指令!");
            return;
        }
    }

    public static void setContainerId(Player player, int containerId) {
        Block targetBlock = player.getTargetBlockExact(8, FluidCollisionMode.NEVER);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
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
        }

        if (sfItem.getItem().getType().isAir()) {
            player.sendMessage(ChatColor.RED + "不可放置的粘液方块！");
        }

        if (POS1 == null || POS2 == null) {
            player.sendMessage(ChatColor.RED + "请先选中一个区域！");
        }

        if (POS1.getWorld() != POS2.getWorld()) {
            player.sendMessage(ChatColor.RED + "请选择同一世界的两个位置！");
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

    public static void worldeditClear(Player player) {
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
                        item.callItemHandler(BlockBreakHandler.class, h -> {
                            h.onPlayerBreak(
                                    new BlockBreakEvent(targetBlock, player),
                                    new ItemStack(Material.AIR),
                                    new ArrayList<>()
                            );
                        });
                    }
                    BlockStorage.deleteLocationInfoUnsafely(targetBlock.getLocation(), true);
                    targetBlock.setType(Material.AIR);
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
        if (slimefunItem instanceof NetworkQuantumStorage) {
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
            String quantumStoredId = SlimefunItem.getByItem(stored).getId();
            if (ID_UPDATE_MAP.get(quantumStoredId) != null) {
                player.sendMessage(ChatColor.GREEN + "已更新存储内的物品！");
                stored.setItemMeta(SlimefunItem.getById(ID_UPDATE_MAP.get(quantumStoredId)).getItem().getItemMeta());
            }
            DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, quantumCache);
            quantumCache.updateMetaLore(meta);
            itemInHand.setItemMeta(meta);
            player.sendMessage(ChatColor.GREEN + "已更新物品！");
        }

        if (slimefunItem instanceof CargoStorageUnit) {
            player.sendMessage(ChatColor.RED + "暂不支持此物品的更新");
            return;
        }

        if (ID_UPDATE_MAP.get(currentId) != null) {
            itemInHand.setItemMeta(SlimefunItem.getById(ID_UPDATE_MAP.get(currentId)).getItem().getItemMeta());
            player.sendMessage(ChatColor.GREEN + "已更新物品！");
        } else {
            player.sendMessage(ChatColor.RED + "不支持其他物品的更新");
        }
    }

    public static void help(CommandSender sender, String mainCommand) {
        if (mainCommand == null) {
            sender.sendMessage(ChatColor.GOLD + "网络命令帮助:");
            sender.sendMessage(ChatColor.GOLD + "/networks help - 显示此帮助信息.");
            sender.sendMessage(ChatColor.GOLD + "/networks fillquantum <amount> - 填充手持量子存储物品的存储量.");
            sender.sendMessage(ChatColor.GOLD + "/networks fixblueprint <keyInMeta> - 修复手持合成蓝图.");
            sender.sendMessage(ChatColor.GOLD + "/networks restore - 恢复失效的网络抽屉单元.");
            sender.sendMessage(ChatColor.GOLD + "/networks addstorageitem <amount> - 向手持物品的网络抽屉中添加物品.");
            sender.sendMessage(ChatColor.GOLD + "/networks reducestorageitem <amount> - 从手持物品的网络抽屉中减少物品.");
            sender.sendMessage(ChatColor.GOLD + "/networks setquantum <amount> - 设置手持量子存储物品的存储量.");
            sender.sendMessage(ChatColor.GOLD + "/networks setcontainerid <containerId> - 设置网络抽屉的容器ID.");
            sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - 粘液创世神功能.");
            sender.sendMessage(ChatColor.GOLD + "/networks updateItem - 更新手持物品.");
            return;
        }
        switch (mainCommand.toLowerCase(Locale.ROOT)){
            case "help" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks help - 显示此帮助信息.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks help");
            }
            case "fillquantum" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks fillquantum <amount> - 设置手持量子存储的存储量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks fillquantum 1000");
            }
            case "fixblueprint" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks fixblueprint <keyInMeta> - 修复手持合成蓝图.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks fixblueprint networks-changed");
            }
            case "restore" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks restore - 恢复失效的网络抽屉单元.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks restore");
            }
            case "addstorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks addstorageitem <amount> - 向指向的网络抽屉中添加手中物品指定数量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks addstorageitem 1000");
            }
            case "reducestorageitem" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks reducestorageitem <amount> - 向指向的网络抽屉中减少手中物品指定数量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks reducestorageitem 1000");
            }
            case "setquantum" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks setquantum <amount> - 设置指向的量子存储的物品为手上的物品，并设置存储指定数量.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks setquantum 1000");
            }
            case "setcontainerid" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks setcontainerid <containerId> - 设置指向的网络抽屉的容器ID.");
                sender.sendMessage(ChatColor.GOLD + "ex: /networks setcontainerid 6");
            }
            case "worldedit" -> {
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit <subCommand> - 粘液创世神功能.");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos1 - 选择第一个位置");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit pos2 - 选择第二个位置");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> - 粘贴粘液方块");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> override - 覆盖原本的数据");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit paste <sfid> keep - 保留原本的数据");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit clear - 清除粘液方块");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockmenu setSlot <slot> - 修改选中区域的粘液方法的菜单的对应槽位为手上物品");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockinfo add <key> <value> - 增加粘液方块信息");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockinfo remove <value> - 移除粘液方块信息");
                sender.sendMessage(ChatColor.GOLD + "/networks worldedit blockinfo set <key> <value> - 设置粘液方块信息");
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
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> raw = onTabCompleteRaw(args);
        return StringUtil.copyPartialMatches(args[args.length - 1], raw, new ArrayList<>());
    }

    public @NotNull List<String> onTabCompleteRaw(@NotNull String[] args) {
        if (args.length == 1) {
            return List.of(
                    "addstorageitem",
                    "fillquantum",
                    "fixblueprint",
                    "help",
                    "reducestorageitem",
                    "restore",
                    "setcontainerid",
                    "setquantum",
                    "updateItem",
                    "worldedit"
            );
        } else if (args.length == 2) {
            return switch (args[0]) {
                case "help", "restore", "updateItem" -> List.of();
                case "fillquantum" -> List.of("<amount>");
                case "fixblueprint" -> List.of("<keyInMeta>");
                case "addstorageitem"  -> List.of("<amount>");
                case "reducestorageitem"  -> List.of("<amount>");
                case "setquantum" -> List.of("<amount>");
                case "setcontainerid" -> List.of("<containerId>");
                case "worldedit" -> List.of("pos1", "pos2", "paste", "clear", "blockmenu", "blockinfo");
                default -> List.of();
            };
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("worldedit")) {
                return switch (args[1]) {
                    case "pos1", "pos2" -> List.of();
                    case "paste" -> Slimefun.getRegistry().getAllSlimefunItems()
                            .stream()
                            .filter(sfItem -> sfItem.getItem().getType().isBlock())
                            .map(SlimefunItem::getId)
                            .toList();
                    case "blockinfo" -> List.of("add", "remove", "set");
                    case "blockmenu" -> List.of("setSlot");
                    default -> List.of();
                };
            } else {
                return List.of();
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("worldedit")) {
                return switch (args[1].toLowerCase(Locale.ROOT)) {
                    case "paste" -> List.of("override", "keep");
                    default -> List.of();
                };
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("worldedit")) {
                return switch (args[1].toLowerCase(Locale.ROOT)) {
                    case "blockmenu" ->
                        switch (args[2].toLowerCase(Locale.ROOT)) {
                            case "setslot" -> List.of("0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53");
                            default -> List.of();
                        };

                    default -> List.of();
                };
            }
        }
        return new ArrayList<>();
    }
}
