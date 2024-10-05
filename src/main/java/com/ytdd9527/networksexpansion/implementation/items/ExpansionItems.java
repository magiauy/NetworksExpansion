package com.ytdd9527.networksexpansion.implementation.items;

import com.ytdd9527.networksexpansion.api.enums.StorageUnitType;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.core.items.unusable.AuthorHead;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.AncientAltarBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.ArmorForgeBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.CompressorBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.ExpansionWorkbenchBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.GrindStoneBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.JuicerBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.MagicWorkbenchBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.OreCrusherBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.PressureChamberBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.QuantumWorkbenchBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.SmelteryBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoAncientAltar;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoArmorForge;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoCompressor;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoCraftingTable;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoExpansionWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoGrindStone;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoJuicer;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoMagicWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoOreCrusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoPressureChamber;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoQuantumWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoSmeltery;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoAncientAltar;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoArmorForge;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoCompressor;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoExpansionWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoGrindStone;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoJuicer;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoMagicWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoOreCrusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoPressureChamber;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoQuantumWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoSmeltery;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.power.power_outlet.line.LinePowerOutlet;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.advanced.AdvancedLineTransfer;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.advanced.AdvancedLineTransferGrabber;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.advanced.AdvancedLineTransferPusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.basic.LineTransfer;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.basic.LineTransferGrabber;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.basic.LineTransferPusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.basic.LineTransferVanillaGrabber;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.line.basic.LineTransferVanillaPusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.advanced.AdvancedTransfer;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.advanced.AdvancedTransferGrabber;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.advanced.AdvancedTransferPusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.basic.Transfer;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.basic.TransferGrabber;
import com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.basic.TransferPusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.AncientAltarEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.ArmorForgeEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.CompressorEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.ExpansionWorkbenchEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.GrindStoneEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.JuicerEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.MagicWorkbenchEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.OreCrusherEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.PressureChamberEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.QuantumWorkbenchEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.SmelteryEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.manual.Expansion6x6Workbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.manual.ExpansionWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.manual.StorageUnitUpgradeTable;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedExport;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedGreedyBlock;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedImport;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedPurger;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.DueMachine;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkBlueprintDecoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkGridNewStyle;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkInputOnlyMonitor;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkOutputOnlyMonitor;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.Offsetter;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.SmartGrabber;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.SmartPusher;
import com.ytdd9527.networksexpansion.implementation.items.machines.unit.CargoStorageUnit;
import com.ytdd9527.networksexpansion.implementation.items.tools.CargoNodeQuickTool;
import com.ytdd9527.networksexpansion.implementation.items.tools.ItemMover;
import com.ytdd9527.networksexpansion.implementation.items.tools.NetworksExpansionWorldEditAxe;
import com.ytdd9527.networksexpansion.implementation.items.tools.NetworksInfoTool;
import com.ytdd9527.networksexpansion.implementation.menus.ExpansionItemsMenus;
import com.ytdd9527.networksexpansion.implementation.recipes.ExpansionRecipes;
import io.github.sefiraat.networks.slimefun.network.NetworkBridge;
import io.github.sefiraat.networks.slimefun.network.NetworkPowerNode;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;


