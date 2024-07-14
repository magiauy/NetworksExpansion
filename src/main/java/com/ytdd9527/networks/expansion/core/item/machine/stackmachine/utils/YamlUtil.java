package com.ytdd9527.networks.expansion.core.item.machine.stackmachine.utils;

import com.ytdd9527.networks.expansion.core.item.machine.stackmachine.AbstractStackMachine;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlUtil {
    public static YamlConfiguration getYamlConfiguration(File dir, String fileName) {
        File file = new File(dir, fileName);
        if (!file.exists()) {
            return new YamlConfiguration();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static ItemStack readItem(ConfigurationSection section) throws IOException {
        String type = section.getString("type", "mc");
        String inputId = section.getString("id");
        int amount = section.getInt("amount", 1);
        Logger logger = Networks.getInstance().getLogger();
        switch (type) {
            case "mc" -> {
                ItemStack item = new ItemStack(Material.valueOf(inputId));
                item.setAmount(amount);
                return item;
            }
            case "sf" -> {
                SlimefunItem sfItem = SlimefunItem.getById(inputId);
                if (sfItem == null) {
                    logger.severe("无效的参数[recipes.inputs]: " + inputId);
                }
                ItemStack item = StackUtils.getAsQuantity(sfItem.getItem(), 1);
                item.setAmount(amount);
                return item;
            }
            case "saveditem" -> {
                File file = new File(String.valueOf(AbstractStackMachine.getSavedItemsFolder()), inputId + ".yml");
                if (!file.exists()) {
                    logger.severe("无效的参数[recipes.inputs]: " + inputId);
                }

                String fileContext = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                Pattern p = Pattern.compile("v: \\S\\d*");

                Matcher matcher = p.matcher(fileContext);
                if (matcher.find()) {
                    int s = matcher.start();
                    int e = matcher.end();
                    String replace = fileContext.substring(s, e);
                    int v = Integer.parseInt(replace.replace("v: ", ""));

                    if (v > Bukkit.getUnsafe().getDataVersion()) {
                        String r2 = replace.replaceFirst(
                                String.valueOf(v),
                                String.valueOf(Bukkit.getUnsafe().getDataVersion()));
                        fileContext = fileContext.replace(replace, r2);
                        Files.writeString(
                                file.toPath(),
                                fileContext,
                                StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING);
                    }
                }

                YamlConfiguration i = YamlConfiguration.loadConfiguration(file);
                ItemStack item = StackUtils.getAsQuantity(i.getItemStack("item", new ItemStack(Material.AIR)), 1);
                item.setAmount(amount);
                return item;
            }
            default -> {
                return null;
            }
        }
    }

    public static Map<ItemStack, Integer> readItem(ConfigurationSection section, boolean readWeight) throws IOException {
        Map<ItemStack, Integer> items = new HashMap<>();
        for (Object s : section.getList("list")) {
            ConfigurationSection subSection = (ConfigurationSection) s;
            ItemStack item = readItem(section);
            int weight = section.getInt("weight", 100);
            items.put(item, weight);
        }
        return items;
    }
}
