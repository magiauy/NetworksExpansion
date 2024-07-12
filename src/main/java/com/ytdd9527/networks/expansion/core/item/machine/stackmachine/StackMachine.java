package com.ytdd9527.networks.expansion.core.item.machine.stackmachine;

import com.ytdd9527.networks.expansion.core.item.machine.stackmachine.attribute.WorkingCondition;
import com.ytdd9527.networks.expansion.core.item.machine.stackmachine.attribute.WorkingRecipes;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class StackMachine extends AbstractStackMachine {
    YamlConfiguration config;
    Map<String, Path> files = new HashMap<>();
    Map<String, WorkingCondition> environment = new HashMap<>();
    Map<String, WorkingRecipes> recipes = new HashMap<>();

    protected StackMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, NodeType type) {
        super(itemGroup, item, recipeType, recipe, type);
    }

    public void preLoad() {
        try {
            Files.walkFileTree((new File(Networks.getInstance().getDataFolder().toPath().toString(), "template")).toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    Path abstractPath = file.toAbsolutePath();
                    files.put(abstractPath.getFileName().toString(), abstractPath.getParent());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {

    }

    public void initalize() {
        preLoad();
        loadConfig();
    }
}
