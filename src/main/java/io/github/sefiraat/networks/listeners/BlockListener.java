package io.github.sefiraat.networks.listeners;

import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.utils.NetworkUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/*
 * Fix https://github.com/Sefiraat/Networks/issues/188
 * Fix https://github.com/Sefiraat/Networks/issues/192
 */
public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        NetworkUtils.clearNetwork(e.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        NetworkUtils.clearNetwork(e.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent e) {
        NetworkStorage.unregisterChunk(e.getChunk());
    }
}
