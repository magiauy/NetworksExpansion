package com.ytdd9527.networksexpansion.core.items;

import com.ytdd9527.networksexpansion.api.factories.MachineRecipeFactory;
import com.ytdd9527.networksexpansion.api.interfaces.RecipeItem;
import io.github.sefiraat.networks.Networks;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * We may add something soon
 *
 * @author Final_ROOT
 * @since 2.0
 */
public abstract class SpecialSlimefunItem extends SlimefunItem {
    public SpecialSlimefunItem(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public SpecialSlimefunItem(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    protected SpecialSlimefunItem(@Nonnull ItemGroup itemGroup, @Nonnull ItemStack item, @Nonnull String id, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
    }

    @Override
    public void register(@Nonnull SlimefunAddon addon) {
        super.register(addon);
        if (this instanceof RecipeItem recipeItem) {
            int delay = recipeItem.getRegisterRecipeDelay();
            if (delay > 0) {
                this.getAddon().getJavaPlugin().getServer().getScheduler().runTaskLater((Plugin) addon, () -> {
                    (recipeItem).registerDefaultRecipes();
                    MachineRecipeFactory.getInstance().initAdvancedRecipeMap(this.getId());
                }, delay);
            } else {
                (recipeItem).registerDefaultRecipes();
                MachineRecipeFactory.getInstance().initAdvancedRecipeMap(this.getId());
            }
        }
    }

    @Nonnull
    public SpecialSlimefunItem registerThis() {
        this.register(Networks.getInstance());
        return this;
    }
}
