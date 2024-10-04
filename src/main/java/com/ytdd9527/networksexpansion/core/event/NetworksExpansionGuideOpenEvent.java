package com.ytdd9527.networksexpansion.core.event;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;

public class NetworksExpansionGuideOpenEvent extends PlayerEvent {
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    @Getter
    private SlimefunGuideMode mode = SlimefunGuideMode.SURVIVAL_MODE;
    private Result state = Result.DEFAULT;

    public NetworksExpansionGuideOpenEvent(@Nonnull Player who, SlimefunGuideMode mode) {
        super(who);
        this.mode = mode;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public boolean isCancelled() {
        return state == Result.DENY;
    }

    public void setCancelled(boolean cancel) {
        state = cancel ? Result.DENY : Result.DEFAULT;
    }
}
