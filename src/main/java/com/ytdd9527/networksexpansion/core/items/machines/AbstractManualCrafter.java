package com.ytdd9527.networksexpansion.core.items.machines;

import com.ytdd9527.networksexpansion.api.data.SuperRecipe;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import com.ytdd9527.networksexpansion.utils.itemstacks.BlockMenuUtil;
import io.github.sefiraat.networks.slimefun.network.AdminDebuggable;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "deprecation"})
public abstract class AbstractManualCrafter extends SpecialSlimefunItem implements AdminDebuggable, EnergyNetComponent {
    public AbstractManualCrafter(@Nonnull ItemGroup itemGroup, @Nonnull SlimefunItemStack item, @Nonnull RecipeType recipeType, @Nonnull ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        BlockPlaceHandler blockPlaceHandler = getMachineBlockPlaceHandler();
        if (blockPlaceHandler != null) {
            addItemHandler(blockPlaceHandler);
        }

        BlockBreakHandler blockBreakHandler = getMachineBlockBreakHandler();
        if (blockBreakHandler != null) {
            addItemHandler(blockBreakHandler);
        }

        BlockTicker blockTicker = getMachineBlockTicker();
        if (blockTicker != null) {
            addItemHandler(blockTicker);
        }
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                for (Map.Entry<Integer, ItemStack> entry : getBackgrounds().entrySet()) {
                    addItem(entry.getKey(), entry.getValue(), ChestMenuUtils.getEmptyClickHandler());
                }
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || (this.getSlimefunItem().canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (isTransportable()) {
                    if (flow == ItemTransportFlow.WITHDRAW) {
                        return getOutputSlots();
                    }
                    return getInputSlots();
                }
                return new int[0];
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                menu.addMenuClickHandler(getHandleSlot(), (p, slot, item, action) -> {
                    craft(p, menu);
                    return false;
                });
                Map<Integer, ChestMenu.MenuClickHandler> menuClickHandlers = getMenuClickHandlers();
                if (menuClickHandlers != null) {
                    for (Map.Entry<Integer, ChestMenu.MenuClickHandler> entry : menuClickHandlers.entrySet()) {
                        menu.addMenuClickHandler(entry.getKey(), entry.getValue());
                    }
                }
            }
        };
    }

    public void craft(Player player, BlockMenu blockMenu) {
        for (SuperRecipe recipe : getRecipes()) {
            if (getCapacity() < recipe.getConsumeEnergy()) {
                continue;
            }

            if (getCharge(blockMenu.getLocation()) >= recipe.getConsumeEnergy()) {
                if (recipe.getHandler() != null) {
                    recipe.getHandler().accept(player, blockMenu);
                } else {
                    if (recipe.isShaped()) {
                        shapedCraft(recipe, player, blockMenu);
                    } else {
                        shapelessCraft(recipe, player, blockMenu);
                    }
                }
            }
        }
    }

    public void shapedCraft(SuperRecipe recipe, Player player, BlockMenu blockMenu) {
        World world = blockMenu.getLocation().getWorld();
        if (world == null) {
            return;
        }

        for (int i = 0; i < Math.min(recipe.getInput().length, getInputSlots().length); i++) {
            ItemStack wanted = recipe.getInput()[i];
            if (wanted == null || wanted.getType().isAir()) {
                continue;
            }

            ItemStack itemInSlot = blockMenu.getItemInSlot(getInputSlots()[i]);
            if (itemInSlot == null || itemInSlot.getType().isAir()) {
                player.sendMessage("Not enough items in inventory at slot " + getInputSlots()[i] + ".");
                return;
            }

            if (!StackUtils.itemsMatch(itemInSlot, wanted, false, true)) {
                player.sendMessage("Item at slot " + getInputSlots()[i] + " does not match the required item.");
                return;
            }
        }

        for (int i = 0; i < Math.min(recipe.getInput().length, getInputSlots().length); i++) {
            ItemStack wanted = recipe.getInput()[i];
            if (wanted == null || wanted.getType().isAir()) {
                continue;
            }

            blockMenu.consumeItem(getInputSlots()[i], recipe.getInput()[i].getAmount());
        }

        for (ItemStack item : recipe.getOutput()) {
            if (item != null && !item.getType().isAir()) {
                SlimefunItem sfi = SlimefunItem.getByItem(item);
                if (sfi != null) {
                    if (sfi.isDisabled() || sfi.isDisabledIn(world)) {
                        player.sendMessage("This item is disabled in this world.");
                        continue;
                    }
                }
                ItemStack left = BlockMenuUtil.pushItem(blockMenu, item, getOutputSlots());
                if (left != null && !left.getType().isAir()) {
                    player.sendMessage("Not enough space in output slots.");
                    world.dropItem(blockMenu.getLocation(), left);
                }
            }
        }

        removeCharge(blockMenu.getLocation(), recipe.getConsumeEnergy());
        player.sendMessage("Successfully crafted.");
    }

    public void shapelessCraft(SuperRecipe recipe, Player player, BlockMenu blockMenu) {
        World world = blockMenu.getLocation().getWorld();
        if (world == null) {
            return;
        }
        // ItemStack : ItemStack.getAmount()
        Map<ItemStack, Integer> wanted = new HashMap<>();
        for (int i = 0; i < Math.min(recipe.getInput().length, getInputSlots().length); i++) {
            ItemStack wantedItem = recipe.getInput()[i];
            if (wantedItem == null || wantedItem.getType().isAir()) {
                continue;
            }

            if (wanted.containsKey(wantedItem)) {
                wanted.put(wantedItem, wanted.get(wantedItem) + wantedItem.getAmount());
            } else {
                wanted.put(wantedItem, wantedItem.getAmount());
            }
        }

        // ItemStack : ItemStack.getAmount()
        Map<ItemStack, Integer> have = new HashMap<>();
        for (int slot : getInputSlots()) {
            ItemStack itemInSlot = blockMenu.getItemInSlot(slot);
            if (itemInSlot == null || itemInSlot.getType().isAir()) {
                continue;
            }

            if (have.containsKey(itemInSlot)) {
                have.put(itemInSlot, have.get(itemInSlot) + itemInSlot.getAmount());
            } else {
                have.put(itemInSlot, itemInSlot.getAmount());
            }
        }

        for (Map.Entry<ItemStack, Integer> entry : wanted.entrySet()) {
            if (!have.containsKey(entry.getKey()) || have.get(entry.getKey()) < entry.getValue()) {
                player.sendMessage("Not enough items in inventory.");
                break;
            }
        }

        if (!BlockMenuUtil.fits(blockMenu, recipe.getOutput(), getOutputSlots())) {
            player.sendMessage("Not enough space in output slots.");
            return;
        }

        // Consume items
        for (Map.Entry<ItemStack, Integer> entry : wanted.entrySet()) {
            for (int slot : getInputSlots()) {
                ItemStack itemInSlot = blockMenu.getItemInSlot(slot);
                if (itemInSlot == null || itemInSlot.getType().isAir()) {
                    continue;
                }

                if (StackUtils.itemsMatch(itemInSlot, entry.getKey())) {
                    int amount = Math.min(entry.getValue(), itemInSlot.getAmount());
                    blockMenu.consumeItem(slot, amount);

                    wanted.put(entry.getKey(), wanted.get(entry.getKey()) - amount);
                }

                if (wanted.get(entry.getKey()) <= 0) {
                    break;
                }
            }
        }

        if (!wanted.isEmpty()) {
            player.sendMessage("Not enough items in inventory.");
            player.sendMessage("This may a bug or the recipe is invalid.");
            return;
        }

        for (ItemStack item : recipe.getOutput()) {
            if (item != null && !item.getType().isAir()) {
                SlimefunItem sfi = SlimefunItem.getByItem(item);
                if (sfi != null) {
                    if (sfi.isDisabled() || sfi.isDisabledIn(world)) {
                        player.sendMessage("This item is disabled in this world.");
                        continue;
                    }
                }
                ItemStack left = BlockMenuUtil.pushItem(blockMenu, item, getOutputSlots());
                if (left != null && !left.getType().isAir()) {
                    player.sendMessage("Not enough space in output slots.");
                    world.dropItem(blockMenu.getLocation(), left);
                }
            }
        }

        removeCharge(blockMenu.getLocation(), recipe.getConsumeEnergy());
        player.sendMessage("Successfully crafted.");
    }

    @Override
    @Nonnull
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    public abstract List<SuperRecipe> getRecipes();

    public abstract int[] getInputSlots();

    public abstract int[] getOutputSlots();

    public abstract int getHandleSlot();

    public abstract boolean isTransportable();

    public abstract Map<Integer, ItemStack> getBackgrounds();

    public abstract Map<Integer, ChestMenu.MenuClickHandler> getMenuClickHandlers();

    public abstract BlockPlaceHandler getMachineBlockPlaceHandler();

    public abstract BlockBreakHandler getMachineBlockBreakHandler();

    public abstract BlockTicker getMachineBlockTicker();
}
