package com.ytdd9527.networks.expansion.core.data.attributes;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorkingRecipe {
    public String recipeName;
    public int duration;
    public int energy;
    public Map<ItemStack, Integer> inputs;
    public Map<ItemStack, Integer> outputs;
    public Map<ItemStack, Integer> weights;

    public WorkingRecipe(String recipeName, int duration, int energy, Map<ItemStack, Integer> inputs, Map<ItemStack, Integer> outputs, Map<ItemStack, Integer> weights) {
        this.recipeName = recipeName;
        this.duration = duration;
        this.energy = energy;
        this.inputs = inputs;
        this.outputs = outputs;
        this.weights = weights;
    }

    public WorkingRecipe(String recipeName, int duration, int energy, Map<ItemStack, Integer> inputs, Map<ItemStack, Integer> outputs) {
        this.recipeName = recipeName;
        this.duration = duration;
        this.energy = energy;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public boolean isMatch(Map<ItemStack, Integer> incomings) {
        // 检查输入是否匹配
        for (Map.Entry<ItemStack, Integer> entry : this.inputs.entrySet()) {
            ItemStack input = entry.getKey();
            Integer requiredAmount = entry.getValue();
            Integer incomingAmount = incomings.getOrDefault(input, 0);
            if (incomingAmount < requiredAmount) {
                return false;
            }
        }

        return true;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Map<ItemStack, Integer> getInputs() {
        return this.inputs;
    }

    public void setInputs(Map<ItemStack, Integer> inputs) {
        this.inputs = inputs;
    }

    public Map<ItemStack, Integer> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(Map<ItemStack, Integer> outputs) {
        this.outputs = outputs;
    }

    public Map<ItemStack, Integer> getWeights() {
        return this.weights;
    }

    public void setWeights(Map<ItemStack, Integer> weights) {
        this.weights = weights;
    }

    public Map<ItemStack, Integer> outputItems() {
        Map<ItemStack, Integer> goings = new HashMap<>(4, 0.75f);
        Random random = new Random();
        for (ItemStack outputItem : this.outputs.keySet()) {
            if (weights != null) {
                int weight = this.weights.get(outputItem);
                if (weight == 100) {
                    goings.put(outputItem, this.outputs.get(outputItem));
                } else {
                    if (random.nextInt(100) < weight) {
                        goings.put(outputItem, this.outputs.get(outputItem));
                    }
                }
            } else {
                goings.put(outputItem, this.outputs.get(outputItem));
            }
        }
        return goings;
    }
}
