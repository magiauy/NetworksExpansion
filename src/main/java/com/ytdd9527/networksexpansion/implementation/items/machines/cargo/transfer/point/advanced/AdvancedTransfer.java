package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.transfer.point.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.enums.TransportMode;
import com.ytdd9527.networksexpansion.api.interfaces.Configurable;
import com.ytdd9527.networksexpansion.core.items.machines.AdvancedDirectional;
import com.ytdd9527.networksexpansion.utils.DisplayGroupGenerators;
import com.ytdd9527.networksexpansion.utils.LineOperationUtil;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
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

public class AdvancedTransfer extends AdvancedDirectional implements RecipeDisplayItem, Configurable {
    public static final CustomItemStack TEMPLATE_BACKGROUND_STACK = new CustomItemStack(
            Material.BLUE_STAINED_GLASS_PANE, Theme.PASSIVE + "指定需要推送的物品"
    );
    private static final int DEFAULT_PUSH_ITEM_TICK = 1;
    private static final int DEFAULT_GRAB_ITEM_TICK = 1;
    private static final int DEFAULT_REQUIRED_POWER = 5000;
    private static final boolean DEFAULT_USE_SPECIAL_MODEL = false;
    private static final int[] BACKGROUND_SLOTS = new int[]{
            0,
            10,
            18,
            27, 28, 29,
            36, 37, 38
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
    private static final int MINUS_SLOT = 45;
    private static final int SHOW_SLOT = 46;
    private static final int ADD_SLOT = 47;
    private static final int TRANSPORT_MODE_SLOT = 36;
    private static final String KEY_UUID = "display-uuid";
    private static final int TRANSPORT_LIMIT = 3456;
    private static final HashMap<Location, Integer> PUSH_TICKER_MAP = new HashMap<>();
    private static final HashMap<Location, Integer> GRAB_TICKER_MAP = new HashMap<>();
    private int pushItemTick;
    private int grabItemTick;
    private int requiredPower;
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;

    public AdvancedTransfer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.TRANSFER);
        for (int slot : TEMPLATE_SLOTS) {
            this.getSlotsToDrop().add(slot);
        }
        loadConfigurations();
    }

    @Override
    public boolean isExceedLimit(int quantity) {
        return quantity > TRANSPORT_LIMIT;
    }

    @Override
    public int getMaxLimit() {
        return TRANSPORT_LIMIT;
    }

    public void loadConfigurations() {
        String configKey = getId();
        FileConfiguration config = Networks.getInstance().getConfig();


        this.pushItemTick = config.getInt("items." + configKey + ".pushitem-tick", DEFAULT_PUSH_ITEM_TICK);
        this.grabItemTick = config.getInt("items." + configKey + ".grabitem-tick", DEFAULT_GRAB_ITEM_TICK);
        this.requiredPower = config.getInt("items." + configKey + ".required-power", DEFAULT_REQUIRED_POWER);
        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", DEFAULT_USE_SPECIAL_MODEL);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("cloche", DisplayGroupGenerators::generateCloche);
        generatorMap.put("cell", DisplayGroupGenerators::generateCell);

        this.displayGroupGenerator = null;

        if (this.useSpecialModel) {
            String generatorKey = config.getString("items." + configKey + ".use-special-model.type");
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
        if (ticker != null) {
            return ticker;
        } else {
            PUSH_TICKER_MAP.put(location, 0);
            return 0;
        }
    }

    private int getGrabTickCounter(Location location) {
        final Integer ticker = GRAB_TICKER_MAP.get(location);
        if (ticker != null) {
            return ticker;
        } else {
            GRAB_TICKER_MAP.put(location, 0);
            return 0;
        }
    }

    private void updatePushTickCounter(Location location, int pushTick) {
        PUSH_TICKER_MAP.put(location, pushTick);
    }

    private void updateGrabTickCounter(Location location, int grabTick) {
        GRAB_TICKER_MAP.put(location, grabTick);
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

        final NetworkRoot root = definition.getNode().getRoot();
        final TransportMode currentTransportMode = getCurrentTransportMode(blockMenu.getLocation());
        final int limitQuantity = getLimitQuantity(blockMenu.getLocation());

        List<ItemStack> templates = new ArrayList<>();
        for (int slot : this.getItemSlots()) {
            final ItemStack template = blockMenu.getItemInSlot(slot);
            if (template != null && template.getType() != Material.AIR) {
                templates.add(StackUtils.getAsQuantity(template, 1));
            }
        }

        LineOperationUtil.doOperation(blockMenu.getLocation(), direction, 1, false, (targetMenu) -> {
            LineOperationUtil.pushItem(root, targetMenu, templates, currentTransportMode, limitQuantity);
        });
    }

    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getNode(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final BlockFace direction = getCurrentDirection(blockMenu);
        if (direction == BlockFace.SELF) {
            return;
        }

        final NetworkRoot root = definition.getNode().getRoot();
        final int limitQuantity = getLimitQuantity(blockMenu.getLocation());
        final TransportMode mode = getCurrentTransportMode(blockMenu.getLocation());

        LineOperationUtil.doOperation(blockMenu.getLocation(), direction, 1, true, (targetMenu) -> {
            LineOperationUtil.grabItem(root, targetMenu, mode, limitQuantity);
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
                "&a⇩传输数据⇩",
                "",
                "&7[&a推送频率&7]&f:&7 每 &6" + pushItemTick + " SfTick &7推送一次",
                "&7[&a抓取频率&7]&f:&7 每 &6" + grabItemTick + " SfTick &7抓取一次",
                "&7[&a运输耗电&7]&f:&7 每次运输消耗 &6" + requiredPower + " J 网络电力"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩参数⇩",
                "&7默认运输模式: &6首位阻断",
                "&a可调整运输模式",
                "&7默认运输数量: &63456",
                "&a可调整运输数量"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩功能⇩",
                "",
                "&e与链式不同的是，此机器&c只有连续推送和抓取的功能",
                "&c而不是连续转移物品！"
        ));
        return displayRecipes;
    }

    @Override
    protected int getMinusSlot() {
        return MINUS_SLOT;
    }

    @Override
    protected int getShowSlot() {
        return SHOW_SLOT;
    }

    @Override
    protected int getAddSlot() {
        return ADD_SLOT;
    }

    @Override
    protected int getTransportModeSlot() {
        return TRANSPORT_MODE_SLOT;
    }
}
