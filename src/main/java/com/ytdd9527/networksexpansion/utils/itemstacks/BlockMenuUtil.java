package com.ytdd9527.networksexpansion.utils.itemstacks;

import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@UtilityClass
public class BlockMenuUtil {
    @Nullable
    public static ItemStack pushItem(@Nonnull BlockMenu blockMenu, ItemStack item, int... slots) {
        if (item == null || item.getType().isAir()) {
            throw new IllegalArgumentException("Cannot push null or AIR");
        }

        int leftAmount = item.getAmount();

        for (int slot : slots) {
            if (leftAmount <= 0) {
                break;
            }

            ItemStack stack = blockMenu.getItemInSlot(slot);

            if (stack == null) {
                int received = Math.min(leftAmount, item.getMaxStackSize());
                blockMenu.replaceExistingItem(slot, StackUtils.getAsQuantity(item, leftAmount));
                leftAmount -= received;
                item.setAmount(Math.max(0, leftAmount));
            } else {
                if (!StackUtils.itemsMatch(item, stack)) {
                    continue;
                }

                int existingAmount = stack.getAmount();
                int received = Math.max(0, Math.min(item.getMaxStackSize() - existingAmount, leftAmount));
                leftAmount -= received;
                stack.setAmount(existingAmount + received);
                item.setAmount(leftAmount);
            }
        }

        if (leftAmount > 0) {
            return new CustomItemStack(item, leftAmount);
        } else {
            return null;
        }
    }
}