package io.github.sefiraat.networks.network.barrel;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.sefiraat.networks.network.stackcaches.BarrelIdentity;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.utils.StackUtils;
import io.ncbpfluffybear.fluffymachines.items.Barrel;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Getter
public class FluffyBarrel extends BarrelIdentity {
    int limit;
    boolean voidExcess;

    public FluffyBarrel(Location location, ItemStack itemStack, int amount, int limit, boolean voidExcess) {
        super(location, itemStack, amount, BarrelType.FLUFFY);
        this.limit = limit;
        this.voidExcess = voidExcess;
    }

    @Nullable
    @Override
    public ItemStack requestItem(@Nonnull ItemRequest itemRequest) {
        BlockMenu menu = StorageCacheUtils.getMenu(getLocation());
        if (menu == null) {
            return null;
        }

        int stored = (int) getAmount();
        if (stored <= 1) {
            return null;
        }
        if (StackUtils.itemsMatch(getItemStack(), itemRequest.getItemStack())) {
            int need = itemRequest.getAmount();
            int have = stored - 1;
            int take = Math.min(need, have);
            if (take <= 0) {
                return null;
            }
            setStored(getLocation(), stored - take);
            Barrel barrel = (Barrel) StorageCacheUtils.getSfItem(getLocation());
            if (barrel != null) {
                barrel.updateMenu(getLocation().getBlock(), menu, true, getLimit());
            }
            ;
            return StackUtils.getAsQuantity(getItemStack(), take);
        }

        return null;
    }

    public void setStored(Location location, int amount) {
        StorageCacheUtils.setData(location, "stored", String.valueOf(amount));
    }

    @Override
    public void depositItemStack(ItemStack[] itemsToDeposit) {
        if (getAmount() >= getLimit()) {
            return;
        }
        int received = 0;
        for (ItemStack item : itemsToDeposit) {
            if (StackUtils.itemsMatch(item, getItemStack())) {
                int maxCanReceive = (int) (getLimit() - getAmount());
                if (voidExcess && maxCanReceive == 0) {
                    item.setAmount(0);
                    continue;
                }
                int canReceive = Math.min(item.getAmount(), maxCanReceive);
                item.setAmount(item.getAmount() - canReceive);
                received += canReceive;
            }
        }

        StorageCacheUtils.setData(getLocation(), "stored", String.valueOf(getAmount() + received));
    }

    @Override
    public int[] getInputSlot() {
        BlockMenu menu = StorageCacheUtils.getMenu(getLocation());
        if (menu == null) {
            return new int[0];
        }
        return menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.INSERT, null);
    }

    @Override
    public int[] getOutputSlot() {
        BlockMenu menu = StorageCacheUtils.getMenu(getLocation());
        if (menu == null) {
            return new int[0];
        }
        return menu.getPreset().getSlotsAccessedByItemTransport(menu, ItemTransportFlow.WITHDRAW, null);
    }
}
