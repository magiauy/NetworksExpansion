package com.ytdd9527.networksexpansion.core.items.unusable;

import io.github.sefiraat.networks.network.stackcaches.BlueprintInstance;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.Theme;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentCraftingBlueprintType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBlueprint extends UnusableSlimefunItem implements DistinctiveItem {

    public AbstractBlueprint(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @ParametersAreNonnullByDefault
    public static void setBlueprint(ItemStack blueprint, ItemStack[] recipe, ItemStack output) {
        final ItemMeta itemMeta = blueprint.getItemMeta();
        DataTypeMethods.setCustom(itemMeta, Keys.BLUEPRINT_INSTANCE, PersistentCraftingBlueprintType.TYPE, new BlueprintInstance(recipe, output));
        List<String> lore = new ArrayList<>();

        lore.add(Theme.CLICK_INFO + "Assigned Recipe");

        for (ItemStack item : recipe) {
            if (item == null) {
                lore.add(Theme.PASSIVE + "Nothing");
                continue;
            }
            lore.add(Theme.PASSIVE + ChatColor.stripColor(ItemStackHelper.getDisplayName(item)));
        }

        lore.add("");
        lore.add(Theme.CLICK_INFO + "Outputting");

        lore.add(Theme.PASSIVE + ChatColor.stripColor(ItemStackHelper.getDisplayName(output)));

        itemMeta.setLore(lore);

        blueprint.setItemMeta(itemMeta);
    }

    @Override
    public boolean canStack(ItemMeta meta1, ItemMeta meta2) {
        return meta1.getPersistentDataContainer().equals(meta2.getPersistentDataContainer());
    }
}
