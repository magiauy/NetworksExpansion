package com.ytdd9527.networksexpansion.implementation.menus;

import com.ytdd9527.networksexpansion.api.groups.MainItemGroup;
import com.ytdd9527.networksexpansion.api.groups.SubFlexItemGroup;
import com.ytdd9527.networksexpansion.utils.GroupConfigUtil;
import com.ytdd9527.networksexpansion.utils.TextUtil;
import io.github.sefiraat.networks.utils.Keys;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author Final_ROOT
 */
public final class ExpansionItemsMenus {
    // TODO head texture

    /* Slimefun item group */
    public static final NestedItemGroup MAIN_MENU = new NestedItemGroup(getKey("NET_EXPANSION_CATEGORY_MAIN"), new CustomItemStack(Material.LECTERN)) {
        @Override
        public boolean isVisible(@Nonnull Player p, @Nonnull PlayerProfile profile, @Nonnull SlimefunGuideMode mode) {
            return false;
        }
    };

    public static final SubItemGroup MENU_ITEMS = new SubItemGroup(getKey("NET_EXPANSION_ITEMS"), MAIN_MENU, new CustomItemStack(Material.AMETHYST_SHARD, TextUtil.colorRandomString(("Network Expansion - Items"))));
    public static final SubItemGroup MENU_CARGO_SYSTEM = new SubItemGroup(getKey("NET_EXPANSION_SYSTEM"), MAIN_MENU, new CustomItemStack(Material.FURNACE_MINECART, TextUtil.colorRandomString(("Network Expansion - Cargo & Storage"))));
    public static final SubItemGroup MENU_FUNCTIONAL_MACHINE = new SubItemGroup(getKey("NET_EXPANSION_FUNCTIONAL_MACHINE"), MAIN_MENU, new CustomItemStack(Material.LECTERN, TextUtil.colorRandomString(("Network Expansion - Machines"))));
    public static final SubItemGroup MENU_TROPHY = new SubItemGroup(getKey("NET_EXPANSION_TROPHY"), MAIN_MENU, new CustomItemStack(Material.RAW_GOLD_BLOCK, TextUtil.colorRandomString(("Network Expansion - Contribution"))));

    /* My item group */
    public static final MainItemGroup MAIN_ITEM_GROUP = GroupConfigUtil.getMainItemGroup("NET_EXPANSION_ITEM_GROUP", Material.CHEST_MINECART, TextUtil.colorRandomString("Networks - Expansion"));
    // item
    public static final SubFlexItemGroup MAIN_MENU_ITEM = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_MAIN_MENU_ITEM", Material.AMETHYST_SHARD, TextUtil.colorRandomString("Items"));
    public static final SubFlexItemGroup SUB_MENU_TOOL = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_TOOL", Material.SPYGLASS, TextUtil.colorRandomString("Items - Tools"));
    public static final SubFlexItemGroup SUB_MENU_BLUEPRINT = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_BLUEPRINT", Material.BLUE_DYE, TextUtil.colorRandomString("Items - Blueprints"));

    // cargo system
    public static final SubFlexItemGroup MAIN_MENU_CARGO_SYSTEM = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_MAIN_MENU_CARGO_SYSTEM", Material.FURNACE_MINECART, TextUtil.colorRandomString("Cargo & Storage"));
    public static final SubFlexItemGroup SUB_MENU_ADVANCED_STORAGE = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_ADVANCED_STORAGE", Material.BOOKSHELF, TextUtil.colorRandomString("Cargo & Storage - Advanced Storage"));
    public static final SubFlexItemGroup SUB_MENU_NETWORKS_DRAWERS = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_NETWORKS_DRAWERS", Material.CHISELED_BOOKSHELF, TextUtil.colorRandomString("Cargo & Storage - Network Drawer"));
    public static final SubFlexItemGroup SUB_MENU_CARGO = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_CARGO", Material.END_ROD, TextUtil.colorRandomString("Cargo & Storage - Cargo"));

    // functional machine
    public static final SubFlexItemGroup MAIN_MENU_FUNCTIONAL_MACHINE = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_MAIN_MENU_FUNCTIONAL_MACHINE", Material.LECTERN, TextUtil.colorRandomString("Machines"));
    public static final SubFlexItemGroup SUB_MENU_CORE_MACHINE = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_CORE_MACHINE", Material.AMETHYST_BLOCK, TextUtil.colorRandomString("Machines - Machines Core"));
    public static final SubFlexItemGroup SUB_MENU_ADVANCED_NET = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_ADVANCED_NET", Material.BLACK_STAINED_GLASS, TextUtil.colorRandomString("Machines - Advanced Network Machines"));
    public static final SubFlexItemGroup SUB_MENU_BRIDGE = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_BRIDGE", Material.WHITE_STAINED_GLASS, TextUtil.colorRandomString("Machines - More Bridges"));
    public static final SubFlexItemGroup SUB_MENU_GRID = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_GRID", Material.NOTE_BLOCK, TextUtil.colorRandomString("Machines - Grids"));
    public static final SubFlexItemGroup SUB_MENU_ENCODER = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_ENCODER", Material.TARGET, TextUtil.colorRandomString("Machines - Encoders"));
    public static final SubFlexItemGroup SUB_MENU_CRAFTER_MACHINE = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_CRAFTER_MACHINE", Material.CRAFTING_TABLE, TextUtil.colorRandomString("Machines - Automatic Synthesizer"));

    // trophy
    public static final SubFlexItemGroup MAIN_MENU_TROPHY = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_MAIN_MENU_TROPHY", Material.RAW_GOLD_BLOCK, TextUtil.colorRandomString("Contribute"));
    public static final SubFlexItemGroup SUB_MENU_AUTHOR = GroupConfigUtil.getSubFlexItemGroup("NET_EXPANSION_SUB_MENU_AUTHOR", Material.PLAYER_HEAD, TextUtil.colorRandomString("Author"));

    private static NamespacedKey getKey(String key) {
        return Keys.newKey(key);
    }
}
