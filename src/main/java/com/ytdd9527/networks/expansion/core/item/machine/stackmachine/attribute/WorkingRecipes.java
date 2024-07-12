package com.ytdd9527.networks.expansion.core.item.machine.stackmachine.attribute;

import org.bukkit.inventory.ItemStack;

public class WorkingRecipes {
    public WorkingRecipe[] recipes;

    public WorkingRecipes(WorkingRecipe[] recipes) {
        this.recipes = recipes;
    }

    public WorkingRecipe findNextRecipe(ItemStack[] inputs) {
        for (WorkingRecipe recipe : recipes) {
            if (recipe.isMatch(inputs)) {
                return recipe;
            }
        }
        return null;
    }
}
