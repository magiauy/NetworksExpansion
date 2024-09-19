package com.ytdd9527.networksexpansion.api.data;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

@Data
@Getter
@ToString
@SuppressWarnings("unused")
public class SuperRecipe {
    private final String name;
    private final ItemStack[] input;
    private final ItemStack[] output;
    private final int consumeEnergy;
    private final BiConsumer<Player, BlockMenu> handler;
    private final boolean isShaped;

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack[] output, int consumeEnergy) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = consumeEnergy;
        this.handler = null;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack[] output) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = 0;
        this.handler = null;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack output, int consumeEnergy) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = consumeEnergy;
        this.handler = null;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack output) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = 0;
        this.handler = null;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack[] output, int consumeEnergy, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack[] output, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = output;
        this.consumeEnergy = 0;
        this.handler = handler;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack output, int consumeEnergy, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack[] input, ItemStack output, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = input;
        this.output = new ItemStack[]{output};
        this.consumeEnergy = 0;
        this.handler = handler;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack input, ItemStack[] output, int consumeEnergy, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = output;
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack input, ItemStack[] output, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = output;
        this.consumeEnergy = 0;
        this.handler = handler;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack input, ItemStack output, int consumeEnergy, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = new ItemStack[]{output};
        this.consumeEnergy = consumeEnergy;
        this.handler = handler;
    }

    public SuperRecipe(String name, boolean isShaped, ItemStack input, ItemStack output, BiConsumer<Player, BlockMenu> handler) {
        this.name = name;
        this.isShaped = isShaped;
        this.input = new ItemStack[]{input};
        this.output = new ItemStack[]{output};
        this.consumeEnergy = 0;
        this.handler = handler;
    }
}
