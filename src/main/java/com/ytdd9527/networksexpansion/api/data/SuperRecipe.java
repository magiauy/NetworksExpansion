package com.ytdd9527.networksexpansion.api.data;

import com.ytdd9527.networksexpansion.api.interfaces.SuperRecipeHandler;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

@Data
@Getter
@ToString
@SuppressWarnings("unused")
public class SuperRecipe {
    private final ItemStack[] input;
    private final ItemStack[] output;
    private final int consumeEnergy;
    private final SuperRecipeHandler handler;
    private final boolean isShaped;

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack[] output, int consumeEnergy) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = consumeEnergy;
        this.handler = null;
    }

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack[] output) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = 0;
        this.handler = null;
    }

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack output, int consumeEnergy) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = consumeEnergy;
        this.handler = null;
    }

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack output) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = 0;
        this.handler = null;
    }

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack[] output, int consumeEnergy, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack[] output, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = 0;
        this.handler = handler;
    }

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack output, int consumeEnergy, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(boolean isShaped, ItemStack[] input, ItemStack output, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = 0;
        this.handler = handler;
    }

    public SuperRecipe(boolean isShaped, ItemStack input, ItemStack[] output, int consumeEnergy, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = output;
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(boolean isShaped, ItemStack input, ItemStack[] output, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = output;
        this.consumeEnergy = 0;
        this.handler = handler;
    }

    public SuperRecipe(boolean isShaped, ItemStack input, ItemStack output, int consumeEnergy, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = new ItemStack[]{output};
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(boolean isShaped, ItemStack input, ItemStack output, SuperRecipeHandler handler) {
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = new ItemStack[]{output};
        this.consumeEnergy = 0;
        this.handler = handler;
    }
}
