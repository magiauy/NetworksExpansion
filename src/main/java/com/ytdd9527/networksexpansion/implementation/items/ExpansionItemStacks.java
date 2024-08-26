package com.ytdd9527.networksexpansion.implementation.items;


import com.ytdd9527.networksexpansion.api.enums.Skins;
import com.ytdd9527.networksexpansion.api.enums.StorageUnitType;
import com.ytdd9527.networksexpansion.utils.itemstacks.ItemStackUtil;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ytdd9527
 * @noinspection SpellCheckingInspection
 * @since 2.0
 */
public class ExpansionItemStacks {
    //工作台

public static final SlimefunItemStack NETWORK_EXPANSION_WORKBENCH = Theme.Random(
                "NTW_EXPANSION_WORKBENCH",
                new ItemStack(Material.BAMBOO_BLOCK),
                Theme.MACHINE,
                "Network Expansion Workbench",
                "Can be used to craft various materials and machines for network expansion"
);

    //工具
public static final SlimefunItemStack NETWORK_COORDINATE_CONFIGURATOR = Theme.Random(
                "NTW_EXPANSION_COORDINATE_CONFIGURATOR",
                new ItemStack(Material.RECOVERY_COMPASS),
                Theme.TOOL,
                "Network Coordinate Configurator",
                "Deprecated"
);
public static final SlimefunItemStack WORLD_EDIT_AXE = Theme.Random(
                "NTW_EXPANSION_WORLD_EDIT_AXE",
                new ItemStack(Material.DIAMOND_AXE),
                Theme.TOOL,
                "Network Slimefun World Edit Axe",
                "Only available for administrators",
                "Right-click to select the first position",
                "Shift + Right-click to select the second position"
);
    //高级网络物品
public static final SlimefunItemStack ADVANCED_IMPORT = Theme.Random(
                "NTW_EXPANSION_ADVANCED_IMPORT",
                Enchanted(Material.RED_STAINED_GLASS),
                Theme.MACHINE,
                "Advanced Network Importer",
                "The Advanced Network Importer will send its items into the network",
                "Up to 54 item stacks can be transferred per SF tick",
                "Can receive items from the Cargo Network"
);
public static final SlimefunItemStack ADVANCED_EXPORT = Theme.Random(
        "NTW_EXPANSION_ADVANCED_EXPORT",
        Enchanted(Material.BLUE_STAINED_GLASS),
        Theme.MACHINE,
        "Advanced Network Exporter",
        "The Advanced Network Exporter can be set to continuously send out a specified quantity of items from the network",
        "Items can be extracted from it using a Cargo Network"
);
public static final SlimefunItemStack ADVANCED_PURGER = Theme.Random(
                "NTW_EXPANSION_ADVANCED_PURGER",
                Enchanted(Material.YELLOW_STAINED_GLASS),
                Theme.MACHINE,
                "Advanced Network Purger",
                "The Advanced Network Purger continuously removes specified items from the network",
                "The removed items will disappear immediately, use with caution!"
);
public static final SlimefunItemStack ADVANCED_GREEDY_BLOCK = Theme.Random(
                "NTW_EXPANSION_ADVANCED_GREEDY_BLOCK",
                Enchanted(Material.GRAY_STAINED_GLASS),
                Theme.MACHINE,
                "Advanced Network Blocker",
                "The Advanced Network Blocker can be set to collect a specified item from various inputs in the network",
                "Up to 9 groups can be collected",
                "Once the collection is full, the item will be blocked from being transferred in the network",
                "No other network blocks will receive the item."
);

public static final SlimefunItemStack NETWORK_CAPACITOR_5 = Theme.Random(
                "NTW_EXPANSION_CAPACITOR_5",
                new ItemStack(Material.CYAN_GLAZED_TERRACOTTA),
                Theme.MACHINE,
                "Network Capacitor (5)",
                "The Network Capacitor can receive power from the energy network and store it",
                "for use by other network devices",
                "",
                MessageFormat.format("{0}Capacity: {1}{2}", Theme.CLICK_INFO, Theme.PASSIVE, 100000000)
);

public static final SlimefunItemStack NETWORK_CAPACITOR_6 = Theme.Random(
                "NTW_EXPANSION_CAPACITOR_6",
                new ItemStack(Material.BLUE_GLAZED_TERRACOTTA),
                Theme.MACHINE,
                "Network Capacitor (6)",
                "The Network Capacitor can receive power from the energy network and store it",
                "for use by other network devices",
                "",
                MessageFormat.format("{0}Capacity: {1}{2}", Theme.CLICK_INFO, Theme.PASSIVE, Integer.MAX_VALUE)
);

//Storage
public static final SlimefunItemStack ADVANCED_QUANTUM_STORAGE = Theme.Random(
                "NTW_EXPANSION_ADVANCED_QUANTUM_STORAGE",
                new ItemStack(Material.AMETHYST_BLOCK),
                Theme.MACHINE,
                "Advanced Quantum Storage",
                "Customizable maximum storage capacity",
                "Please note that once the quantity is set, it cannot be set to a smaller value than before",
                "Otherwise, it will be cleared to the current maximum capacity"
);

    //运输 LINE_TRANSFER POINT_TRANSFER_PUSHER

    //对点传输器
public static final SlimefunItemStack POINT_TRANSFER = Theme.Random(
                "NTW_EXPANSION_POINT_TRANSFER",
                Enchanted(Material.END_ROD),
                Theme.MACHINE,
                "Point Transfer"
);
public static final SlimefunItemStack POINT_TRANSFER_GRABBER = Theme.Random(
                "NTW_EXPANSION_POINT_TRANSFER_GRABBER",
                new ItemStack(Material.END_ROD),
                Theme.MACHINE,
                "Point Transfer Grabber"
);

    //链式传输
public static final SlimefunItemStack LINE_TRANSFER_PUSHER = Theme.Random(
        "NTW_EXPANSION_LINE_TRANSFER_PUSHER",
        new ItemStack(Material.OBSERVER),
        Theme.MACHINE,
        "Network Line Transfer Pusher",
        "&cOnly supports slime containers",
        "&6Transport distance: 32 blocks",
        "&7Default transport mode: &6First Slot Blocking",
        "&cTransport mode cannot be adjusted",
        "&7Default transport quantity: &664",
        "&cTransport quantity cannot be adjusted",
        "&eUnlike the Random Skill's Line Transfer, this machine",
        " &conly has continuous pushing functionality",
        "&cInstead of continuous item transfer!",
        "&7Transport mode explanation:",
        "&6First Slot Blocking&7: &eOnly pushes to the first slot that can accept pushed items"
);
public static final SlimefunItemStack LINE_TRANSFER_GRABBER = Theme.Random(
        "NTW_EXPANSION_LINE_TRANSFER_GRABBER",
        new ItemStack(Material.TARGET),
        Theme.MACHINE,
        "Network Line Transfer Grabber",
        "&cOnly supports slime containers",
        "&6Transport distance: 32 blocks",
        "&7Default transport mode: &6First Slot Blocking",
        "&cTransport mode cannot be adjusted",
        "&7Default transport quantity: &664",
        "&cTransport quantity cannot be adjusted",
        "&eUnlike the Random Skill's Line Transfer, this machine ",
        "&conly has continuous grabbing functionality",
        "&cInstead of continuous item transfer!",
        "&7Transport mode explanation:",
        "&6First Slot Blocking&7: &eOnly grabs from the first slot that has items"
);
public static final SlimefunItemStack LINE_TRANSFER = Theme.Random(
        "NTW_EXPANSION_LINE_TRANSFER",
        new ItemStack(Material.PISTON),
        Theme.MACHINE,
        "Network Line Transfer",
        "&cOnly supports slime containers",
        "&6Transport distance: 32 blocks",
        "&7Default transport mode: &6First Slot Blocking",
        "&cTransport mode cannot be adjusted",
        "&7Default transport quantity: &664",
        "&cTransport quantity cannot be adjusted",
        "&eUnlike the Random Skill's Line Transfer, this machine ",
        "&chas both continuous pushing and grabbing functionality",
        "&cInstead of continuous item transfer!",
        "&7Transport mode explanation:",
        "&6First Slot Blocking&7: &eOnly pushes / grabs from the first slot that has items"
);

