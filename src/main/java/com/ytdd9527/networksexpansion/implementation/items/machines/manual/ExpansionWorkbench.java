package com.ytdd9527.networksexpansion.implementation.items.machines.manual;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.core.items.SpecialSlimefunItem;
import io.github.sefiraat.networks.slimefun.network.AdminDebuggable;
import io.github.sefiraat.networks.utils.Keys;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpansionWorkbench extends SpecialSlimefunItem implements AdminDebuggable {

    public static final CustomItemStack BACKGROUND_STACK = new CustomItemStack(
            Material.WHITE_STAINED_GLASS_PANE, Theme.PASSIVE + " "
    );
    private static final int[] BACKGROUND_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 24, 26, 27, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int[] RECIPE_SLOTS = {
            10, 11, 12, 19, 20, 21, 28, 29, 30
    };
    private static final int CRAFT_SLOT = 23;
    private static final int OUTPUT_SLOT = 25;
    private static final CustomItemStack CRAFT_BUTTON_STACK = new CustomItemStack(
            Material.BOOK,
            Theme.CLICK_INFO + "Craft"
    );
    private static final Map<ItemStack[], ItemStack> RECIPES = new HashMap<>();

    public static final RecipeType TYPE = new RecipeType(
            Keys.newKey("expansion-workbench"),
            Theme.themedItemStack(
                    Material.BAMBOO_BLOCK,
                    Theme.MACHINE,
                    "Network Expansion Workbench",
                    "Produced in the Expansion Workbench"
            ),
            ExpansionWorkbench::addRecipe
    );

    @ParametersAreNonnullByDefault
    public ExpansionWorkbench(
            ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public static void addRecipe(ItemStack[] input, ItemStack output) {
        RECIPES.put(input, output);
    }

    @Override
    public void preRegister() {
        addItemHandler(getBlockBreakHandler());
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {
            @Override
            public void init() {
                drawBackground(BACKGROUND_STACK, BACKGROUND_SLOTS);
                addItem(CRAFT_SLOT, CRAFT_BUTTON_STACK, (p, slot, item, action) -> false);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || (this.getSlimefunItem().canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return new int[]{OUTPUT_SLOT};
                }
                return new int[0];
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                menu.addMenuClickHandler(CRAFT_SLOT, (p, slot, item, action) -> {
                    craft(p, menu);
                    return false;
                });
            }
        };
    }

    public void craft(@Nonnull Player player, @Nonnull BlockMenu menu) {
        final ItemStack[] inputs = new ItemStack[RECIPE_SLOTS.length];
        int i = 0;

        // Fill the inputs
        for (int recipeSlot : RECIPE_SLOTS) {
            ItemStack stack = menu.getItemInSlot(recipeSlot);
            inputs[i] = stack;
            i++;
        }

        Map.Entry<ItemStack[], ItemStack> matchedEntry = null;
        ItemStack crafted = null;

        // Go through each recipe, test and set the ItemStack if found
        for (Map.Entry<ItemStack[], ItemStack> entry : RECIPES.entrySet()) {
            if (testRecipe(inputs, entry.getKey())) {
                crafted = entry.getValue().clone();
                matchedEntry = entry;
                break;
            }
        }

        if (crafted != null) {
            SlimefunItem sfi = SlimefunItem.getByItem(crafted);
            if (sfi != null && sfi.isDisabled()) {
                player.sendMessage(Theme.WARNING + "This item has been disabled");
                return;
            }
            ItemStack itemInOutputSlot = menu.getItemInSlot(OUTPUT_SLOT);
            if (itemInOutputSlot != null && itemInOutputSlot.getType() != crafted.getType()) {
                // Output slot is occupied by a different type of item
                player.sendMessage(Theme.WARNING + "Need to clear the output slot");
                return;
            }
            if (itemInOutputSlot != null && !itemInOutputSlot.getType().isAir()) {
                if (!StackUtils.itemsMatch(itemInOutputSlot, crafted)) {
                    player.sendMessage(Theme.WARNING + "Output slot is full, please clear to continue production");
                    return;
                }

                int maxStackSize = crafted.getMaxStackSize();
                if (itemInOutputSlot.getAmount() >= maxStackSize) {
                    player.sendMessage(Theme.WARNING + "Output slot is full, please clear to continue production");
                    return;
                }
            }
            // Craft the item and put it into the output slot
            menu.pushItem(crafted, OUTPUT_SLOT);

            // Consume all the items in the recipe slots
            for (int j = 0; j < 9; j++) {
                int recipeSlot = RECIPE_SLOTS[j];
                if (menu.getItemInSlot(recipeSlot) != null) {
                    menu.consumeItem(recipeSlot, matchedEntry.getKey()[j].getAmount(), true);
                }
            }
        }
    }

    private boolean testRecipe(ItemStack[] input, ItemStack[] recipe) {
        for (int test = 0; test < recipe.length; test++) {
            if (!SlimefunUtils.isItemSimilar(input[test], recipe[test], true, false, false)) {
                return false;
            }
        }
        return true;
    }

    private BlockBreakHandler getBlockBreakHandler() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent event, ItemStack itemStack, List<ItemStack> drops) {
                BlockMenu menu = StorageCacheUtils.getMenu(event.getBlock().getLocation());
                menu.dropItems(menu.getLocation(), RECIPE_SLOTS);
                menu.dropItems(menu.getLocation(), OUTPUT_SLOT);
            }
        };
    }

}
