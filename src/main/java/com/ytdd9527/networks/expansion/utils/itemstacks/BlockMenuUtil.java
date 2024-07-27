package com.ytdd9527.networks.expansion.utils.itemstacks;

import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMenuUtil {
    @Nullable
    public static ItemStack pushItem(@Nonnull BlockMenu blockMenu, ItemStack item, int... slots) {
        if (item == null || item.getType().isAir()) {
            throw new IllegalArgumentException("Cannot push null or AIR");
        }

        ItemStackWrapper wrapper = null;
        int amount = item.getAmount();

        for (int slot : slots) {
            if (amount <= 0) {
                break;
            }

            ItemStack stack = blockMenu.getItemInSlot(slot);

            if (stack == null) {
                blockMenu.replaceExistingItem(slot, StackUtils.getAsQuantity(item, Math.min(item.getAmount(), item.getMaxStackSize())));
                item.setAmount(Math.max(0, item.getAmount() - item.getMaxStackSize()));
            } else {
                int maxStackSize = Math.min(stack.getMaxStackSize(), blockMenu.toInventory().getMaxStackSize());
                if (stack.getAmount() < maxStackSize) {
                    if (wrapper == null) {
                        wrapper = ItemStackWrapper.wrap(item);
                    }

                    if (!StackUtils.itemsMatch(wrapper, stack)) {
                        continue;
                    }

                    amount -= (maxStackSize - stack.getAmount());
                    stack.setAmount(Math.min(stack.getAmount() + item.getAmount(), maxStackSize));
                    item.setAmount(amount);
                }
            }
        }

        if (amount > 0) {
            return new CustomItemStack(item, amount);
        } else {
            return null;
        }
    }
}