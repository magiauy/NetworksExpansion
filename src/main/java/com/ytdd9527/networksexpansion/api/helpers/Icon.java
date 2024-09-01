package com.ytdd9527.networksexpansion.api.helpers;


import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


/**
 * @author Final_ROOT
 */
public class Icon {
    public static final ItemStack BORDER_ICON = ChestMenuUtils.getBackground();
    public static final ItemStack ERROR_ICON = new ItemStack(Material.BARRIER);
    public static final ItemStack RECIPE_ICON = new ItemStack(Material.PAPER);
}
