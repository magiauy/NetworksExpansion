package com.ytdd9527.networksexpansion.api.helpers;


import com.ytdd9527.networksexpansion.utils.itemstacks.ItemStackUtil;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


/**
 * @author Final_ROOT
 */
public class Icon {
    public static final ItemStack BORDER_ICON = ItemStackUtil.getCleanItem(ChestMenuUtils.getBackground());
    public static final ItemStack ERROR_ICON = ItemStackUtil.getCleanItem(new ItemStack(Material.BARRIER));
    public static final ItemStack RECIPE_ICON = ItemStackUtil.getCleanItem(new ItemStack(Material.PAPER));
}
