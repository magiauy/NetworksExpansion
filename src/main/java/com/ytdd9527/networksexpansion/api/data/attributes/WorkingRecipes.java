package com.ytdd9527.networksexpansion.api.data.attributes;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WorkingRecipes {
    public WorkingCondition[] conditions;
    public WorkingRecipe[] recipes;

    public WorkingRecipes(WorkingRecipe[] recipes) {
        this.recipes = recipes;
    }

    public WorkingRecipe findNextRecipe(Map<ItemStack, Integer> inputs) {
        for (WorkingRecipe recipe : recipes) {
            if (recipe.isMatch(inputs)) {
                return recipe;
            }
        }
        return null;
    }

    public boolean isOk(Block block) {
        for (WorkingCondition condition : conditions) {
            if (!condition.isOk(block)) {
                return false;
            }
        }
        return true;
    }
}
