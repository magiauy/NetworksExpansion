package com.ytdd9527.networksexpansion.implementation.items.machines.manual;

import com.ytdd9527.networksexpansion.api.data.StorageUnitData;
import com.ytdd9527.networksexpansion.api.enums.StorageUnitType;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.implementation.items.ExpansionItemStacks;
import com.ytdd9527.networksexpansion.implementation.items.machines.unit.CargoStorageUnit;
import com.ytdd9527.networksexpansion.utils.databases.DataStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class StorageUnitUpgradeTable extends NetworkObject {
    private static final Map<ItemStack[], ItemStack> recipes = new HashMap<>();
    public final static RecipeType TYPE = new RecipeType(
            Keys.STORAGE_UNIT_UPGRADE_TABLE,
            ExpansionItemStacks.STORAGE_UNIT_UPGRADE_TABLE,
            StorageUnitUpgradeTable::addRecipe
    );
    private final int[] border = {0, 8, 9, 17, 18, 26};
    private final int[] innerBorder = {1, 5, 6, 7, 10, 14, 16, 19, 23, 24, 25};
    private final int[] inputSlots = {2, 3, 4, 11, 12, 13, 20, 21, 22};
    private final int outputSlot = 15;
    private final int actionBtnSlot = 17;
    private final ItemStack actionBtn = new CustomItemStack(Material.REDSTONE_TORCH, "&6点击升级", "");

    public StorageUnitUpgradeTable(
            ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.MODEL);
        for (int slot : inputSlots) {
            this.getSlotsToDrop().add(slot);
        }

        this.getSlotsToDrop().add(outputSlot);

        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                for (int slot : border) {
                    addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }
                ItemStack innerBorderItem = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "", "");
                for (int slot : innerBorder) {
                    addItem(slot, innerBorderItem, ChestMenuUtils.getEmptyClickHandler());
                }
                addItem(outputSlot, null, new AdvancedMenuClickHandler() {
                    @Override
                    public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                        ItemStack itemInSlot = e.getInventory().getItem(slot);
                        return (cursor == null || cursor.getType().isAir()) && (itemInSlot == null || itemInSlot.getType() != Material.BARRIER);
                    }

                    @Override
                    public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                        return false;
                    }
                });
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Block b) {
                menu.addMenuClickHandler(actionBtnSlot, (p, slot, item, action) -> {
                    craft(p, menu);
                    return false;
                });
                menu.replaceExistingItem(actionBtnSlot, actionBtn);
            }

            @Override
            public boolean canOpen(@NotNull Block b, @NotNull Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || (canUse(p, false) && Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
    }

    public static void addRecipe(ItemStack[] recipe, ItemStack out) {
        recipes.put(recipe, out);
    }

    private void craft(Player p, BlockMenu menu) {
        for (Map.Entry<ItemStack[], ItemStack> each : recipes.entrySet()) {
            if (match(menu, each.getKey())) {
                ItemStack itemInSlot = menu.getItemInSlot(outputSlot);
                int id = CargoStorageUnit.getBoundId(menu.getItemInSlot(inputSlots[4]));
                ItemStack out = each.getValue().clone();
                if (id != -1) {
                    if (DataStorage.isContainerLoaded(id)) {
                        if (DataStorage.getCachedStorageData(id).isPresent()) {
                            StorageUnitData data = DataStorage.getCachedStorageData(id).get();
                            upgrade(data);
                        }
                    } else {
                        // Schedule to the same thread to ensure that this task is running after data request
                        DataStorage.requestStorageData(id);
                        Networks.getQueryQueue().scheduleQuery(() -> {
                            if (DataStorage.getCachedStorageData(id).isPresent()) {
                                StorageUnitData data = DataStorage.getCachedStorageData(id).get();
                                upgrade(data);
                            }
                            return true;
                        });
                    }
                    out = CargoStorageUnit.bindId(out, id);
                }
                SlimefunItemStack sfis = (SlimefunItemStack) out;
                SlimefunItem sfi = SlimefunItem.getById(sfis.getItemId());
                if (sfi != null && sfi.isDisabled()) {
                    sendDebugMessage(menu.getLocation(), "Output item is disabled");
                    return;
                }
                if (itemInSlot == null || itemInSlot.getType().isAir()) {
                    menu.replaceExistingItem(outputSlot, out);
                } else if (StackUtils.itemsMatch(itemInSlot, out)) {
                    if (itemInSlot.getAmount() + out.getAmount() <= itemInSlot.getMaxStackSize()) {
                        itemInSlot.setAmount(itemInSlot.getAmount() + out.getAmount());
                    } else {
                        sendDebugMessage(menu.getLocation(), "Output slot is full");
                        return;
                    }
                } else {
                    sendDebugMessage(menu.getLocation(), "Output slot already contains different item");
                    return;
                }
                for (int slot : inputSlots) {
                    menu.consumeItem(slot);
                }

                return;
            }
        }

        p.sendMessage("&c没有合适的配方");
        sendDebugMessage(menu.getLocation(), "No matching recipe found");
    }

    private boolean match(BlockMenu menu, ItemStack[] recipe) {
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                SlimefunItem sfItem = SlimefunItem.getByItem(menu.getItemInSlot(inputSlots[i]));
                if (sfItem != null && SlimefunUtils.isItemSimilar(recipe[i], sfItem.getItem(), true, false)) {
                    continue;
                } else if (!StackUtils.itemsMatch(recipe[i], menu.getItemInSlot(inputSlots[i]))) {
                    return false;
                }
            }
            if (!StackUtils.itemsMatch(recipe[i], menu.getItemInSlot(inputSlots[i]))) {
                return false;
            }
        }
        return true;
    }

    private void upgrade(StorageUnitData d) {
        StorageUnitType next = d.getSizeType().next();
        if (next != null) {
            d.setSizeType(next);
        }
    }
}
