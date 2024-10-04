package com.ytdd9527.networksexpansion.core.items.unusable;

import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.implementation.recipes.ExpansionRecipes;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class AuthorHead extends SpecialSlimefunItem {

    public AuthorHead(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, ExpansionRecipes.NULL);
    }

    public AuthorHead(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull ItemStack recipeOutput) {
        super(itemGroup, item, RecipeType.NULL, ExpansionRecipes.NULL, recipeOutput);
    }

    protected AuthorHead(@Nonnull ItemGroup itemGroup, @Nonnull ItemStack item, @Nonnull String id) {
        super(itemGroup, item, id, RecipeType.NULL, ExpansionRecipes.NULL);
    }


}
