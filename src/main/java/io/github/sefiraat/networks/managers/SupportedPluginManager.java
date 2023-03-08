package io.github.sefiraat.networks.managers;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class SupportedPluginManager {

    private static SupportedPluginManager instance;

    private final boolean infinityExpansion;
    private final boolean netheopoiesis;
    private final boolean mcMMO;

    public SupportedPluginManager() {
        Preconditions.checkArgument(instance == null, "Cannot instantiate class");
        instance = this;
        this.infinityExpansion = Bukkit.getPluginManager().isPluginEnabled("InfinityExpansion");
        this.netheopoiesis = Bukkit.getPluginManager().isPluginEnabled("Netheopoiesis");
        this.mcMMO = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
    }

    public boolean isInfinityExpansion() {
        return infinityExpansion;
    }

    public boolean isNetheopoiesis() {
        return netheopoiesis;
    }

    public boolean isMcMMO() {
        return mcMMO;
    }

    public static SupportedPluginManager getInstance() {
        return instance;
    }
}