    //链式传输Plus
public static final SlimefunItemStack LINE_TRANSFER_PLUS_PUSHER = Theme.Random(
                "NTW_EXPANSION_LINE_TRANSFER_PLUS_PUSHER",
                new ItemStack(Material.OBSERVER),
                Theme.MACHINE,
                "Network Line Transfer Plus [Pusher]",
                "&cOnly supports slime containers",
                "&6Transport distance: 64 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&cTransport mode cannot be adjusted",
                "&7Default transport quantity: &664",
                "&cTransport quantity cannot be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine",
                " &conly has continuous pushing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation:",
                "&6First Slot Blocking&7: &eOnly pushes to the first slot that can accept pushed items"
);
public static final SlimefunItemStack LINE_TRANSFER_PLUS_GRABBER = Theme.Random(
                "NTW_EXPANSION_LINE_TRANSFER_PLUS_GRABBER",
                new ItemStack(Material.TARGET),
                Theme.MACHINE,
                "Network Line Transfer Plus [Grabber]",
                "&cOnly supports slime containers",
                "&6Transport distance: 64 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&cTransport mode cannot be adjusted",
                "&7Default transport quantity: &664",
                "&cTransport quantity cannot be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine ",
                "&conly has continuous grabbing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation:",
                "&6First Slot Blocking&7: &eOnly grabs from the first slot that has items"
);
public static final SlimefunItemStack LINE_TRANSFER_PLUS = Theme.Random(
                "NTW_EXPANSION_LINE_TRANSFER_PLUS",
                new ItemStack(Material.STICKY_PISTON),
                Theme.MACHINE,
                "Network Line Transfer Plus",
                "&cOnly supports slime containers",
                "&6Transport distance: 64 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&cTransport mode cannot be adjusted",
                "&7Default transport quantity: &664",
                "&cTransport quantity cannot be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine ",
                "&chas both continuous pushing and grabbing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation:",
                "&6First Slot Blocking&7: &eOnly pushes / grabs from the first slot that has items"
);

public static final SlimefunItemStack LINE_TRANSFER_VANILLA_PUSHER = Theme.Random(
                "NTW_EXPANSION_LINE_TRANSFER_VANILLA_PUSHER",
                new ItemStack(Material.OBSERVER),
                Theme.MACHINE,
                "Vanilla Line Transfer [Pusher]",
                "&cOnly supports vanilla containers",
                "&6Transport distance: 32 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&cTransport mode cannot be adjusted",
                "&7Default transport quantity: &664",
                "&cTransport quantity cannot be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine ",
                "&conly has continuous pushing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation:",
                "&6First Slot Blocking&7: &eOnly pushes to the first slot that can accept pushed items"
);

public static final SlimefunItemStack LINE_TRANSFER_VANILLA_GRABBER = Theme.Random(
                "NTW_EXPANSION_LINE_TRANSFER_VANILLA_GRABBER",
                new ItemStack(Material.TARGET),
                Theme.MACHINE,
                "Vanilla Line Transfer [Grabber]",
                "&cOnly supports vanilla containers",
                "&6Transport distance: 32 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&cTransport mode cannot be adjusted",
                "&7Default transport quantity: &664",
                "&cTransport quantity cannot be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine ",
                "&conly has continuous grabbing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation:",
                "&6First Slot Blocking&7: &eOnly grabs from the first slot that has items"
);

