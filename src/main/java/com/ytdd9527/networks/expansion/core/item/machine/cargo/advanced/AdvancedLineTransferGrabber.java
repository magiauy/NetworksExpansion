package com.ytdd9527.networks.expansion.core.item.machine.cargo.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.core.enums.TransportMode;
import com.ytdd9527.networks.expansion.util.DisplayGroupGenerators;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class AdvancedLineTransferGrabber extends AdvancedDirectional implements RecipeDisplayItem {
    private static BukkitTask transferTask;
    private static final String KEY_UUID = "display-uuid";
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private static final int TRANSPORT_LIMIT = 3456;

    private static final int[] BACKGROUND_SLOTS = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 16, 17, 18, 19, 21, 23, 24, 25, 26, 28, 29, 21, 31, 32, 34, 35, 39, 40, 41, 42, 43, 44
    };
    // 抓取不需要设置运输模式
    private static final int TRANSPORT_MODE_SLOT = 27;
    private static final int MINUS_SLOT = 36;
    private static final int SHOW_SLOT = 37;
    private static final int ADD_SLOT = 38;

    private int grabItemTick;
    private int maxDistance;

    private static final Map<Location, Integer> GRAB_TICKER_MAP = new HashMap<>();

    public AdvancedLineTransferGrabber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String configKey) {
        super(itemGroup, item, recipeType, recipe, NodeType.LINE_TRANSMITTER_GRABBER);
        loadConfigurations(configKey);
    }

    @Override
    public boolean comeMaxLimit(int currentNumber) {
        return currentNumber > TRANSPORT_LIMIT;
    }

    @Override
    public int getMaxLimit() {
        return TRANSPORT_LIMIT;
    }

    private void loadConfigurations(String configKey) {
        FileConfiguration config = Networks.getInstance().getConfig();

        int defaultMaxDistance = 64;
        int defaultGrabItemTick = 1;
        boolean defaultUseSpecialModel = false;

        this.maxDistance = config.getInt("items." + configKey + ".max-distance", defaultMaxDistance);
        this.grabItemTick = config.getInt("items." + configKey + ".grabitem-tick", defaultGrabItemTick);
        this.useSpecialModel = config.getBoolean("items." + configKey + ".use-special-model.enable", defaultUseSpecialModel);


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
    private void performGrabItemOperation(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            tryGrabItem(blockMenu);
        }
    }

    public static void cancelTransferTask() {
        if (transferTask != null && !transferTask.isCancelled()) {
            transferTask.cancel();
        }
    }
    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);
        final Location location = block.getLocation();
        int tickCounter = getTickCounter(location);
        tickCounter = (tickCounter + 1) % grabItemTick;
        if (tickCounter == 0) {
            performGrabItemOperation(blockMenu);
        }
        updateTickCounter(location, tickCounter);
    }
    private int getTickCounter(Location location) {
        final Integer tickCounter = GRAB_TICKER_MAP.get(location);
        if (tickCounter == null) {
            GRAB_TICKER_MAP.put(location, 0);
            return 0;
        } else {
            return tickCounter;
        }
    }
    private void updateTickCounter(Location location, int tickCounter) {
        GRAB_TICKER_MAP.put(location, tickCounter);
    }
    private void tryGrabItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }
        final NetworkRoot root = definition.getNode().getRoot();

        final BlockFace direction = this.getCurrentDirection(blockMenu);
        final TransportMode mode = this.getCurrentTransportMode(blockMenu.getLocation());
        Block currentBlock = blockMenu.getBlock().getRelative(direction);

        for (int i = 0; i < maxDistance; i++) {
            // 如果方块是空气，退出
            if (currentBlock.getType().isAir()) {
                break;
            }

            BlockMenu targetMenu = StorageCacheUtils.getMenu(currentBlock.getLocation());
            // 如果没有blockMenu，退出
            if (targetMenu == null) {
                break;
            }
            // 获取输出槽
            final int[] slots = targetMenu.getPreset().getSlotsAccessedByItemTransport(targetMenu, ItemTransportFlow.WITHDRAW, null);
            int free = getCurrentNumber(blockMenu.getLocation());

            switch (mode) {
                case NULL_ONLY -> {
                    // 无操作
                }
                case NONE, NONNULL_ONLY -> {
                    for (int slot : slots) {
                        ItemStack itemStack = targetMenu.getItemInSlot(slot);
                        if (itemStack != null && !itemStack.getType().isAir()) {
                            int canConsume = Math.min(itemStack.getAmount(), free);
                            ItemStack clone = itemStack.clone();
                            clone.setAmount(canConsume);
                            root.addItemStack(clone);
                            int consumed = canConsume - clone.getAmount();
                            itemStack.setAmount(itemStack.getAmount() - consumed);
                            free -= consumed;
                            if (free <= 0) {
                                break;
                            }
                        }
                    }
                }
                case FIRST_ONLY -> {
                    int slot = slots[0];
                    ItemStack itemStack = targetMenu.getItemInSlot(slot);
                    if (itemStack != null && !itemStack.getType().isAir()) {
                        int canConsume = Math.min(itemStack.getAmount(), free);
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(canConsume);
                        root.addItemStack(clone);
                        int consumed = canConsume - clone.getAmount();
                        itemStack.setAmount(itemStack.getAmount() - consumed);
                        free -= consumed;
                        if (free <= 0) {
                            break;
                        }
                    }
                }
                case LAST_ONLY -> {
                    int slot = slots[slots.length - 1];
                    ItemStack itemStack = targetMenu.getItemInSlot(slot);
                    if (itemStack != null && !itemStack.getType().isAir()) {
                        int canConsume = Math.min(itemStack.getAmount(), free);
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(canConsume);
                        root.addItemStack(clone);
                        int consumed = canConsume - clone.getAmount();
                        itemStack.setAmount(itemStack.getAmount() - consumed);
                        free -= consumed;
                        if (free <= 0) {
                            break;
                        }
                    }
                }
                case FIRST_STOP -> {
                    for (int slot : slots) {
                        ItemStack itemStack = targetMenu.getItemInSlot(slot);
                        if (itemStack != null && !itemStack.getType().isAir()) {
                            int canConsume = Math.min(itemStack.getAmount(), free);
                            ItemStack clone = itemStack.clone();
                            clone.setAmount(canConsume);
                            root.addItemStack(clone);
                            int consumed = canConsume - clone.getAmount();
                            itemStack.setAmount(itemStack.getAmount() - consumed);
                            free -= consumed;
                            break;
                        }
                    }
                }
            }
            currentBlock = currentBlock.getRelative(direction);
        }
    }
    @Override
    protected Particle.DustOptions getDustOptions() {
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

    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    protected int getMinusSlot() {
        return MINUS_SLOT;
    }

    protected int getShowSlot() {
        return SHOW_SLOT;
    }

    protected int getAddSlot() {
        return ADD_SLOT;
    }


    @NotNull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes  = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩传输数据⇩",
                "",
                "&7[&a最大距离&7]&f:&6" + maxDistance + "方块",
                "&7[&a抓取频率&7]&f:&7 每 &6" + grabItemTick + " SfTick &7抓取一次"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩参数⇩",
                "&7默认运输模式: &6无限制",
                "&a可调整运输模式",
                "&7默认运输数量: &63456",
                "&a可调整运输数量"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩功能⇩",
                "",
                "&e与链式不同的是，此机器&c只有连续抓取的功能",
                "&c而不是连续转移物品！"
        ));
        return displayRecipes ;
    }

    @Override
    protected int getTransportModeSlot() {
        return TRANSPORT_MODE_SLOT;
    }
}
