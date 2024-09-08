package io.github.sefiraat.networks.network.barrel;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.sefiraat.networks.network.stackcaches.BarrelIdentity;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.utils.StackUtils;
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
    }

    @Nullable
    @Override
    public ItemStack requestItem(@Nonnull ItemRequest itemRequest) {
        BlockMenu menu = StorageCacheUtils.getMenu(getLocation());
        if (menu == null) {
            return null;
        }
        int total = 0;
        for (int slot : getOutputSlot()) {
            ItemStack itemInSlot = menu.getItemInSlot(slot);
            if (StackUtils.itemsMatch(itemInSlot, itemRequest.getItemStack())) {
                int need = itemRequest.getAmount();
                int have = itemInSlot.getAmount();
                int take = Math.min(need, have);
                total += take;
                itemInSlot.setAmount(have - take);
                itemRequest.receiveAmount(take);
                if (itemRequest.getAmount() <= 0) {
                    return StackUtils.getAsQuantity(itemRequest.getItemStack(), total);
                }
            }
        }

        return null;
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
                int canReceive = Math.min(item.getAmount(), maxCanReceive);
                item.setAmount(item.getAmount() - canReceive);
                received += canReceive;
                if (voidExcess) {
                    item.setAmount(0);
                }
            }
        }

        StorageCacheUtils.setData(getLocation(), "stored", String.valueOf(received));
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
