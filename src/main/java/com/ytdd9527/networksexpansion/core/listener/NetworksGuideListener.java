package com.ytdd9527.networksexpansion.core.listener;

import com.ytdd9527.networksexpansion.core.event.NetworksExpansionGuideOpenEvent;
import com.ytdd9527.networksexpansion.core.util.GuideUtil;
import com.ytdd9527.networksexpansion.implementation.items.ExpansionItemStacks;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideImplementation;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Optional;

public class NetworksGuideListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Networks.getInstance().getLogger().info("onInteract");
        ItemStack item = e.getItem();
        if (item != null && !item.getType().isAir()) {
            if (!e.getPlayer().isOp()) {
                return;
            }

            if (Objects.equals(ItemStackHelper.getDisplayName(item), ExpansionItemStacks.NETWORK_EXPANSION_GUIDE.getDisplayName())) {
                NetworksExpansionGuideOpenEvent event = new NetworksExpansionGuideOpenEvent(e.getPlayer(), e.getAction(), e.getItem(), e.getClickedBlock(), e.getBlockFace());
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    e.setCancelled(true);

                    Networks.getInstance().getLogger().info("open guide");
                    openGuide(e.getPlayer());
                }
            }
        }
    }

    public void openGuide(Player player) {
        Optional<PlayerProfile> optional = PlayerProfile.find(player);

        if (optional.isPresent()) {
            PlayerProfile profile = optional.get();
            SlimefunGuideImplementation guide = GuideUtil.getGuide();
            profile.getGuideHistory().openLastEntry(guide);
        } else {
            GuideUtil.openMainMenuAsync(player, 1);
        }
    }
}
