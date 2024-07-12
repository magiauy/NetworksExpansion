package com.ytdd9527.networks.expansion.core.item.machine.stackmachine.attribute;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

public class WorkingCondition {

    private WorkingConditionType type;
    private String value;

    public WorkingCondition(WorkingConditionType type, String value) {
        this.type = type;
        this.value = value;
    }

    public enum WorkingConditionType {
        LIGHT_LEVEL,
        BLOCK,
        WATERLOGGED,
        WORLD_NAME,
        GAME_TIME,
        NONE
    }

    public WorkingConditionType getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public void setType(WorkingConditionType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOk(Block block) {
        if (this.type == WorkingConditionType.NONE) {
            return true;
        }
        switch (this.type) {
            case LIGHT_LEVEL -> {
                return block.getLightFromSky() >= Integer.parseInt(this.value);
            }
            case BLOCK -> {
                //TODO: no idea yet.
                return false;
            }
            case WATERLOGGED -> {
                BlockData blockData = block.getBlockData();
                if (blockData instanceof Waterlogged waterlogged) {
                    return waterlogged.isWaterlogged() == Boolean.parseBoolean(this.value);
                }  else {
                    return false;
                }
            }
            case WORLD_NAME -> {
                return block.getWorld().getName().equals(this.value);
            }
            case GAME_TIME -> {
                int min = Integer.parseInt(this.value.split("..")[0]);
                int max = Integer.parseInt(this.value.split("..")[1]);
                return min <= block.getWorld().getTime() && block.getWorld().getTime() <= max;
            }
            default -> {
                return false;
            }
        }
    }
}
