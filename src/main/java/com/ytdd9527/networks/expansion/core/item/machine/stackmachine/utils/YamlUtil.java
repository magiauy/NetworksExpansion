package com.ytdd9527.networks.expansion.core.item.machine.stackmachine.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlUtil {
    private YamlConfiguration getYamlConfiguration(File dir, String fileName) {
        File file = new File(dir, fileName);
        if (!file.exists()) {
            return new YamlConfiguration();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

}
