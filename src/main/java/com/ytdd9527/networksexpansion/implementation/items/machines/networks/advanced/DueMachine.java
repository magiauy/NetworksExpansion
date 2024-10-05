package com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.utils.BlockMenuUtil;
import com.ytdd9527.networksexpansion.utils.itemstacks.ItemStackUtil;
import io.github.sefiraat.networks.slimefun.network.AdminDebuggable;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DueMachine extends SpecialSlimefunItem implements AdminDebuggable {
    private static final int[] DUE_ITEM_SLOTS = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7
    };

    private static final int[] DUE_BORDERS = new int[]{
            8
    };

    private static final int[] DUE_INPUT_SPLIT = new int[]{
            9, 10, 11, 12, 13, 14, 15, 16
    };

    private static final int[] INPUT_OUTPUT_SPLIT = new int[]{
            27, 28, 29, 30, 31, 32, 33, 34
    };

    private static final int[] BACKGROUND_BORDERS = new int[]{
            17, 35
    };

    private static final int[] INPUT_SLOTS = new int[]{
            18, 19, 20, 21, 22, 23, 24, 25,
    };

    private static final int[] OUTPUT_SLOTS = new int[]{
            36, 37, 38, 39, 40, 41, 42, 43,
    };

    private static final int[] INPUT_BORDERS = new int[]{
            26
    };

    private static final int[] OUTPUT_BORDERS = new int[]{
            44
    };

    private static final ItemStack DUE_BORDER_ICON = ItemStackUtil.getCleanItem(
            new CustomItemStack(
                    Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    ChatColor.GREEN + "← 在此放置预期物品"
            )
    );

    private static final ItemStack DUE_INPUT_SPLIT_ICON = ItemStackUtil.getCleanItem(
            new CustomItemStack(
                    Material.YELLOW_STAINED_GLASS_PANE,
                    ChatColor.GREEN + "↑ 预期物品 & 输入物品 ↓"
            )
    );

    private static final ItemStack INPUT_OUTPUT_SPLIT_ICON = ItemStackUtil.getCleanItem(
            new CustomItemStack(
                    Material.ORANGE_STAINED_GLASS_PANE,
                    ChatColor.GREEN + "↑ 输入物品 & 输出物品 ↓"
            )
    );

    public DueMachine(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private static void onTick(@Nonnull Block block) {
        Location location = block.getLocation();
        BlockMenu blockMenu = StorageCacheUtils.getMenu(location);
        if (blockMenu == null) {
            return;
        }

        for (int i = 0; i < DUE_ITEM_SLOTS.length; i++) {
            int dueSlot = DUE_ITEM_SLOTS[i];
            int inputSlot = INPUT_SLOTS[i];
            int outputSlot = OUTPUT_SLOTS[i];
            ItemStack dueItem = blockMenu.getItemInSlot(dueSlot);
            if (dueItem == null || dueItem.getType() == Material.AIR) {
                continue;
            }
            ItemStack inputItem = blockMenu.getItemInSlot(inputSlot);
            if (inputItem == null || inputItem.getType() == Material.AIR) {
                continue;
            }
            if (StackUtils.itemsMatch(inputItem, dueItem, false, true)) {
                if (BlockMenuUtil.fits(blockMenu, dueItem, outputSlot)) {
                    blockMenu.consumeItem(inputSlot, dueItem.getAmount());
                    BlockMenuUtil.pushItem(blockMenu, dueItem.clone(), outputSlot);
                }
            }
        }
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {

            }
        });

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent blockBreakEvent, @Nonnull ItemStack itemStack, @Nonnull List<ItemStack> list) {

            }
        });

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, SlimefunBlockData data) {
                onTick(block);
            }
        });
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                setSize(45);
                for (int slot : DUE_BORDERS) {
                    addItem(slot, DUE_BORDER_ICON, ChestMenuUtils.getEmptyClickHandler());
                }

                for (int slot : DUE_INPUT_SPLIT) {
                    addItem(slot, DUE_INPUT_SPLIT_ICON, ChestMenuUtils.getEmptyClickHandler());
                }

                for (int slot : INPUT_OUTPUT_SPLIT) {
                    addItem(slot, INPUT_OUTPUT_SPLIT_ICON, ChestMenuUtils.getEmptyClickHandler());
                }

                for (int slot : BACKGROUND_BORDERS) {
                    addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }

                for (int slot : INPUT_BORDERS) {
                    addItem(slot, ChestMenuUtils.getInputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
                }

                for (int slot : OUTPUT_BORDERS) {
                    addItem(slot, ChestMenuUtils.getOutputSlotTexture(), ChestMenuUtils.getEmptyClickHandler());
                }
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || (Slimefun.getPermissionsService().hasPermission(player, this.getSlimefunItem()) && Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                if (itemTransportFlow == ItemTransportFlow.WITHDRAW) {
                    return OUTPUT_SLOTS;
                }

                return new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow itemTransportFlow, ItemStack incoming) {
                if (itemTransportFlow == ItemTransportFlow.INSERT) {
                    if (incoming == null || incoming.getType() == Material.AIR) {
                        return new int[0];
                    }

                    List<Integer> matched = new ArrayList<>();
                    for (int i = 0; i < DUE_ITEM_SLOTS.length; i++) {
                        int dueSlot = DUE_ITEM_SLOTS[i];
                        int inputSlot = INPUT_SLOTS[i];
                        if (StackUtils.itemsMatch(menu.getItemInSlot(dueSlot), incoming)) {
                            matched.add(inputSlot);
                        }
                    }

                    if (matched.isEmpty()) {
                        return new int[0];
                    } else {
                        int[] slots = new int[matched.size()];
                        for (int i = 0; i < matched.size(); i++) {
                            slots[i] = matched.get(i);
                        }
                        return slots;
                    }
                } else {
                    return OUTPUT_SLOTS;
                }
            }
        };
    }
}
