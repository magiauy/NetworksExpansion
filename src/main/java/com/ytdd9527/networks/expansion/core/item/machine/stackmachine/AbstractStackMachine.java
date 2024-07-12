package com.ytdd9527.networks.expansion.core.item.machine.stackmachine;

import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractStackMachine extends NetworkObject {

    protected AbstractStackMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, NodeType type) {
        super(itemGroup, item, recipeType, recipe, type);
    }
}
