package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.basic;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.enums.TransportMode;
import com.ytdd9527.networksexpansion.api.interfaces.Configurable;
import com.ytdd9527.networksexpansion.utils.DisplayGroupGenerators;
import com.ytdd9527.networksexpansion.utils.LineOperationUtil;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


public class LineTransferGrabber extends NetworkDirectional implements RecipeDisplayItem, Configurable {
    private static final int PARTICLE_INTERVAL = 2;
    private static final int DEFAULT_MAX_DISTANCE = 32;
    private static final int DEFAULT_GRAB_ITEM_TICK = 1;
    private static final boolean DEFAULT_USE_SPECIAL_MODEL = false;
    private static final String KEY_UUID = "display-uuid";
    private final HashMap<Location, Integer> TICKER_MAP = new HashMap<>();
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private int grabItemTick;
    private int maxDistance;

    public LineTransferGrabber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String itemId) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER_GRABBER);
        loadConfigurations(itemId);
    }

    private void loadConfigurations(String itemId) {
        FileConfiguration config = Networks.getInstance().getConfig();

        int defaultMaxDistance = 32;
        int defaultGrabItemTick = 1;
        boolean defaultUseSpecialModel = false;

        this.maxDistance = config.getInt("items." + itemId + ".max-distance", defaultMaxDistance);
        this.grabItemTick = config.getInt("items." + itemId + ".grabitem-tick", defaultGrabItemTick);
        this.useSpecialModel = config.getBoolean("items." + itemId + ".use-special-model.enable", defaultUseSpecialModel);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("cloche", DisplayGroupGenerators::generateCloche);

        this.displayGroupGenerator = null;

        if (this.useSpecialModel) {
            String generatorKey = config.getString("items." + itemId + ".use-special-model.type");
            this.displayGroupGenerator = generatorMap.get(generatorKey);
            if (this.displayGroupGenerator == null) {
                Networks.getInstance().getLogger().warning("未知的展示组类型 '" + generatorKey + "', 特殊模型已禁用。");
                this.useSpecialModel = false;
            }
        }

    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);

        if (blockMenu == null) {
            return;
        }
        final Location location = blockMenu.getLocation();
        if (grabItemTick != 1) {
            int tickCounter = getTickCounter(location);
            tickCounter = (tickCounter + 1) % grabItemTick;

            if (tickCounter == 0) {
                tryGrabItem(blockMenu);
            }

            updateTickCounter(location, tickCounter);
        } else {
            tryGrabItem(blockMenu);
        }
    }

    private int getTickCounter(Location location) {
        final Integer ticker = TICKER_MAP.get(location);
        if (ticker == null) {
            TICKER_MAP.put(location, 0);
            return 0;
        }
        return ticker;
    }

    private void updateTickCounter(Location location, int tickCounter) {
        TICKER_MAP.put(location, tickCounter);
    }

    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getNode(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }
        final NetworkRoot root = definition.getNode().getRoot();

        final boolean drawParticle = blockMenu.hasViewer();
        LineOperationUtil.doOperation(
                blockMenu.getLocation(),
                direction,
                maxDistance,
                true,
                true,
                drawParticle,
                PARTICLE_INTERVAL,
                (targetMenu) -> {
                    LineOperationUtil.grabItem(root, targetMenu, TransportMode.FIRST_STOP, 64);
                });
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        // 返回一个Particle.DustOptions对象，设置为黄绿色粒子
        return new Particle.DustOptions(Color.LIME, 5);
    }

    @Override
    public void onPlace(BlockPlaceEvent e) {
        super.onPlace(e);
        if (useSpecialModel) {
            e.getBlock().setType(Material.BARRIER);
            setupDisplay(e.getBlock().getLocation());
        }
    }

    @Override
    public void postBreak(BlockBreakEvent e) {
        super.postBreak(e);
        Location location = e.getBlock().getLocation();
        removeDisplay(location);
        e.getBlock().setType(Material.AIR);
    }

    private void setupDisplay(@Nonnull Location location) {
        if (this.displayGroupGenerator != null) {
            DisplayGroup displayGroup = this.displayGroupGenerator.apply(location.clone().add(0.5, 0, 0.5));
            StorageCacheUtils.setData(location, KEY_UUID, displayGroup.getParentUUID().toString());
        }
    }

    private void removeDisplay(@Nonnull Location location) {
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            group.remove();
        }
    }

    @Nullable
    private UUID getDisplayGroupUUID(@Nonnull Location location) {
        String uuid = StorageCacheUtils.getData(location, KEY_UUID);
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
    }

    @Nullable
    private DisplayGroup getDisplayGroup(@Nonnull Location location) {
        UUID uuid = getDisplayGroupUUID(location);
        if (uuid == null) {
            return null;
        }
        return DisplayGroup.fromUUID(uuid);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aTransferring data",
                "",
                "&7[&aMaximum distance&7]&f: &6" + maxDistance + " blocks",
                "&7[&aGrab rate&7]&f:&7 Every &6" + grabItemTick + " SfTick &7Grab once"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aParameter",
                "&7Default mode: &6First Stop",
                "&cUnchangable mode",
                "&7Default quantity: &664",
                "&cUnchangable mode"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aFeature",
                "",
                "&eLine grabber will grab items through",
                "&ea line of machines it facing."
        ));
        return displayRecipes;
    }
}
