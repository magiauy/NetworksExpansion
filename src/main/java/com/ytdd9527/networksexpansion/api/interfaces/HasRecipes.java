package com.ytdd9527.networksexpansion.api.interfaces;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface HasRecipes {
    static Map<ItemStack[], ItemStack> getRecipes() {
        return new HashMap<>();
    }
}
