package com.ytdd9527.networksexpansion.api.data;

import com.ytdd9527.networksexpansion.api.enums.StorageUnitType;
import com.ytdd9527.networksexpansion.implementation.items.machines.unit.CargoStorageUnit;
import com.ytdd9527.networksexpansion.utils.databases.DataStorage;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ToString
public class StorageUnitData {

    private final int id;
    private final OfflinePlayer owner;
    private final Map<Integer, ItemContainer> storedItems;
    private boolean isPlaced;
    private StorageUnitType sizeType;
    private Location lastLocation;

    public StorageUnitData(int id, String ownerUUID, StorageUnitType sizeType, boolean isPlaced, Location lastLocation) {
        this(id, Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)), sizeType, isPlaced, lastLocation, new HashMap<>());
    }

    public StorageUnitData(int id, String ownerUUID, StorageUnitType sizeType, boolean isPlaced, Location lastLocation, Map<Integer, ItemContainer> storedItems) {
        this(id, Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)), sizeType, isPlaced, lastLocation, storedItems);
    }

    public StorageUnitData(int id, OfflinePlayer owner, StorageUnitType sizeType, boolean isPlaced, Location lastLocation, Map<Integer, ItemContainer> storedItems) {
        this.id = id;
        this.owner = owner;
        this.sizeType = sizeType;
        this.isPlaced = isPlaced;
        this.lastLocation = lastLocation;
        this.storedItems = storedItems;
    }

    public static boolean isBlacklisted(@Nonnull ItemStack itemStack) {
        // if item is air, it's blacklisted
        if (itemStack.getType() == Material.AIR) {
            return true;
        }
        // if item has invalid durability, it's blacklisted
        if (itemStack.getType().getMaxDurability() < 0) {
            return true;
        }
        // if item is a shulker box, it's blacklisted
        if (Tag.SHULKER_BOXES.isTagged(itemStack.getType())) {
            return true;
        }
        // if item is a bundle, it's blacklisted
        if (itemStack.getType() == Material.BUNDLE) {
            return true;
        }

        return false;
    }

    /**
     * Add item to unit, the amount will be the item stack amount
     *
     * @param item: item will be added
     * @return the amount actual added
     */
    public int addStoredItem(ItemStack item, boolean contentLocked) {
        return addStoredItem(item, item.getAmount(), contentLocked, false);
    }

    public int addStoredItem(ItemStack item, boolean contentLocked, boolean force) {
        return addStoredItem(item, item.getAmount(), contentLocked, force);
    }

    public int addStoredItem(ItemStack item, int amount, boolean contentLocked) {
        return addStoredItem(item, amount, contentLocked, false);
    }

    /**
     * Add item to unit
     *
     * @param item:   item will be added
     * @param amount: amount will be added
     * @return the amount actual added
     */
    public int addStoredItem(ItemStack item, int amount, boolean contentLocked, boolean force) {
        ItemStackWrapper wrapper = ItemStackWrapper.wrap(item);
        int add = 0;
        boolean isVoidExcess = CargoStorageUnit.isVoidExcess(getLastLocation());
        for (ItemContainer each : storedItems.values()) {
            if (each.isSimilar(wrapper)) {
                // Found existing one, add amount
                add = Math.min(amount, sizeType.getEachMaxSize() - each.getAmount());
                if (isVoidExcess) {
                    if (add > 0) {
                        each.addAmount(add);
                        DataStorage.setStoredAmount(id, each.getId(), each.getAmount());
                    } else {
                        item.setAmount(0);
                        return add;
                    }
                } else {
                    each.addAmount(add);
                    DataStorage.setStoredAmount(id, each.getId(), each.getAmount());
                }
                return add;
            }
        }

        // isforce?
        if (!force) {
            // If in content locked mode, no new input allowed
            if (contentLocked || CargoStorageUnit.isLocked(getLastLocation())) return 0;
        }
        // Not found, new one
        if (storedItems.size() < sizeType.getMaxItemCount()) {
            add = Math.min(amount, sizeType.getEachMaxSize());
            int itemId = DataStorage.getItemId(item);
            storedItems.put(itemId, new ItemContainer(itemId, item, add));
            DataStorage.addStoredItem(id, itemId, add);
            return add;
        }
        return add;
    }

    public int getId() {
        return id;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public synchronized void setPlaced(boolean isPlaced) {
        if (this.isPlaced != isPlaced) {
            this.isPlaced = isPlaced;
            DataStorage.setContainerStatus(id, isPlaced);
        }
    }

    public StorageUnitType getSizeType() {
        return sizeType;
    }

    public synchronized void setSizeType(StorageUnitType sizeType) {
        if (this.sizeType != sizeType) {
            this.sizeType = sizeType;
            DataStorage.setContainerSizeType(id, sizeType);
        }
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public synchronized void setLastLocation(Location lastLocation) {
        if (this.lastLocation != lastLocation) {
            this.lastLocation = lastLocation;
            DataStorage.setContainerLocation(id, lastLocation);
        }
    }

    public void removeItem(int itemId) {
        if (storedItems.remove(itemId) != null) {
            DataStorage.deleteStoredItem(id, itemId);
        }
    }

    public void setItemAmount(int itemId, int amount) {
        if (amount < 0) {
            // Directly remove
            removeItem(itemId);
            return;
        }
        ItemContainer container = storedItems.get(itemId);
        if (container != null) {
            container.setAmount(amount);
            DataStorage.setStoredAmount(id, itemId, amount);
        }
    }

    public void removeAmount(int itemId, int amount) {
        ItemContainer container = storedItems.get(itemId);
        if (container != null) {
            container.removeAmount(amount);
            if (container.getAmount() <= 0 && !CargoStorageUnit.isLocked(getLastLocation())) {
                removeItem(itemId);
                return;
            }
            DataStorage.setStoredAmount(id, itemId, container.getAmount());
        }
    }

    public int getStoredTypeCount() {
        return storedItems.size();
    }

    public int getTotalAmount() {
        int re = 0;
        for (ItemContainer each : storedItems.values()) {
            re += each.getAmount();
        }
        return re;
    }

    public List<ItemContainer> getStoredItems() {
        return new ArrayList<>(storedItems.values());
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    @Nullable
    public ItemStack requestItem(@Nonnull ItemRequest itemRequest) {
        ItemStack item = itemRequest.getItemStack();
        if (item == null) {
            return null;
        }

        int amount = itemRequest.getAmount();
        for (ItemContainer itemContainer : getStoredItems()) {
            int containerAmount = itemContainer.getAmount();
            if (StackUtils.itemsMatch(itemContainer.getSample(), item)) {
                int take = Math.min(amount, containerAmount);
                if (take <= 0) {
                    break;
                }
                itemContainer.removeAmount(take);
                DataStorage.setStoredAmount(id, itemContainer.getId(), itemContainer.getAmount());
                ItemStack clone = item.clone();
                clone.setAmount(take);
                return clone;
            }
        }
        return null;
    }

    public void depositItemStack(@Nonnull ItemStack[] itemsToDeposit, boolean contentLocked) {
        for (ItemStack item : itemsToDeposit) {
            depositItemStack(item, contentLocked);
        }
    }

    public void depositItemStack(ItemStack itemsToDeposit, boolean contentLocked, boolean force) {
        if (itemsToDeposit == null || isBlacklisted(itemsToDeposit)) {
            return;
        }
        int actualAdded = addStoredItem(itemsToDeposit, contentLocked, force);
        itemsToDeposit.setAmount(itemsToDeposit.getAmount() - actualAdded);
    }

    public void depositItemStack(ItemStack item, boolean contentLocked) {
        depositItemStack(item, contentLocked, false);
    }
}