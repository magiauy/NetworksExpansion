package com.ytdd9527.networks.expansion.core.items.machines.autocrafters.system.encoders;

import com.ytdd9527.networks.expansion.core.helpers.SupportedMagicWorkbenchRecipes;
import com.ytdd9527.networks.expansion.core.items.machines.autocrafters.system.blueprints.MagicWorkbenchBlueprint;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public class MagicWorkbenchEncoder extends AbstractEncoder {

    public MagicWorkbenchEncoder(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public void blueprintSetter(ItemStack itemStack, ItemStack[] inputs, ItemStack crafted) {
        MagicWorkbenchBlueprint.setBlueprint(itemStack, inputs, crafted);
    }

    public boolean isValidBlueprint(ItemStack blueprint) {
        return SlimefunItem.getByItem(blueprint) instanceof MagicWorkbenchBlueprint;
    }

    public Set<Map.Entry<ItemStack[], ItemStack>> getRecipeEntries() {
        return SupportedMagicWorkbenchRecipes.getRecipes().entrySet();
    }

    ;

    public boolean getRecipeTester(ItemStack[] inputs, ItemStack[] recipe) {
        return SupportedMagicWorkbenchRecipes.testRecipe(inputs, recipe);
    }

    ;
}
