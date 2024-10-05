package com.ytdd9527.networksexpansion.implementation.items.machines.autocrafters.basic;

import com.ytdd9527.networksexpansion.api.helpers.SupportedPressureChamberRecipes;
import com.ytdd9527.networksexpansion.core.items.machines.AbstractAutoCrafter;
import com.ytdd9527.networksexpansion.implementation.items.blueprints.PressureChamberBlueprint;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class AutoPressureChamber extends AbstractAutoCrafter {
    public AutoPressureChamber(
            ItemGroup itemGroup,
            SlimefunItemStack item,
            RecipeType recipeType,
            ItemStack[] recipe,
            int chargePerCraft,
            boolean withholding
    ) {
        super(itemGroup, item, recipeType, recipe, chargePerCraft, withholding);
    }

    public Set<Map.Entry<ItemStack[], ItemStack>> getRecipeEntries() {
        return SupportedPressureChamberRecipes.getRecipes().entrySet();
    }

    public boolean getRecipeTester(ItemStack[] inputs, ItemStack[] recipe) {
        return SupportedPressureChamberRecipes.testRecipe(inputs, recipe);
    }

    public boolean isValidBlueprint(SlimefunItem item) {
        return item instanceof PressureChamberBlueprint;
    }
}
