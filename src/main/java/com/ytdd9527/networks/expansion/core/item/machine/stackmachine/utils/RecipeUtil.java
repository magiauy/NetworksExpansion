package com.ytdd9527.networks.expansion.core.item.machine.stackmachine.utils;

import com.ytdd9527.networks.expansion.core.item.machine.stackmachine.attribute.WorkingRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RecipeUtil {
    public static int getCraftLevel(Map<ItemStack, Long> allItems, WorkingRecipe recipe, int maxAmount) {
        int maxCraftable = Integer.MAX_VALUE;

        // 计算可以合成的最大数量，并更新allItems
        for (Map.Entry<ItemStack, Integer> entry : recipe.getInputs().entrySet()) {
            ItemStack item = entry.getKey();
            int requiredAmount = entry.getValue();
            long availableAmount = allItems.getOrDefault(item, 0L);

            // 计算可以合成的最大数量
            if (requiredAmount <= 0) {
                continue;
            }
            long canCraft = availableAmount / requiredAmount;
            if (canCraft == 0) {
                // 如果无法合成任何一个recipe，直接返回
                return 0;
            }
            maxCraftable = (int) Math.min(maxCraftable, canCraft);
        }

        maxCraftable = Math.min(maxCraftable, maxAmount);
        return maxCraftable;
    }
    public static Map<ItemStack, Integer> getCraftOutputs(Map<ItemStack, Long> allItems, WorkingRecipe recipe, int maxCraftable) {
        if (maxCraftable == 0) {
            return new HashMap<>();
        }

        // 更新allItems，减去用于合成的物品数量，并增加合成的结果物品数量
        for (Map.Entry<ItemStack, Integer> entry : recipe.getInputs().entrySet()) {
            ItemStack item = entry.getKey();
            int requiredAmount = entry.getValue();
            allItems.put(item, allItems.get(item) - requiredAmount * maxCraftable);
        }
        Map<ItemStack, Integer> outputItem = recipe.outputItems();
        Map<ItemStack, Integer> outputs = new HashMap<>();
        for (Map.Entry<ItemStack, Integer> entry : outputItem.entrySet()) {
            outputs.put(entry.getKey(), entry.getValue() * maxCraftable);
        }
        return outputs;
    }
}
