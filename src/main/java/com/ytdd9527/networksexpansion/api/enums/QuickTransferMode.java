package com.ytdd9527.networksexpansion.api.enums;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum QuickTransferMode {

    FROM_QUANTUM(new CustomItemStack(Material.QUARTZ_BLOCK, "&a从量子存储器传输", "")),
    TO_QUANTUM(new CustomItemStack(Material.QUARTZ_BLOCK, "&a到量子存储器传输", ""));
    private final Map<QuickTransferMode, ItemStack> displayItems = new HashMap<>();

    QuickTransferMode(ItemStack displayItem) {
        displayItems.put(this, displayItem);
    }

    public ItemStack getDisplayItem() {
        return displayItems.get(this);
    }

}