    //高级链式传输
public static final SlimefunItemStack ADVANCED_LINE_TRANSFER_PUSHER = Theme.Random(
                "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PUSHER",
                Enchanted(Material.OBSERVER),
                Theme.MACHINE,
                "Network Line Transfer Pusher",
                "&cOnly supports slime containers",
                "&6Transport distance: 32 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&aTransport mode can be adjusted",
                "&7Default transport quantity: &63456",
                "&aTransport quantity can be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine ",
                "&conly has continuous pushing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation: ",
                "&6Unrestricted&7: &eCan push to any slot",
                "&6Only Empty&7: &eOnly pushes to empty slots",
                "&6Only Non-Empty&7: &eOnly pushes to non-empty slots",
                "&6Only First Slot&7: &eOnly pushes to the first slot",
                "&6Only Last Slot&7: &eOnly pushes to the last slot",
                "&6First Slot Blocking&7: &eOnly pushes to the first slot that can accept pushed items",
                "&6Lazy Mode&7: &eWhen the first slot is empty, push to all slots"
);
public static final SlimefunItemStack ADVANCED_LINE_TRANSFER_GRABBER = Theme.Random(
                "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_GRABBER",
                Enchanted(Material.TARGET),
                Theme.MACHINE,
                "Network Line Transfer Grabber",
                "&cOnly supports slime containers",
                "&6Transport distance: 32 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&aTransport mode can be adjusted",
                "&7Default transport quantity: &63456",
                "&aTransport quantity can be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine ",
                "&conly has continuous grabbing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation: ",
                "&6Unrestricted&7: &eCan grab from any slot",
                "&6Only Empty&7: &eOnly grabs from empty slots",
                "&6Only Non-Empty&7: &eOnly grabs from non-empty slots",
                "&6Only First Slot&7: &eOnly grabs from the first slot",
                "&6Only Last Slot&7: &eOnly grabs from the last slot",
                "&6First Slot Blocking&7: &eOnly grabs from the first slot that has items",
                "&6Lazy Mode&7: &eWhen the first slot has items, grab from all slots"
);
public static final SlimefunItemStack ADVANCED_LINE_TRANSFER = Theme.Random(
                "NTW_EXPANSION_ADVANCED_LINE_TRANSFER",
                Enchanted(Material.PISTON),
                Theme.MACHINE,
                "Network Line Transfer",
                "&cOnly supports slime containers",
                "&6Transport distance: 32 blocks",
                "&7Default transport mode: &6First Slot Blocking",
                "&aTransport mode can be adjusted",
                "&7Default transport quantity: &63456",
                "&aTransport quantity can be adjusted",
                "&eUnlike the Random Skill's Line Transfer, this machine ",
                "&chas both continuous pushing and grabbing functionality",
                "&cInstead of continuous item transfer!",
                "&7Transport mode explanation: ",
                "&6Unrestricted&7: &eCan push / grab from any slot",
                "&6Only Empty&7: &eOnly pushes / grabs from empty slots",
                "&6Only Non-Empty&7: &eOnly pushes / grabs from non-empty slots",
                "&6Only First Slot&7: &eOnly pushes / grabs from the first slot",
                "&6Only Last Slot&7: &eOnly pushes / grabs from the last slot",
                "&6First Slot Blocking&7: &eOnly pushes / grabs from the first slot that has items",
                "&6Lazy Mode&7: &eWhen the first slot is empty, push to all slots ",
                "               &eWhen the first slot has items, grab from all slots"
);
//高级链式传输Plus
public static final SlimefunItemStack ADVANCED_LINE_TRANSFER_PLUS_PUSHER = Theme.Random(
        "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS_PUSHER",
        Enchanted(Material.OBSERVER),
        Theme.MACHINE,
        "Network Line Transfer Plus [Pusher]",
        "&cOnly supports slime containers",
        "&6Transport distance: 64 blocks",
        "&7Default transport mode: &6First Slot Blocking",
        "&aTransport mode can be adjusted",
        "&7Default transport quantity: &63456",
        "&aTransport quantity can be adjusted",
        "&eUnlike the Random Skill's Line Transfer, this machine ",
        "&chas both continuous pushing and grabbing functionality",
        "&cInstead of continuous item transfer!",
        "&7Transport mode explanation: ",
        "&6Unrestricted&7: &eCan push to any slot",
        "&6Only Empty&7: &eOnly pushes to empty slots",
        "&6Only Non-Empty&7: &eOnly pushes to non-empty slots",
        "&6Only First Slot&7: &eOnly pushes to the first slot",
        "&6Only Last Slot&7: &eOnly pushes to the last slot",
        "&6First Slot Blocking&7: &eOnly pushes to the first slot that can accept pushed items",
        "&6Lazy Mode&7: &eWhen the first slot is empty, push to all slots"
);
public static final SlimefunItemStack ADVANCED_LINE_TRANSFER_PLUS_GRABBER = Theme.Random(
        "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS_GRABBER",
        Enchanted(Material.TARGET),
        Theme.MACHINE,
        "Network Line Transfer Plus [Grabber]",
        "&cOnly supports slime containers",
        "&6Transport distance: 64 blocks",
        "&7Default transport mode: &6First Slot Blocking",
        "&aTransport mode can be adjusted",
        "&7Default transport quantity: &63456",
        "&aTransport quantity can be adjusted",
        "&eUnlike the Random Skill's Line Transfer, this machine ",
        "&chas both continuous pushing and grabbing functionality",
        "&cInstead of continuous item transfer!",
        "&7Transport mode explanation: ",
        "&6Unrestricted&7: &eCan grab from any slot",
        "&6Only Empty&7: &eOnly grabs from empty slots",
        "&6Only Non-Empty&7: &eOnly grabs from non-empty slots",
        "&6Only First Slot&7: &eOnly grabs from the first slot",
        "&6Only Last Slot&7: &eOnly grabs from the last slot",
        "&6First Slot Blocking&7: &eOnly grabs from the first slot that has items",
        "&6Lazy Mode&7: &eWhen the first slot has items, grab from all slots"
);
public static final SlimefunItemStack ADVANCED_LINE_TRANSFER_PLUS = Theme.Random(
        "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS",
        Enchanted(Material.STICKY_PISTON),
        Theme.MACHINE,
        "Network Line Transfer Plus",
        "&cOnly supports slime containers",
        "&6Transport distance: 64 blocks",
        "&7Default transport mode: &6First Slot Blocking",
        "&aTransport mode can be adjusted",
        "&7Default transport quantity: &63456",
        "&aTransport quantity can be adjusted",
        "&eUnlike the Random Skill's Line Transfer, this machine ",
        "&chas both continuous pushing and grabbing functionality",
        "&cInstead of continuous item transfer!",
        "&7Transport mode explanation: ",
        "&6Unrestricted&7: &eCan push / grab from any slot",
        "&6Only Empty&7: &eOnly pushes / grabs from empty slots",
        "&6Only Non-Empty&7: &eOnly pushes / grabs from non-empty slots",
        "&6Only First Slot&7: &eOnly pushes / grabs from the first slot",
        "&6Only Last Slot&7: &eOnly pushes / grabs from the last slot",
        "&6First Slot Blocking&7: &eOnly pushes / grabs from the first slot that has items",
        "&6Lazy Mode&7: &eWhen the first slot is empty, push to all slots ",
        "               &eWhen the first slot has items, grab from all slots"
);

//网格
public static final SlimefunItemStack NETWORK_GRID_NEW_STYLE = Theme.Random(
                "NTW_EXPANSION_GRID_NEW_STYLE",
                new ItemStack(Material.NOTE_BLOCK),
                Theme.MACHINE,
                "Advanced Network Grid",
                "Advanced network grid allows you to view all items in the network",
                "You can also directly insert or retrieve items",
                "Supports Pinyin search"
);
public static final SlimefunItemStack NETWORK_CRAFTING_GRID_NEW_STYLE = Theme.Random(
                "NTW_EXPANSION_CRAFTING_GRID_NEW_STYLE",
                new ItemStack(Material.JUKEBOX),
                Theme.MACHINE,
                "Advanced Crafting Grid",
                "This grid is similar to the normal grid",
                "But it displays fewer items",
                "However, you can directly use items from the network",
                "for crafting",
                "Supports Pinyin search"
);
public static final SlimefunItemStack NETWORK_ENCODING_GRID_NEW_STYLE = Theme.Random(
                "NTW_EXPANSION_ENCODING_GRID_NEW_STYLE",
                new ItemStack(Material.TARGET),
                Theme.MACHINE,
                "Advanced Encoding Grid",
                "This grid is similar to the advanced grid",
                "But it displays fewer items",
                "However, you can directly use items from the network",
                "for encoding",
                "Supports Pinyin search"
);
//蓝图
public static final SlimefunItemStack MAGIC_WORKBENCH_BLUEPRINT = Theme.Random(
                "NTW_EXPANSION_MAGIC_WORKBENCH_BLUEPRINT",
                new ItemStack(Material.RED_DYE),
                Theme.MACHINE,
                "Magic Workbench Blueprint",
                "An empty blueprint",
                "Can store a magic workbench recipe"
);
public static final SlimefunItemStack ARMOR_FORGE_BLUEPRINT = Theme.Random(
                "NTW_EXPANSION_ARMOR_FORGE_BLUEPRINT",
                new ItemStack(Material.ORANGE_DYE),
                Theme.MACHINE,
                "Armor Forge Blueprint",
                "An empty blueprint",
                "Can store an armor forge recipe"
);
public static final SlimefunItemStack SMELTERY_BLUEPRINT = Theme.Random(
                "NTW_EXPANSION_SMELTERY_BLUEPRINT",
                new ItemStack(Material.YELLOW_DYE),
                Theme.MACHINE,
                "Smeltery Blueprint",
                "An empty blueprint",
                "Can store a smeltery recipe"
);
public static final SlimefunItemStack QUANTUM_WORKBENCH_BLUEPRINT = Theme.Random(
                "NTW_EXPANSION_QUANTUM_WORKBENCH_BLUEPRINT",
                new ItemStack(Material.LIME_DYE),
                Theme.MACHINE,
                "Quantum Workbench Blueprint",
                "An empty blueprint",
                "Can store a quantum workbench recipe"
);
public static final SlimefunItemStack ANCIENT_ALTAR_BLUEPRINT = Theme.Random(
                "NTW_EXPANSION_ANCIENT_ALTAR_BLUEPRINT",
                new ItemStack(Material.CYAN_DYE),
                Theme.MACHINE,
                "Ancient Altar Blueprint",
                "An empty blueprint",
                "Can store an ancient altar recipe"
);
public static final SlimefunItemStack EXPANSION_WORKBENCH_BLUEPRINT = Theme.Random(
                "NTW_EXPANSION_EXPANSION_WORKBENCH_BLUEPRINT",
                new ItemStack(Material.BROWN_DYE),
                Theme.MACHINE,
                "Network Expansion Workbench Blueprint",
                "An empty blueprint",
                "Can store a network expansion workbench recipe"
);

//编码器
public static final SlimefunItemStack MAGIC_WORKBENCH_RECIPE_ENCODER = Theme.Random(
                "NTW_EXPANSION_MAGIC_WORKBENCH_RECIPE_ENCODER",
                new ItemStack(Material.OAK_HANGING_SIGN),
                Theme.MACHINE,
                "Network Magic Workbench Recipe Encoder",
                "Can create magic workbench blueprints based on the input items",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per encoding", Theme.CLICK_INFO, Theme.PASSIVE, 2000)
);
public static final SlimefunItemStack ARMOR_FORGE_RECIPE_ENCODER = Theme.Random(
                "NTW_EXPANSION_ARMOR_FORGE_RECIPE_ENCODER",
                new ItemStack(Material.SPRUCE_HANGING_SIGN),
                Theme.MACHINE,
                "Network Armor Forge Recipe Encoder",
                "Can create armor forge blueprints based on the input items",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per encoding", Theme.CLICK_INFO, Theme.PASSIVE, 2000)
);
public static final SlimefunItemStack SMELTERY_RECIPE_ENCODER = Theme.Random(
                "NTW_EXPANSION_SMELTERY_RECIPE_ENCODER",
                new ItemStack(Material.BIRCH_HANGING_SIGN),
                Theme.MACHINE,
                "Network Smeltery Recipe Encoder",
                "Can create smeltery blueprints based on the input items",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per encoding", Theme.CLICK_INFO, Theme.PASSIVE, 2000)
);
public static final SlimefunItemStack QUANTUM_WORKBENCH_RECIPE_ENCODER = Theme.Random(
                "NTW_EXPANSION_QUANTUM_WORKBENCH_RECIPE_ENCODER",
                new ItemStack(Material.JUNGLE_HANGING_SIGN),
                Theme.MACHINE,
                "Network Quantum Workbench Recipe Encoder",
                "Can create quantum workbench blueprints based on the input items",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per encoding", Theme.CLICK_INFO, Theme.PASSIVE, 2000)
);
public static final SlimefunItemStack ANCIENT_ALTAR_RECIPE_ENCODER = Theme.Random(
                "NTW_EXPANSION_ANCIENT_ALTAR_RECIPE_ENCODER",
                new ItemStack(Material.CHERRY_HANGING_SIGN),
                Theme.MACHINE,
                "Network Ancient Altar Recipe Encoder",
                "Can create ancient altar blueprints based on the input items",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per encoding", Theme.CLICK_INFO, Theme.PASSIVE, 2000)
);
public static final SlimefunItemStack EXPANSION_WORKBENCH_RECIPE_ENCODER = Theme.Random(
                "NTW_EXPANSION_EXPANSION_WORKBENCH_RECIPE_ENCODER",
                new ItemStack(Material.ACACIA_HANGING_SIGN),
                Theme.MACHINE,
                "Network Expansion Workbench Recipe Encoder",
                "Can create expansion workbench blueprints based on the input items",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per encoding", Theme.CLICK_INFO, Theme.PASSIVE, 2000)
);


//网络合成机
public static final SlimefunItemStack AUTO_MAGIC_WORKBENCH = Theme.Random(
                "NTW_EXPANSION_AUTO_MAGIC_WORKBENCH",
                new ItemStack(Material.BOOKSHELF),
                Theme.MACHINE,
                "Network Auto Magic Workbench",
                "The Auto Network Magic Workbench accepts",
                "a magic workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 640)
);
public static final SlimefunItemStack AUTO_MAGIC_WORKBENCH_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_AUTO_MAGIC_WORKBENCH_WITHHOLDING",
                new ItemStack(Material.CHISELED_BOOKSHELF),
                Theme.MACHINE,
                "Network Auto Magic Workbench (Withholding Version)",
                "The Auto Network Magic Workbench accepts",
                "a magic workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 1280)
);
public static final SlimefunItemStack AUTO_ARMOR_FORGE = Theme.Random(
                "NTW_EXPANSION_AUTO_ARMOR_FORGE",
                new ItemStack(Material.SMITHING_TABLE),
                Theme.MACHINE,
                "Network Auto Armor Forge",
                "The Network Auto Armor Forge accepts",
                "an armor forge blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 640)
);
public static final SlimefunItemStack AUTO_ARMOR_FORGE_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_AUTO_ARMOR_FORGE_WITHHOLDING",
                new ItemStack(Material.CARTOGRAPHY_TABLE),
                Theme.MACHINE,
                "Network Auto Armor Forge (Withholding Version)",
                "The Network Auto Armor Forge accepts",
                "an armor forge blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 1280)
);
public static final SlimefunItemStack AUTO_SMELTERY = Theme.Random(
                "NTW_EXPANSION_AUTO_SMELTERY",
                new ItemStack(Material.FURNACE),
                Theme.MACHINE,
                "Network Auto Smeltery",
                "The Network Auto Smeltery accepts",
                "a smeltery blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 640)
);
public static final SlimefunItemStack AUTO_SMELTERY_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_AUTO_SMELTERY_WITHHOLDING",
                new ItemStack(Material.BLAST_FURNACE),
                Theme.MACHINE,
                "Network Auto Smeltery (Withholding Version)",
                "The Network Auto Smeltery accepts",
                "a smeltery blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 1280)
);
public static final SlimefunItemStack AUTO_QUANTUM_WORKBENCH = Theme.Random(
                "NTW_EXPANSION_AUTO_QUANTUM_WORKBENCH",
                new ItemStack(Material.HAY_BLOCK),
                Theme.MACHINE,
                "Network Auto Quantum Workbench",
                "The Network Auto Quantum Workbench accepts",
                "a quantum workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 640)
);
public static final SlimefunItemStack AUTO_QUANTUM_WORKBENCH_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_AUTO_QUANTUM_WORKBENCH_WITHHOLDING",
                new ItemStack(Material.DRIED_KELP_BLOCK),
                Theme.MACHINE,
                "Network Auto Quantum Workbench (Withholding Version)",
                "The Network Auto Quantum Workbench accepts",
                "a quantum workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 1280)
);
public static final SlimefunItemStack AUTO_ANCIENT_ALTAR = Theme.Random(
                "NTW_EXPANSION_AUTO_ANCIENT_ALTAR",
                new ItemStack(Material.CRAFTING_TABLE),
                Theme.MACHINE,
                "Network Auto Ancient Altar",
                "The Network Auto Ancient Altar accepts",
                "an ancient altar blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 640)
);
public static final SlimefunItemStack AUTO_ANCIENT_ALTAR_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_AUTO_ANCIENT_ALTAR_WITHHOLDING",
                new ItemStack(Material.ENCHANTING_TABLE),
                Theme.MACHINE,
                "Network Ancient Altar (Withholding Version)",
                "The Network Auto Ancient Altar accepts",
                "an ancient altar blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 1280)
);
public static final SlimefunItemStack AUTO_EXPANSION_WORKBENCH = Theme.Random(
                "NTW_EXPANSION_AUTO_EXPANSION_WORKBENCH",
                new ItemStack(Material.FIRE_CORAL_BLOCK),
                Theme.MACHINE,
                "Auto Network Expansion Workbench",
                "The Auto Network Expansion Workbench accepts",
                "a network expansion workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 640)
);
public static final SlimefunItemStack AUTO_EXPANSION_WORKBENCH_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_AUTO_EXPANSION_WORKBENCH_WITHHOLDING",
                new ItemStack(Material.HORN_CORAL_BLOCK),
                Theme.MACHINE,
                "Network Expansion Workbench (Withholding Version)",
                "The Auto Network Expansion Workbench accepts",
                "a network expansion workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 1280)
);