public class ExpansionItems {
    public static final ExpansionWorkbench NETWORKS_EXPANSION_WORKBENCH = new ExpansionWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORKS_EXPANSION_WORKBENCH,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            ExpansionRecipes.NE_EXPANSION_WORKBENCH
    );

    public static final Expansion6x6Workbench NETWORKS_EXPANSION_6X6_WORKBENCH = new Expansion6x6Workbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORKS_EXPANSION_WORKBENCH_6X6,
            RecipeType.NULL,
            ExpansionRecipes.NULL
    );

    public static final AdvancedImport ADVANCED_IMPORT = new AdvancedImport(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_IMPORT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_IMPORT
    );

    public static final AdvancedExport ADVANCED_EXPORT = new AdvancedExport(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_EXPORT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_EXPORT
    );

    public static final AdvancedPurger ADVANCED_PURGER = new AdvancedPurger(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_PURGER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_PURGER
    );

    public static final AdvancedGreedyBlock ADVANCED_GREEDY_BLOCK = new AdvancedGreedyBlock(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_GREEDY_BLOCK,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_GREEDY_BLOCK
    );

    public static final NetworkPowerNode NETWORK_CAPACITOR_5 = new NetworkPowerNode(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_CAPACITOR_5,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_CAPACITOR_5,
            100000000
    );

    public static final NetworkPowerNode NETWORK_CAPACITOR_6 = new NetworkPowerNode(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_CAPACITOR_6,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_CAPACITOR_6,
            Integer.MAX_VALUE
    );

    public static final NetworkInputOnlyMonitor NETWORK_INPUT_ONLY_MONITOR = new NetworkInputOnlyMonitor(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_INPUT_ONLY_MONITOR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_INPUT_ONLY_MONITOR
    );

    public static final NetworkOutputOnlyMonitor NETWORK_OUTPUT_ONLY_MONITOR = new NetworkOutputOnlyMonitor(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_OUTPUT_ONLY_MONITOR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_OUTPUT_ONLY_MONITOR
    );

    public static final NetworkQuantumStorage ADVANCED_QUANTUM_STORAGE = new NetworkQuantumStorage(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_QUANTUM_STORAGE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_QUANTUM_STORAGE,
            NetworkQuantumStorage.getSizes()[10]
    );

    public static final NetworkGridNewStyle NETWORK_GRID_NEW_STYLE = new NetworkGridNewStyle(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_GRID_NEW_STYLE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_GRID_NEW_STYLE
    );

    // Blueprints
    public static final MagicWorkbenchBlueprint MAGIC_WORKBENCH_BLUEPRINT = new MagicWorkbenchBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.MAGIC_WORKBENCH_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.MAGIC_WORKBENCH_BLUEPRINT
    );

    public static final ArmorForgeBlueprint ARMOR_FORGE_BLUEPRINT = new ArmorForgeBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.ARMOR_FORGE_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ARMOR_FORGE_BLUEPRINT
    );

    public static final SmelteryBlueprint SMELTERY_BLUEPRINT = new SmelteryBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.SMELTERY_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.SMELTERY_BLUEPRINT
    );

    public static final QuantumWorkbenchBlueprint QUANTUM_WORKBENCH_BLUEPRINT = new QuantumWorkbenchBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.QUANTUM_WORKBENCH_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.QUANTUM_WORKBENCH_BLUEPRINT
    );

    public static final AncientAltarBlueprint ANCIENT_ALTAR_BLUEPRINT = new AncientAltarBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.ANCIENT_ALTAR_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ANCIENT_ALTAR_BLUEPRINT
    );

    public static final ExpansionWorkbenchBlueprint EXPANSION_WORKBENCH_BLUEPRINT = new ExpansionWorkbenchBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.EXPANSION_WORKBENCH_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.EXPANSION_WORKBENCH_BLUEPRINT
    );

    public static final CompressorBlueprint COMPRESSOR_BLUEPRINT = new CompressorBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.COMPRESSOR_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.COMPRESSOR_BLUEPRINT
    );

    public static final GrindStoneBlueprint GRIND_STONE_BLUEPRINT = new GrindStoneBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.GRIND_STONE_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.GRIND_STONE_BLUEPRINT
    );

    public static final JuicerBlueprint JUICER_BLUEPRINT = new JuicerBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.JUICER_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.JUICER_BLUEPRINT
    );

    public static final OreCrusherBlueprint ORE_CRUSHER_BLUEPRINT = new OreCrusherBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.ORE_CRUSHER_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ORE_CRUSHER_BLUEPRINT
    );

    public static final PressureChamberBlueprint PRESSURE_CHAMBER_BLUEPRINT = new PressureChamberBlueprint(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.PRESSURE_CHAMBER_BLUEPRINT,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.PRESSURE_CHAMBER_BLUEPRINT
    );

    // Encoders
    public static final MagicWorkbenchEncoder MAGIC_WORKBENCH_RECIPE_ENCODER = new MagicWorkbenchEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.MAGIC_WORKBENCH_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.MAGIC_WORKBENCH_RECIPE_ENCODER
    );

    public static final ArmorForgeEncoder ARMOR_FORGE_RECIPE_ENCODER = new ArmorForgeEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ARMOR_FORGE_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ARMOR_FORGE_RECIPE_ENCODER
    );

    public static final SmelteryEncoder SMELTERY_RECIPE_ENCODER = new SmelteryEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.SMELTERY_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.SMELTERY_RECIPE_ENCODER
    );

    public static final QuantumWorkbenchEncoder QUANTUM_WORKBENCH_RECIPE_ENCODER = new QuantumWorkbenchEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.QUANTUM_WORKBENCH_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.QUANTUM_WORKBENCH_RECIPE_ENCODER
    );

    public static final AncientAltarEncoder ANCIENT_ALTAR_RECIPE_ENCODER = new AncientAltarEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ANCIENT_ALTAR_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ANCIENT_ALTAR_RECIPE_ENCODER
    );

    public static final ExpansionWorkbenchEncoder EXPANSION_WORKBENCH_RECIPE_ENCODER = new ExpansionWorkbenchEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.EXPANSION_WORKBENCH_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.EXPANSION_WORKBENCH_RECIPE_ENCODER
    );

    public static final CompressorEncoder COMPRESSOR_RECIPE_ENCODER = new CompressorEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.COMPRESSOR_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.COMPRESSOR_RECIPE_ENCODER
    );

    public static final GrindStoneEncoder GRIND_STONE_RECIPE_ENCODER = new GrindStoneEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.GRIND_STONE_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.GRIND_STONE_RECIPE_ENCODER
    );

    public static final JuicerEncoder JUICER_RECIPE_ENCODER = new JuicerEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.JUICER_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.JUICER_RECIPE_ENCODER
    );

    public static final OreCrusherEncoder ORE_CRUSHER_RECIPE_ENCODER = new OreCrusherEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ORE_CRUSHER_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ORE_CRUSHER_RECIPE_ENCODER
    );

    public static final PressureChamberEncoder PRESSURE_CHAMBER_RECIPE_ENCODER = new PressureChamberEncoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.PRESSURE_CHAMBER_RECIPE_ENCODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.PRESSURE_CHAMBER_RECIPE_ENCODER
    );
    // Auto Crafters
    public static final AutoMagicWorkbench AUTO_MAGIC_WORKBENCH = new AutoMagicWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_MAGIC_WORKBENCH,
            64,
            false
    );

    public static final AutoMagicWorkbench AUTO_MAGIC_WORKBENCH_WITHHOLDING = new AutoMagicWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            128,
            true
    );

    public static final AutoArmorForge AUTO_ARMOR_FORGE = new AutoArmorForge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ARMOR_FORGE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ARMOR_FORGE,
            64,
            false
    );

    public static final AutoArmorForge AUTO_ARMOR_FORGE_WITHHOLDING = new AutoArmorForge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ARMOR_FORGE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ARMOR_FORGE_WITHHOLDING,
            128,
            true
    );

    public static final AutoSmeltery AUTO_SMELTERY = new AutoSmeltery(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_SMELTERY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_SMELTERY,
            64,
            false
    );

    public static final AutoSmeltery AUTO_SMELTERY_WITHHOLDING = new AutoSmeltery(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_SMELTERY_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_SMELTERY_WITHHOLDING,
            128,
            true
    );

    public static final AutoQuantumWorkbench AUTO_QUANTUM_WORKBENCH = new AutoQuantumWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_QUANTUM_WORKBENCH,
            64,
            false
    );

    public static final AutoQuantumWorkbench AUTO_QUANTUM_WORKBENCH_WITHHOLDING = new AutoQuantumWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            128,
            true
    );

    public static final AutoAncientAltar AUTO_ANCIENT_ALTAR = new AutoAncientAltar(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ANCIENT_ALTAR,
            64,
            false
    );

    public static final AutoAncientAltar AUTO_ANCIENT_ALTAR_WITHHOLDING = new AutoAncientAltar(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ANCIENT_ALTAR_WITHHOLDING,
            128,
            true
    );

    public static final AutoExpansionWorkbench AUTO_EXPANSION_WORKBENCH = new AutoExpansionWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_EXPANSION_WORKBENCH,
            64,
            false
    );

    public static final AutoExpansionWorkbench AUTO_EXPANSION_WORKBENCH_WITHHOLDING = new AutoExpansionWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            128,
            true
    );

    public static final AutoCompressor AUTO_COMPRESSOR = new AutoCompressor(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_COMPRESSOR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_COMPRESSOR,
            64,
            false
    );

    public static final AutoCompressor AUTO_COMPRESSOR_WITHHOLDING = new AutoCompressor(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_COMPRESSOR_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_COMPRESSOR_WITHHOLDING,
            128,
            true
    );

    public static final AutoGrindStone AUTO_GRIND_STONE = new AutoGrindStone(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_GRIND_STONE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_GRIND_STONE,
            64,
            false
    );

    public static final AutoGrindStone AUTO_GRIND_STONE_WITHHOLDING = new AutoGrindStone(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_GRIND_STONE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_GRIND_STONE_WITHHOLDING,
            128,
            true
    );

    public static final AutoJuicer AUTO_JUICER = new AutoJuicer(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_JUICER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_JUICER,
            64,
            false
    );

    public static final AutoJuicer AUTO_JUICER_WITHHOLDING = new AutoJuicer(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_JUICER_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_JUICER_WITHHOLDING,
            128,
            true
    );

    public static final AutoOreCrusher AUTO_ORE_CRUSHER = new AutoOreCrusher(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ORE_CRUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ORE_CRUSHER,
            64,
            false
    );

    public static final AutoOreCrusher AUTO_ORE_CRUSHER_WITHHOLDING = new AutoOreCrusher(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ORE_CRUSHER_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ORE_CRUSHER_WITHHOLDING,
            128,
            true
    );

    public static final AutoPressureChamber AUTO_PRESSURE_CHAMBER = new AutoPressureChamber(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_PRESSURE_CHAMBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_PRESSURE_CHAMBER,
            64,
            false
    );

    public static final AutoPressureChamber AUTO_PRESSURE_CHAMBER_WITHHOLDING = new AutoPressureChamber(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_PRESSURE_CHAMBER_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_PRESSURE_CHAMBER_WITHHOLDING,
            128,
            true
    );

    // Advanced Auto Crafters
    public static final AdvancedAutoMagicWorkbench ADVANCED_AUTO_MAGIC_WORKBENCH = new AdvancedAutoMagicWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_MAGIC_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_MAGIC_WORKBENCH,
            640,
            false
    );

    public static final AdvancedAutoMagicWorkbench ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING = new AdvancedAutoMagicWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoArmorForge ADVANCED_AUTO_ARMOR_FORGE = new AdvancedAutoArmorForge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ARMOR_FORGE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ARMOR_FORGE,
            640,
            false
    );

    public static final AdvancedAutoArmorForge ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING = new AdvancedAutoArmorForge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoSmeltery ADVANCED_AUTO_SMELTERY = new AdvancedAutoSmeltery(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_SMELTERY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_SMELTERY,
            640,
            false
    );

    public static final AdvancedAutoSmeltery ADVANCED_AUTO_SMELTERY_WITHHOLDING = new AdvancedAutoSmeltery(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_SMELTERY_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_SMELTERY_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoQuantumWorkbench ADVANCED_AUTO_QUANTUM_WORKBENCH = new AdvancedAutoQuantumWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_QUANTUM_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_QUANTUM_WORKBENCH,
            640,
            false
    );

    public static final AdvancedAutoQuantumWorkbench ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING = new AdvancedAutoQuantumWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoAncientAltar ADVANCED_AUTO_ANCIENT_ALTAR = new AdvancedAutoAncientAltar(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ANCIENT_ALTAR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ANCIENT_ALTAR,
            640,
            false
    );

    public static final AdvancedAutoAncientAltar ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING = new AdvancedAutoAncientAltar(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoExpansionWorkbench ADVANCED_AUTO_EXPANSION_WORKBENCH = new AdvancedAutoExpansionWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_EXPANSION_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_EXPANSION_WORKBENCH,
            640,
            false
    );

    public static final AdvancedAutoExpansionWorkbench ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING = new AdvancedAutoExpansionWorkbench(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoCraftingTable ADVANCED_AUTO_CRAFTING_TABLE = new AdvancedAutoCraftingTable(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_CRAFTING_TABLE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_CRAFTING_TABLE,
            640,
            false
    );

    public static final AdvancedAutoCraftingTable ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING = new AdvancedAutoCraftingTable(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoCompressor ADVANCED_AUTO_COMPRESSOR = new AdvancedAutoCompressor(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_COMPRESSOR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_COMPRESSOR,
            640,
            false
    );

    public static final AdvancedAutoCompressor ADVANCED_AUTO_COMPRESSOR_WITHHOLDING = new AdvancedAutoCompressor(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_COMPRESSOR_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_COMPRESSOR_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoGrindStone ADVANCED_AUTO_GRIND_STONE = new AdvancedAutoGrindStone(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_GRIND_STONE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_GRIND_STONE,
            640,
            false
    );

    public static final AdvancedAutoGrindStone ADVANCED_AUTO_GRIND_STONE_WITHHOLDING = new AdvancedAutoGrindStone(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_GRIND_STONE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_GRIND_STONE_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoJuicer ADVANCED_AUTO_JUICER = new AdvancedAutoJuicer(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_JUICER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_JUICER,
            640,
            false
    );

    public static final AdvancedAutoJuicer ADVANCED_AUTO_JUICER_WITHHOLDING = new AdvancedAutoJuicer(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_JUICER_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_JUICER_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoOreCrusher ADVANCED_AUTO_ORE_CRUSHER = new AdvancedAutoOreCrusher(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ORE_CRUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ORE_CRUSHER,
            640,
            false
    );

    public static final AdvancedAutoOreCrusher ADVANCED_AUTO_ORE_CRUSHER_WITHHOLDING = new AdvancedAutoOreCrusher(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ORE_CRUSHER_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ORE_CRUSHER_WITHHOLDING,
            1280,
            true
    );

    public static final AdvancedAutoPressureChamber ADVANCED_AUTO_PRESSURE_CHAMBER = new AdvancedAutoPressureChamber(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_PRESSURE_CHAMBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_PRESSURE_CHAMBER,
            640,
            false
    );

    public static final AdvancedAutoPressureChamber ADVANCED_AUTO_PRESSURE_CHAMBER_WITHHOLDING = new AdvancedAutoPressureChamber(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_PRESSURE_CHAMBER_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_PRESSURE_CHAMBER_WITHHOLDING,
            1280,
            true
    );

    // Transfer
    public static final LineTransferPusher LINE_TRANSFER_PUSHER = new LineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PUSHER,
            "NTW_EXPANSION_LINE_TRANSFER_PUSHER"
    );

    public static final LineTransferGrabber LINE_TRANSFER_GRABBER = new LineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_GRABBER,
            "NTW_EXPANSION_LINE_TRANSFER_GRABBER"
    );
    public static final LineTransfer LINE_TRANSFER = new LineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER,
            "NTW_EXPANSION_LINE_TRANSFER"
    );

    public static final LineTransferPusher LINE_TRANSFER_PLUS_PUSHER = new LineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PLUS_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PLUS_PUSHER,
            "NTW_EXPANSION_LINE_TRANSFER_PLUS_PUSHER"
    );
    public static final LineTransferGrabber LINE_TRANSFER_PLUS_GRABBER = new LineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PLUS_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PLUS_GRABBER,
            "NTW_EXPANSION_LINE_TRANSFER_PLUS_GRABBER"
    );
    public static final LineTransfer LINE_TRANSFER_PLUS = new LineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PLUS,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PLUS,
            "NTW_EXPANSION_LINE_TRANSFER_PLUS"
    );

    public static final LineTransferVanillaPusher LINE_TRANSFER_VANILLA_PUSHER = new LineTransferVanillaPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_VANILLA_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_VANILLA_PUSHER,
            "NTW_EXPANSION_LINE_TRANSFER_VANILLA_PUSHER"
    );

    public static final LineTransferVanillaGrabber LINE_TRANSFER_VANILLA_GRABBER = new LineTransferVanillaGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_VANILLA_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_VANILLA_GRABBER,
            "NTW_EXPANSION_LINE_TRANSFER_VANILLA_GRABBER"
    );

    public static final AdvancedLineTransferPusher ADVANCED_LINE_TRANSFER_PUSHER = new AdvancedLineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PUSHER,
            "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PUSHER"
    );

    public static final AdvancedLineTransferGrabber ADVANCED_LINE_TRANSFER_GRABBER = new AdvancedLineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_GRABBER,
            "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_GRABBER"
    );
    public static final AdvancedLineTransfer ADVANCED_LINE_TRANSFER = new AdvancedLineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER,
            "NTW_EXPANSION_ADVANCED_LINE_TRANSFER"
    );

    public static final AdvancedLineTransferPusher ADVANCED_LINE_TRANSFER_PLUS_PUSHER = new AdvancedLineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PLUS_PUSHER,
            "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS_PUSHER"
    );
    public static final AdvancedLineTransferGrabber ADVANCED_LINE_TRANSFER_PLUS_GRABBER = new AdvancedLineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PLUS_GRABBER,
            "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS_GRABBER"
    );
    public static final AdvancedLineTransfer ADVANCED_LINE_TRANSFER_PLUS = new AdvancedLineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PLUS,
            "NTW_EXPANSION_ADVANCED_LINE_TRANSFER_PLUS"
    );

    public static final CargoNodeQuickTool CARGO_NODE_QUICK_TOOL = new CargoNodeQuickTool(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.CARGO_NODE_QUICK_TOOL,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            ExpansionRecipes.CARGO_NODE_QUICK_TOOL
    );

    public static final NetworksExpansionWorldEditAxe WORLD_EDIT_AXE = new NetworksExpansionWorldEditAxe(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.WORLD_EDIT_AXE
    );

    public static final NetworksInfoTool INFO_TOOL = new NetworksInfoTool(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.INFO_TOOL
    );

    public static final StorageUnitUpgradeTable STORAGE_UNIT_UPGRADE_TABLE = new StorageUnitUpgradeTable(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.STORAGE_UNIT_UPGRADE_TABLE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.STORAGE_UNIT_UPGRADE_TABLE
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_1 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_1,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_1,
            StorageUnitType.TINY
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_2 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_2,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_2,
            StorageUnitType.MINI
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_3 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_3,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_3,
            StorageUnitType.SMALL
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_4 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_4,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_4,
            StorageUnitType.MEDIUM
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_5 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_5,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_5,
            StorageUnitType.LARGE
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_6 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_6,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_6,
            StorageUnitType.ENHANCED
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_7 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_7,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_7,
            StorageUnitType.ADVANCED
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_8 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_8,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_8,
            StorageUnitType.EXTRA
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_9 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_9,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_9,
            StorageUnitType.ULTRA
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_10 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_10,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_10,
            StorageUnitType.END_GAME_BASIC
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_11 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_11,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_11,
            StorageUnitType.END_GAME_INTERMEDIATE
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_12 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_12,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_12,
            StorageUnitType.END_GAME_ADVANCED
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_13 = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_13,
            StorageUnitUpgradeTable.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_13,
            StorageUnitType.END_GAME_MAX
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_1_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_1_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_1_MODEL,
            StorageUnitType.TINY,
            "CARGO_STORAGE_UNIT_1_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_2_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_2_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_2_MODEL,
            StorageUnitType.MINI,
            "CARGO_STORAGE_UNIT_2_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_3_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_3_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_3_MODEL,
            StorageUnitType.SMALL,
            "CARGO_STORAGE_UNIT_3_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_4_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_4_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_4_MODEL,
            StorageUnitType.MEDIUM,
            "CARGO_STORAGE_UNIT_4_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_5_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_5_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_5_MODEL,
            StorageUnitType.LARGE,
            "CARGO_STORAGE_UNIT_5_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_6_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_6_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_6_MODEL,
            StorageUnitType.ENHANCED,
            "CARGO_STORAGE_UNIT_6_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_7_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_7_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_7_MODEL,
            StorageUnitType.ADVANCED,
            "CARGO_STORAGE_UNIT_7_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_8_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_8_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_8_MODEL,
            StorageUnitType.EXTRA,
            "CARGO_STORAGE_UNIT_8_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_9_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_9_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_9_MODEL,
            StorageUnitType.ULTRA,
            "CARGO_STORAGE_UNIT_9_MODEL"
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_10_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_10_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_10_MODEL,
            StorageUnitType.END_GAME_BASIC,
            "CARGO_STORAGE_UNIT_10_MODEL"
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_11_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_11_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_11_MODEL,
            StorageUnitType.END_GAME_INTERMEDIATE,
            "CARGO_STORAGE_UNIT_11_MODEL"
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_12_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_12_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_12_MODEL,
            StorageUnitType.END_GAME_ADVANCED,
            "CARGO_STORAGE_UNIT_12_MODEL"
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_13_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_13_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_13_MODEL,
            StorageUnitType.END_GAME_MAX,
            "CARGO_STORAGE_UNIT_13_MODEL"
    );

    // Bridges
    public static final NetworkBridge NETWORK_BRIDGE_WHITE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_WHITE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_WHITE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_WHITE, 8),
            "NETWORK_BRIDGE_WHITE"
    );
    public static final NetworkBridge NETWORK_BRIDGE_LIGHT_GRAY = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_GRAY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_LIGHT_GRAY,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_GRAY, 8),
            "NETWORK_BRIDGE_LIGHT_GRAY"
    );
    public static final NetworkBridge NETWORK_BRIDGE_GRAY = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_GRAY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_GRAY,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_GRAY, 8),
            "NETWORK_BRIDGE_GRAY"
    );
    public static final NetworkBridge NETWORK_BRIDGE_BLACK = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_BLACK,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_BLACK,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_BLACK, 8),
            "NETWORK_BRIDGE_BLACK"
    );
    public static final NetworkBridge NETWORK_BRIDGE_BROWN = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_BROWN,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_BROWN,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_BROWN, 8),
            "NETWORK_BRIDGE_BROWN"
    );
    public static final NetworkBridge NETWORK_BRIDGE_RED = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_RED,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_RED,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_RED, 8),
            "NETWORK_BRIDGE_RED"
    );
    public static final NetworkBridge NETWORK_BRIDGE_ORANGE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_ORANGE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_ORANGE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_ORANGE, 8),
            "NETWORK_BRIDGE_ORANGE"
    );
    public static final NetworkBridge NETWORK_BRIDGE_YELLOW = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_YELLOW,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_YELLOW,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_YELLOW, 8),
            "NETWORK_BRIDGE_YELLOW"
    );
    public static final NetworkBridge NETWORK_BRIDGE_LIME = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_LIME,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_LIME,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_LIME, 8),
            "NETWORK_BRIDGE_LIME"
    );
    public static final NetworkBridge NETWORK_BRIDGE_GREEN = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_GREEN,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_GREEN,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_GREEN, 8),
            "NETWORK_BRIDGE_GREEN"
    );
    public static final NetworkBridge NETWORK_BRIDGE_CYAN = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_CYAN,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_CYAN,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_CYAN, 8),
            "NETWORK_BRIDGE_CYAN"
    );
    public static final NetworkBridge NETWORK_BRIDGE_LIGHT_BLUE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_BLUE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_LIGHT_BLUE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_BLUE, 8),
            "NETWORK_BRIDGE_LIGHT_BLUE"
    );
    public static final NetworkBridge NETWORK_BRIDGE_BLUE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_BLUE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_BLUE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_BLUE, 8),
            "NETWORK_BRIDGE_BLUE"
    );
    public static final NetworkBridge NETWORK_BRIDGE_PURPLE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_PURPLE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_PURPLE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_PURPLE, 8),
            "NETWORK_BRIDGE_PURPLE"
    );
    public static final NetworkBridge NETWORK_BRIDGE_MAGENTA = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_MAGENTA,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_MAGENTA,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_MAGENTA, 8),
            "NETWORK_BRIDGE_MAGENTA"
    );
    public static final NetworkBridge NETWORK_BRIDGE_PINK = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_PINK,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_PINK,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_PINK, 8),
            "NETWORK_BRIDGE_PINK"
    );

    public static final SpecialSlimefunItem AUTHOR_SEFIRAAT = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_SEFIRAAT,
            RecipeType.NULL,
            ExpansionRecipes.NULL
    );

    public static final SpecialSlimefunItem AUTHOR_YBW0014 = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_YBW0014,
            RecipeType.NULL,
            ExpansionRecipes.NULL
    );

    public static final SpecialSlimefunItem AUTHOR_YITOUDAIDAI = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_YITOUDAIDAI,
            RecipeType.NULL,
            ExpansionRecipes.NULL
    );

    public static final SpecialSlimefunItem AUTHOR_TINALNESS = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_TINALNESS,
            RecipeType.NULL,
            ExpansionRecipes.NULL
    );
    public static final SpecialSlimefunItem MODIFIER_MAGIAUY = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.MODIFIER_MAGIAUY,
            RecipeType.NULL,
            ExpansionRecipes.NULL
    );
    public static final SpecialSlimefunItem TRANSLATOR_SKYFATORI = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.TRANSLATOR_SKYFATORI,
            RecipeType.NULL,
            ExpansionRecipes.NULL
    );
    public static final SpecialSlimefunItem NETWORKS_EXPANSION_SURVIVAL_GUIDE = new NetworksExpansionGuide(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.NETWORKS_EXPANSION_SURVIVAL_GUIDE
    );

    public static final SpecialSlimefunItem NETWORKS_EXPANSION_CHEAT_GUIDE = new NetworksExpansionGuide(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.NETWORKS_EXPANSION_CHEAT_GUIDE
    );

    public static final SpecialSlimefunItem ITEM_MOVER = new ItemMover(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.ITEM_MOVER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ITEM_MOVER
    );

    public static final NetworkBlueprintDecoder NETWORK_BLUEPRINT_DECODER = new NetworkBlueprintDecoder(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BLUEPRINT_DECODER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BLUEPRINT_DECODER
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_1 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_1,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_1
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_2 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_2,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_2
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_3 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_3,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_3
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_4 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_4,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_4
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_5 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_5,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_5
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_6 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_6,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_6
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_7 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_7,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_7
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_8 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_8,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_8
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_9 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_9,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_9
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_10 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_10,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_10
    );

    public static final LinePowerOutlet LINE_POWER_OUTLET_11 = new LinePowerOutlet(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.LINE_POWER_OUTLET_11,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_POWER_OUTLET_11
    );

    public static final DueMachine DUE_MACHINE = new DueMachine(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.DUE_MACHINE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.DUE_MACHINE
    );

    public static final Offsetter OFFSETTER = new Offsetter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.OFFSETTER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.OFFSETTER
    );
}
