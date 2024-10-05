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
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class LineTransfer extends NetworkDirectional implements RecipeDisplayItem, Configurable {
    public static final int DEFAULT_MAX_DISTANCE = 32;
    public static final int DEFAULT_PUSH_ITEM_TICK = 1;
    public static final int DEFAULT_GRAB_ITEM_TICK = 1;
    public static final int DEFAULT_REQUIRED_POWER = 5000;
    public static final boolean DEFAULT_USE_SPECIAL_MODEL = false;
    public static final CustomItemStack TEMPLATE_BACKGROUND_STACK = new CustomItemStack(
            Material.BLUE_STAINED_GLASS_PANE, Theme.PASSIVE + "Push Item Matching"
    );
    private static final int PARTICLE_INTERVAL = 2;
    private static final int[] BACKGROUND_SLOTS = new int[]{
            0,
            10,
            18,
            27, 28, 29,
            36, 37, 38,
            45, 46, 47
    };
    private static final int[] TEMPLATE_BACKGROUND = new int[]{
            3,
            12,
            21,
            30,
            39,
            48
    };
    private static final int[] TEMPLATE_SLOTS = new int[]{
            4, 5, 6, 7, 8,
            13, 14, 15, 16, 17,
            22, 23, 24, 25, 26,
            31, 32, 33, 34, 35,
            40, 41, 42, 43, 44,
            49, 50, 51, 52, 53,
    };
    private static final int NORTH_SLOT = 1;
    private static final int SOUTH_SLOT = 19;
    private static final int EAST_SLOT = 11;
    private static final int WEST_SLOT = 9;
    private static final int UP_SLOT = 2;
    private static final int DOWN_SLOT = 20;
    private static final String KEY_UUID = "display-uuid";
    private final HashMap<Location, Integer> PUSH_TICKER_MAP = new HashMap<>();
    private final HashMap<Location, Integer> GRAB_TICKER_MAP = new HashMap<>();
    private int maxDistance;
    private int pushItemTick;
    private int grabItemTick;
    private int requiredPower;
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;

    public LineTransfer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String itemId) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER);
        for (int slot : TEMPLATE_SLOTS) {
            this.getSlotsToDrop().add(slot);
        }
        loadConfigurations(itemId);
    }

    private void loadConfigurations(String itemId) {
        int defaultMaxDistance = 32;
        int defaultPushItemTick = 1;
        int defaultGrabItemTick = 1;
        int defaultRequiredPower = 5000;
        boolean defaultUseSpecialModel = false;

        FileConfiguration config = Networks.getInstance().getConfig();

        this.maxDistance = config.getInt("items." + itemId + ".max-distance", defaultMaxDistance);
        this.pushItemTick = config.getInt("items." + itemId + ".pushitem-tick", defaultPushItemTick);
        this.grabItemTick = config.getInt("items." + itemId + ".grabitem-tick", defaultGrabItemTick);
        this.requiredPower = config.getInt("items." + itemId + ".required-power", defaultRequiredPower);
        this.useSpecialModel = config.getBoolean("items." + itemId + ".use-special-model.enable", defaultUseSpecialModel);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("cloche", DisplayGroupGenerators::generateCloche);
        generatorMap.put("cell", DisplayGroupGenerators::generateCell);

        this.displayGroupGenerator = null;

        if (this.useSpecialModel) {
            String generatorKey = config.getString("items." + itemId + ".use-special-model.type");
            this.displayGroupGenerator = generatorMap.get(generatorKey);
            if (this.displayGroupGenerator == null) {
                Networks.getInstance().getLogger().warning("未知类型 '" + generatorKey + "', 模型已禁用。");
                this.useSpecialModel = false;
            }
        }
    }

    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        final Location location = block.getLocation();

        if (blockMenu == null) {
            return;
        }

        if (pushItemTick != 1) {
            int currentPushTick = getPushTickCounter(location);
            if (currentPushTick == 0) {
                tryPushItem(blockMenu);
            }
            currentPushTick = (currentPushTick + 1) % pushItemTick;
            updatePushTickCounter(location, currentPushTick);
        } else {
            tryPushItem(blockMenu);
        }

        if (grabItemTick != 1) {
            int currentGrabTick = getGrabTickCounter(location);
            if (currentGrabTick == 0) {
                tryGrabItem(blockMenu);
            }
            currentGrabTick = (currentGrabTick + 1) % grabItemTick;
            updateGrabTickCounter(location, currentGrabTick);
        } else {
            tryGrabItem(blockMenu);
        }

    }

    private int getPushTickCounter(Location location) {
        final Integer ticker = PUSH_TICKER_MAP.get(location);
        if (ticker == null) {
            PUSH_TICKER_MAP.put(location, 0);
            return 0;
        }
        return ticker;
    }

    private int getGrabTickCounter(Location location) {
        final Integer ticker = GRAB_TICKER_MAP.get(location);
        if (ticker == null) {
            GRAB_TICKER_MAP.put(location, 0);
            return 0;
        }
        return ticker;
    }

    private void updatePushTickCounter(Location location, int ticker) {
        PUSH_TICKER_MAP.put(location, ticker);
    }

    private void updateGrabTickCounter(Location location, int ticker) {
        GRAB_TICKER_MAP.put(location, ticker);
    }

    private void tryPushItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getNode(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final BlockFace direction = this.getCurrentDirection(blockMenu);
        if (direction == BlockFace.SELF) {
            return;
        }

        List<ItemStack> templates = new ArrayList<>();
        for (int slot : this.getItemSlots()) {
            final ItemStack template = blockMenu.getItemInSlot(slot);
            if (template != null && template.getType() != Material.AIR) {
                templates.add(StackUtils.getAsQuantity(template, 1));
            }
        }

        final NetworkRoot root = definition.getNode().getRoot();
        final BlockFace direction = this.getCurrentDirection(blockMenu);

        final boolean drawParticle = blockMenu.hasViewer();
        LineOperationUtil.doOperation(
                blockMenu.getLocation(),
                direction,
                maxDistance,
                false,
                false,
                drawParticle,
                PARTICLE_INTERVAL,
                (targetMenu) -> {
                    LineOperationUtil.pushItem(root, targetMenu, templates, TransportMode.FIRST_STOP, 64);
                });
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

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @Nullable
    @Override
    protected int[] getOtherBackgroundSlots() {
        return TEMPLATE_BACKGROUND;
    }

    @Nullable
    @Override
    protected CustomItemStack getOtherBackgroundStack() {
        return TEMPLATE_BACKGROUND_STACK;
    }

    @Override
    public int getNorthSlot() {
        return NORTH_SLOT;
    }

    @Override
    public int getSouthSlot() {
        return SOUTH_SLOT;
    }

    @Override
    public int getEastSlot() {
        return EAST_SLOT;
    }

    @Override
    public int getWestSlot() {
        return WEST_SLOT;
    }

    @Override
    public int getUpSlot() {
        return UP_SLOT;
    }

    @Override
    public int getDownSlot() {
        return DOWN_SLOT;
    }

    @Override
    public int[] getItemSlots() {
        return TEMPLATE_SLOTS;
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
                "&aTransferring data",
                "",
                "&7[&aMaximum distance&7]&f: &6" + maxDistance + " blocks",
                "&7[&aPush rate&7]&f:&7 Every &6" + pushItemTick + " SfTick &7Push once",
                "&7[&aGrab rate&7]&f:&7 Every &6" + grabItemTick + " SfTick &7Grab once",
                "&7[&aPower needed&7]&f:&7 Per transport &6" + requiredPower + " J Network Power"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aParameter",
                "&7Default mode: &6First Stop",
                "&cUnchangable mode",
                "&7Default quantity: &664",
                "&cUnchangable quantity"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aFeature",
                "",
                "&eLine transfer will grab and push items",
                "&ethrough a line of machines it facing."
        ));
        return displayRecipes;
    }
}