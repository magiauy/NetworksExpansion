package com.ytdd9527.networksexpansion.api.interfaces;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public interface CanTestRecipe {
    static boolean testRecipe(@Nonnull ItemStack[] input, @Nonnull ItemStack[] recipe) {
        return false;
    }
}
