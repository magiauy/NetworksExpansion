package com.ytdd9527.networksexpansion.utils.itemstacks;

import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class BlockMenuUtil {
    @Nullable
    public static ItemStack pushItem(@Nonnull BlockMenu blockMenu, @Nonnull ItemStack item, int... slots) {
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

    @Nonnull
    public static Map<ItemStack, Integer> pushItem(@Nonnull BlockMenu blockMenu, @Nonnull List<ItemStack> items, int... slots) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cannot push null or empty list");
        }

        Map<ItemStack, Integer> itemMap = new HashMap<>();
        for (ItemStack item : items) {
            if (item != null && !item.getType().isAir()) {
                ItemStack leftOver = pushItem(blockMenu, item, slots);
                if (leftOver != null) {
                    itemMap.put(leftOver, itemMap.getOrDefault(leftOver, 0) + leftOver.getAmount());
                }
            }
        }

        return itemMap;
    }

    public static boolean fits(@Nonnull BlockMenu blockMenu, @Nonnull ItemStack item, int... slots) {
        if (item == null || item.getType().isAir()) {
            return true;
        }

        int incoming = item.getAmount();
        for (int slot : slots) {
            ItemStack stack = blockMenu.getItemInSlot(slot);

            if (stack == null || stack.getType().isAir()) {
                incoming -= item.getMaxStackSize();
            }

            else if (stack.getMaxStackSize() > stack.getAmount() && StackUtils.itemsMatch(item, stack)) {
                incoming -= stack.getMaxStackSize() - stack.getAmount();
            }

            if (incoming <= 0) {
                return true;
            }
        }

        return false;
    }

    public static boolean fits(@Nonnull BlockMenu blockMenu, @Nonnull ItemStack[] items, int... slots) {
        if (items == null || items.length == 0) {
            return true;
        }

        List<ItemStack> listItems = new ArrayList<>();
        for (ItemStack item : items) {
            if (item != null && !item.getType().isAir()) {
                listItems.add(item);
            }
        }

        return fits(blockMenu, listItems, slots);
    }
    public static boolean fits(@Nonnull BlockMenu blockMenu, @Nonnull List<ItemStack> items, int... slots) {
        if (items == null || items.isEmpty()) {
            return true;
        }

        List<ItemStack> cloneMenu = new ArrayList<>();
        for (int slot : slots) {
            ItemStack stack = blockMenu.getItemInSlot(slot);
            cloneMenu.set(slot, stack);
        }

        for (ItemStack item : items) {
            int leftAmount = item.getAmount();
            for (int slot : slots) {
                if (leftAmount <= 0) {
                    break;
                }

                ItemStack stack = cloneMenu.get(slot);

                if (stack == null) {
                    int received = Math.min(leftAmount, item.getMaxStackSize());
                    cloneMenu.set(slot, StackUtils.getAsQuantity(item, leftAmount));
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
                return false;
            }
        }

        return true;
    }
}