// Advanced Auto Magic Workbench
public static final SlimefunItemStack ADVANCED_AUTO_MAGIC_WORKBENCH = Theme.Random(
                "NTW_EXPANSION_ADVANCED_AUTO_MAGIC_WORKBENCH",
                Enchanted(Material.BOOKSHELF),
                Theme.MACHINE,
                "Advanced Auto Magic Workbench",
                "The Advanced Auto Magic Workbench accepts",
                "a magic workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 6400)
);
public static final SlimefunItemStack ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING",
                Enchanted(Material.CHISELED_BOOKSHELF),
                Theme.MACHINE,
                "Advanced Auto Magic Workbench (Withholding Version)",
                "The Advanced Auto Magic Workbench accepts",
                "a magic workbench blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 12800)
);
public static final SlimefunItemStack ADVANCED_AUTO_ARMOR_FORGE = Theme.Random(
                "NTW_EXPANSION_ADVANCED_AUTO_ARMOR_FORGE",
                Enchanted(Material.SMITHING_TABLE),
                Theme.MACHINE,
                "Advanced Auto Armor Forge",
                "The Advanced Auto Armor Forge accepts",
                "an armor forge blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 6400)
);
public static final SlimefunItemStack ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING",
                Enchanted(Material.CARTOGRAPHY_TABLE),
                Theme.MACHINE,
                "Advanced Auto Armor Forge (Withholding Version)",
                "The Advanced Auto Armor Forge accepts",
                "an armor forge blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 12800)
);
public static final SlimefunItemStack ADVANCED_AUTO_SMELTERY = Theme.Random(
                "NTW_EXPANSION_ADVANCED_AUTO_SMELTERY",
                Enchanted(Material.FURNACE),
                Theme.MACHINE,
                "Advanced Auto Smeltery",
                "The Advanced Auto Smeltery accepts",
                "a smeltery blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 6400)
);
public static final SlimefunItemStack ADVANCED_AUTO_SMELTERY_WITHHOLDING = Theme.Random(
                "NTW_EXPANSION_ADVANCED_AUTO_SMELTERY_WITHHOLDING",
                Enchanted(Material.BLAST_FURNACE),
                Theme.MACHINE,
                "Advanced Auto Smeltery (Withholding Version)",
                "The Advanced Auto Smeltery accepts",
                "a smeltery blueprint. When the",
                "blueprint output item is requested",
                "while there is none in the network",
                "it will be crafted if you have",
                "materials.",
                "",
                "A Withholding Crafter will keep",
                "a stack in the output and stop",
                "crafting. The stack can been seen",
                "in the Network and also allows for",
                "cargo.",
                "",
                MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 12800)
);
public static final SlimefunItemStack ADVANCED_AUTO_QUANTUM_WORKBENCH = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_QUANTUM_WORKBENCH",
        Enchanted(Material.HAY_BLOCK),
        Theme.MACHINE,
        "Advanced Auto Quantum Workbench",
        "The Advanced Auto Quantum Workbench accepts",
        "a quantum workbench. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 6400)
);
public static final SlimefunItemStack ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING",
        Enchanted(Material.DRIED_KELP_BLOCK),
        Theme.MACHINE,
        "Advanced Auto Quantum Workbench (Withholding Version)",
        "The Advanced Auto Quantum Workbench accepts",
        "a quantum workbench. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        "A Withholding Crafter will keep",
        "a stack in the output and stop",
        "crafting. The stack can been seen",
        "in the Network and also allows for",
        "cargo.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 12800)
);
public static final SlimefunItemStack ADVANCED_AUTO_ANCIENT_ALTAR = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_ANCIENT_ALTAR",
        Enchanted(Material.CRAFTING_TABLE),
        Theme.MACHINE,
        "Advanced Auto Ancient Altar",
        "The Advanced Auto Ancient Altar accepts",
        "an ancient altar. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 6400)
);
public static final SlimefunItemStack ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING",
        Enchanted(Material.ENCHANTING_TABLE),
        Theme.MACHINE,
        "Advanced Auto Ancient Altar (Withholding Version)",
        "The Advanced Auto Ancient Altar accepts",
        "an ancient altar. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        "A Withholding Crafter will keep",
        "a stack in the output and stop",
        "crafting. The stack can been seen",
        "in the Network and also allows for",
        "cargo.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 12800)
);
public static final SlimefunItemStack ADVANCED_AUTO_EXPANSION_WORKBENCH = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_EXPANSION_WORKBENCH",
        Enchanted(Material.FIRE_CORAL_BLOCK),
        Theme.MACHINE,
        "Advanced Auto Network Expansion Workbench",
        "The Advanced Auto Network Expansion Workbench accepts",
        "a network expansion workbench blueprint. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 6400)
);
public static final SlimefunItemStack ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING",
        Enchanted(Material.HORN_CORAL_BLOCK),
        Theme.MACHINE,
        "Advanced Auto Network Expansion Workbench (Withholding Version)",
        "The Advanced Auto Network Expansion Workbench accepts",
        "a network expansion workbench blueprint. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        "A Withholding Crafter will keep",
        "a stack in the output and stop",
        "crafting. The stack can been seen",
        "in the Network and also allows for",
        "cargo.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 12800)
);

public static final SlimefunItemStack ADVANCED_AUTO_CRAFTING_TABLE = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_CRAFTING",
        Enchanted(Material.CRAFTING_TABLE),
        Theme.MACHINE,
        "Advanced Auto Crafting Table",
        "The Advanced Auto Crafting Tableaccepts",
        "a crafting blueprint. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 6400)
);
public static final SlimefunItemStack ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING = Theme.Random(
        "NTW_EXPANSION_ADVANCED_AUTO_CRAFTING_WITHHOLDING",
        Enchanted(Material.CRAFTING_TABLE),
        Theme.MACHINE,
        "Advanced Auto Crafting Table (Withholding Version)",
        "The Advanced Auto Crafting Tableaccepts",
        "a crafting blueprint. When the",
        "blueprint output item is requested",
        "while there is none in the network",
        "it will be crafted if you have",
        "materials.",
        "",
        "A Withholding Crafter will keep",
        "a stack in the output and stop",
        "crafting. The stack can been seen",
        "in the Network and also allows for",
        "cargo.",
        "",
        MessageFormat.format("{0}Network Power Consumption: {1}{2} per crafting", Theme.CLICK_INFO, Theme.PASSIVE, 12800)
);

