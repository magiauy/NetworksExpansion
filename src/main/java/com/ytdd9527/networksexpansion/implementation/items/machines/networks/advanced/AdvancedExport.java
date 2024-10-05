package com.ytdd9527.networksexpansion.implementation.items.machines.networks.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.implementation.items.ExpansionItems;
import com.ytdd9527.networksexpansion.utils.BlockMenuUtil;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.slimefun.network.NetworkObject;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AdvancedExport extends NetworkObject implements RecipeDisplayItem {

    private static final int[] BACKGROUND_SLOTS = {18, 19, 20, 21, 23, 24, 25, 45, 46, 47, 48, 50, 51, 52, 53};
    private static final int[] TEST_ITEM_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17};
    private static final int[] TEST_ITEM_BACKDROP = {22};

    private static final int[] OUTPUT_ITEM_SLOTS = {
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44,
    };
    private static final int[] OUTPUT_ITEM_BACKDROP = {49};
    private static final CustomItemStack TEST_BACKDROP_STACK = new CustomItemStack(
            Material.GREEN_STAINED_GLASS_PANE,
            Theme.SUCCESS + "Export Item Matching"
    );
    private static final CustomItemStack OUTPUT_BACKDROP_STACK = new CustomItemStack(
            Material.ORANGE_STAINED_GLASS_PANE,
            Theme.SUCCESS + "Output Slot"
    );
    private final int lockModeSlot = 26;
    private final ItemSetting<Integer> tickRate;

    public AdvancedExport(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.ADVANCED_EXPORT);
        this.tickRate = new IntRangeSetting(this, "tick_rate", 1, 1, 10);
        addItemSetting(this.tickRate);


        for (int testItemSlot : TEST_ITEM_SLOTS) {
            this.getSlotsToDrop().add(testItemSlot);
        }
        for (int outputItemSlot : OUTPUT_ITEM_SLOTS) {
            this.getSlotsToDrop().add(outputItemSlot);
        }

        addItemHandler(
                new BlockTicker() {

                    private int tick = 1;

                    @Override
                    public boolean isSynchronized() {
                        return false;
                    }

                    @Override
                    public void tick(Block block, SlimefunItem item, SlimefunBlockData data) {
                        if (tick <= 1) {
                            final BlockMenu blockMenu = data.getBlockMenu();
                            addToRegistry(block);
                            tryFetchItem(blockMenu);

                        }
                    }

                    @Override
                    public void uniqueTick() {
                        tick = tick <= 1 ? tickRate.getValue() : tick - 1;
                    }
                },
                new BlockBreakHandler(true, true) {
                    @Override
                    public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                        BlockMenu blockMenu = StorageCacheUtils.getMenu(e.getBlock().getLocation());

                        for (int testitemslot : getTestSlots()) {
                            blockMenu.dropItems(blockMenu.getLocation(), testitemslot);
                        }
                        for (int outputitemslot : getOutputSlots()) {
                            blockMenu.dropItems(blockMenu.getLocation(), outputitemslot);
                        }

                    }
                }
        );
    }

    private void tryFetchItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getNode(blockMenu.getLocation());
        if (definition == null || definition.getNode() == null) {
            return;
        }
        NetworkRoot networkRoot = definition.getNode().getRoot();

        List<ItemRequest> itemRequests = new ArrayList<>();
        int totalFreeStackSpaces = 0;
        int totalFreeSlot = 0;

        // 计算输出槽的空闲空间
        for (int outputSlot : getOutputSlots()) {
            ItemStack currentStack = blockMenu.getItemInSlot(outputSlot);
            if (currentStack == null || currentStack.getType() == Material.AIR) {
                totalFreeStackSpaces += 64;
            } else {
                totalFreeStackSpaces += currentStack.getMaxStackSize() - currentStack.getAmount();
            }
        }

        // no free space, we should escape quickly
        if (totalFreeStackSpaces == 0) {
            return;
        }

        // for each every slot, then make itemRequests
        for (int testItemSlot : getTestSlots()) {
            ItemStack currentStack = blockMenu.getItemInSlot(testItemSlot);
            if (currentStack != null && currentStack.getType() != Material.AIR) {
                itemRequests.add(new ItemRequest(StackUtils.getAsQuantity(currentStack, 1), currentStack.getAmount()));
            }
        }

        // if there is no item request, we should escape quickly
        if (itemRequests.isEmpty()) {
            return;
        }

        // fetch items from network
        ItemStack fetched = null;
        for (ItemRequest itemRequest : itemRequests) {
            fetched = networkRoot.getItemStack(itemRequest); // fetch item from network
            if (fetched != null) {
                // amount may not be excepted, but it is the max amount we can fetch.
                placeItems(networkRoot, blockMenu, fetched.clone(), fetched.getAmount(), getOutputSlots());
            }
        }
    }

    private void placeItems(@Nonnull NetworkRoot root, @Nonnull BlockMenu blockMenu, @Nonnull ItemStack itemStack, @Nonnull int itemAmount, int[] outputSlots) {
        BlockMenuUtil.pushItem(blockMenu, itemStack, outputSlots);

        if (itemStack.getAmount() > 0) {
            returnItems(root, itemStack.clone());
        }
    }

    private void returnItems(@Nonnull NetworkRoot root, @Nonnull ItemStack itemStack) {
        root.addItemStack(itemStack);
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                for (int slot : BACKGROUND_SLOTS) {
                    addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
                }

                drawBackground(BACKGROUND_SLOTS);
                drawBackground(TEST_BACKDROP_STACK, TEST_ITEM_BACKDROP);
                drawBackground(OUTPUT_BACKDROP_STACK, OUTPUT_ITEM_BACKDROP);
                addItem(lockModeSlot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());

            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || (ExpansionItems.ADVANCED_EXPORT.canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return getOutputSlots();
                }
                return new int[0];
            }
        };
    }

    private int[] getTestSlots() {
        return TEST_ITEM_SLOTS;
    }

    private int[] getOutputSlots() {
        return OUTPUT_ITEM_SLOTS;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>();
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aTransporting mechanism",
                "",
                "&eMain feature&f:",
                "&7Precise control: output items as filtered",
                "&7Effective transport: items exported at a 1:1 ratio",
                "&7Smart allocation: fills output slots until its full",
                "",
                "&eUsage&f:",
                "&7Put item on the filter slot to filter the output",
                "&7item type and quantity",
                "&7Optimize your automation, adjusting the transportation",
                "&7frequency through the settings to set the delay",
                "&7Advance Export will automatically process without",
                "&7much manual operation from players"));
        return displayRecipes;
    }
}
