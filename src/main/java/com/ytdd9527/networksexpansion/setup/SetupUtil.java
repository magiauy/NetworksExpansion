package com.ytdd9527.networksexpansion.setup;

import com.ytdd9527.networksexpansion.implementation.items.ExpansionItems;
import com.ytdd9527.networksexpansion.implementation.menus.ExpansionItemsMenus;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.slimefun.NetworkSlimefunItems;
import net.guizhanss.slimefun4.utils.WikiUtils;

public class SetupUtil {

    public static void setupItem() {
        NetworkSlimefunItems.setup();

        /* 物品 */
        ExpansionItemsMenus.SUB_MENU_TOOL.addTo(
                ExpansionItems.CARGO_NODE_QUICK_TOOL.registerThis(),
                ExpansionItems.WORLD_EDIT_AXE.registerThis(),
                ExpansionItems.INFO_TOOL.registerThis(),
                ExpansionItems.NETWORK_EXPANSION_SURVIVAL_GUIDE.registerThis(),
                ExpansionItems.NETWORK_EXPANSION_CHEAT_GUIDE.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_BLUEPRINT.addTo(
                ExpansionItems.MAGIC_WORKBENCH_BLUEPRINT.registerThis(),
                ExpansionItems.ARMOR_FORGE_BLUEPRINT.registerThis(),
                ExpansionItems.SMELTERY_BLUEPRINT.registerThis(),
                ExpansionItems.QUANTUM_WORKBENCH_BLUEPRINT.registerThis(),
                ExpansionItems.ANCIENT_ALTAR_BLUEPRINT.registerThis(),
                ExpansionItems.EXPANSION_WORKBENCH_BLUEPRINT.registerThis()
        );
        /* 货运与存储 */
        ExpansionItems.ADVANCED_QUANTUM_STORAGE.setSupportsCustomMaxAmount(true);
        ExpansionItemsMenus.SUB_MENU_ADVANCED_STORAGE.addTo(
                ExpansionItems.ADVANCED_QUANTUM_STORAGE.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_NETWORKS_DRAWERS.addTo(
                ExpansionItems.CARGO_STORAGE_UNIT_1.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_2.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_3.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_4.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_5.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_6.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_7.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_8.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_9.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_10.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_11.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_12.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_13.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_NETWORKS_DRAWERS.addTo(
                ExpansionItems.CARGO_STORAGE_UNIT_1_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_2_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_3_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_4_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_5_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_6_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_7_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_8_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_9_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_10_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_11_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_12_MODEL.registerThis(),
                ExpansionItems.CARGO_STORAGE_UNIT_13_MODEL.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CARGO.addTo(
                ExpansionItems.NETWORK_INPUT_ONLY_MONITOR.registerThis(),
                ExpansionItems.NETWORK_OUTPUT_ONLY_MONITOR.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CARGO.addTo(
                ExpansionItems.LINE_TRANSFER.registerThis(),
                ExpansionItems.LINE_TRANSFER_GRABBER.registerThis(),
                ExpansionItems.LINE_TRANSFER_PUSHER.registerThis(),
                ExpansionItems.LINE_TRANSFER_PLUS.registerThis(),
                ExpansionItems.LINE_TRANSFER_PLUS_PUSHER.registerThis(),
                ExpansionItems.LINE_TRANSFER_PLUS_GRABBER.registerThis(),
                ExpansionItems.LINE_TRANSFER_VANILLA_PUSHER.registerThis(),
                ExpansionItems.LINE_TRANSFER_VANILLA_GRABBER.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CARGO.addTo(
                ExpansionItems.ADVANCED_LINE_TRANSFER.registerThis(),
                ExpansionItems.ADVANCED_LINE_TRANSFER_GRABBER.registerThis(),
                ExpansionItems.ADVANCED_LINE_TRANSFER_PUSHER.registerThis(),
                ExpansionItems.ADVANCED_LINE_TRANSFER_PLUS.registerThis(),
                ExpansionItems.ADVANCED_LINE_TRANSFER_PLUS_PUSHER.registerThis(),
                ExpansionItems.ADVANCED_LINE_TRANSFER_PLUS_GRABBER.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CARGO.addTo(
                ExpansionItems.TRANSFER.registerThis(),
                ExpansionItems.TRANSFER_GRABBER.registerThis(),
                ExpansionItems.TRANSFER_PUSHER.registerThis(),
                ExpansionItems.ADVANCED_TRANSFER.registerThis(),
                ExpansionItems.ADVANCED_TRANSFER_GRABBER.registerThis(),
                ExpansionItems.ADVANCED_TRANSFER_PUSHER.registerThis()
        );

        ExpansionItemsMenus.SUB_MENU_CARGO.addTo(
                ExpansionItems.SMART_GRABBER.registerThis(),
                ExpansionItems.SMART_PUSHER.registerThis()
        );
        /* 功能机器 */
        ExpansionItemsMenus.SUB_MENU_CORE_MACHINE.addTo(
                ExpansionItems.NETWORK_EXPANSION_WORKBENCH.registerThis(),
                ExpansionItems.STORAGE_UNIT_UPGRADE_TABLE.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_ADVANCED_NET.addTo(
                ExpansionItems.ADVANCED_IMPORT.registerThis(),
                ExpansionItems.ADVANCED_EXPORT.registerThis(),
                ExpansionItems.ADVANCED_PURGER.registerThis(),
                ExpansionItems.ADVANCED_GREEDY_BLOCK.registerThis(),
                ExpansionItems.NETWORK_CAPACITOR_5.registerThis(),
                ExpansionItems.NETWORK_CAPACITOR_6.registerThis(),
                ExpansionItems.NETWORK_GRID_NEW_STYLE.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_BRIDGE.addTo(
                ExpansionItems.NETWORK_BRIDGE_WHITE.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_LIGHT_GRAY.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_GRAY.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_BLACK.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_BROWN.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_RED.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_ORANGE.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_YELLOW.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_LIME.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_GREEN.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_CYAN.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_LIGHT_BLUE.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_BLUE.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_PURPLE.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_MAGENTA.registerThis(),
                ExpansionItems.NETWORK_BRIDGE_PINK.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_ENCODER.addTo(
                ExpansionItems.MAGIC_WORKBENCH_RECIPE_ENCODER.registerThis(),
                ExpansionItems.ARMOR_FORGE_RECIPE_ENCODER.registerThis(),
                ExpansionItems.SMELTERY_RECIPE_ENCODER.registerThis(),
                ExpansionItems.QUANTUM_WORKBENCH_RECIPE_ENCODER.registerThis(),
                ExpansionItems.ANCIENT_ALTAR_RECIPE_ENCODER.registerThis(),
                ExpansionItems.EXPANSION_WORKBENCH_RECIPE_ENCODER.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CRAFTER_MACHINE.addTo(
                ExpansionItems.AUTO_MAGIC_WORKBENCH.registerThis(),
                ExpansionItems.AUTO_ARMOR_FORGE.registerThis(),
                ExpansionItems.AUTO_SMELTERY.registerThis(),
                ExpansionItems.AUTO_QUANTUM_WORKBENCH.registerThis(),
                ExpansionItems.AUTO_ANCIENT_ALTAR.registerThis(),
                ExpansionItems.AUTO_EXPANSION_WORKBENCH.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CRAFTER_MACHINE.addTo(
                ExpansionItems.AUTO_MAGIC_WORKBENCH_WITHHOLDING.registerThis(),
                ExpansionItems.AUTO_ARMOR_FORGE_WITHHOLDING.registerThis(),
                ExpansionItems.AUTO_SMELTERY_WITHHOLDING.registerThis(),
                ExpansionItems.AUTO_QUANTUM_WORKBENCH_WITHHOLDING.registerThis(),
                ExpansionItems.AUTO_ANCIENT_ALTAR_WITHHOLDING.registerThis(),
                ExpansionItems.AUTO_EXPANSION_WORKBENCH_WITHHOLDING.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CRAFTER_MACHINE.addTo(
                ExpansionItems.ADVANCED_AUTO_MAGIC_WORKBENCH.registerThis(),
                ExpansionItems.ADVANCED_AUTO_ARMOR_FORGE.registerThis(),
                ExpansionItems.ADVANCED_AUTO_SMELTERY.registerThis(),
                ExpansionItems.ADVANCED_AUTO_QUANTUM_WORKBENCH.registerThis(),
                ExpansionItems.ADVANCED_AUTO_ANCIENT_ALTAR.registerThis(),
                ExpansionItems.ADVANCED_AUTO_EXPANSION_WORKBENCH.registerThis(),
                ExpansionItems.ADVANCED_AUTO_CRAFTING_TABLE.registerThis()
        );
        ExpansionItemsMenus.SUB_MENU_CRAFTER_MACHINE.addTo(
                ExpansionItems.ADVANCED_AUTO_MAGIC_WORKBENCH_WITHHOLDING.registerThis(),
                ExpansionItems.ADVANCED_AUTO_ARMOR_FORGE_WITHHOLDING.registerThis(),
                ExpansionItems.ADVANCED_AUTO_SMELTERY_WITHHOLDING.registerThis(),
                ExpansionItems.ADVANCED_AUTO_QUANTUM_WORKBENCH_WITHHOLDING.registerThis(),
                ExpansionItems.ADVANCED_AUTO_ANCIENT_ALTAR_WITHHOLDING.registerThis(),
                ExpansionItems.ADVANCED_AUTO_EXPANSION_WORKBENCH_WITHHOLDING.registerThis(),
                ExpansionItems.ADVANCED_AUTO_CRAFTING_TABLE_WITHHOLDING.registerThis()
        );

        /* authors */
        ExpansionItemsMenus.SUB_MENU_AUTHOR.addTo(
                ExpansionItems.AUTHOR_SEFIRAAT.registerThis(),
                ExpansionItems.AUTHOR_YBW0014.registerThis(),
                ExpansionItems.AUTHOR_YITOUDAIDAI.registerThis(),
                ExpansionItems.AUTHOR_TINALNESS.registerThis()
        );
    }

    private static void setupMenu() {
        Networks networks = Networks.getInstance();

        ExpansionItemsMenus.MAIN_MENU.setTier(0);

        ExpansionItemsMenus.MENU_ITEMS.setTier(0);
        ExpansionItemsMenus.MENU_CARGO_SYSTEM.setTier(0);
        ExpansionItemsMenus.MENU_FUNCTIONAL_MACHINE.setTier(0);
        ExpansionItemsMenus.MENU_TROPHY.setTier(0);

        ExpansionItemsMenus.MAIN_ITEM_GROUP.addTo(ExpansionItemsMenus.MAIN_MENU_ITEM,
                ExpansionItemsMenus.SUB_MENU_TOOL,
                ExpansionItemsMenus.SUB_MENU_BLUEPRINT
        );
        ExpansionItemsMenus.MAIN_MENU_ITEM.addFrom(
                ExpansionItemsMenus.SUB_MENU_TOOL,
                ExpansionItemsMenus.SUB_MENU_BLUEPRINT
        );

        ExpansionItemsMenus.MAIN_ITEM_GROUP.addTo(ExpansionItemsMenus.MAIN_MENU_CARGO_SYSTEM,
                ExpansionItemsMenus.SUB_MENU_ADVANCED_STORAGE,
                ExpansionItemsMenus.SUB_MENU_NETWORKS_DRAWERS,
                ExpansionItemsMenus.SUB_MENU_CARGO
        );
        ExpansionItemsMenus.MAIN_MENU_CARGO_SYSTEM.addFrom(
                ExpansionItemsMenus.SUB_MENU_ADVANCED_STORAGE,
                ExpansionItemsMenus.SUB_MENU_NETWORKS_DRAWERS,
                ExpansionItemsMenus.SUB_MENU_CARGO
        );

        ExpansionItemsMenus.MAIN_ITEM_GROUP.addTo(ExpansionItemsMenus.MAIN_MENU_FUNCTIONAL_MACHINE,
                ExpansionItemsMenus.SUB_MENU_CORE_MACHINE,
                ExpansionItemsMenus.SUB_MENU_ADVANCED_NET,
                ExpansionItemsMenus.SUB_MENU_BRIDGE,
                ExpansionItemsMenus.SUB_MENU_ENCODER,
                ExpansionItemsMenus.SUB_MENU_CRAFTER_MACHINE
        );
        ExpansionItemsMenus.MAIN_MENU_FUNCTIONAL_MACHINE.addFrom(
                ExpansionItemsMenus.SUB_MENU_CORE_MACHINE,
                ExpansionItemsMenus.SUB_MENU_ADVANCED_NET,
                ExpansionItemsMenus.SUB_MENU_BRIDGE,
                ExpansionItemsMenus.SUB_MENU_ENCODER,
                ExpansionItemsMenus.SUB_MENU_CRAFTER_MACHINE
        );

        ExpansionItemsMenus.MAIN_ITEM_GROUP.addTo(ExpansionItemsMenus.MAIN_MENU_TROPHY,
                ExpansionItemsMenus.SUB_MENU_AUTHOR
        );
        ExpansionItemsMenus.MAIN_MENU_TROPHY.addFrom(
                ExpansionItemsMenus.SUB_MENU_AUTHOR
        );

        ExpansionItemsMenus.MAIN_ITEM_GROUP.setTier(0);
        ExpansionItemsMenus.MAIN_ITEM_GROUP.register(networks);
    }

    public static void setupWiki() {
        WikiUtils.setupJson(Networks.getInstance());
    }

    public static void setupIntegration() {
        Networks.getInstance().setupIntegrations();
    }

    public static void setupAll() {

        setupItem();
        setupMenu();
        setupWiki();
        setupIntegration();
    }

}
