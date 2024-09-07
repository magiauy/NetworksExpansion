package com.ytdd9527.networksexpansion.implementation.items.tools;

import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
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

    public NetworksExpansionWorldEditAxe(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[]{});
        addItemHandler(
                (ItemUseHandler) e -> {
                    final Player player = e.getPlayer();
                    if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "你没有权限使用此物品！");
                        return;
                    }
                    final Optional<Block> optional = e.getClickedBlock();
                    if (optional.isPresent()) {
                        final Location location = optional.get().getLocation();
                        if (!player.isSneaking()) {
                            NetworksMain.setPos1(player, location);
                            if (NetworksMain.getPos2(player) == null) {
                                player.sendMessage(ChatColor.GREEN + "Set Pos1 to " + NetworksMain.locationToString(NetworksMain.getPos1(player)));
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Set Pos1 to " + NetworksMain.locationToString(NetworksMain.getPos1(player)) + "(" + NetworksMain.locationRange(NetworksMain.getPos1(player), NetworksMain.getPos2(player)) + " Blocks)");
                            }
                        } else {
                            NetworksMain.setPos2(player, location);
                            if (NetworksMain.getPos1(player) == null) {
                                player.sendMessage(ChatColor.GREEN + "Set Pos2 to " + NetworksMain.locationToString(NetworksMain.getPos2(player)));
                            } else {
                                player.sendMessage(ChatColor.GREEN + "Set Pos2 to " + NetworksMain.locationToString(NetworksMain.getPos2(player)) + "(" + NetworksMain.locationRange(NetworksMain.getPos1(player), NetworksMain.getPos2(player)) + " Blocks)");
                            }
                        }
                    }
                    e.cancel();
                }
        );
    }
}