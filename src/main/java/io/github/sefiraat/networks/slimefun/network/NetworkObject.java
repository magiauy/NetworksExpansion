package io.github.sefiraat.networks.slimefun.network;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;

import lombok.Getter;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
public abstract class NetworkObject extends SpecialSlimefunItem implements AdminDebuggable {

    private final NodeType nodeType;

    private final List<Integer> slotsToDrop = new ArrayList<>();

    protected static final Set<BlockFace> CHECK_FACES = Set.of(
        BlockFace.UP,
        BlockFace.DOWN,
        BlockFace.NORTH,
        BlockFace.SOUTH,
        BlockFace.EAST,
        BlockFace.WEST
    );


    protected NetworkObject(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, NodeType type) {
        this(itemGroup, item, recipeType, recipe, null, type);
    }

    protected NetworkObject(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, NodeType type) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
        this.nodeType = type;
        addItemHandler(
                new BlockTicker() {

                    @Override
                    public boolean isSynchronized() {
                        return runSync();
                    }

                    @Override
                    public void tick(Block b, SlimefunItem item, SlimefunBlockData data) {
                        addToRegistry(b);
                    }
                },
                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent event, ItemStack item, List<ItemStack> drops) {
                        preBreak(event);
                        onBreak(event);
                        postBreak(event);
                    }
                },
                new BlockPlaceHandler(false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerPlace(BlockPlaceEvent event) {
                        prePlace(event);
                        onPlace(event);
                        postPlace(event);
                    }
                }
        );
    }

    protected void addToRegistry(@Nonnull Block block) {
        if (!NetworkStorage.getAllNetworkObjects().containsKey(block.getLocation())) {
            final NodeDefinition nodeDefinition = new NodeDefinition(nodeType);
            NetworkStorage.getAllNetworkObjects().put(block.getLocation(), nodeDefinition);
        }
    }

    protected void preBreak(@Nonnull BlockBreakEvent event) {

    }

    protected void onBreak(@Nonnull BlockBreakEvent event) {
        final Location location = event.getBlock().getLocation();
        final BlockMenu blockMenu = StorageCacheUtils.getMenu(location);

        if (blockMenu != null) {
            for (int i : getSlotsToDrop()) {
                blockMenu.dropItems(location, i);
            }
        }

        Slimefun.getDatabaseManager().getBlockDataController().removeBlock(location);
    }

    protected void postBreak(@Nonnull BlockBreakEvent event) {

    }

    protected void prePlace(@Nonnull BlockPlaceEvent event) {
        Block target = event.getBlock();
        Location controllerLocation = null;

        for (BlockFace checkFace : CHECK_FACES) {
            Block checkBlock = target.getRelative(checkFace);

            // Check for node definitions. If there isn't one, we don't care
            NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(checkBlock.getLocation());
            if (definition == null) {
                continue;
            }

            // There is a definition, if it has a node, then it's part of an active network.
            if (definition.getNode() != null) {
                NetworkRoot networkRoot = definition.getNode().getRoot();
                if (controllerLocation == null) {
                    // First network found, store root location
                    controllerLocation = networkRoot.getController();
                } else if (!controllerLocation.equals(networkRoot.getController())) {
                    // Location differs from that previously recorded, would result in two controllers
                    cancelPlace(event);
                }
            }
        }
    }

    protected void cancelPlace(BlockPlaceEvent event) {
        event.getPlayer().sendMessage(Theme.ERROR.getColor() + "This placement would connect two controllers!");
        event.setCancelled(true);

    }

    protected void onPlace(@Nonnull BlockPlaceEvent event) {

    }

    protected void postPlace(@Nonnull BlockPlaceEvent event) {

    }

    public boolean isAdminDebuggable() {
        return false;
    }

    public boolean runSync() {
        return false;
    }

}
