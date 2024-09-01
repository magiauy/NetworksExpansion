package com.ytdd9527.networksexpansion.implementation.items.machines.manual;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.data.StorageUnitData;
import com.ytdd9527.networksexpansion.api.enums.StorageUnitType;
import com.ytdd9527.networksexpansion.implementation.items.ExpansionItemStacks;
import com.ytdd9527.networksexpansion.implementation.items.machines.unit.CargoStorageUnit;
import com.ytdd9527.networksexpansion.utils.DisplayGroupGenerators;
import com.ytdd9527.networksexpansion.utils.databases.DataStorage;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.sefiraat.networks.utils.Keys;
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
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class StorageUnitUpgradeTableModel extends NetworkObject {
    private static final Map<ItemStack[], ItemStack> recipes = new HashMap<>();
    public final static RecipeType TYPE = new RecipeType(
            Keys.STORAGE_UNIT_UPGRADE_TABLE_MODEL,
            ExpansionItemStacks.STORAGE_UNIT_UPGRADE_TABLE_MODEL,
            StorageUnitUpgradeTableModel::addRecipe
    );
    private static final String KEY_UUID = "display-uuid";
    private final int[] border = {0, 8, 9, 17, 18, 26};
    private final int[] innerBorder = {1, 5, 6, 7, 10, 14, 16, 19, 23, 24, 25};
    private final int[] inputSlots = {2, 3, 4, 11, 12, 13, 20, 21, 22};
    private final int outputSlot = 15;
    private final int actionBtnSlot = 17;
    private final ItemStack actionBtn = new CustomItemStack(Material.REDSTONE_TORCH, "&6点击升级", "");
    private boolean useSpecialModel;
    private Function<Location, DisplayGroup> displayGroupGenerator;

    public StorageUnitUpgradeTableModel(
            ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String itemId) {
        super(itemGroup, item, recipeType, recipe, NodeType.MODEL);

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
                        return (cursor == null || cursor.getType() == Material.AIR) && (itemInSlot == null || itemInSlot.getType() != Material.BARRIER);
                    }

                    @Override
                    public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                        return false;
                    }
                });
            }

            @Override
            public void newInstance(@NotNull BlockMenu menu, @NotNull Block b) {
                menu.addMenuClickHandler(actionBtnSlot, ((p, slot, item, action) -> {
                    craft(menu);
                    return false;
                }));
                menu.replaceExistingItem(actionBtnSlot, actionBtn);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || (canUse(p, false) && Slimefun.getProtectionManager().hasPermission(p, b, Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
        loadConfigurations(itemId);
    }

    public static void addRecipe(ItemStack[] recipe, ItemStack out) {
        recipes.put(recipe, out);
    }

    private void loadConfigurations(String itemId) {
        FileConfiguration config = Networks.getInstance().getConfig();


        boolean defaultUseSpecialModel = false;
        this.useSpecialModel = config.getBoolean("items." + itemId + ".use-special-model.enable", defaultUseSpecialModel);


        Map<String, Function<Location, DisplayGroup>> generatorMap = new HashMap<>();
        generatorMap.put("StorageUnitUpgradeTable", DisplayGroupGenerators::generateStorageUnitUpgradeTable);

        this.displayGroupGenerator = null;

        if (this.useSpecialModel) {
            String generatorKey = config.getString("items." + itemId + ".use-special-model.type");
            this.displayGroupGenerator = generatorMap.get(generatorKey);
            if (this.displayGroupGenerator == null) {
                Networks.getInstance().getLogger().warning("未知的展示组类型 '" + generatorKey + "', 特殊模型已禁用。");
                this.useSpecialModel = false;
            }
        }

    }

    private void craft(BlockMenu menu) {
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
                    return;
                }
                if (itemInSlot == null || itemInSlot.getType().isAir()) {
                    menu.replaceExistingItem(outputSlot, out);
                } else if (StackUtils.itemsMatch(itemInSlot, out)) {
                    if (itemInSlot.getAmount() + out.getAmount() <= itemInSlot.getMaxStackSize()) {
                        itemInSlot.setAmount(itemInSlot.getAmount() + out.getAmount());
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                for (int slot : inputSlots) {
                    menu.consumeItem(slot);
                }
            }
        }
    }

    private boolean match(BlockMenu menu, ItemStack[] recipe) {
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                SlimefunItem sfItem = SlimefunItem.getByItem(menu.getItemInSlot(inputSlots[i]));
                if (sfItem != null && SlimefunUtils.isItemSimilar(recipe[i], sfItem.getItem(), true, false)) {
                    continue;
                }
                return false;
            }
            if (!SlimefunUtils.isItemSimilar(recipe[i], menu.getItemInSlot(inputSlots[i]), true, false)) {
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

    @Override
    public void preRegister() {
        if (useSpecialModel) {
            addItemHandler(new BlockPlaceHandler(false) {
                @Override
                public void onPlayerPlace(@NotNull BlockPlaceEvent e) {
                    e.getBlock().setType(Material.BARRIER);
                    setupDisplay(e.getBlock().getLocation());
                }
            });
        }

        // 添加破坏处理器，不管 useSpecialModel 的值如何，破坏时的逻辑都应该执行
        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                Location location = e.getBlock().getLocation();
                removeDisplay(location);
                e.getBlock().setType(Material.AIR);
            }
        });
    }

    private void setupDisplay(@Nonnull Location location) {
        if (this.displayGroupGenerator != null) {
            DisplayGroup displayGroup = this.displayGroupGenerator.apply(location.clone().add(0.5, 0, 0.5));
            StorageCacheUtils.setData(location, KEY_UUID, displayGroup.getParentUUID().toString());
        }
    }

    private void removeDisplay(@Nonnull Location location) {
        DisplayGroup group = getDisplayGroup(location);
        if (group != null) {
            group.remove();
        }
    }

    @Nullable
    private UUID getDisplayGroupUUID(@Nonnull Location location) {
        String uuid = StorageCacheUtils.getData(location, KEY_UUID);
        if (uuid == null) {
            return null;
        }
        return UUID.fromString(uuid);
    }

    @Nullable
    private DisplayGroup getDisplayGroup(@Nonnull Location location) {
        UUID uuid = getDisplayGroupUUID(location);
        if (uuid == null) {
            return null;
        }
        return DisplayGroup.fromUUID(uuid);
    }
}
