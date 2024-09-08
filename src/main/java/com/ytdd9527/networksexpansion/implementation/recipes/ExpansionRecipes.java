package com.ytdd9527.networksexpansion.implementation.recipes;

import com.ytdd9527.networksexpansion.implementation.items.ExpansionItemStacks;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static io.github.sefiraat.networks.slimefun.NetworkSlimefunItems.*;

public class ExpansionRecipes {

    public static final ItemStack[] NULL = new ItemStack[]{
            null, null, null,
            null, null, null,
            null, null, null
    };

    // Workbench
    public static final ItemStack[] NE_EXPANSION_WORKBENCH = new ItemStack[]{
            EMPOWERED_AI_CORE.getItem(), SlimefunItems.ADVANCED_CIRCUIT_BOARD, EMPOWERED_AI_CORE.getItem(),
            OPTIC_CABLE.getItem(), NETWORK_BRIDGE.getItem(), OPTIC_CABLE.getItem(),
            EMPOWERED_AI_CORE.getItem(), SlimefunItems.ADVANCED_CIRCUIT_BOARD, EMPOWERED_AI_CORE.getItem()
    };

    // Point Transfers
    public static final ItemStack[] POINT_TRANSFER = new ItemStack[]{
            NETWORK_GRABBER.getItem(), NETWORK_PUSHER.getItem(), NETWORK_GRABBER.getItem(),
            NETWORK_PUSHER.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), NETWORK_PUSHER.getItem(),
            NETWORK_GRABBER.getItem(), NETWORK_PUSHER.getItem(), NETWORK_GRABBER.getItem()
    };
    public static final ItemStack[] POINT_TRANSFER_GRABBER = new ItemStack[]{
            NETWORK_GRABBER.getItem(), NETWORK_GRABBER.getItem(), NETWORK_GRABBER.getItem(),
            OPTIC_CABLE.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), OPTIC_CABLE.getItem(),
            NETWORK_GRABBER.getItem(), NETWORK_GRABBER.getItem(), NETWORK_GRABBER.getItem()
    };

    // Line Transfers
    public static final ItemStack[] LINE_TRANSFER_PUSHER = new ItemStack[]{
            NETWORK_PUSHER.getItem(), NETWORK_EXPORT.getItem(), AI_CORE.getItem(),
            NETWORK_EXPORT.getItem(), NETWORK_MONITOR.getItem(), NETWORK_EXPORT.getItem(),
            AI_CORE.getItem(), NETWORK_EXPORT.getItem(), NETWORK_PUSHER.getItem()
    };

    public static final ItemStack[] LINE_TRANSFER_PLUS_PUSHER = new ItemStack[]{
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.LINE_TRANSFER_PUSHER, OPTIC_CABLE.getItem(),
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem()
    };
    public static final ItemStack[] ADVANCED_LINE_TRANSFER_PUSHER = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), ExpansionItemStacks.LINE_TRANSFER_PUSHER, NETWORK_BRIDGE.getItem(),
            EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(),
            NETWORK_BRIDGE.getItem(), ExpansionItemStacks.LINE_TRANSFER_PUSHER, NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] ADVANCED_LINE_TRANSFER_PLUS_PUSHER = new ItemStack[]{
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PUSHER, OPTIC_CABLE.getItem(),
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem()
    };

    public static final ItemStack[] LINE_TRANSFER_GRABBER = new ItemStack[]{
            NETWORK_GRABBER.getItem(), NETWORK_IMPORT.getItem(), AI_CORE.getItem(),
            NETWORK_IMPORT.getItem(), NETWORK_MONITOR.getItem(), NETWORK_IMPORT.getItem(),
            AI_CORE.getItem(), NETWORK_IMPORT.getItem(), NETWORK_GRABBER.getItem()
    };
    public static final ItemStack[] LINE_TRANSFER_PLUS_GRABBER = new ItemStack[]{
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.LINE_TRANSFER_GRABBER, OPTIC_CABLE.getItem(),
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem()
    };
    public static final ItemStack[] ADVANCED_LINE_TRANSFER_GRABBER = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), ExpansionItemStacks.LINE_TRANSFER_GRABBER, NETWORK_BRIDGE.getItem(),
            EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(),
            NETWORK_BRIDGE.getItem(), ExpansionItemStacks.LINE_TRANSFER_GRABBER, NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] ADVANCED_LINE_TRANSFER_PLUS_GRABBER = new ItemStack[]{
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.ADVANCED_LINE_TRANSFER_GRABBER, OPTIC_CABLE.getItem(),
            SHRINKING_BASE.getItem(), OPTIC_CABLE.getItem(), SHRINKING_BASE.getItem()
    };
    public static final ItemStack[] LINE_TRANSFER = new ItemStack[]{
            ExpansionItemStacks.LINE_TRANSFER_PUSHER, NETWORK_IMPORT.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_EXPORT.getItem(), NETWORK_MONITOR.getItem(), NETWORK_EXPORT.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_IMPORT.getItem(), ExpansionItemStacks.LINE_TRANSFER_GRABBER
    };

    public static final ItemStack[] LINE_TRANSFER_PLUS = new ItemStack[]{
            ExpansionItemStacks.LINE_TRANSFER_PLUS_PUSHER, AI_CORE.getItem(), NETWORK_BRIDGE.getItem(),
            AI_CORE.getItem(), AI_CORE.getItem(), AI_CORE.getItem(),
            NETWORK_BRIDGE.getItem(), AI_CORE.getItem(), ExpansionItemStacks.LINE_TRANSFER_PLUS_GRABBER
    };
    public static final ItemStack[] ADVANCED_LINE_TRANSFER = new ItemStack[]{
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PUSHER, EMPOWERED_AI_CORE.getItem(), AI_CORE.getItem(),
            EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(),
            AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(), ExpansionItemStacks.ADVANCED_LINE_TRANSFER_GRABBER
    };

    public static final ItemStack[] ADVANCED_LINE_TRANSFER_PLUS = new ItemStack[]{
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS_PUSHER, EMPOWERED_AI_CORE.getItem(), AI_CORE.getItem(),
            EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(),
            AI_CORE.getItem(), EMPOWERED_AI_CORE.getItem(), ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS_GRABBER
    };

    public static final ItemStack[] LINE_TRANSFER_VANILLA_GRABBER = new ItemStack[]{
            NETWORK_VANILLA_GRABBER.getItem(), OPTIC_CABLE.getItem(), NETWORK_VANILLA_GRABBER.getItem(),
            AI_CORE.getItem(), AI_CORE.getItem(), AI_CORE.getItem(),
            NETWORK_VANILLA_GRABBER.getItem(), OPTIC_CABLE.getItem(), NETWORK_VANILLA_GRABBER.getItem()
    };

    public static final ItemStack[] LINE_TRANSFER_VANILLA_PUSHER = new ItemStack[]{
            NETWORK_VANILLA_PUSHER.getItem(), OPTIC_CABLE.getItem(), NETWORK_VANILLA_PUSHER.getItem(),
            AI_CORE.getItem(), AI_CORE.getItem(), AI_CORE.getItem(),
            NETWORK_VANILLA_PUSHER.getItem(), OPTIC_CABLE.getItem(), NETWORK_VANILLA_PUSHER.getItem()
    };

    public static final ItemStack[] ADVANCED_IMPORT = new ItemStack[]{
            NETWORK_IMPORT.getItem(), NETWORK_IMPORT.getItem(), NETWORK_IMPORT.getItem(),
            OPTIC_STAR.getItem(), OPTIC_STAR.getItem(), OPTIC_STAR.getItem(),
            NETWORK_IMPORT.getItem(), NETWORK_IMPORT.getItem(), NETWORK_IMPORT.getItem()
    };

    public static final ItemStack[] ADVANCED_EXPORT = new ItemStack[]{
            NETWORK_EXPORT.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(), NETWORK_EXPORT.getItem(),
            NETWORK_EXPORT.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(), NETWORK_EXPORT.getItem(),
            NETWORK_EXPORT.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(), NETWORK_EXPORT.getItem()
    };

    public static final ItemStack[] ADVANCED_PURGER = new ItemStack[]{
            RADIOACTIVE_OPTIC_STAR.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(),
            RADIOACTIVE_OPTIC_STAR.getItem(), NETWORK_PURGER.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(),
            RADIOACTIVE_OPTIC_STAR.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(), RADIOACTIVE_OPTIC_STAR.getItem()
    };

    public static final ItemStack[] ADVANCED_GREEDY_BLOCK = new ItemStack[]{
            SYNTHETIC_EMERALD_SHARD.getItem(), NETWORK_GREEDY_BLOCK.getItem(), SYNTHETIC_EMERALD_SHARD.getItem(),
            NETWORK_GREEDY_BLOCK.getItem(), NETWORK_GREEDY_BLOCK.getItem(), NETWORK_GREEDY_BLOCK.getItem(),
            SHRINKING_BASE.getItem(), NETWORK_GREEDY_BLOCK.getItem(), SHRINKING_BASE.getItem()
    };

    public static final ItemStack[] NETWORK_INPUT_ONLY_MONITOR = new ItemStack[]{
            OPTIC_GLASS.getItem(), OPTIC_CABLE.getItem(), OPTIC_GLASS.getItem(),
            OPTIC_CABLE.getItem(), NETWORK_MONITOR.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_GLASS.getItem(), SlimefunItems.CARGO_INPUT_NODE, OPTIC_GLASS.getItem()
    };


    public static final ItemStack[] NETWORK_OUTPUT_ONLY_MONITOR = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CARGO_OUTPUT_NODE_2, OPTIC_GLASS.getItem(),
            OPTIC_CABLE.getItem(), NETWORK_MONITOR.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_GLASS.getItem(), OPTIC_CABLE.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] NETWORK_CAPACITOR_5 = new ItemStack[]{
            NETWORK_CAPACITOR_4.getItem(), NETWORK_CAPACITOR_4.getItem(), NETWORK_CAPACITOR_4.getItem(),
            NETWORK_CAPACITOR_4.getItem(), NETWORK_CAPACITOR_4.getItem(), NETWORK_CAPACITOR_4.getItem(),
            NETWORK_CAPACITOR_4.getItem(), NETWORK_CAPACITOR_4.getItem(), NETWORK_CAPACITOR_4.getItem()
    };

    public static final ItemStack[] NETWORK_CAPACITOR_6 = new ItemStack[]{
            ExpansionItemStacks.NETWORK_CAPACITOR_5, ExpansionItemStacks.NETWORK_CAPACITOR_5, ExpansionItemStacks.NETWORK_CAPACITOR_5,
            ExpansionItemStacks.NETWORK_CAPACITOR_5, ExpansionItemStacks.NETWORK_CAPACITOR_5, ExpansionItemStacks.NETWORK_CAPACITOR_5,
            ExpansionItemStacks.NETWORK_CAPACITOR_5, ExpansionItemStacks.NETWORK_CAPACITOR_5, ExpansionItemStacks.NETWORK_CAPACITOR_5
    };

    public static final ItemStack[] MAGIC_WORKBENCH_BLUEPRINT = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), CRAFTING_BLUEPRINT.getItem(), OPTIC_CABLE.getItem(),
            new ItemStack(Material.BOOKSHELF), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.DISPENSER)
    };

    public static final ItemStack[] ARMOR_FORGE_BLUEPRINT = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            CRAFTING_BLUEPRINT.getItem(), new ItemStack(Material.ANVIL), CRAFTING_BLUEPRINT.getItem(),
            OPTIC_CABLE.getItem(), new ItemStack(Material.DISPENSER), OPTIC_CABLE.getItem()
    };

    public static final ItemStack[] SMELTERY_BLUEPRINT = new ItemStack[]{
            CRAFTING_BLUEPRINT.getItem(), new ItemStack(Material.NETHER_BRICK_FENCE), CRAFTING_BLUEPRINT.getItem(),
            new ItemStack(Material.NETHER_BRICKS), new ItemStack(Material.DISPENSER), new ItemStack(Material.NETHER_BRICKS),
            CRAFTING_BLUEPRINT.getItem(), SlimefunItems.IGNITION_CHAMBER, CRAFTING_BLUEPRINT.getItem()
    };

    public static final ItemStack[] QUANTUM_WORKBENCH_BLUEPRINT = new ItemStack[]{
            OPTIC_CABLE.getItem(), SlimefunItems.ADVANCED_CIRCUIT_BOARD, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), CRAFTING_BLUEPRINT.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), SlimefunItems.ADVANCED_CIRCUIT_BOARD, OPTIC_CABLE.getItem()
    };

    public static final ItemStack[] ANCIENT_ALTAR_BLUEPRINT = new ItemStack[]{
            SlimefunItems.ANCIENT_PEDESTAL, SlimefunItems.ANCIENT_PEDESTAL, SlimefunItems.ANCIENT_PEDESTAL,
            CRAFTING_BLUEPRINT.getItem(), SlimefunItems.ANCIENT_ALTAR, CRAFTING_BLUEPRINT.getItem(),
            SlimefunItems.ANCIENT_PEDESTAL, SlimefunItems.ANCIENT_PEDESTAL, SlimefunItems.ANCIENT_PEDESTAL
    };

    public static final ItemStack[] EXPANSION_WORKBENCH_BLUEPRINT = new ItemStack[]{
            NetworksSlimefunItemStacks.NETWORK_BRIDGE, SlimefunItems.ANCIENT_PEDESTAL, NetworksSlimefunItemStacks.NETWORK_BRIDGE,
            CRAFTING_BLUEPRINT.getItem(), ExpansionItemStacks.NETWORK_EXPANSION_WORKBENCH, CRAFTING_BLUEPRINT.getItem(),
            NetworksSlimefunItemStacks.NETWORK_BRIDGE, SlimefunItems.ANCIENT_PEDESTAL, NetworksSlimefunItemStacks.NETWORK_BRIDGE
    };

    public static final ItemStack[] MAGIC_WORKBENCH_RECIPE_ENCODER = new ItemStack[]{
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.BASIC_CIRCUIT_BOARD,
            ExpansionItemStacks.MAGIC_WORKBENCH_BLUEPRINT, ExpansionItemStacks.AUTO_MAGIC_WORKBENCH, ExpansionItemStacks.MAGIC_WORKBENCH_BLUEPRINT,
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARGO_MOTOR, SlimefunItems.BASIC_CIRCUIT_BOARD
    };

    public static final ItemStack[] ARMOR_FORGE_RECIPE_ENCODER = new ItemStack[]{
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.BASIC_CIRCUIT_BOARD,
            ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT, ExpansionItemStacks.AUTO_ARMOR_FORGE, ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT,
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARGO_MOTOR, SlimefunItems.BASIC_CIRCUIT_BOARD
    };

    public static final ItemStack[] SMELTERY_RECIPE_ENCODER = new ItemStack[]{
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.BASIC_CIRCUIT_BOARD,
            ExpansionItemStacks.SMELTERY_BLUEPRINT, ExpansionItemStacks.AUTO_SMELTERY, ExpansionItemStacks.SMELTERY_BLUEPRINT,
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARGO_MOTOR, SlimefunItems.BASIC_CIRCUIT_BOARD
    };

    public static final ItemStack[] QUANTUM_WORKBENCH_RECIPE_ENCODER = new ItemStack[]{
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.BASIC_CIRCUIT_BOARD,
            ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT, ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH, ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT,
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARGO_MOTOR, SlimefunItems.BASIC_CIRCUIT_BOARD
    };

    public static final ItemStack[] ANCIENT_ALTAR_RECIPE_ENCODER = new ItemStack[]{
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.BASIC_CIRCUIT_BOARD,
            ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT, ExpansionItemStacks.AUTO_ANCIENT_ALTAR, ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT,
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARGO_MOTOR, SlimefunItems.BASIC_CIRCUIT_BOARD
    };

    public static final ItemStack[] EXPANSION_WORKBENCH_RECIPE_ENCODER = new ItemStack[]{
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.ANDROID_MEMORY_CORE, SlimefunItems.BASIC_CIRCUIT_BOARD,
            ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT, ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH, ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT,
            SlimefunItems.BASIC_CIRCUIT_BOARD, SlimefunItems.CARGO_MOTOR, SlimefunItems.BASIC_CIRCUIT_BOARD
    };
    // Crafter
    public static final ItemStack[] AUTO_MAGIC_WORKBENCH = new ItemStack[]{
            OPTIC_GLASS.getItem(), OPTIC_CABLE.getItem(), OPTIC_GLASS.getItem(),
            ExpansionItemStacks.MAGIC_WORKBENCH_BLUEPRINT, SIMPLE_NANOBOTS.getItem(), ExpansionItemStacks.MAGIC_WORKBENCH_BLUEPRINT,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_MAGIC_WORKBENCH_WITHHOLDING = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_MAGIC_WORKBENCH,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_ARMOR_FORGE = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.ARMOR_AUTO_CRAFTER, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT, SIMPLE_NANOBOTS.getItem(), ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_ARMOR_FORGE_WITHHOLDING = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.AUTO_ARMOR_FORGE, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_ARMOR_FORGE,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_SMELTERY = new ItemStack[]{
            OPTIC_GLASS.getItem(), ExpansionItemStacks.SMELTERY_BLUEPRINT, OPTIC_GLASS.getItem(),
            SlimefunItems.ELECTRIC_SMELTERY_2, SIMPLE_NANOBOTS.getItem(), SlimefunItems.ELECTRIC_SMELTERY_2,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_SMELTERY_WITHHOLDING = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.AUTO_SMELTERY, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_SMELTERY,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_QUANTUM_WORKBENCH = new ItemStack[]{
            OPTIC_GLASS.getItem(), NETWORK_QUANTUM_WORKBENCH.getItem(), OPTIC_GLASS.getItem(),
            ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT, SIMPLE_NANOBOTS.getItem(), ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_QUANTUM_WORKBENCH_WITHHOLDING = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_ANCIENT_ALTAR = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT, SIMPLE_NANOBOTS.getItem(), ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_ANCIENT_ALTAR_WITHHOLDING = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_ANCIENT_ALTAR,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_EXPANSION_WORKBENCH = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT, SIMPLE_NANOBOTS.getItem(), ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER.getItem(), OPTIC_GLASS.getItem()
    };

    public static final ItemStack[] AUTO_EXPANSION_WORKBENCH_WITHHOLDING = new ItemStack[]{
            OPTIC_GLASS.getItem(), SlimefunItems.CRAFTER_SMART_PORT, OPTIC_GLASS.getItem(),
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH,
            OPTIC_GLASS.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), OPTIC_GLASS.getItem()
    };
    // Advanced Auto Crafter
    public static final ItemStack[] ADVANCED_AUTO_CRAFTING_TABLE = new ItemStack[]{
            NETWORK_AUTO_CRAFTER.getItem(), ADVANCED_NANOBOTS.getItem(), NETWORK_AUTO_CRAFTER.getItem(),
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            NETWORK_AUTO_CRAFTER.getItem(), NETWORK_RECIPE_ENCODER.getItem(), NETWORK_AUTO_CRAFTER.getItem()
    };

    public static final ItemStack[] ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING = new ItemStack[]{
            NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), ADVANCED_NANOBOTS.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(),
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem(), NETWORK_RECIPE_ENCODER.getItem(), NETWORK_AUTO_CRAFTER_WITHHOLDING.getItem()
    };

    public static final ItemStack[] ADVANCED_AUTO_MAGIC_WORKBENCH = new ItemStack[]{
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_MAGIC_WORKBENCH,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH, ExpansionItemStacks.MAGIC_WORKBENCH_RECIPE_ENCODER, ExpansionItemStacks.AUTO_MAGIC_WORKBENCH
    };

    public static final ItemStack[] ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING = new ItemStack[]{
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH_WITHHOLDING, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH_WITHHOLDING, ExpansionItemStacks.MAGIC_WORKBENCH_RECIPE_ENCODER, ExpansionItemStacks.AUTO_MAGIC_WORKBENCH_WITHHOLDING
    };

    public static final ItemStack[] ADVANCED_AUTO_ARMOR_FORGE = new ItemStack[]{
            ExpansionItemStacks.AUTO_ARMOR_FORGE, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_ARMOR_FORGE,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_ARMOR_FORGE, ExpansionItemStacks.ARMOR_FORGE_RECIPE_ENCODER, ExpansionItemStacks.AUTO_ARMOR_FORGE
    };

    public static final ItemStack[] ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING = new ItemStack[]{
            ExpansionItemStacks.AUTO_ARMOR_FORGE_WITHHOLDING, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_ARMOR_FORGE_WITHHOLDING,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_ARMOR_FORGE_WITHHOLDING, ExpansionItemStacks.ARMOR_FORGE_RECIPE_ENCODER, ExpansionItemStacks.AUTO_ARMOR_FORGE_WITHHOLDING
    };

    public static final ItemStack[] ADVANCED_AUTO_SMELTERY = new ItemStack[]{
            ExpansionItemStacks.AUTO_SMELTERY, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_SMELTERY,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_SMELTERY, ExpansionItemStacks.SMELTERY_RECIPE_ENCODER, ExpansionItemStacks.AUTO_SMELTERY
    };

    public static final ItemStack[] ADVANCED_AUTO_SMELTERY_WITHHOLDING = new ItemStack[]{
            ExpansionItemStacks.AUTO_SMELTERY_WITHHOLDING, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_SMELTERY_WITHHOLDING,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_SMELTERY_WITHHOLDING, ExpansionItemStacks.SMELTERY_RECIPE_ENCODER, ExpansionItemStacks.AUTO_SMELTERY_WITHHOLDING
    };

    public static final ItemStack[] ADVANCED_AUTO_QUANTUM_WORKBENCH = new ItemStack[]{
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH, ExpansionItemStacks.QUANTUM_WORKBENCH_RECIPE_ENCODER, ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH
    };

    public static final ItemStack[] ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING = new ItemStack[]{
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH_WITHHOLDING, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH_WITHHOLDING, ExpansionItemStacks.QUANTUM_WORKBENCH_RECIPE_ENCODER, ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH_WITHHOLDING
    };

    public static final ItemStack[] ADVANCED_AUTO_ANCIENT_ALTAR = {
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_ANCIENT_ALTAR,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR, ExpansionItemStacks.ANCIENT_ALTAR_RECIPE_ENCODER, ExpansionItemStacks.AUTO_ANCIENT_ALTAR
    };

    public static final ItemStack[] ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING = {
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR_WITHHOLDING, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_ANCIENT_ALTAR_WITHHOLDING,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR_WITHHOLDING, ExpansionItemStacks.ANCIENT_ALTAR_RECIPE_ENCODER, ExpansionItemStacks.AUTO_ANCIENT_ALTAR_WITHHOLDING
    };

    public static final ItemStack[] ADVANCED_AUTO_EXPANSION_WORKBENCH = {
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH, ExpansionItemStacks.EXPANSION_WORKBENCH_RECIPE_ENCODER, ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH
    };

    public static final ItemStack[] ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING = {
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH_WITHHOLDING, ADVANCED_NANOBOTS.getItem(), ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            ADVANCED_NANOBOTS.getItem(), INTERDIMENSIONAL_PRESENCE.getItem(), ADVANCED_NANOBOTS.getItem(),
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH_WITHHOLDING, ExpansionItemStacks.EXPANSION_WORKBENCH_RECIPE_ENCODER, ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH_WITHHOLDING
    };
    // Grid
    public static final ItemStack[] NETWORK_GRID_NEW_STYLE = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), OPTIC_CABLE.getItem(), NETWORK_BRIDGE.getItem(),
            OPTIC_CABLE.getItem(), NETWORK_GRID.getItem(), OPTIC_CABLE.getItem(),
            NETWORK_BRIDGE.getItem(), OPTIC_CABLE.getItem(), NETWORK_BRIDGE.getItem()
    };
    // Storages
    public static final ItemStack[] ADVANCED_QUANTUM_STORAGE = new ItemStack[]{
            OPTIC_STAR.getItem(), NETWORK_CONFIGURATOR.getItem(), OPTIC_STAR.getItem(),
            OPTIC_CABLE.getItem(), NETWORK_QUANTUM_STORAGE_8.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_STAR.getItem(), NETWORK_CONFIGURATOR.getItem(), OPTIC_STAR.getItem()
    };
    // Bridges
    public static final ItemStack[] NETWORK_BRIDGE_WHITE = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.WHITE_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_LIGHT_GRAY = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.LIGHT_GRAY_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_GRAY = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.GRAY_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_BLACK = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.BLACK_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_BROWN = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.BROWN_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_RED = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.RED_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_ORANGE = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.ORANGE_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_YELLOW = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.YELLOW_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_LIME = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.LIME_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_GREEN = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.GREEN_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_CYAN = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.CYAN_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_LIGHT_BLUE = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.LIGHT_BLUE_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_BLUE = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.BLUE_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_PURPLE = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.PURPLE_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_MAGENTA = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.MAGENTA_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] NETWORK_BRIDGE_PINK = new ItemStack[]{
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), new ItemStack(Material.PINK_DYE), NETWORK_BRIDGE.getItem(),
            NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_BRIDGE.getItem()
    };

    public static final ItemStack[] CARGO_NODE_QUICK_TOOL = new ItemStack[]{
            new ItemStack(Material.LEATHER), SlimefunItems.SOLAR_PANEL, new ItemStack(Material.LEATHER),
            new ItemStack(Material.LEATHER), SlimefunItems.ANDROID_MEMORY_CORE, new ItemStack(Material.LEATHER),
            SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.SMALL_CAPACITOR, SlimefunItems.ADVANCED_CIRCUIT_BOARD
    };

    public static final ItemStack[] STORAGE_UNIT_UPGRADE_TABLE = new ItemStack[]{
            EMPOWERED_AI_CORE.getItem(), ADVANCED_NANOBOTS.getItem(), EMPOWERED_AI_CORE.getItem(),
            ADVANCED_NANOBOTS.getItem(), NETWORK_QUANTUM_WORKBENCH.getItem(), ADVANCED_NANOBOTS.getItem(),
            EMPOWERED_AI_CORE.getItem(), ADVANCED_NANOBOTS.getItem(), EMPOWERED_AI_CORE.getItem()
    };
    public static final ItemStack[] STORAGE_UNIT_UPGRADE_TABLE_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.STORAGE_UNIT_UPGRADE_TABLE, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_1 = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            NETWORK_QUANTUM_STORAGE_1.getItem(), NETWORK_BRIDGE.getItem(), NETWORK_QUANTUM_STORAGE_1.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_2 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_9.getItem(), ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT, NETWORK_QUANTUM_STORAGE_9.getItem(),
            ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT, ExpansionItemStacks.CARGO_STORAGE_UNIT_1, ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT,
            NETWORK_QUANTUM_STORAGE_9.getItem(), ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT, NETWORK_QUANTUM_STORAGE_9.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_3 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_10.getItem(), ExpansionItemStacks.SMELTERY_BLUEPRINT, NETWORK_QUANTUM_STORAGE_10.getItem(),
            ExpansionItemStacks.SMELTERY_BLUEPRINT, ExpansionItemStacks.CARGO_STORAGE_UNIT_2, ExpansionItemStacks.SMELTERY_BLUEPRINT,
            NETWORK_QUANTUM_STORAGE_10.getItem(), ExpansionItemStacks.SMELTERY_BLUEPRINT, NETWORK_QUANTUM_STORAGE_10.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_4 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_1.getItem(), SlimefunItems.BOOSTED_URANIUM, NETWORK_QUANTUM_STORAGE_1.getItem(),
            SlimefunItems.BOOSTED_URANIUM, ExpansionItemStacks.CARGO_STORAGE_UNIT_3, SlimefunItems.BOOSTED_URANIUM,
            NETWORK_QUANTUM_STORAGE_1.getItem(), SlimefunItems.BOOSTED_URANIUM, NETWORK_QUANTUM_STORAGE_1.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_5 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_2.getItem(), SlimefunItems.NETHER_ICE, NETWORK_QUANTUM_STORAGE_2.getItem(),
            SlimefunItems.NETHER_ICE, ExpansionItemStacks.CARGO_STORAGE_UNIT_4, SlimefunItems.NETHER_ICE,
            NETWORK_QUANTUM_STORAGE_2.getItem(), SlimefunItems.NETHER_ICE, NETWORK_QUANTUM_STORAGE_2.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_6 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_3.getItem(), SlimefunItems.FUEL_BUCKET, NETWORK_QUANTUM_STORAGE_3.getItem(),
            SlimefunItems.FUEL_BUCKET, ExpansionItemStacks.CARGO_STORAGE_UNIT_5, SlimefunItems.FUEL_BUCKET,
            NETWORK_QUANTUM_STORAGE_3.getItem(), SlimefunItems.FUEL_BUCKET, NETWORK_QUANTUM_STORAGE_3.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_7 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_3.getItem(), OPTIC_STAR.getItem(), NETWORK_QUANTUM_STORAGE_3.getItem(),
            OPTIC_STAR.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_6, OPTIC_STAR.getItem(),
            NETWORK_QUANTUM_STORAGE_3.getItem(), OPTIC_STAR.getItem(), NETWORK_QUANTUM_STORAGE_3.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_8 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_3.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(), NETWORK_QUANTUM_STORAGE_3.getItem(),
            RADIOACTIVE_OPTIC_STAR.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_7, RADIOACTIVE_OPTIC_STAR.getItem(),
            NETWORK_QUANTUM_STORAGE_3.getItem(), RADIOACTIVE_OPTIC_STAR.getItem(), NETWORK_QUANTUM_STORAGE_3.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_9 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_4.getItem(), ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT, NETWORK_QUANTUM_STORAGE_4.getItem(),
            ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT, ExpansionItemStacks.CARGO_STORAGE_UNIT_8, ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT,
            NETWORK_QUANTUM_STORAGE_4.getItem(), ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT, NETWORK_QUANTUM_STORAGE_4.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_10 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_4.getItem(), ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT, NETWORK_QUANTUM_STORAGE_4.getItem(),
            ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT, ExpansionItemStacks.CARGO_STORAGE_UNIT_9, ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT,
            NETWORK_QUANTUM_STORAGE_4.getItem(), ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT, NETWORK_QUANTUM_STORAGE_4.getItem()
    };

    public static final ItemStack[] CARGO_STORAGE_UNIT_11 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_5.getItem(), ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT, NETWORK_QUANTUM_STORAGE_5.getItem(),
            ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT, ExpansionItemStacks.CARGO_STORAGE_UNIT_10, ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT,
            NETWORK_QUANTUM_STORAGE_5.getItem(), ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT, NETWORK_QUANTUM_STORAGE_5.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_12 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_6.getItem(), NETWORK_QUANTUM_STORAGE_8.getItem(), NETWORK_QUANTUM_STORAGE_6.getItem(),
            NETWORK_QUANTUM_STORAGE_8.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_11, NETWORK_QUANTUM_STORAGE_8.getItem(),
            NETWORK_QUANTUM_STORAGE_6.getItem(), NETWORK_QUANTUM_STORAGE_8.getItem(), NETWORK_QUANTUM_STORAGE_6.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_13 = new ItemStack[]{
            NETWORK_QUANTUM_STORAGE_7.getItem(), NETWORK_QUANTUM_STORAGE_8.getItem(), NETWORK_QUANTUM_STORAGE_7.getItem(),
            NETWORK_QUANTUM_STORAGE_8.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_12, NETWORK_QUANTUM_STORAGE_8.getItem(),
            NETWORK_QUANTUM_STORAGE_7.getItem(), NETWORK_QUANTUM_STORAGE_8.getItem(), NETWORK_QUANTUM_STORAGE_7.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_1_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_1, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_2_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_2, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_3_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_3, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_4_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_4, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_5_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_5, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_6_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_6, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_7_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_7, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_8_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_8, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_9_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_9, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_10_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_10, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_11_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_11, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_12_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_12, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
    public static final ItemStack[] CARGO_STORAGE_UNIT_13_MODEL = new ItemStack[]{
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), ExpansionItemStacks.CARGO_STORAGE_UNIT_13, OPTIC_CABLE.getItem(),
            OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem(), OPTIC_CABLE.getItem()
    };
}
