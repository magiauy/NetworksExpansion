package com.ytdd9527.networks.expansion.core.data.attributes;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.core.enums.WorkingConditionType;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Locale;

@Getter
public class WorkingCondition {

    private WorkingConditionType type;
    private YamlConfiguration value;

    public WorkingCondition(WorkingConditionType type, YamlConfiguration value) {
        this.type = type;
        this.value = value;
    }

    public void setType(WorkingConditionType type) {
        this.type = type;
    }

    public void setValue(YamlConfiguration value) {
        this.value = value;
    }

    public boolean isOk(Block block) {
        if (this.type == WorkingConditionType.NONE) {
            return true;
        }
        switch (this.type) {
            case LIGHT_LEVEL -> {
                return block.getLightFromSky() >= this.value.getInt("light_level");
            }
            case BLOCK -> {
                ConfigurationSection direction = this.value.getConfigurationSection("direction");
                int x = direction.getInt("x");
                int y = direction.getInt("y");
                int z = direction.getInt("z");
                Block targetBlock = block.getRelative(x, y, z);
                ConfigurationSection list = this.value.getConfigurationSection("list");
                String blockName = list.getString("mc");
                if (blockName != null && blockName.toLowerCase(Locale.ROOT).equals(targetBlock.getType().name().toUpperCase(Locale.ROOT))) {
                    return true;
                }
                String sfId = list.getString("sf");
                if (sfId != null && sfId.equals(StorageCacheUtils.getSfItem(targetBlock.getLocation()).getId())) {
                    return true;
                }
                return false;
            }
            case WATERLOGGED -> {
                BlockData blockData = block.getBlockData();
                if (blockData instanceof Waterlogged waterlogged) {
                    return waterlogged.isWaterlogged() == this.value.getBoolean("value");
                } else {
                    return false;
                }
            }
            case WORLD_NAME -> {
                return block.getWorld().getName().equals(this.value.getString("world_name"));
            }
            case GAME_TIME -> {
                int min = Integer.parseInt(this.value.getString("range").split("..")[0]);
                int max = Integer.parseInt(this.value.getString("range").split("..")[1]);
                return min <= block.getWorld().getTime() && block.getWorld().getTime() <= max;
            }
            default -> {
                return false;
            }
        }
    }
}
