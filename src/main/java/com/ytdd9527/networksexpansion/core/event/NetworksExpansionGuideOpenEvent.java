package com.ytdd9527.networksexpansion.core.event;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class NetworksExpansionGuideOpenEvent extends PlayerEvent {
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private Result state = Result.DEFAULT;

    public NetworksExpansionGuideOpenEvent(@Nonnull Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public boolean isCancelled() {
        return state == Result.DENY;
    }

    public void setCancelled(boolean cancel) {
        state = cancel? Result.DENY : Result.DEFAULT;
    }
}
