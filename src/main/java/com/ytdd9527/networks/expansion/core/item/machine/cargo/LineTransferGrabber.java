package com.ytdd9527.networks.expansion.core.item.machine.cargo;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.util.DisplayGroupGenerators;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


public class LineTransferGrabber extends NetworkDirectional implements RecipeDisplayItem {
    private static BukkitTask transferTask;

    private static final String KEY_UUID = "display-uuid";
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;
    private static final ItemStack AIR = new CustomItemStack(Material.AIR);
    private int grabItemTick;
    private int maxDistance;
    private final HashMap<Location, Integer> TICKER_MAP = new HashMap<>();

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
    private void performGrabbingOperationAsync(@Nullable BlockMenu blockMenu) {
        if (blockMenu != null) {
            transferTask = new BukkitRunnable() {
                @Override
                public void run() {
                    tryGrabItem(blockMenu);
                }
            }.runTaskAsynchronously(Networks.getInstance());
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

        // 初始化Tick计数器
        final Location location = blockMenu.getLocation();
        int tickCounter = getTickCounter(location);
        tickCounter = (tickCounter + 1) % grabItemTick;

        // 每10个Tick执行一次抓取操作
        if (tickCounter == 0) {
            performGrabbingOperationAsync(blockMenu);
        }

        // 更新Tick计数器
        updateTickCounter(location, tickCounter);
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
        final NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }
        final NetworkRoot root = definition.getNode().getRoot();

        final BlockFace direction = this.getCurrentDirection(blockMenu);
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
            int[] slots = targetMenu.getPreset().getSlotsAccessedByItemTransport(targetMenu, ItemTransportFlow.WITHDRAW, null);
            for (int slot : slots) {
                ItemStack itemStack = targetMenu.getItemInSlot(slot);
                if (itemStack != null && !itemStack.getType().isAir()) {
                    root.addItemStack(itemStack);
                    break;
                }
            }
            currentBlock = currentBlock.getRelative(direction);
        }
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
        List<ItemStack> displayRecipes  = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩传输数据⇩",
                "",
                "&7[&a最大距离&7]&f:&6" + maxDistance + "方块",
                "&7[&a抓取频率&7]&f:&7 每 &6" + grabItemTick + " SfTick &7抓取一次"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩参数⇩",
                "&7默认运输模式: &6首位阻断",
                "&c不可调整运输模式",
                "&7默认运输数量: &664",
                "&c不可调整运输数量"
        ));
        displayRecipes.add(AIR);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&a⇩功能⇩",
                "",
                "&e与链式不同的是，此机器&c只有连续抓取的功能",
                "&c而不是连续转移物品！"
        ));
        return displayRecipes ;
    }
}
