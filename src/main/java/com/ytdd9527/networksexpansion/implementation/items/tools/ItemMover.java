package com.ytdd9527.networksexpansion.implementation.items.tools;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.implementation.items.ExpansionItemStacks;
import com.ytdd9527.networksexpansion.implementation.items.machines.unit.CargoStorageUnit;
import io.github.mooy1.infinityexpansion.items.storage.StorageCache;
import io.github.mooy1.infinityexpansion.items.storage.StorageUnit;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.managers.SupportedPluginManager;
import io.github.sefiraat.networks.network.barrel.FluffyBarrel;
import io.github.sefiraat.networks.network.barrel.InfinityBarrel;
import io.github.sefiraat.networks.network.barrel.NetworkStorage;
import io.github.sefiraat.networks.network.stackcaches.BarrelIdentity;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.network.stackcaches.QuantumCache;
import io.github.sefiraat.networks.slimefun.network.NetworkQuantumStorage;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.datatypes.DataTypeMethods;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.ncbpfluffybear.fluffymachines.items.Barrel;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ItemMover extends SpecialSlimefunItem implements DistinctiveItem {
    @Nonnull
    public static final List<String> DEFAULT_LORE = ExpansionItemStacks.ITEM_MOVER.getItemMeta() == null ? new ArrayList<>() : (ExpansionItemStacks.ITEM_MOVER.getItemMeta().hasLore() && ExpansionItemStacks.ITEM_MOVER.getItemMeta().getLore() != null ? ExpansionItemStacks.ITEM_MOVER.getItemMeta().getLore() : new ArrayList<>());

    public ItemMover(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        addItemHandler(
                (ItemUseHandler) e -> {
                    final Player player = e.getPlayer();
                    final Optional<Block> optional = e.getClickedBlock();
                    if (optional.isPresent()) {
                        final ItemStack itemStack = e.getItem();
                        if (itemStack.getType() != Material.DEBUG_STICK) {
                            player.sendMessage(ChatColor.RED + "不是一个有效的物品转移棒");
                            return;
                        }
                        if (itemStack.getAmount() != 1) {
                            player.sendMessage(ChatColor.RED + "不是一个有效的物品转移棒");
                            return;
                        }
                        if (!itemStack.hasItemMeta() || itemStack.getItemMeta() == null) {
                            player.sendMessage(ChatColor.RED + "不是一个有效的物品转移棒");
                            return;
                        }
                        final Location location = optional.get().getLocation();
                        if (!player.isSneaking()) {
                            // Right-click
                            tryDepositIntoMover(player, itemStack, location);
                        } else {
                            // Shift+Right-click
                            tryWithdrawFromMover(player, itemStack, location);
                        }
                    }
                    e.cancel();
                }
        );
    }

    @Nullable
    public static ItemStack getStoredItemStack(PersistentDataHolder holder) {
        if (holder == null) {
            return null;
        }

        return DataTypeMethods.getCustom(holder, Keys.ITEM_MOVER_ITEM, DataType.ITEM_STACK);
    }

    @Nullable
    public static ItemStack getStoredItemStack(ItemStack mover) {
        if (mover == null || mover.getType().isAir()) {
            return null;
        }

        final ItemMeta itemMeta = mover.getItemMeta();
        if (!mover.hasItemMeta() || itemMeta == null) {
            return null;
        }

        return getStoredItemStack(itemMeta);
    }

    public static int getStoredAmount(PersistentDataHolder holder) {
        if (holder == null) {
            return 0;
        }

        final Integer amount = DataTypeMethods.getCustom(holder, Keys.ITEM_MOVER_AMOUNT, PersistentDataType.INTEGER);
        if (amount == null) {
            return 0;
        }

        return amount;
    }

    public static int getStoredAmount(ItemStack mover) {
        if (mover == null || mover.getType().isAir()) {
            return 0;
        }

        final ItemMeta itemMeta = mover.getItemMeta();
        if (!mover.hasItemMeta() || itemMeta == null) {
            return 0;
        }

        return getStoredAmount(itemMeta);
    }

    public static void setStoredItemStack(ItemStack mover, @Nullable ItemStack itemStack) {
        if (mover == null || mover.getType().isAir()) {
            return;
        }

        final ItemMeta itemMeta = mover.getItemMeta();
        if (!mover.hasItemMeta() || itemMeta == null) {
            return;
        }

        if (itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() != 1) {
            clearPDC(mover);
            return;
        }

        DataTypeMethods.setCustom(itemMeta, Keys.ITEM_MOVER_ITEM, DataType.ITEM_STACK, itemStack);
        mover.setItemMeta(itemMeta);
    }

    public static void setStoredAmount(ItemStack mover, int amount) {
        if (mover == null || mover.getType().isAir()) {
            return;
        }

        final ItemMeta itemMeta = mover.getItemMeta();
        if (!mover.hasItemMeta() || itemMeta == null) {
            return;
        }

        if (amount <= 0) {
            clearPDC(mover);
        }

        DataTypeMethods.setCustom(itemMeta, Keys.ITEM_MOVER_AMOUNT, PersistentDataType.INTEGER, amount);
        mover.setItemMeta(itemMeta);
    }

    public static void depositItem(ItemStack mover, ItemStack incoming) {
        if (incoming == null || incoming.getType().isAir()) {
            return;
        }

        if (incoming.getAmount() > 0) {
            ItemStack stored = getStoredItemStack(mover);
            int storedAmount = getStoredAmount(mover);
            if (stored == null || stored.getType().isAir()) {
                setStoredItemStack(mover, StackUtils.getAsQuantity(incoming, 1));
                setStoredAmount(mover, incoming.getAmount());
                incoming.setAmount(0);
            } else if (StackUtils.itemsMatch(stored, incoming)) {
                int maxCanReceive = Integer.MAX_VALUE - storedAmount;
                int incomingAmount = incoming.getAmount();
                int canReceive = Math.min(maxCanReceive, incomingAmount);
                setStoredAmount(mover, getStoredAmount(mover) + canReceive);
                incoming.setAmount(incomingAmount - canReceive);
            }
        }
    }

    private static void clearPDC(ItemStack mover) {
        if (mover == null || mover.getType().isAir()) {
            return;
        }

        final ItemMeta itemMeta = mover.getItemMeta();
        if (!mover.hasItemMeta() || itemMeta == null) {
            return;
        }

        DataTypeMethods.removeCustom(itemMeta, Keys.ITEM_MOVER_ITEM);
        DataTypeMethods.removeCustom(itemMeta, Keys.ITEM_MOVER_AMOUNT);
    }

    public static void updateLore(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return;
        }

        if (!itemStack.hasItemMeta() || itemStack.getItemMeta() == null) {
            return;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        final ItemStack storedItemStack = getStoredItemStack(itemMeta);
        final int amount = getStoredAmount(itemMeta);

        List<String> lore = cloneDefaultLore();
        if (storedItemStack != null && amount > 0) {
            lore.add(ChatColor.AQUA + "已存储: " + ItemStackHelper.getDisplayName(storedItemStack));
            lore.add(ChatColor.AQUA + "数量: " + amount);
        } else {
            clearPDC(itemStack);
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    @Nullable
    public static BarrelIdentity getBarrel(@Nonnull Player player, @Nonnull Location location) {
        final SlimefunItem sfitem = StorageCacheUtils.getSfItem(location);

        if (sfitem == null) {
            return null;
        }

        final boolean infinityEnabled = SupportedPluginManager.getInstance().isInfinityExpansion();
        final boolean fluffyEnabled = SupportedPluginManager.getInstance().isFluffyMachines();

        /*if (infinityEnabled && sfitem instanceof StorageUnit unit) {
            return getInfinityBarrel(location, unit);
        } else */
        if (fluffyEnabled && sfitem instanceof Barrel barrel) {
            return getFluffyBarrel(location, barrel);
        } else if (sfitem instanceof NetworkQuantumStorage) {
            return getNetworkStorage(location);
        } else if (sfitem instanceof CargoStorageUnit) {
            player.sendMessage(ChatColor.RED + "请使用网络抽屉的快速转移模式");
            return null;
        }

        return null;
    }

    @Nullable
    public static InfinityBarrel getInfinityBarrel(@Nonnull Location location, @Nonnull StorageUnit unit) {
        StorageCache cache = unit.getCache(location);
        if (cache == null) {
            return null;
        }

        BlockMenu blockMenu = StorageCacheUtils.getMenu(location);
        if (blockMenu == null) {
            return null;
        }

        final SlimefunBlockData data = StorageCacheUtils.getBlock(blockMenu.getLocation());
        if (data == null) {
            return null;
        }

        final String storedString = data.getData("stored");
        if (storedString == null || storedString.isEmpty()) {
            return null;
        }

        final int stored;
        try {
            stored = Integer.parseInt(storedString);
        } catch (NumberFormatException e) {
            return null;
        }

        if (stored <= 0) {
            return null;
        }

        final ItemStack rawDisplayItem = blockMenu.getItemInSlot(13);
        final ItemStack displayItem = rawDisplayItem.clone();
        if (!displayItem.hasItemMeta()) {
            return null;
        }
        final ItemMeta displayItemMeta = displayItem.getItemMeta();
        if (displayItemMeta == null) {
            return null;
        }

        Byte correct = DataTypeMethods.getCustom(displayItemMeta, Keys.INFINITY_DISPLAY, PersistentDataType.BYTE);
        if (correct == null || correct != 1) {
            return null;
        }

        displayItemMeta.getPersistentDataContainer().remove(Keys.INFINITY_DISPLAY);
        displayItem.setItemMeta(displayItemMeta);

        return new InfinityBarrel(location, displayItem, stored, cache);
    }

    @Nullable
    private static FluffyBarrel getFluffyBarrel(@Nonnull Location location, @Nonnull Barrel barrel) {
        Block block = location.getBlock();
        ItemStack itemStack;
        try {
            itemStack = barrel.getStoredItem(block);
        } catch (NullPointerException ignored) {
            return null;
        }

        if (itemStack == null || itemStack.getType().isAir()) {
            return null;
        }

        final ItemStack clone = itemStack.clone();

        int stored = barrel.getStored(block);

        if (stored <= 0) {
            return null;
        }
        int limit = barrel.getCapacity(block);
        boolean voidExcess = Boolean.parseBoolean(StorageCacheUtils.getData(location, "trash"));

        return new FluffyBarrel(
                location,
                clone,
                stored,
                limit,
                voidExcess
        );
    }

    @Nullable
    public static NetworkStorage getNetworkStorage(@Nonnull Location location) {
        QuantumCache cache = NetworkQuantumStorage.getCaches().get(location);
        if (cache == null) {
            return null;
        }

        final ItemStack stored = cache.getItemStack();
        if (stored == null || stored.getType().isAir()) {
            return null;
        }

        final long storedAmount = cache.getAmount();
        if (storedAmount < 0) {
            return null;
        }

        return new NetworkStorage(location, stored, storedAmount);
    }

    private static void tryDepositIntoMover(Player player, ItemStack mover, Location clickedLocation) {
        int storedAmount = getStoredAmount(mover);
        ItemStack storedItemStack = getStoredItemStack(mover);
        BarrelIdentity barrel = getBarrel(player, clickedLocation);
        if (barrel == null || barrel.getItemStack() == null || barrel.getAmount() <= 0) {
            player.sendMessage(ChatColor.RED + "请选择一个有效的存储.");
            return;
        }

        int have = barrel.getAmount() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) barrel.getAmount();
        if (have <= 0) {
            player.sendMessage(ChatColor.RED + "存储中没有可用的物品.");
            return;
        }

        ItemRequest itemRequest = new ItemRequest(barrel.getItemStack(), 0);
        if (storedItemStack == null && storedAmount <= 0) {
            itemRequest.setAmount(have);
        } else if (StackUtils.itemsMatch(storedItemStack, barrel.getItemStack())) {
            int maxCanReceive = Integer.MAX_VALUE - storedAmount;
            itemRequest.setAmount(Math.min(maxCanReceive, have));
        }

        ItemStack fetched = barrel.requestItem(itemRequest);

        if (fetched == null || fetched.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "无法获取物品.");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "已存储 " + ItemStackHelper.getDisplayName(fetched) + " x" + fetched.getAmount() + " 至物品转移棒");
        depositItem(mover, fetched);
        updateLore(mover);
    }

    private static void tryWithdrawFromMover(Player player, ItemStack mover, Location clickedLocation) {
        int storedAmount = getStoredAmount(mover);
        ItemStack storedItemStack = getStoredItemStack(mover);
        BarrelIdentity barrel = getBarrel(player, clickedLocation);
        if (barrel == null) {
            player.sendMessage(ChatColor.RED + "请选择一个有效的存储.");
            return;
        }

        int have = barrel.getAmount() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) barrel.getAmount();

        if (storedItemStack == null || storedAmount <= 0) {
            player.sendMessage(ChatColor.RED + "没有可用的物品.");
            return;
        }

        if (have >= Integer.MAX_VALUE) {
            player.sendMessage(ChatColor.RED + "存储已满");
            return;
        }

        ItemStack clone = StackUtils.getAsQuantity(storedItemStack, storedAmount);
        player.sendMessage(ChatColor.GREEN + "已输出 " + ItemStackHelper.getDisplayName(clone) + " x" + clone.getAmount() + " 至存储.");
        barrel.depositItemStack(clone);
        setStoredAmount(mover, clone.getAmount());
        updateLore(mover);
    }

    private static List<String> cloneDefaultLore() {
        return new ArrayList<>(DEFAULT_LORE);
    }

    @Override
    public boolean canStack(@Nonnull ItemMeta itemMeta, @Nonnull ItemMeta itemMeta1) {
        return itemMeta.getPersistentDataContainer().equals(itemMeta1.getPersistentDataContainer());
    }
}
