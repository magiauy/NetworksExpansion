package com.ytdd9527.networks.expansion.core.items.tools;

import com.ytdd9527.networks.expansion.core.items.SpecialSlimefunItem;
import io.github.sefiraat.networks.commands.NetworksMain;
import io.github.sefiraat.networks.utils.Keys;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class NetworksExpansionWorldEditAxe extends SpecialSlimefunItem {

    private static final NamespacedKey TARGET_LOCATION = Keys.newKey("target-location");

    public NetworksExpansionWorldEditAxe(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[]{});
        addItemHandler(
                (ItemUseHandler) e -> {
                    final Player player = e.getPlayer();
                    final Optional<Block> optional = e.getClickedBlock();
                    if (optional.isPresent()) {
                        final Location location = optional.get().getLocation();
                        if (player.isSneaking()) {
                            NetworksMain.worldeditPos2(location);
                            player.sendMessage(ChatColor.GREEN + "Set Pos2 to [World(" + location.getWorld().getName() + "), X(" + location.getBlockX() + "), Y(" + location.getBlockY() + "), Z(" + location.getBlockZ() + ")]");
                        } else {
                            NetworksMain.worldeditPos1(location);
                            player.sendMessage(ChatColor.GREEN + "Set Pos1 to [World(" + location.getWorld().getName() + "), X(" + location.getBlockX() + "), Y(" + location.getBlockY() + "), Z(" + location.getBlockZ() + ")]");
                        }
                    }
                    e.cancel();
                }
        );
    }
}