//网桥
public static final SlimefunItemStack NETWORK_BRIDGE_WHITE = Theme.Random(
                "NTW_EXPANSION_BRIDGE_WHITE",
                new ItemStack(Material.WHITE_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (White)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_LIGHT_GRAY = Theme.Random(
                "NTW_EXPANSION_BRIDGE_LIGHT_GRAY",
                new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Light Gray)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_GRAY = Theme.Random(
                "NTW_EXPANSION_BRIDGE_GRAY",
                new ItemStack(Material.GRAY_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Gray)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_BLACK = Theme.Random(
                "NTW_EXPANSION_BRIDGE_BLACK",
                new ItemStack(Material.BLACK_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Black)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_BROWN = Theme.Random(
                "NTW_EXPANSION_BRIDGE_BROWN",
                new ItemStack(Material.BROWN_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Brown)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_RED = Theme.Random(
                "NTW_EXPANSION_BRIDGE_RED",
                new ItemStack(Material.RED_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Red)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_ORANGE = Theme.Random(
                "NTW_EXPANSION_BRIDGE_ORANGE",
                new ItemStack(Material.ORANGE_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Orange)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_YELLOW = Theme.Random(
                "NTW_EXPANSION_BRIDGE_YELLOW",
                new ItemStack(Material.YELLOW_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Yellow)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_LIME = Theme.Random(
                "NTW_EXPANSION_BRIDGE_LIME",
                new ItemStack(Material.LIME_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Lime)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_GREEN = Theme.Random(
                "NTW_EXPANSION_BRIDGE_GREEN",
                new ItemStack(Material.GREEN_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Green)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_CYAN = Theme.Random(
                "NTW_EXPANSION_BRIDGE_CYAN",
                new ItemStack(Material.CYAN_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Cyan)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_LIGHT_BLUE = Theme.Random(
                "NTW_EXPANSION_BRIDGE_LIGHT_BLUE",
                new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Light Blue)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_BLUE = Theme.Random(
                "NTW_EXPANSION_BRIDGE_BLUE",
                new ItemStack(Material.BLUE_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Blue)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_PURPLE = Theme.Random(
                "NTW_EXPANSION_BRIDGE_PURPLE",
                new ItemStack(Material.PURPLE_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Purple)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_MAGENTA = Theme.Random(
                "NTW_EXPANSION_BRIDGE_MAGENTA",
                new ItemStack(Material.MAGENTA_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Magenta)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
public static final SlimefunItemStack NETWORK_BRIDGE_PINK = Theme.Random(
                "NTW_EXPANSION_BRIDGE_PINK",
                new ItemStack(Material.PINK_STAINED_GLASS),
                Theme.MACHINE,
                "Network Bridge (Pink)",
                "The network bridge is used to connect different network items",
                "to form a complete network",
                "and layout the network more clearly"
);
    private static final String thanks = "&x&F&8&7&D&F&1&lC&x&F&8&8&5&F&2&lo&x&F&9&8&D&F&3&lu&x&F&9&9&5&F&4&lr&x&F&A&9&E&F&5&lt&x&F&A&A&6&F&5&le&x&F&B&A&E&F&6&ls&x&F&B&B&6&F&7&ly &x&F&C&C&6&F&9&lo&x&F&C&C&E&F&A&lf &x&F&D&D&F&F&C&lK&x&F&E&E&7&F&C&lo&x&F&E&E&F&F&D&ln&x&F&F&F&7&F&E&lj&x&F&F&F&F&F&F&la&x&F&6&F&E&F&F&lc &x&E&3&F&D&F&F&lS&x&D&9&F&C&F&F&ll&x&D&0&F&B&F&F&li&x&C&6&F&A&F&F&lm&x&B&D&F&A&F&F&le &x&A&A&F&8&F&F&lT&x&A&1&F&7&F&F&le&x&9&7&F&6&F&F&lc&x&8&E&F&6&F&F&lh &x&7&B&F&4&F&F&lS&x&7&B&F&4&F&F&le&x&7&B&F&4&F&F&lr&x&7&B&F&4&F&F&lv&x&7&B&F&4&F&F&le&x&7&B&F&4&F&F&lr";
    private static final Map<StorageUnitType, SlimefunItemStack> typeMap = new HashMap<>();
public static SlimefunItemStack CARGO_NODE_QUICK_TOOL = Theme.Random(
        "NTW_EXPANSION_CARGO_NODE_QUICK_TOOL",
        new ItemStack(Material.BONE),
        Theme.MACHINE,
        "Cargo Node Quick Tool",
        "&aRight-click: Set the configuration of the cargo node it points to",
        "&eSneak + Right-click: Load the configuration from the pointed cargo node",
        "",
        thanks,
        ""
);
public static SlimefunItemStack STORAGE_UNIT_UPGRADE_TABLE = Theme.Random(
        "NTW_EXPANSION_STORAGE_UPGRADE_TABLE",
        new ItemStack(Material.CARTOGRAPHY_TABLE),
        Theme.MACHINE,
        "Network Drawer Upgrade Table",
        "&eUsed to upgrade network drawers",
        "",
        thanks,
        ""
);
public static SlimefunItemStack STORAGE_UNIT_UPGRADE_TABLE_MODEL = Theme.model(
        "NTW_EXPANSION_STORAGE_UPGRADE_TABLE",
        Skins.STORAGE_UNIT_UPGRADE_TABLE_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Drawer Upgrade Table",
        "&eUsed to upgrade network drawers",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_1 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_1",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer I",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.TINY.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.TINY.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_2 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_2",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer II",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.MINI.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.MINI.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_3 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_3",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer III",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.SMALL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.SMALL.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_4 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_4",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer IV",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.MEDIUM.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.MEDIUM.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_5 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_5",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer V",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.LARGE.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.LARGE.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_6 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_6",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer VI",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.ENHANCED.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.ENHANCED.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);

public static SlimefunItemStack CARGO_STORAGE_UNIT_7 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_7",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer VII",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.ADVANCED.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.ADVANCED.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_8 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_8",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer VIII",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.EXTRA.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.EXTRA.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_9 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_9",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Drawer IX",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.ULTRA.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.ULTRA.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_10 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_10",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Cargo Storage Unit X",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.END_GAME_BASIC.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_BASIC.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_11 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_11",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Cargo Storage Unit XI",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.END_GAME_INTERMEDIATE.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_INTERMEDIATE.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_12 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_12",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Cargo Storage Unit XII",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.END_GAME_ADVANCED.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_ADVANCED.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);


public static SlimefunItemStack CARGO_STORAGE_UNIT_13 = Theme.Random(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_13",
        new ItemStack(Material.CHISELED_BOOKSHELF),
        Theme.MACHINE,
        "Network Cargo Storage Unit XIII",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.END_GAME_MAX.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_MAX.getEachMaxSize() + " items",
        "",
        thanks,
        ""
);

public static SlimefunItemStack CARGO_STORAGE_UNIT_1_MODEL = Theme.model(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_1",
        Skins.CARGO_STORAGE_UNIT_1_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Cargo Storage Unit I",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.TINY_MODEL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.TINY_MODEL.getEachMaxSize() + " items",
        "",
        "&7⇨ &eRequires Network Wrench to dismantle model blocks",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_2_MODEL = Theme.model(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_2",
        Skins.CARGO_STORAGE_UNIT_2_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Cargo Storage Unit II",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.MINI_MODEL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.MINI_MODEL.getEachMaxSize() + " items",
        "",
        "&7⇨ &eRequires Network Wrench to remove the machine",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_3_MODEL = Theme.model(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_3",
        Skins.CARGO_STORAGE_UNIT_3_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Cargo Storage Unit III",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.SMALL_MODEL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.SMALL_MODEL.getEachMaxSize() + " items",
        "",
        "&7⇨ &eRequires Network Wrench to remove the machine",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_4_MODEL = Theme.model(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_4",
        Skins.CARGO_STORAGE_UNIT_4_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Cargo Storage Unit IV",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.MEDIUM_MODEL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.MEDIUM_MODEL.getEachMaxSize() + " items",
        "",
        "&7⇨ &eRequires Network Wrench to remove the machine",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_5_MODEL = Theme.model(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_5",
        Skins.CARGO_STORAGE_UNIT_5_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Cargo Storage Unit V",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.LARGE_MODEL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.LARGE_MODEL.getEachMaxSize() + " items",
        "",
        "&7⇨ &eRequires Network Wrench to remove the machine",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_6_MODEL = Theme.model(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_6",
        Skins.CARGO_STORAGE_UNIT_6_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Cargo Storage Unit VI",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.ENHANCED_MODEL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.ENHANCED_MODEL.getEachMaxSize() + " items",
        "",
        "&7⇨ &eRequires Network Wrench to remove the machine",
        "",
        thanks,
        ""
);

public static SlimefunItemStack CARGO_STORAGE_UNIT_7_MODEL = Theme.model(
        "NTW_EXPANSION_CARGO_STORAGE_UNIT_7",
        Skins.CARGO_STORAGE_UNIT_7_MODEL.getPlayerHead(),
        Theme.MACHINE,
        "Network Cargo Storage Unit VII",
        "&6Supports fast input/output through networks",
        "",
        "&7⇨ &eCan store " + StorageUnitType.ADVANCED_MODEL.getMaxItemCount() + " types of items",
        "&7⇨ &eEach type can hold up to " + StorageUnitType.ADVANCED_MODEL.getEachMaxSize() + " items",
        "",
        "&7⇨ &eRequires Network Wrench to remove the machine",
        "",
        thanks,
        ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_8_MODEL = Theme.model(
                "NTW_EXPANSION_CARGO_STORAGE_UNIT_8",
                Skins.CARGO_STORAGE_UNIT_8_MODEL.getPlayerHead(),
                Theme.MACHINE,
                "Network Cargo Storage Unit VIII",
                "&6Supports fast input/output through networks",
                "",
                "&7⇨ &eCan store " + StorageUnitType.EXTRA_MODEL.getMaxItemCount() + " types of items",
                "&7⇨ &eEach type can hold up to " + StorageUnitType.EXTRA_MODEL.getEachMaxSize() + " items",
                "",
                "&7⇨ &eRequires Network Wrench to remove the machine",
                "",
                thanks,
                ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_9_MODEL = Theme.model(
                "NTW_EXPANSION_CARGO_STORAGE_UNIT_9",
                Skins.CARGO_STORAGE_UNIT_9_MODEL.getPlayerHead(),
                Theme.MACHINE,
                "Network Cargo Storage Unit IX",
                "&6Supports fast input/output through networks",
                "",
                "&7⇨ &eCan store " + StorageUnitType.ULTRA_MODEL.getMaxItemCount() + " types of items",
                "&7⇨ &eEach type can hold up to " + StorageUnitType.ULTRA_MODEL.getEachMaxSize() + " items",
                "",
                "&7⇨ &eRequires Network Wrench to remove the machine",
                "",
                thanks,
                ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_10_MODEL = Theme.model(
                "NTW_EXPANSION_CARGO_STORAGE_UNIT_10",
                Skins.CARGO_STORAGE_UNIT_10_MODEL.getPlayerHead(),
                Theme.MACHINE,
                "Network Cargo Storage Unit X",
                "&6Supports fast input/output through networks",
                "",
                "&7⇨ &eCan store " + StorageUnitType.END_GAME_BASIC_MODEL.getMaxItemCount() + " types of items",
                "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_BASIC_MODEL.getEachMaxSize() + " items",
                "",
                "&7⇨ &eRequires Network Wrench to remove the machine",
                "",
                thanks,
                ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_11_MODEL = Theme.model(
                "NTW_EXPANSION_CARGO_STORAGE_UNIT_11",
                Skins.CARGO_STORAGE_UNIT_11_MODEL.getPlayerHead(),
                Theme.MACHINE,
                "Network Cargo Storage Unit XI",
                "&6Supports fast input/output through networks",
                "",
                "&7⇨ &eCan store " + StorageUnitType.END_GAME_INTERMEDIATE_MODEL.getMaxItemCount() + " types of items",
                "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_INTERMEDIATE_MODEL.getEachMaxSize() + " items",
                "",
                "&7⇨ &eRequires Network Wrench to remove the machine",
                "",
                thanks,
                ""
);
public static SlimefunItemStack CARGO_STORAGE_UNIT_12_MODEL = Theme.model(
                "NTW_EXPANSION_CARGO_STORAGE_UNIT_12",
                Skins.CARGO_STORAGE_UNIT_12_MODEL.getPlayerHead(),
                Theme.MACHINE,
                "Network Cargo Storage Unit XII",
                "&6Supports fast input/output through networks",
                "",
                "&7⇨ &eCan store " + StorageUnitType.END_GAME_ADVANCED_MODEL.getMaxItemCount() + " types of items",
                "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_ADVANCED_MODEL.getEachMaxSize() + " items",
                "",
                "&7⇨ &eRequires Network Wrench to remove the machine",
                "",
                thanks,
                ""
);


public static SlimefunItemStack CARGO_STORAGE_UNIT_13_MODEL = Theme.model(
                "NTW_EXPANSION_CARGO_STORAGE_UNIT_13",
                Skins.CARGO_STORAGE_UNIT_13_MODEL.getPlayerHead(),
                Theme.MACHINE,
                "Network Cargo Storage Unit XIII",
                "&6Supports fast input/output through networks",
                "",
                "&7⇨ &eCan store " + StorageUnitType.END_GAME_MAX_MODEL.getMaxItemCount() + " types of items",
                "&7⇨ &eEach type can hold up to " + StorageUnitType.END_GAME_MAX_MODEL.getEachMaxSize() + " items",
                "",
                "&7⇨ &eRequires Network Wrench to remove the machine",
                "",
                thanks,
                ""
);
    public static ItemStack SEFIRAAT_ITEMSTACK = new CustomItemStack(Material.PLAYER_HEAD);
    public static final SlimefunItemStack AUTHOR_SEFIRAAT = Theme.Random(
            "NETWORKS_AUTHOR_SEFIRAAT",
            SEFIRAAT_ITEMSTACK,
            Theme.GUIDE,
            "Sefitard",
            "Bugworks' author"
    );
    public static ItemStack YBW0014_ITEMSTACK = new CustomItemStack(Material.PLAYER_HEAD);
    public static final SlimefunItemStack AUTHOR_YBW0014 = Theme.Random(
            "NETWORKS_CHINESE_LOCALIZATION_AUTHOR_YBW0014",
            YBW0014_ITEMSTACK,
            Theme.GUIDE,
            "ybw0014",
            "Networks Chinese Localization's author"
    );
    public static ItemStack YITOUDAIDAI_ITEMSTACK = new CustomItemStack(Material.PLAYER_HEAD);
    public static final SlimefunItemStack AUTHOR_YITOUDAIDAI = Theme.Random(
            "NETWORKS_EXPANSION_AUTHOR_YITOUDAIDAI",
            YITOUDAIDAI_ITEMSTACK,
            Theme.GUIDE,
            "yitoudaidai",
            "Networks Expansion's author"
    );
    public static ItemStack TINALNESS_ITEMSTACK = new CustomItemStack(Material.PLAYER_HEAD);
    public static final SlimefunItemStack AUTHOR_TINALNESS = Theme.Random(
            "NETWORKS_EXPANSION_AUTHOR_TINALNESS",
            TINALNESS_ITEMSTACK,
            Theme.GUIDE,
            "tinalness",
            "Networks Expansion's author"
    );

    static {
        try {
            SEFIRAAT_ITEMSTACK = getItemStack("rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHQAAj09dAABdnQABHR5cGV0AARtZXRhdXEAfgAGAAAABHQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA2JdAALUExBWUVSX0hFQURzcQB+AABzcQB+AAN1cQB+AAYAAAAFcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0AAZEYW1hZ2V0AAtza3VsbC1vd25lcnVxAH4ABgAAAAV0AAhJdGVtTWV0YXQABVNLVUxMdACaeyJleHRyYSI6W3siYm9sZCI6ZmFsc2UsIml0YWxpYyI6ZmFsc2UsInVuZGVybGluZWQiOmZhbHNlLCJzdHJpa2V0aHJvdWdoIjpmYWxzZSwib2JmdXNjYXRlZCI6ZmFsc2UsImNvbG9yIjoid2hpdGUiLCJ0ZXh0IjoiU2t1bGwgb2YgU2VmaXJhYXQifV0sInRleHQiOiIifXNxAH4ADgAAAANzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAh0AAh1bmlxdWVJZHQABG5hbWV0AApwcm9wZXJ0aWVzdXEAfgAGAAAABHQADVBsYXllclByb2ZpbGV0ACQ0ZDY1MDllMi1kYTEyLTQyYTctOTk2YS01NjhiYWU0MTdmN2J0AAhTZWZpcmFhdHNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAABc3IAF2phdmEudXRpbC5MaW5rZWRIYXNoTWFwNMBOXBBswPsCAAFaAAthY2Nlc3NPcmRlcnhyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAADcQB+ACJ0AAh0ZXh0dXJlc3QABXZhbHVldAGcZXdvZ0lDSjBhVzFsYzNSaGJYQWlJRG9nTVRjeU1qTTBNRE16TmpFeU1Dd0tJQ0FpY0hKdlptbHNaVWxrSWlBNklDSTBaRFkxTURsbE1tUmhNVEkwTW1FM09UazJZVFUyT0dKaFpUUXhOMlkzWWlJc0NpQWdJbkJ5YjJacGJHVk9ZVzFsSWlBNklDSlRaV1pwY21GaGRDSXNDaUFnSW5OcFoyNWhkSFZ5WlZKbGNYVnBjbVZrSWlBNklIUnlkV1VzQ2lBZ0luUmxlSFIxY21WeklpQTZJSHNLSUNBZ0lDSlRTMGxPSWlBNklIc0tJQ0FnSUNBZ0luVnliQ0lnT2lBaWFIUjBjRG92TDNSbGVIUjFjbVZ6TG0xcGJtVmpjbUZtZEM1dVpYUXZkR1Y0ZEhWeVpTOWlZakkzTWpVNU1qUmxNRGxrTm1Jd1ltUm1OV0ZpT0RZMFpUWXpaamd3WldJNE9EQmlabUUyWm1VeFptRXhOMlk1Wm1SaU5qRmlZekZoWlRFeE1HUmlJZ29nSUNBZ2ZRb2dJSDBLZlE9PXQACXNpZ25hdHVyZXQCrHcraU9VYmpKUUxZRG9rTlU3YVkreVk1cStxMEZodWNoNEI0eW00Y1RqWGJHeEtPUUlaODFaUnhzTDdDR0dCVlVxampJMjFZbDRJaSs3aVBScjFPM1FMbXNlMGN4Tk1iVmxsV3UyZFg4TDlNTEhPUmZSbUp2M0FrVFdKZTlvNXZQb1JvVmtSOHhqbTRNQWJWQ3Rray80eVNKYlZ0UFJPaW96d2NtYm12UWpCRmFSbHIrV3pjeVhxdzkvOVBaL3NYd0hMQXlKRmZRWWdKcklla09JaUR4V1FXeDRDQmNnQUwrQ1NPaTkyRkRUazgyR3JYb01EaUJSRmhuaGdJQnFONSt2TW1leGVsRTJlSk1Pbi80L3FXV2dFY0FKZ2Jja1lQaDRublNoRkFLWnBVdmtwd1k2QS9DWG02bnRmTHcrOStuS3NmaUpRcWtpY0FXckExb21ZVEpPYjR2SmVEcG5EeUZuMVJLb3dnNWdHbjlOMzBRN1ZhRzkrMytReTRHRVh4SjVseTNoWVJzZjkxdFYrRm1GRkpmWTJjZWdVc0h5K0VVOWlBU1pqQ2hSY2tTQVNocmpyR1gxMkJQc2xaemx2Znk2WlNzWFhxNmYzOVhlVm5GbXZENmcyMk9takhLbWc4LzZja1FidkRaSkVoeFlYN0FLanNqaTdQcDl5RU4wRGE4aTFOR0hBdWNVS0hmcVNuNkZ0RTFjNlBmckhOWUVmT3c2eCtCRDJIejlnOFFvSElSRllPekZ3cGJBWkp5NWE4bkNxUWR6Y2hMTGpueG9sU0REZVEzZ1JWUVZPT3ltcFFBMUlXa3lyd3h0ZjFPTndBMVQxSXpGazUwUjdoNkNoWU1ZblRNNGVuakJvWFFtOUxkZEVHZVM3emtiTzZSK2VDVmZ3bmNyQXdnSTJNPXgAeA==");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            YBW0014_ITEMSTACK = getItemStack("rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHQAAj09dAABdnQABHR5cGV0AARtZXRhdXEAfgAGAAAABHQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA2JdAALUExBWUVSX0hFQURzcQB+AABzcQB+AAN1cQB+AAYAAAAFcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0AAZEYW1hZ2V0AAtza3VsbC1vd25lcnVxAH4ABgAAAAV0AAhJdGVtTWV0YXQABVNLVUxMdACZeyJleHRyYSI6W3siYm9sZCI6ZmFsc2UsIml0YWxpYyI6ZmFsc2UsInVuZGVybGluZWQiOmZhbHNlLCJzdHJpa2V0aHJvdWdoIjpmYWxzZSwib2JmdXNjYXRlZCI6ZmFsc2UsImNvbG9yIjoid2hpdGUiLCJ0ZXh0IjoiU2t1bGwgb2YgeWJ3MDAxNCJ9XSwidGV4dCI6IiJ9c3EAfgAOAAAAA3NxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHQACHVuaXF1ZUlkdAAEbmFtZXQACnByb3BlcnRpZXN1cQB+AAYAAAAEdAANUGxheWVyUHJvZmlsZXQAJDY1YzA5YjhjLWZjMzktNDNhNS05MTFiLTAxZjQ1NzdkNzRhZnQAB3lidzAwMTRzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAAAXcEAAAAAXNyABdqYXZhLnV0aWwuTGlua2VkSGFzaE1hcDTATlwQbMD7AgABWgALYWNjZXNzT3JkZXJ4cgARamF2YS51dGlsLkhhc2hNYXAFB9rBwxZg0QMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4cD9AAAAAAAAMdwgAAAAQAAAAA3EAfgAidAAIdGV4dHVyZXN0AAV2YWx1ZXQBmGV3b2dJQ0owYVcxbGMzUmhiWEFpSURvZ01UY3lNak0wTURNd05UZzBNU3dLSUNBaWNISnZabWxzWlVsa0lpQTZJQ0kyTldNd09XSTRZMlpqTXprME0yRTFPVEV4WWpBeFpqUTFOemRrTnpSaFppSXNDaUFnSW5CeWIyWnBiR1ZPWVcxbElpQTZJQ0o1WW5jd01ERTBJaXdLSUNBaWMybG5ibUYwZFhKbFVtVnhkV2x5WldRaUlEb2dkSEoxWlN3S0lDQWlkR1Y0ZEhWeVpYTWlJRG9nZXdvZ0lDQWdJbE5MU1U0aUlEb2dld29nSUNBZ0lDQWlkWEpzSWlBNklDSm9kSFJ3T2k4dmRHVjRkSFZ5WlhNdWJXbHVaV055WVdaMExtNWxkQzkwWlhoMGRYSmxMemd5WVRWak9HUmxNemRrTVdFME9HWTBNVEExTTJJNFkyTXlZV0poWldNM09XWm1ZbVl3Wm1NeE1UUTJOR0l5T1RCaU1EVTNaR1F4WkRGa016Z3pOMlVpQ2lBZ0lDQjlDaUFnZlFwOXQACXNpZ25hdHVyZXQCrERINmpXTVpvb1NMdzVTNU41S2hBRXBqb2RrTExwMHZtbzY4cVUyL2t2ODVMbzExd0pwaEpMdUUySkp3WkVEdlpFZGNzeEgwYlY4N1BnUVVaTHAwWTNCaVNZSTVZczRqSWZUVUU1MmFhblZTdEF0VU1oNVRZUDlHRkZzVVVqSlFFODY5OEZnKzlZKzhDY3E5bHplQW9GMEtzbTgzM1FuT2diUlJpRmhaUHQ0VFp6alpMWXJwTHp0WGl6blIrajFUaWRobkVuT1A3WFZSeWlkckE4VmhtZklGWU13VzlnMTErYTlOV0wrZGUzejY3cjA3TlFoY0pHdVl6VXFKRVZQNFNwSUZYaTMzM1YxRTZTMjMrakV4K081RHd3enVZNlMyMlhFTVgrZmJTMHRpMTJ0Nzh4TWJ1V2dGV3k1SHZ4YjZJTmxoenFmRUQwNUtlb2M2NEMrWWErSzFieGVXRGQyOGpESlFnS1h5Z2xlZFV3Y21Ia1lMV0Z0OXkwOEV6WlJnYUZJekxWbFJjdlFMTWR4aGpyQ2tRUFFUYlFEcTZFc0RQbm91UklKQU14S0JETlBJNHNTZlh2cnJRMUkrVU03bG5RVXlZUTdXdG5tL2M4bHN5S3ZHdUxpZ2RWdEVmbnE0Yk9KMjF4VXZhVjEwNHNLdlYvMEJiTVFWVVArZ1M3ZkNSWUR5UWsvNHpDeVZpSjdaT2lBbkxncmQ3d3daSmxUZ1F4bXp3dWhhdWFIM3owNmVYVHR3MU5RUW8xRkM1S3I5MUxnek1nckF3OHhSMUlpZWRpclg1UEJzWUtRR3hQb1BQcmN3ckFmdXlRbDNwWWltazZFNzdwYlFUSGRIa3grNUhnOGhzQjFSZXJUS3pIaEhtMDhSVHRIbDBvTzdUaUptOGo4akloM1ZKV1JnPXgAeA==");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            YITOUDAIDAI_ITEMSTACK = getItemStack("rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHQAAj09dAABdnQABHR5cGV0AARtZXRhdXEAfgAGAAAABHQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA2JdAALUExBWUVSX0hFQURzcQB+AABzcQB+AAN1cQB+AAYAAAAFcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0AAZEYW1hZ2V0AAtza3VsbC1vd25lcnVxAH4ABgAAAAV0AAhJdGVtTWV0YXQABVNLVUxMdACdeyJleHRyYSI6W3siYm9sZCI6ZmFsc2UsIml0YWxpYyI6ZmFsc2UsInVuZGVybGluZWQiOmZhbHNlLCJzdHJpa2V0aHJvdWdoIjpmYWxzZSwib2JmdXNjYXRlZCI6ZmFsc2UsImNvbG9yIjoid2hpdGUiLCJ0ZXh0IjoiU2t1bGwgb2YgeWl0b3VkYWlkYWkifV0sInRleHQiOiIifXNxAH4ADgAAAANzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAh0AAh1bmlxdWVJZHQABG5hbWV1cQB+AAYAAAADdAANUGxheWVyUHJvZmlsZXQAJDJkMGMyMTQxLTRjMGItMzMxYy1hNDU2LWE5YjVhMjVlYjI1OHQAC3lpdG91ZGFpZGFp");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            TINALNESS_ITEMSTACK = getItemStack("rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHQAAj09dAABdnQABHR5cGV0AARtZXRhdXEAfgAGAAAABHQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA2JdAALUExBWUVSX0hFQURzcQB+AABzcQB+AAN1cQB+AAYAAAAFcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0AAZEYW1hZ2V0AAtza3VsbC1vd25lcnVxAH4ABgAAAAV0AAhJdGVtTWV0YXQABVNLVUxMdACbeyJleHRyYSI6W3siYm9sZCI6ZmFsc2UsIml0YWxpYyI6ZmFsc2UsInVuZGVybGluZWQiOmZhbHNlLCJzdHJpa2V0aHJvdWdoIjpmYWxzZSwib2JmdXNjYXRlZCI6ZmFsc2UsImNvbG9yIjoid2hpdGUiLCJ0ZXh0IjoiU2t1bGwgb2YgdGluYWxuZXNzIn1dLCJ0ZXh0IjoiIn1zcQB+AA4AAAADc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIdAAIdW5pcXVlSWR0AARuYW1ldAAKcHJvcGVydGllc3VxAH4ABgAAAAR0AA1QbGF5ZXJQcm9maWxldAAkZTg5YmMwYjUtZDYyNC0zZGIzLWEzNmUtYmM3YzdjMjM2ZjRldAAJdGluYWxuZXNzc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAAFzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hNYXA0wE5cEGzA+wIAAVoAC2FjY2Vzc09yZGVyeHIAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAANxAH4AInQACHRleHR1cmVzdAAFdmFsdWV0AZxld29nSUNKMGFXMWxjM1JoYlhBaUlEb2dNVGN5TWpNME1EUTJNalkzTUN3S0lDQWljSEp2Wm1sc1pVbGtJaUE2SUNJME1USTRNR1ZqTmpBd1l6azBaREZtT1dJMk5UYzJOelExT1dVd05EQXhaaUlzQ2lBZ0luQnliMlpwYkdWT1lXMWxJaUE2SUNKQmJXRjZhVzVuUVNJc0NpQWdJbk5wWjI1aGRIVnlaVkpsY1hWcGNtVmtJaUE2SUhSeWRXVXNDaUFnSW5SbGVIUjFjbVZ6SWlBNklIc0tJQ0FnSUNKVFMwbE9JaUE2SUhzS0lDQWdJQ0FnSW5WeWJDSWdPaUFpYUhSMGNEb3ZMM1JsZUhSMWNtVnpMbTFwYm1WamNtRm1kQzV1WlhRdmRHVjRkSFZ5WlM4M016RXhNamM0TldZMk5HUTRNVFF4TURNNU16RTFNRFZoWTJVd01EQTBPR013T0Rjek16YzNPRFUxTlRCak9UbGhOamMwTkRsak16a3lZak01TnpjeUlnb2dJQ0FnZlFvZ0lIMEtmUT09dAAJc2lnbmF0dXJldAKsVEpoR2FmL2tzQjJJVXE3ZkUrSUkzQ0EvMGQ5MU9KL0NhcS96azMvVGdsK1BJM0NQeHlKK1ZvdFpvZWZEYTRtUGlXNWJOank5NGM3L21QRWN1WFpKVVFSZUV4L0JRTmZyRENpUm9FdWdFMzBOVDhDTDBTaCtpdTkvK1lvTGtUSFV0ZVNuZFBwZTlMYlJzSzQwMnhEV3NTbUFQcVNSZHlUU21OMjhqTi82UW5sMzI5K3NWbHlKTWVEZjluamxYRnp2Z3JrNjRoUlRCK3JCZU9uL25qamE2UVNjVnBmQUpjZy9hZ2xGY01NNEV0OVlMdlBTVjV2WVo1UXhEbzFHL1drenk4WGFZME81ZU1CdXZQV3E4VFZ4WVRBUXFlcTNtM0xta1I0c1MrTU5iTmc3VFFFNjhTUDQ5TU5ObUJXM0tqM2dwNmxFNzNRZkFXUmcwVFBPRXhYSklTN1RsZGk1cVlQcGorRDljSng5VGRNYnRIUlA0Qzduc0NuWnJCVXB4RENsbXB4OVBzYlo1VlpwbkltZm9ldmxqeXFtS2gwWHBzM1JwZE1tNjk4a1JWQUk3Qzd6Yi8wNVpjQ3N1ekRpL1FoMlJoaDFaY2lXQXhZazljeEdwb2YzU04xU1ppMGdrVmpXY0dJNlNNRm9GNVQveWlhaFpsVlppdzE3VU5kMlQza2xvUWhyajFvWkFURVh6Yk1GR1lnY3drY0VQNElPNjRvK2dhWFM3WVRidEZJVUxRZTJHRTN3R0hTL2JnM2lPblhZa0hncG5lRlBBQXVVQ0hiZHRWYktmNjUyVDNiK2E5MHpRcWI0Z1B4M0lWLzVwMG1HbWc4dm5kNzBLR1lnQ3FOdWZQbHZpSmxjNEhENlhJcm84S1A2RnFzMUxXUGpGSEttQmtlL0RzVy93NGM9eAB4");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static {
        typeMap.put(StorageUnitType.TINY, CARGO_STORAGE_UNIT_1);
        typeMap.put(StorageUnitType.MINI, CARGO_STORAGE_UNIT_2);
        typeMap.put(StorageUnitType.SMALL, CARGO_STORAGE_UNIT_3);
        typeMap.put(StorageUnitType.MEDIUM, CARGO_STORAGE_UNIT_4);
        typeMap.put(StorageUnitType.LARGE, CARGO_STORAGE_UNIT_5);
        typeMap.put(StorageUnitType.ENHANCED, CARGO_STORAGE_UNIT_6);
        typeMap.put(StorageUnitType.ADVANCED, CARGO_STORAGE_UNIT_7);
        typeMap.put(StorageUnitType.EXTRA, CARGO_STORAGE_UNIT_8);
        typeMap.put(StorageUnitType.ULTRA, CARGO_STORAGE_UNIT_9);
        typeMap.put(StorageUnitType.END_GAME_BASIC, CARGO_STORAGE_UNIT_10);
        typeMap.put(StorageUnitType.END_GAME_INTERMEDIATE, CARGO_STORAGE_UNIT_11);
        typeMap.put(StorageUnitType.END_GAME_ADVANCED, CARGO_STORAGE_UNIT_12);
        typeMap.put(StorageUnitType.END_GAME_MAX, CARGO_STORAGE_UNIT_13);

        typeMap.put(StorageUnitType.TINY_MODEL, CARGO_STORAGE_UNIT_1_MODEL);
        typeMap.put(StorageUnitType.MINI_MODEL, CARGO_STORAGE_UNIT_2_MODEL);
        typeMap.put(StorageUnitType.SMALL_MODEL, CARGO_STORAGE_UNIT_3_MODEL);
        typeMap.put(StorageUnitType.MEDIUM_MODEL, CARGO_STORAGE_UNIT_4_MODEL);
        typeMap.put(StorageUnitType.LARGE_MODEL, CARGO_STORAGE_UNIT_5_MODEL);
        typeMap.put(StorageUnitType.ENHANCED_MODEL, CARGO_STORAGE_UNIT_6_MODEL);
        typeMap.put(StorageUnitType.ADVANCED_MODEL, CARGO_STORAGE_UNIT_7_MODEL);
        typeMap.put(StorageUnitType.EXTRA_MODEL, CARGO_STORAGE_UNIT_8_MODEL);
        typeMap.put(StorageUnitType.ULTRA_MODEL, CARGO_STORAGE_UNIT_9_MODEL);
        typeMap.put(StorageUnitType.END_GAME_BASIC_MODEL, CARGO_STORAGE_UNIT_10_MODEL);
        typeMap.put(StorageUnitType.END_GAME_INTERMEDIATE_MODEL, CARGO_STORAGE_UNIT_11_MODEL);
        typeMap.put(StorageUnitType.END_GAME_ADVANCED_MODEL, CARGO_STORAGE_UNIT_12_MODEL);
        typeMap.put(StorageUnitType.END_GAME_MAX_MODEL, CARGO_STORAGE_UNIT_13_MODEL);
    }

    public static SlimefunItemStack getStorageItemFromType(StorageUnitType type) {
        return typeMap.get(type);
    }

    private static ItemStack getItemStack(String base64Str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream stream = new ByteArrayInputStream(Base64Coder.decodeLines(base64Str));
        BukkitObjectInputStream bs = new BukkitObjectInputStream(stream);
        ItemStack re = (ItemStack) bs.readObject();
        bs.close();
        return re;
    }

    public static ItemStack Enchanted(Material material) {
        return ItemStackUtil.getPreEnchantedItemStack(material);
    }

}
