package com.ytdd9527.networksexpansion.implementation.items.tools;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class NetworksInfoTool extends SpecialSlimefunItem {
    public NetworksInfoTool(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, RecipeType.NULL, new ItemStack[]{});
        addItemHandler(
                (ItemUseHandler) e -> {
                    e.cancel();
                    final Player player = e.getPlayer();
                    if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "你没有权限使用此物品！");
                        return;
                    }
                    final Optional<Block> optional = e.getClickedBlock();
                    if (optional.isPresent()) {
                        final Location location = optional.get().getLocation();
                        if (player.isSneaking()) {
                            location.add(0, 1, 0);
                        }
                        final SlimefunItem sfi = StorageCacheUtils.getSfItem(location);
                        if (sfi instanceof NetworkObject) {
                            final NodeDefinition nodeDefinition = NetworkStorage.getNode(location);
                            if (nodeDefinition == null) {
                                player.sendMessage(ChatColor.GREEN + "nodeDefinition = null");
                                return;
                            }

                            player.sendMessage(ChatColor.GREEN + "nodeDefinition.Charge = " + nodeDefinition.getCharge());
                            player.sendMessage(ChatColor.GREEN + "nodeDefinition.Type = " + nodeDefinition.getType());

                            if (nodeDefinition.getNode() == null) {
                                player.sendMessage(ChatColor.GREEN + "nodeDefinition.Node = null");
                                return;
                            }

                            player.sendMessage(ChatColor.GREEN + "nodeDefinition.Node.NodePosition = " + nodeDefinition.getNode().getNodePosition());

                            if (nodeDefinition.getNode().getRoot() == null) {
                                player.sendMessage(ChatColor.GREEN + "nodeDefinition.Node.Root = null");
                                return;
                            }

                            player.sendMessage(ChatColor.GREEN + "nodeDefinition.Node.Root.NodePosition = " + nodeDefinition.getNode().getRoot().getNodePosition());
                        } else {
                            player.sendMessage(ChatColor.RED + "Not a NetworkObject");
                        }
                    }
                }
        );
    }
}