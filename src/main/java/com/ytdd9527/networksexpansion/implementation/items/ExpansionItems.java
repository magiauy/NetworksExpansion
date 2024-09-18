package com.ytdd9527.networksexpansion.implementation.items;

import com.ytdd9527.networksexpansion.api.enums.StorageUnitType;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.core.items.unusable.AuthorHead;
import com.ytdd9527.networksexpansion.core.items.unusable.NetworksExpansionGuide;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.AncientAltarBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.ArmorForgeBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.ExpansionWorkbenchBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.MagicWorkbenchBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.QuantumWorkbenchBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.SmelteryBlueprint;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoAncientAltarCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoArmorForgeCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoCraftingTableCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoExpansionWorkbenchCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoMagicWorkbenchCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoQuantumWorkbenchCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.advanced.AdvancedAutoSmelteryCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoAncientAltarCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoArmorForgeCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoExpansionWorkbenchCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoMagicWorkbenchCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoQuantumWorkbenchCrafter;
import com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic.AutoSmelteryCrafter;
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
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.ExpansionWorkbenchEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.MagicWorkbenchEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.QuantumWorkbenchEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.encoders.SmelteryEncoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.manual.ExpansionWorkbench;
import com.ytdd9527.networksexpansion.implementation.items.machines.manual.StorageUnitUpgradeTable;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedExport;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedGreedyBlock;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedImport;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.AdvancedPurger;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkBlueprintDecoder;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkGridNewStyle;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkInputOnlyMonitor;
import com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced.NetworkOutputOnlyMonitor;
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
            ExpansionRecipes.NETWORKS_EXPANSION_WORKBENCH
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
    // Auto Crafters
    public static final AutoMagicWorkbenchCrafter AUTO_MAGIC_WORKBENCH = new AutoMagicWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_MAGIC_WORKBENCH,
            640,
            false
    );

    public static final AutoMagicWorkbenchCrafter AUTO_MAGIC_WORKBENCH_WITHHOLDING = new AutoMagicWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            1280,
            true
    );

    public static final AutoArmorForgeCrafter AUTO_ARMOR_FORGE = new AutoArmorForgeCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ARMOR_FORGE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ARMOR_FORGE,
            640,
            false
    );

    public static final AutoArmorForgeCrafter AUTO_ARMOR_FORGE_WITHHOLDING = new AutoArmorForgeCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ARMOR_FORGE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ARMOR_FORGE_WITHHOLDING,
            1280,
            true
    );

    public static final AutoSmelteryCrafter AUTO_SMELTERY = new AutoSmelteryCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_SMELTERY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_SMELTERY,
            640,
            false
    );

    public static final AutoSmelteryCrafter AUTO_SMELTERY_WITHHOLDING = new AutoSmelteryCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_SMELTERY_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_SMELTERY_WITHHOLDING,
            1280,
            true
    );

    public static final AutoQuantumWorkbenchCrafter AUTO_QUANTUM_WORKBENCH = new AutoQuantumWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_QUANTUM_WORKBENCH,
            640,
            false
    );

    public static final AutoQuantumWorkbenchCrafter AUTO_QUANTUM_WORKBENCH_WITHHOLDING = new AutoQuantumWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            1280,
            true
    );

    public static final AutoAncientAltarCrafter AUTO_ANCIENT_ALTAR = new AutoAncientAltarCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ANCIENT_ALTAR,
            640,
            false
    );

    public static final AutoAncientAltarCrafter AUTO_ANCIENT_ALTAR_WITHHOLDING = new AutoAncientAltarCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_ANCIENT_ALTAR_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_ANCIENT_ALTAR_WITHHOLDING,
            1280,
            true
    );

    public static final AutoExpansionWorkbenchCrafter AUTO_EXPANSION_WORKBENCH = new AutoExpansionWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_EXPANSION_WORKBENCH,
            640,
            false
    );

    public static final AutoExpansionWorkbenchCrafter AUTO_EXPANSION_WORKBENCH_WITHHOLDING = new AutoExpansionWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            1280,
            true
    );

    // Advanced Auto Crafters
    public static final AdvancedAutoMagicWorkbenchCrafter ADVANCED_AUTO_MAGIC_WORKBENCH = new AdvancedAutoMagicWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_MAGIC_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_MAGIC_WORKBENCH,
            6400,
            false
    );

    public static final AdvancedAutoMagicWorkbenchCrafter ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING = new AdvancedAutoMagicWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING,
            12800,
            true
    );

    public static final AdvancedAutoArmorForgeCrafter ADVANCED_AUTO_ARMOR_FORGE = new AdvancedAutoArmorForgeCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ARMOR_FORGE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ARMOR_FORGE,
            6400,
            false
    );

    public static final AdvancedAutoArmorForgeCrafter ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING = new AdvancedAutoArmorForgeCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING,
            12800,
            true
    );

    public static final AdvancedAutoSmelteryCrafter ADVANCED_AUTO_SMELTERY = new AdvancedAutoSmelteryCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_SMELTERY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_SMELTERY,
            6400,
            false
    );

    public static final AdvancedAutoSmelteryCrafter ADVANCED_AUTO_SMELTERY_WITHHOLDING = new AdvancedAutoSmelteryCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_SMELTERY_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_SMELTERY_WITHHOLDING,
            12800,
            true
    );

    public static final AdvancedAutoQuantumWorkbenchCrafter ADVANCED_AUTO_QUANTUM_WORKBENCH = new AdvancedAutoQuantumWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_QUANTUM_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_QUANTUM_WORKBENCH,
            6400,
            false
    );

    public static final AdvancedAutoQuantumWorkbenchCrafter ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING = new AdvancedAutoQuantumWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING,
            12800,
            true
    );

    public static final AdvancedAutoAncientAltarCrafter ADVANCED_AUTO_ANCIENT_ALTAR = new AdvancedAutoAncientAltarCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ANCIENT_ALTAR,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ANCIENT_ALTAR,
            6400,
            false
    );

    public static final AdvancedAutoAncientAltarCrafter ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING = new AdvancedAutoAncientAltarCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING,
            12800,
            true
    );

    public static final AdvancedAutoExpansionWorkbenchCrafter ADVANCED_AUTO_EXPANSION_WORKBENCH = new AdvancedAutoExpansionWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_EXPANSION_WORKBENCH,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_EXPANSION_WORKBENCH,
            6400,
            false
    );

    public static final AdvancedAutoExpansionWorkbenchCrafter ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING = new AdvancedAutoExpansionWorkbenchCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING,
            12800,
            true
    );

    public static final AdvancedAutoCraftingTableCrafter ADVANCED_AUTO_CRAFTING_TABLE = new AdvancedAutoCraftingTableCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_CRAFTING_TABLE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_CRAFTING_TABLE,
            6400,
            false
    );

    public static final AdvancedAutoCraftingTableCrafter ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING = new AdvancedAutoCraftingTableCrafter(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING,
            12800,
            true
    );
    /*
    public static final PointTransfer POINT_TRANSFER = new PointTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.POINT_TRANSFER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.POINT_TRANSFER
    );
    public static final PointTransferGrabber POINT_TRANSFER_GRABBER = new PointTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.POINT_TRANSFER_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.POINT_TRANSFER_GRABBER
    );
     */
    public static final LineTransferPusher LINE_TRANSFER_PUSHER = new LineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PUSHER
    );

    public static final LineTransferGrabber LINE_TRANSFER_GRABBER = new LineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_GRABBER
    );
    public static final LineTransfer LINE_TRANSFER = new LineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER
    );

    public static final LineTransferPusher LINE_TRANSFER_PLUS_PUSHER = new LineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PLUS_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PLUS_PUSHER
    );
    public static final LineTransferGrabber LINE_TRANSFER_PLUS_GRABBER = new LineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PLUS_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PLUS_GRABBER
    );
    public static final LineTransfer LINE_TRANSFER_PLUS = new LineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_PLUS,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_PLUS
    );

    public static final LineTransferVanillaPusher LINE_TRANSFER_VANILLA_PUSHER = new LineTransferVanillaPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_VANILLA_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_VANILLA_PUSHER
    );

    public static final LineTransferVanillaGrabber LINE_TRANSFER_VANILLA_GRABBER = new LineTransferVanillaGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.LINE_TRANSFER_VANILLA_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.LINE_TRANSFER_VANILLA_GRABBER
    );

    public static final AdvancedLineTransferPusher ADVANCED_LINE_TRANSFER_PUSHER = new AdvancedLineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PUSHER
    );

    public static final AdvancedLineTransferGrabber ADVANCED_LINE_TRANSFER_GRABBER = new AdvancedLineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_GRABBER
    );
    public static final AdvancedLineTransfer ADVANCED_LINE_TRANSFER = new AdvancedLineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER
    );

    public static final AdvancedLineTransferPusher ADVANCED_LINE_TRANSFER_PLUS_PUSHER = new AdvancedLineTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PLUS_PUSHER
    );
    public static final AdvancedLineTransferGrabber ADVANCED_LINE_TRANSFER_PLUS_GRABBER = new AdvancedLineTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PLUS_GRABBER
    );
    public static final AdvancedLineTransfer ADVANCED_LINE_TRANSFER_PLUS = new AdvancedLineTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_LINE_TRANSFER_PLUS,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_LINE_TRANSFER_PLUS
    );

    public static final TransferPusher TRANSFER_PUSHER = new TransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.TRANSFER_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.TRANSFER_PUSHER
    );

    public static final TransferGrabber TRANSFER_GRABBER = new TransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.TRANSFER_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.TRANSFER_GRABBER
    );

    public static final Transfer TRANSFER = new Transfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.TRANSFER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.TRANSFER
    );

    public static final AdvancedTransferPusher ADVANCED_TRANSFER_PUSHER = new AdvancedTransferPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_TRANSFER_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_TRANSFER_PUSHER
    );

    public static final AdvancedTransferGrabber ADVANCED_TRANSFER_GRABBER = new AdvancedTransferGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_TRANSFER_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_TRANSFER_GRABBER
    );

    public static final AdvancedTransfer ADVANCED_TRANSFER = new AdvancedTransfer(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.ADVANCED_TRANSFER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.ADVANCED_TRANSFER
    );

    public static final SmartGrabber SMART_GRABBER = new SmartGrabber(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.SMART_GRABBER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.SMART_GRABBER
    );

    public static final SmartPusher SMART_PUSHER = new SmartPusher(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.SMART_PUSHER,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.SMART_PUSHER
    );

    public static final CargoNodeQuickTool CARGO_NODE_QUICK_TOOL = new CargoNodeQuickTool(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.CARGO_NODE_QUICK_TOOL,
            RecipeType.ENHANCED_CRAFTING_TABLE,
            ExpansionRecipes.CARGO_NODE_QUICK_TOOL
    );

    public static final NetworksExpansionWorldEditAxe WORLDEDIT_AXE = new NetworksExpansionWorldEditAxe(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.WORLDEDIT_AXE
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
            StorageUnitType.TINY
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_2_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_2_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_2_MODEL,
            StorageUnitType.MINI
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_3_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_3_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_3_MODEL,
            StorageUnitType.SMALL
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_4_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_4_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_4_MODEL,
            StorageUnitType.MEDIUM
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_5_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_5_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_5_MODEL,
            StorageUnitType.LARGE
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_6_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_6_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_6_MODEL,
            StorageUnitType.ENHANCED
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_7_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_7_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_7_MODEL,
            StorageUnitType.ADVANCED
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_8_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_8_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_8_MODEL,
            StorageUnitType.EXTRA
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_9_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_9_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_9_MODEL,
            StorageUnitType.ULTRA
    );

    public static final CargoStorageUnit CARGO_STORAGE_UNIT_10_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_10_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_10_MODEL,
            StorageUnitType.END_GAME_BASIC
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_11_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_11_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_11_MODEL,
            StorageUnitType.END_GAME_INTERMEDIATE
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_12_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_12_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_12_MODEL,
            StorageUnitType.END_GAME_ADVANCED
    );
    public static final CargoStorageUnit CARGO_STORAGE_UNIT_13_MODEL = new CargoStorageUnit(
            ExpansionItemsMenus.MENU_CARGO_SYSTEM,
            ExpansionItemStacks.CARGO_STORAGE_UNIT_13_MODEL,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.CARGO_STORAGE_UNIT_13_MODEL,
            StorageUnitType.END_GAME_MAX
    );

    // Bridges
    public static final NetworkBridge NETWORK_BRIDGE_WHITE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_WHITE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_WHITE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_WHITE, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_LIGHT_GRAY = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_GRAY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_LIGHT_GRAY,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_GRAY, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_GRAY = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_GRAY,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_GRAY,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_GRAY, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_BLACK = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_BLACK,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_BLACK,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_BLACK, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_BROWN = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_BROWN,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_BROWN,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_BROWN, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_RED = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_RED,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_RED,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_RED, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_ORANGE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_ORANGE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_ORANGE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_ORANGE, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_YELLOW = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_YELLOW,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_YELLOW,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_YELLOW, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_LIME = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_LIME,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_LIME,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_LIME, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_GREEN = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_GREEN,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_GREEN,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_GREEN, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_CYAN = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_CYAN,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_CYAN,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_CYAN, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_LIGHT_BLUE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_BLUE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_LIGHT_BLUE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_LIGHT_BLUE, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_BLUE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_BLUE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_BLUE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_BLUE, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_PURPLE = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_PURPLE,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_PURPLE,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_PURPLE, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_MAGENTA = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_MAGENTA,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_MAGENTA,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_MAGENTA, 8)
    );
    public static final NetworkBridge NETWORK_BRIDGE_PINK = new NetworkBridge(
            ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE,
            ExpansionItemStacks.NETWORK_BRIDGE_PINK,
            ExpansionWorkbench.TYPE,
            ExpansionRecipes.NETWORK_BRIDGE_PINK,
            StackUtils.getAsQuantity(ExpansionItemStacks.NETWORK_BRIDGE_PINK, 8)
    );

    public static final SpecialSlimefunItem AUTHOR_SEFIRAAT = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_SEFIRAAT
    );

    public static final SpecialSlimefunItem AUTHOR_YBW0014 = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_YBW0014
    );

    public static final SpecialSlimefunItem AUTHOR_YITOUDAIDAI = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_YITOUDAIDAI
    );

    public static final SpecialSlimefunItem AUTHOR_TINALNESS = new AuthorHead(
            ExpansionItemsMenus.MENU_TROPHY,
            ExpansionItemStacks.AUTHOR_TINALNESS
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
            RecipeType.NULL,
            ExpansionRecipes.ITEM_MOVER
    );

    public static final NetworkBlueprintDecoder NETWORK_BLUEPRINT_DECODER = new NetworkBlueprintDecoder(
            ExpansionItemsMenus.MENU_ITEMS,
            ExpansionItemStacks.NETWORK_BLUEPRINT_DECODER,
            RecipeType.NULL,
            ExpansionRecipes.NETWORK_BLUEPRINT_DECODER
    );
}
