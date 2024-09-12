package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.utils;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.enums.TransportMode;
import com.ytdd9527.networksexpansion.api.enums.TransportResult;
import com.ytdd9527.networksexpansion.utils.itemstacks.BlockMenuUtil;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.utils.StackUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

@UtilityClass
public class TransferUtil {
    public static void doTransport(Location startLocation, BlockFace direction, int limit, boolean skipNoMenu, Consumer<BlockMenu> consumer) {
        Block block = startLocation.getBlock();
        for (int i = 0; i < limit; i++) {
            block = block.getRelative(direction);
            BlockMenu blockMenu = StorageCacheUtils.getMenu(block.getLocation());
            if (blockMenu == null) {
                if (skipNoMenu) {
                    continue;
                } else {
                    return;
                }
            }

            consumer.accept(blockMenu);
        }
    }
    public static TransportResult grabItem(
            @Nonnull NetworkRoot root,
            @Nonnull BlockMenu blockMenu,
            @Nonnull TransportMode transportMode,
            int limitQuantity
    ) {
        final int[] slots = blockMenu.getPreset().getSlotsAccessedByItemTransport(blockMenu, ItemTransportFlow.WITHDRAW, null);

        int limit = limitQuantity;
        switch (transportMode) {
            case NONE, NONNULL_ONLY -> {
                /*
                 * Grab all the items.
                 */
                for (int slot : slots) {
                    final ItemStack item = blockMenu.getItemInSlot(slot);
                    if (item != null && !item.getType().isAir()) {
                        final int exceptedReceive = Math.min(item.getAmount(), limit);
                        final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                        root.addItemStack(clone);
                        item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                        limit -= exceptedReceive - clone.getAmount();
                        if (limit <= 0) {
                            break;
                        }
                    }
                }
            }
            case NULL_ONLY -> {
                /*
                 * Nothing to do.
                 */
            }
            case FIRST_ONLY -> {
                /*
                 * Grab the first item only.
                 */
                if (slots.length > 0) {
                    final ItemStack item = blockMenu.getItemInSlot(slots[0]);
                    if (item != null && !item.getType().isAir()) {
                        final int exceptedReceive = Math.min(item.getAmount(), limit);
                        final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                        root.addItemStack(clone);
                        item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                        limit -= exceptedReceive - clone.getAmount();
                        if (limit <= 0) {
                            break;
                        }
                    }
                }
            }
            case LAST_ONLY -> {
                /*
                 * Grab the last item only.
                 */
                if (slots.length > 0) {
                    final ItemStack item = blockMenu.getItemInSlot(slots[slots.length - 1]);
                    if (item != null && !item.getType().isAir()) {
                        final int exceptedReceive = Math.min(item.getAmount(), limit);
                        final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                        root.addItemStack(clone);
                        item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                        limit -= exceptedReceive - clone.getAmount();
                        if (limit <= 0) {
                            break;
                        }
                    }
                }
            }
            case FIRST_STOP -> {
                /*
                 * Grab the first non-null item only.
                 */
                for (int slot : slots) {
                    final ItemStack item = blockMenu.getItemInSlot(slot);
                    if (item != null && !item.getType().isAir()) {
                        final int exceptedReceive = Math.min(item.getAmount(), limit);
                        final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                        root.addItemStack(clone);
                        item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                        limit -= exceptedReceive - clone.getAmount();
                        break;
                    }
                }
            }
            case LAZY -> {
                /*
                 * When it's first item is non-null, we will grab all the items.
                 */
                if (slots.length > 0) {
                    final ItemStack delta = blockMenu.getItemInSlot(slots[0]);
                    if (delta != null && !delta.getType().isAir()) {
                        for (int slot : slots) {
                            ItemStack item = blockMenu.getItemInSlot(slot);
                            if (item != null && !item.getType().isAir()) {
                                final int exceptedReceive = Math.min(item.getAmount(), limit);
                                final ItemStack clone = StackUtils.getAsQuantity(item, exceptedReceive);
                                root.addItemStack(clone);
                                item.setAmount(item.getAmount() - (exceptedReceive - clone.getAmount()));
                                limit -= exceptedReceive - clone.getAmount();
                                if (limit <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return TransportResult.CONTINUE;
    }

    public static void pushItem(
            @Nonnull NetworkRoot root,
            @Nonnull BlockMenu blockMenu,
            @Nonnull List<ItemStack> clones,
            @Nonnull TransportMode transportMode,
            int limitQuantity
    ) {
        for (ItemStack clone : clones) {
            pushItem(root, blockMenu, clone, transportMode, limitQuantity);
        }
    }

    public static TransportResult pushItem(
            @Nonnull NetworkRoot root,
            @Nonnull BlockMenu blockMenu,
            @Nonnull ItemStack clone,
            @Nonnull TransportMode transportMode,
            int limitQuantity
    ) {
        final ItemRequest itemRequest = new ItemRequest(clone, clone.getMaxStackSize());

        final int[] slots = blockMenu.getPreset().getSlotsAccessedByItemTransport(blockMenu, ItemTransportFlow.INSERT, clone);
        switch (transportMode) {
            case NONE -> {
                int freeSpace = 0;
                for (int slot : slots) {
                    final ItemStack itemStack = blockMenu.getItemInSlot(slot);
                    if (itemStack == null || itemStack.getType().isAir()) {
                        freeSpace += clone.getMaxStackSize();
                    } else {
                        if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                            continue;
                        }
                        if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                            final int availableSpace = itemStack.getMaxStackSize() - itemStack.getAmount();
                            if (availableSpace > 0) {
                                freeSpace += availableSpace;
                            }
                        }
                    }
                }
                if (freeSpace <= 0) {
                    return TransportResult.CONTINUE;
                }
                itemRequest.setAmount(Math.min(freeSpace, limitQuantity));

                final ItemStack retrieved = root.getItemStack(itemRequest);
                if (retrieved != null && !retrieved.getType().isAir()) {
                    BlockMenuUtil.pushItem(blockMenu, retrieved, slots);
                }
            }

            case NULL_ONLY -> {
                int free = limitQuantity;
                for (int slot : slots) {
                    final ItemStack itemStack = blockMenu.getItemInSlot(slot);
                    if (itemStack == null || itemStack.getType().isAir()) {
                        itemRequest.setAmount(clone.getMaxStackSize());
                    } else {
                        continue;
                    }
                    itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                    final ItemStack retrieved = root.getItemStack(itemRequest);
                    if (retrieved != null && !retrieved.getType().isAir()) {
                        free -= retrieved.getAmount();
                        blockMenu.pushItem(retrieved, slot);
                        if (free <= 0) {
                            break;
                        }
                    }
                }
            }

            case NONNULL_ONLY -> {
                int free = limitQuantity;
                for (int slot : slots) {
                    final ItemStack itemStack = blockMenu.getItemInSlot(slot);
                    if (itemStack == null || itemStack.getType().isAir()) {
                        continue;
                    }
                    if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                        continue;
                    }
                    if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                        final int space = itemStack.getMaxStackSize() - itemStack.getAmount();
                        if (space > 0) {
                            itemRequest.setAmount(space);
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                    itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                    final ItemStack retrieved = root.getItemStack(itemRequest);
                    if (retrieved != null && !retrieved.getType().isAir()) {
                        free -= retrieved.getAmount();
                        blockMenu.pushItem(retrieved, slot);
                        if (free <= 0) {
                            break;
                        }
                    }
                }
            }
            case FIRST_ONLY -> {
                int free = limitQuantity;
                if (slots.length == 0) {
                    break;
                }
                final int slot = slots[0];
                final ItemStack itemStack = blockMenu.getItemInSlot(slot);
                if (itemStack == null || itemStack.getType().isAir()) {
                    itemRequest.setAmount(clone.getMaxStackSize());
                } else {
                    if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                        return TransportResult.CONTINUE;
                    }
                    if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                        final int space = itemStack.getMaxStackSize() - itemStack.getAmount();
                        if (space > 0) {
                            itemRequest.setAmount(space);
                        } else {
                            return TransportResult.CONTINUE;
                        }
                    } else {
                        return TransportResult.CONTINUE;
                    }
                }
                itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                final ItemStack retrieved = root.getItemStack(itemRequest);
                if (retrieved != null && !retrieved.getType().isAir()) {
                    free -= retrieved.getAmount();
                    blockMenu.pushItem(retrieved, slot);
                    if (free <= 0) {
                        break;
                    }
                }
            }
            case LAST_ONLY -> {
                int free = limitQuantity;
                if (slots.length == 0) {
                    break;
                }
                final int slot = slots[slots.length - 1];
                final ItemStack itemStack = blockMenu.getItemInSlot(slot);
                if (itemStack == null || itemStack.getType().isAir()) {
                    itemRequest.setAmount(clone.getMaxStackSize());
                } else {
                    if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                        return TransportResult.CONTINUE;
                    }
                    if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                        final int space = itemStack.getMaxStackSize() - itemStack.getAmount();
                        if (space > 0) {
                            itemRequest.setAmount(space);
                        } else {
                            return TransportResult.CONTINUE;
                        }
                    } else {
                        return TransportResult.CONTINUE;
                    }
                }
                itemRequest.setAmount(Math.min(itemRequest.getAmount(), free));

                final ItemStack retrieved = root.getItemStack(itemRequest);
                if (retrieved != null && !retrieved.getType().isAir()) {
                    free -= retrieved.getAmount();
                    blockMenu.pushItem(retrieved, slot);
                    if (free <= 0) {
                        break;
                    }
                }
            }
            case FIRST_STOP -> {
                int freeSpace = 0;
                for (int slot : slots) {
                    final ItemStack itemStack = blockMenu.getItemInSlot(slot);
                    if (itemStack == null || itemStack.getType().isAir()) {
                        freeSpace += clone.getMaxStackSize();
                        break;
                    } else {
                        if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                            continue;
                        }
                        if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                            final int availableSpace = itemStack.getMaxStackSize() - itemStack.getAmount();
                            if (availableSpace > 0) {
                                freeSpace += availableSpace;
                            }
                        }
                        break;
                    }
                }
                if (freeSpace <= 0) {
                    return TransportResult.CONTINUE;
                }
                itemRequest.setAmount(Math.min(freeSpace, limitQuantity));

                final ItemStack retrieved = root.getItemStack(itemRequest);
                if (retrieved != null && !retrieved.getType().isAir()) {
                    BlockMenuUtil.pushItem(blockMenu, retrieved, slots);
                }
            }
            case LAZY -> {
                if (slots.length > 0) {
                    final ItemStack delta = blockMenu.getItemInSlot(slots[0]);
                    if (delta == null || delta.getType().isAir()) {
                        int freeSpace = 0;
                        for (int slot : slots) {
                            final ItemStack itemStack = blockMenu.getItemInSlot(slot);
                            if (itemStack == null || itemStack.getType().isAir()) {
                                freeSpace += clone.getMaxStackSize();
                            } else {
                                if (itemStack.getAmount() >= clone.getMaxStackSize()) {
                                    continue;
                                }
                                if (StackUtils.itemsMatch(itemRequest, itemStack)) {
                                    final int availableSpace = itemStack.getMaxStackSize() - itemStack.getAmount();
                                    if (availableSpace > 0) {
                                        freeSpace += availableSpace;
                                    }
                                }
                            }
                        }
                        if (freeSpace <= 0) {
                            return TransportResult.CONTINUE;
                        }
                        itemRequest.setAmount(Math.min(freeSpace, limitQuantity));

                        final ItemStack retrieved = root.getItemStack(itemRequest);
                        if (retrieved != null && !retrieved.getType().isAir()) {
                            BlockMenuUtil.pushItem(blockMenu, retrieved, slots);
                        }
                    }
                }
            }
        }
        return TransportResult.CONTINUE;
    }
}
