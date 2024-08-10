package com.ytdd9527.networksexpansion.core.items.machines.encoders;

import com.ytdd9527.networksexpansion.api.helpers.SupportedSmelteryRecipes;
import com.ytdd9527.networksexpansion.core.items.blueprints.SmelteryBlueprint;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class SmelteryEncoder extends AbstractEncoder {

    public SmelteryEncoder(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public void blueprintSetter(ItemStack itemStack, ItemStack[] inputs, ItemStack crafted) {
        SmelteryBlueprint.setBlueprint(itemStack, inputs, crafted);
    }

    public boolean isValidBlueprint(ItemStack blueprint) {
        return SlimefunItem.getByItem(blueprint) instanceof SmelteryBlueprint;
    }

    public Set<Map.Entry<ItemStack[], ItemStack>> getRecipeEntries() {
        return SupportedSmelteryRecipes.getRecipes().entrySet();
    }

    ;

    public boolean getRecipeTester(ItemStack[] inputs, ItemStack[] recipe) {
        return SupportedSmelteryRecipes.testRecipe(inputs, recipe);
    }

    ;
}
