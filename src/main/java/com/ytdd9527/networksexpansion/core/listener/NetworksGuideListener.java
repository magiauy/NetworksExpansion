package com.ytdd9527.networksexpansion.core.listener;

import com.ytdd9527.networksexpansion.core.event.NetworksExpansionGuideOpenEvent;
import com.ytdd9527.networksexpansion.implementation.items.ExpansionItemStacks;
import io.github.sefiraat.networks.utils.StackUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class NetworksGuideListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item != null && !item.getType().isAir()) {
            if (StackUtils.itemsMatch(item, ExpansionItemStacks.NETWORK_EXPANSION_GUIDE)) {
                NetworksExpansionGuideOpenEvent event = new NetworksExpansionGuideOpenEvent(e.getPlayer(), e.getAction(), e.getItem(), e.getClickedBlock(), e.getBlockFace());
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    e.setCancelled(true);
                    // open the guide
                }
            }
        }
    }
}
