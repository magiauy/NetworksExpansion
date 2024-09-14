package com.ytdd9527.networksexpansion.core.items.unusable;

import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NetworkExpansionGuide extends SpecialSlimefunItem {

    public NetworkExpansionGuide(@NotNull ItemGroup itemGroup, @NotNull SlimefunItemStack item, @NotNull RecipeType recipeType, @NotNull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }
}
