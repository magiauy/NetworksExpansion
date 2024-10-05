package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.basic;

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


public class TransferGrabber extends NetworkDirectional implements RecipeDisplayItem, Configurable {
    private static final int DEFAULT_GRAB_ITEM_TICK = 1;
    private static final boolean DEFAULT_USE_SPECIAL_MODEL = false;

    private static final String KEY_UUID = "display-uuid";
    private final HashMap<Location, Integer> TICKER_MAP = new HashMap<>();
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private int grabItemTick;

    public TransferGrabber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.TRANSFER_GRABBER);
        loadConfigurations();
    }

    public void loadConfigurations() {
        String configKey = getId();
        FileConfiguration config = Networks.getInstance().getConfig();

        this.grabItemTick = config.getInt("items." + configKey + ".grabitem-tick", DEFAULT_GRAB_ITEM_TICK);
        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", DEFAULT_USE_SPECIAL_MODEL);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("cloche", DisplayGroupGenerators::generateCloche);

        this.displayGroupGenerator = null;

        if (this.useSpecialModel) {
            String generatorKey = config.getString("items." + configKey + ".use-special-model.type");
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

        final BlockFace direction = this.getCurrentDirection(blockMenu);
        if (direction == BlockFace.SELF) {
            return;
        }

        final NetworkRoot root = definition.getNode().getRoot();

        LineOperationUtil.doOperation(blockMenu.getLocation(), direction, 1, true, (targetMenu) -> {
            LineOperationUtil.grabItem(root, targetMenu, TransportMode.FIRST_STOP, 64);
        });
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.LIME, 5);
    }

    @Override
    public void onPlace(@Nonnull BlockPlaceEvent e) {
        super.onPlace(e);
        if (useSpecialModel) {
            e.getBlock().setType(Material.BARRIER);
            setupDisplay(e.getBlock().getLocation());
        }
    }

    @Override
    public void postBreak(@Nonnull BlockBreakEvent e) {
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
                "&a⇩传输数据⇩",
                "",
                "&7[&a抓取频率&7]&f:&7 每 &6" + grabItemTick + " SfTick &7抓取一次"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩参数⇩",
                "&7默认运输模式: &6首位阻断",
                "&c不可调整运输模式",
                "&7默认运输数量: &664",
                "&c不可调整运输数量"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩功能⇩",
                "",
                "&e与链式不同的是，此机器&c只有连续抓取的功能",
                "&c而不是连续转移物品！"
        ));
        return displayRecipes;
    }
}
