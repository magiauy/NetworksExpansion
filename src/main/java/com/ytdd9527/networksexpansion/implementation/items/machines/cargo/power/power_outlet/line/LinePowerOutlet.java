package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.power.power_outlet.line;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.interfaces.Configurable;
import com.ytdd9527.networksexpansion.utils.LineOperationUtil;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LinePowerOutlet extends NetworkDirectional implements Configurable {
    private static final int DEFAULT_RATE = 2000;
    private static final int DEFAULT_MAX_DISTANCE = 32;
    private int maxDistance;
    private int rate;

    public LinePowerOutlet(@Nonnull ItemGroup itemGroup,
                           @Nonnull SlimefunItemStack item,
                           @Nonnull RecipeType recipeType,
                           @Nonnull ItemStack[] recipe
    ) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_POWER_OUTLET);
        loadConfigurations();
    }

    @Override
    public void loadConfigurations() {
        String configKey = getId();
        FileConfiguration config = Networks.getInstance().getConfig();
        this.rate = config.getInt("items." + configKey + ".rate", DEFAULT_RATE);
        this.maxDistance = config.getInt("items." + configKey + ".max_distance", DEFAULT_MAX_DISTANCE);
    }

    @Override
    public void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block b) {
        super.onTick(blockMenu, b);
        if (blockMenu == null) {
            return;
        }

        outPower(blockMenu);
    }

    private void outPower(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getNode(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final NetworkRoot root = definition.getNode().getRoot();
        final BlockFace blockFace = getCurrentDirection(blockMenu);
        LineOperationUtil.doOperation(blockMenu.getLocation(), blockFace, this.maxDistance, false, (targetMenu) -> {
            LineOperationUtil.outPower(targetMenu.getLocation(), root, this.rate);
        });
    }
}
