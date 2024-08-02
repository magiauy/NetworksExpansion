package com.ytdd9527.networks.expansion.core.items.machines.stackmachines;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.core.data.attributes.WorkingStatus;
import com.ytdd9527.networks.expansion.core.data.attributes.WorkingRecipe;
import com.ytdd9527.networks.expansion.setup.ExpansionItemStacks;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.slimefun.NetworksSlimefunItemStacks;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.sefiraat.networks.utils.datatypes.PersistentQuantumStorageType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractStackMachine extends NetworkObject {
    static final Path SAVEDITEM_FOLDER = (new File(Networks.getInstance().getDataFolder().toPath().toString(), "saveditems")).toPath();
    final int[] BACKGROUND_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 11, 14, 17,
            18, 20, 23, 26,
            27, 29, 32, 35,
            36, 38, 41, 44,
            45, 46, 47, 48, 49, 50, 51, 52
    };
    final int[] INPUT_SLOTS = {
            12, 13,
            21, 22,
            30, 31,
            39, 40
    };
    static final int[] STORAGE_SLOTS = {
            15, 16,
            24, 25,
            33, 34,
            42, 43
    };
    final int WORKING_STATUS_SLOT = 10;
    final int MACHINE_INPUT_SLOT = 19;
    final int TIPS_BACKGROUND_SLOT = 28;
    final int MACHINE_OUTPUT_SLOT = 37;
    final int RECIPE_DISPLAY_SLOT = 53;
    static final Map<SlimefunItemStack, Integer> quantumMap = new HashMap<>();
    static {
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_0, 64);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_1, 4096);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_2, 32768);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_3, 262144);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_4, 2097152);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_5, 16777216);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_6, 134217728);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_7, 1073741824);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_8, Integer.MAX_VALUE);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_9, 256);
        quantumMap.put(NetworksSlimefunItemStacks.NETWORK_QUANTUM_STORAGE_10, 1024);
        quantumMap.put(ExpansionItemStacks.ADVANCED_QUANTUM_STORAGE, Integer.MAX_VALUE);
    }
    final Map<Location, Integer> workingAmount = new HashMap<>();
    final Map<Location, String> workingMachineId = new HashMap<>();
    final Map<Location, WorkingRecipe> workingRecipe = new HashMap<>();
    final Map<Location, Integer> workingProgress = new HashMap<>();
    final Map<Location, Integer> maxWorkingProgress = new HashMap<>();
    final Map<Location, NetworkRoot> networkRoots = new HashMap<>();
    final Map<Location, Integer> workingLevels = new HashMap<>();
    final Map<Location, Set<ItemStack>> leftovers = new HashMap<>();

    public AbstractStackMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, NodeType type) {
        super(itemGroup, item, recipeType, recipe, type);
        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                for (int slot : BACKGROUND_SLOTS) {
                    addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(WORKING_STATUS_SLOT, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(TIPS_BACKGROUND_SLOT, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                addItem(RECIPE_DISPLAY_SLOT, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
            }

            @Override
            public void newInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block) {
                viewingDo(blockMenu, block);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || (canUse(player, false) && Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
    }

    public static Path getSavedItemsFolder() {
        return SAVEDITEM_FOLDER;
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent event, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                onBreak(event);
            }
        });

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem sfItem, Config config) {
                onTick(block);
            }
        });

    }

    public void setWorkingStatus(@Nonnull BlockMenu blockMenu, WorkingStatus workingStatus) {
        if (blockMenu.hasViewer()) {
            blockMenu.replaceExistingItem(WORKING_STATUS_SLOT, getWorkingStatusItem(blockMenu.getLocation(), workingStatus));
        }
        StorageCacheUtils.setData(blockMenu.getLocation(), "working_status", workingStatus.toString());
    }

    public ItemStack getWorkingStatusItem(Location location, WorkingStatus workingStatus) {
        return switch (workingStatus) {
            case NO_NETWORK -> new CustomItemStack(
                    Material.RED_STAINED_GLASS_PANE,
                    "&a工作状态",
                    "&c未找到网络！"
            );
            case NO_ENOUGH_ENERGY -> new CustomItemStack(
                    Material.RED_STAINED_GLASS_PANE,
                    "&a工作状态",
                    "&c网络电力不足！"
            );
            case NO_MACHINE -> new CustomItemStack(
                    Material.RED_STAINED_GLASS_PANE,
                    "&a工作状态",
                    "&c等待中..."
            );
            case WRONG_MACHINE -> new CustomItemStack(
                    Material.RED_STAINED_GLASS_PANE,
                    "&a工作状态",
                    "&c无效的机器！"
            );
            case NO_MATCH -> new CustomItemStack(
                    Material.RED_STAINED_GLASS_PANE,
                    "&a工作状态",
                    "&c无匹配的配方！"
            );
            case WRONG_ENVIRONMENT -> new CustomItemStack(
                    Material.RED_STAINED_GLASS_PANE,
                    "&a工作状态",
                    "&c当前环境不符合条件！"
            );
            case FULL_OUTPUT -> getLeftoverItemStack(location);
            case WORKING -> getWorkingItemStack(location);
        };
    }

    public long getWorkingPower(Location location) {
        return (long) getWorkingRecipe(location).getEnergy() * getWorkingAmount(location);
    }

    public int getWorkingAmount(Location location) {
        return this.workingAmount.getOrDefault(location, 0);
    }

    public String getWorkingMachineId(Location location) {
        return this.workingMachineId.getOrDefault(location, "");
    }

    public WorkingRecipe getWorkingRecipe(Location location) {
        return this.workingRecipe.getOrDefault(location, null);
    }

    public int getWorkingProgress(Location location) {
        return this.workingProgress.getOrDefault(location, 0);
    }

    public int getMaxWorkingProgress(Location location) {
        return this.maxWorkingProgress.getOrDefault(location, 0);
    }

    public NetworkRoot getNetworkRoot(Location location) {
        return this.networkRoots.getOrDefault(location, null);
    }

    public int getWorkingLevel(Location location) {
        return this.workingLevels.getOrDefault(location, 0);
    }

    public Set<ItemStack> getLeftovers(Location location) {
        return this.leftovers.getOrDefault(location, null);
    }

    public void setWorkingAmount(Location location, int amount) {
        this.workingAmount.put(location, amount);
        StorageCacheUtils.setData(location, "working_amount", String.valueOf(amount));
    }

    public void setWorkingMachineId(Location location, String machineId) {
        this.workingMachineId.put(location, machineId);
        StorageCacheUtils.setData(location, "working_machine_id", machineId);
    }

    public void setWorkingRecipe(Location location, WorkingRecipe recipe) {
        this.workingRecipe.put(location, recipe);
    }

    public void setWorkingProgress(Location location, int progress) {
        this.workingProgress.put(location, progress);
    }

    public void setMaxWorkingProgress(Location location, int maxProgress) {
        this.maxWorkingProgress.put(location, maxProgress);
    }

    public void setNetworkRoot(Location location, NetworkRoot networkRoot) {
        this.networkRoots.put(location, networkRoot);
    }

    public void setWorkingLevel(Location location, int level) {
        this.workingLevels.put(location, level);
    }

    public void setLeftovers(Location location, Set<ItemStack> leftovers) {
        this.leftovers.put(location, leftovers);
    }

    public Map<Location, Integer> getWorkingAmounts() {
        return this.workingAmount;
    }

    public Map<Location, String> getWorkingMachineIds() {
        return this.workingMachineId;
    }

    public Map<Location, WorkingRecipe> getWorkingRecipes() {
        return this.workingRecipe;
    }

    public Map<Location, Integer> getWorkingProgresses() {
        return this.workingProgress;
    }

    public Map<Location, Integer> getMaxWorkingProgresses() {
        return this.maxWorkingProgress;
    }

    public Map<Location, NetworkRoot> getNetworkRoots() {
        return this.networkRoots;
    }

    public Map<Location, Integer> getWorkingLevels() {
        return this.workingLevels;
    }

    public Map<Location, Set<ItemStack>> getLeftoversMap() {
        return this.leftovers;
    }

    public String getShowingName(Map<ItemStack, Integer> items) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ItemStack item : items.keySet()) {
            stringBuilder
                    .append(ItemStackHelper.getDisplayName(item))
                    .append(" x ")
                    .append(items.get(item))
                    .append(", ");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        return stringBuilder.toString();
    }

    public String getShowingName(Set<ItemStack> items) {
        Map<ItemStack, Integer> map = new HashMap<>();
        for (ItemStack item : items) {
            map.put(StackUtils.getAsQuantity(item, 1), item.getAmount());
        }
        return getShowingName(map);
    }

    public ItemStack getLeftoverItemStack(Location location) {
        return new CustomItemStack(
                Material.RED_STAINED_GLASS_PANE,
                "&a工作状态",
                "&c阻塞中! ",
                "&a当前工作机器: " + ItemStackHelper.getDisplayName(SlimefunItem.getById(getWorkingMachineId(location)).getItem()),
                "&a当前已有机器数量: " + getWorkingAmount(location),
                "&a当前工作机器数量: " + getWorkingLevel(location),
                "&c当前阻塞物品: " + getShowingName(getLeftovers(location))
        );
    }

    public ItemStack getWorkingItemStack(Location location) {
        return new CustomItemStack(
                Material.LIME_STAINED_GLASS_PANE,
                "&a工作状态",
                "&a工作中! ",
                "&a当前工作所需电力: " + getWorkingPower(location) + " J/tick",
                "&a当前工作机器: " + ItemStackHelper.getDisplayName(SlimefunItem.getById(getWorkingMachineId(location)).getItem()),
                "&a当前已有机器数量: " + getWorkingAmount(location),
                "&a当前工作机器数量: " + getWorkingLevel(location),
                "&a当前工作进度: " + getWorkingProgress(location) + " / " + getMaxWorkingProgress(location),
                "&a输入: " + getShowingName(getWorkingRecipe(location).getInputs()),
                "&a输出: " + getShowingName(getWorkingRecipe(location).getOutputs())
        );
    }

    public void viewingDo(BlockMenu blockMenu, Block block) {
        Location location = block.getLocation();
        NodeDefinition definition = NetworkStorage.getAllNetworkObjects().get(location);
        if (definition == null || definition.getType() == null) {
            setWorkingStatus(blockMenu, WorkingStatus.NO_NETWORK);
            return;
        }
        networkRoots.put(location, definition.getNode().getRoot());
        ItemStack workingMachine = blockMenu.getItemInSlot(MACHINE_OUTPUT_SLOT);
        if (workingMachine == null || workingMachine.getType().isAir()) {
            workingMachine = SlimefunItem.getById(getWorkingMachineId(location)).getItem();
            ItemStack clone = workingMachine.clone();
            int canTake = Math.min(workingMachine.getMaxStackSize(), getWorkingAmount(location));
            clone.setAmount(canTake);
            setWorkingAmount(location, getWorkingAmount(location) - canTake);
            blockMenu.replaceExistingItem(MACHINE_OUTPUT_SLOT, clone);
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(workingMachine);
        if (sfItem == null) {
            setWorkingStatus(blockMenu, WorkingStatus.WRONG_MACHINE);
            return;
        }

        if (!isValidMachine(sfItem.getId())) {
            setWorkingStatus(blockMenu, WorkingStatus.WRONG_MACHINE);
            return;
        }
        setWorkingMachineId(location, sfItem.getId());

        String bs_workingAmount = StorageCacheUtils.getData(location, "working_amount");
        if (bs_workingAmount == null) {
            setWorkingStatus(blockMenu, WorkingStatus.NO_MACHINE);
            return;
        }
        int amount = Integer.parseInt(bs_workingAmount);
        setWorkingAmount(location, amount);
    }

    @Override
    public void onBreak(@Nonnull BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        String id = getWorkingMachineId(location);
        if (id != null) {
            ItemStack itemStack = SlimefunItem.getById(id).getItem();
            itemStack.setAmount(getWorkingAmount(location));
            location.getWorld().dropItem(location, itemStack);
        }
        workingAmount.remove(location);
        workingMachineId.remove(location);
        workingRecipe.remove(location);
        workingProgress.remove(location);
        maxWorkingProgress.remove(location);
        networkRoots.remove(location);
        workingLevels.remove(location);
        StorageCacheUtils.removeData(location, "working_amount");
    }

    public abstract boolean isValidMachine(String machineId);

    public void onTick(Block block) {
        BlockMenu blockMenu = StorageCacheUtils.getMenu(block.getLocation());
        if (blockMenu != null && blockMenu.hasViewer()) {
            viewingDo(blockMenu, block);
        }
    }

    /**
     *
     * 寻找第一个存在需求物品的存储，并尝试从中获取物品
     */
    public static ItemStack getItemStack(@Nonnull BlockMenu blockMenu, @Nonnull NetworkRoot root, @Nonnull ItemRequest ir) {
        ItemStack stackToReturn = null;
        for (int slot : STORAGE_SLOTS) {
            ItemStack item = blockMenu.getItemInSlot(slot);
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (!(sfItem instanceof NetworkQuantumStorage)) {
                continue;
            }
            ItemMeta meta = item.getItemMeta();
            QuantumCache cache = DataTypeMethods.getCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE);
            if (cache == null || cache.getItemStack() == null) {
                continue;
                // goto 补充物品
            }
            if (StackUtils.itemsMatch(cache.getItemStack(), ir.getItemStack())) {
                int canReceive = (int) Math.min(cache.getAmount(), ir.getAmount());
                if (stackToReturn == null) {
                    stackToReturn = StackUtils.getAsQuantity(cache.getItemStack(), 0);
                }
                cache.setAmount((int) (cache.getAmount() - canReceive));
                DataTypeMethods.setCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE, cache);
                ir.receiveAmount(canReceive);
                stackToReturn.setAmount(stackToReturn.getAmount() + canReceive);
                if (ir.getAmount() <= 0) {
                    return stackToReturn;
                }
            }
        }

        if (stackToReturn != null && stackToReturn.getAmount() > 0) {
            return stackToReturn;
        }

        // 补充物品，这里之后总是返回null
        for (int slot : STORAGE_SLOTS) {
            ItemStack item = blockMenu.getItemInSlot(slot);
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (!(sfItem instanceof NetworkQuantumStorage)) {
                continue;
            }
            ItemMeta meta = item.getItemMeta();
            QuantumCache cache = DataTypeMethods.getCustom(meta, Keys.QUANTUM_STORAGE_INSTANCE, PersistentQuantumStorageType.TYPE);
            ItemRequest tir = new ItemRequest(ir.getItemStack(), ir.getAmount());
            int limit = 0;
            int max = 0;
            if (cache == null) {
                max = limit = quantumMap.getOrDefault(item, 0);
            } else if (cache.getItemStack() == null) {
                max = limit = cache.getLimit();
            } else if (StackUtils.itemsMatch(cache.getItemStack(), ir.getItemStack())) {
                limit = (int) (cache.getLimit() - cache.getAmount());
                max = cache.getLimit();
            }
            tir.setAmount(Math.min(limit, ir.getAmount()));
            ItemStack received = root.getItemStack(tir);
            if (received == null) {
                return null;
            }
            tir.receiveAmount(received.getAmount());
            DataTypeMethods.setCustom(
                    meta,
                    Keys.QUANTUM_STORAGE_INSTANCE,
                    PersistentQuantumStorageType.TYPE,
                    new QuantumCache(
                            StackUtils.getAsQuantity(received, 1),
                            received.getAmount(),
                            max,
                            false,
                            false
                    )
            );
            item.setItemMeta(meta);
            return null;
        }
        return null;
    }
}
