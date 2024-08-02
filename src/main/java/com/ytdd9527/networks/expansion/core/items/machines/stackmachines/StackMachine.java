package com.ytdd9527.networks.expansion.core.items.machines.stackmachines;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.core.data.attributes.WorkingStatus;
import com.ytdd9527.networks.expansion.core.data.attributes.WorkingRecipe;
import com.ytdd9527.networks.expansion.core.data.attributes.WorkingRecipes;
import com.ytdd9527.networks.expansion.core.managers.ConfigManager;
import com.ytdd9527.networks.expansion.utils.itemstacks.RecipeUtil;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class StackMachine extends AbstractStackMachine {
    static Map<String, Path> files = new HashMap<>();
    static Map<String, WorkingRecipes> recipes = new HashMap<>();
    Map<Location, Set<ItemStack>> leftovers = new HashMap<>();
    Map<Location, Map<ItemStack, Integer>> cachedInputs = new HashMap<>();
    Map<Location, WorkingRecipe> lastMatch = new HashMap<>();

    protected StackMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, NodeType type) {
        super(itemGroup, item, recipeType, recipe, type);
    }

    public static void preLoad() {
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

    public static void loadConfig() throws IOException {
        Logger logger = Networks.getInstance().getLogger();
        for (String fileName : files.keySet()) {
            File path = files.get(fileName).toFile();
            String filePath = path + "/" + fileName;
            YamlConfiguration configuration = ConfigManager.getYamlConfiguration(path, fileName);
            String id = configuration.getString("machine");
            if (id == null) {
                logger.severe("无效的配置文件: " + filePath);
                continue;
            }
            SlimefunItem machine = SlimefunItem.getById(id);
            if (machine == null) {
                logger.severe("@" + filePath + "无效的参数[machine]: " + id);
                continue;
            }
            List<?> recipesContent = configuration.getList("recipes");
            if (recipesContent == null) {
                recipesContent = configuration.getList("empty");
            }
            if (recipesContent == null) {
                logger.severe("无效的配置文件: " + filePath);
                continue;
            }
            ArrayList<WorkingRecipe> workingRecipes = new ArrayList<>();
            for (Object recipeContent : recipesContent) {
                ConfigurationSection section = (ConfigurationSection) recipeContent;
                String name = section.getString("name");
                int duration = section.getInt("duration");
                int energy = section.getInt("energy");
                List<?> inputs = section.getList("input");
                Map<ItemStack, Integer> inputItems = new HashMap<>();
                if (inputs != null) {
                    for (Object input : inputs) {
                        ConfigurationSection inputSection = (ConfigurationSection) input;
                        String type = inputSection.getString("type");

                        ItemStack item = ConfigManager.readItem(inputSection);
                        inputItems.put(item, item.getAmount());
                    }
                }
                List<?> outputs = section.getList("output");
                Map<ItemStack, Integer> outputItems = new HashMap<>();
                Map<ItemStack, Integer> weights = new HashMap<>();
                if (outputs != null) {
                    for (Object output : outputs) {
                        ConfigurationSection outputSection = (ConfigurationSection) output;
                        if (outputSection.getString("type").equals("weight")) {
                            Map<ItemStack, Integer> weightItems = ConfigManager.readItem(outputSection, true);
                            for (ItemStack item : weightItems.keySet()) {
                                outputItems.put(item, item.getAmount());
                                weights.put(item, weightItems.get(item));
                            }
                        }
                        ItemStack item = ConfigManager.readItem(outputSection);
                        outputItems.put(item, item.getAmount());
                    }
                }
                for (ItemStack item : outputItems.keySet()) {
                    if (!weights.containsKey(item)) {
                        weights.put(item, 100);
                    }
                }

                if (weights.isEmpty()) {
                    workingRecipes.add(new WorkingRecipe(name, duration, energy, inputItems, outputItems));
                } else {
                    workingRecipes.add(new WorkingRecipe(name, duration, energy, inputItems, outputItems, weights));
                }
            }
            recipes.put(id, new WorkingRecipes(workingRecipes.toArray(new WorkingRecipe[0])));
        }
    }

    public static void initialize() throws IOException {
        preLoad();
        loadConfig();
    }

    @Override
    public boolean isValidMachine(String id) {
        return recipes.containsKey(id);
    }

    public Map<ItemStack, Integer> getInputs(BlockMenu blockMenu) {
        Map<ItemStack, Integer> inputItems = cachedInputs.get(blockMenu.getLocation());
        if (blockMenu.hasViewer() || inputItems == null) {
            inputItems = new HashMap<>();
            for (int slot : INPUT_SLOTS) {
                ItemStack input = blockMenu.getItemInSlot(slot);
                if (input != null && !input.getType().isAir()) {
                    inputItems.merge(input, input.getAmount(), Integer::sum);
                }
            }
            cachedInputs.put(blockMenu.getLocation(), inputItems);
        }
        return inputItems;
    }

    @Override
    public void onTick(Block block) {
        super.onTick(block);
        BlockMenu blockMenu = StorageCacheUtils.getMenu(block.getLocation());
        if (blockMenu == null) {
            return;
        }

        Location location = block.getLocation();
        NetworkRoot root = getNetworkRoot(location);
        if (root == null) {
            return;
        }

        if (leftovers.get(location) != null) {
            for (ItemStack item : leftovers.get(location)) {
                root.addItemStack(item);
                if (item.getAmount() == 0) {
                    leftovers.get(location).remove(item);
                }
            }
            if (leftovers.get(location).isEmpty()) {
                leftovers.remove(location);
            } else {
                setWorkingStatus(blockMenu, WorkingStatus.FULL_OUTPUT);
            }
            return;
        }

        setWorkingStatus(blockMenu, WorkingStatus.WORKING);
        String id = getWorkingMachineId(location);
        if (id == null) {
            return;
        }
        WorkingRecipes workingRecipes = recipes.get(id);
        if (workingRecipes == null) {
            return;
        }

        Map<ItemStack, Integer> inputItems = getInputs(blockMenu);

        WorkingRecipe workingRecipe = lastMatch.get(location);
        if (workingRecipe == null || !workingRecipe.isMatch(inputItems)) {
            workingRecipe = workingRecipes.findNextRecipe(inputItems);
        }
        setWorkingRecipe(location, workingRecipe);
        if (workingRecipe == null) {
            setWorkingStatus(blockMenu, WorkingStatus.NO_MATCH);
            return;
        }

        Map<ItemStack, Long> allItems = root.getAmount(inputItems.keySet());
        Map<ItemStack, Long> clone = new HashMap<>(allItems);
        int level = RecipeUtil.getCraftLevel(clone, workingRecipe, getWorkingAmount(location));
        Map<ItemStack, Integer> outputItems = RecipeUtil.getCraftOutputs(allItems, workingRecipe, level);
        if (!outputItems.isEmpty()) {
            for (ItemStack item : clone.keySet()) {
                root.getItemStack(new ItemRequest(item, (int) (clone.get(item) - allItems.get(item))));
            }

            Set<ItemStack> leftover = new HashSet<>();
            for (ItemStack item : outputItems.keySet()) {
                ItemStack itemStack = item.clone();
                itemStack.setAmount(outputItems.get(item));
                if (itemStack.getAmount() > 0) {
                    leftover.add(itemStack);
                }
            }
            leftovers.put(location, leftover);
        }
    }
}
