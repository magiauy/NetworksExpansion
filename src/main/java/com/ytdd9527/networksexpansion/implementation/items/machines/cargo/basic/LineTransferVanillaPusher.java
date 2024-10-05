package com.ytdd9527.networksexpansion.implementation.items.machines.cargo.basic;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networksexpansion.api.enums.MCVersion;
import com.ytdd9527.networksexpansion.api.interfaces.Configurable;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.network.stackcaches.ItemRequest;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.sefiraat.networks.utils.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.CrafterInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LineTransferVanillaPusher extends NetworkDirectional implements RecipeDisplayItem, Configurable {
    private static final int DEFAULT_MAX_DISTANCE = 32;
    private static final int DEFAULT_GRAB_ITEM_TICK = 1;

    private static final int[] BACKGROUND_SLOTS = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 15, 16, 17, 18, 20, 22, 23, 27, 28, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private static final int[] INPUT_SLOTS = new int[]{24, 25, 26};
    private static final int NORTH_SLOT = 11;
    private static final int SOUTH_SLOT = 29;
    private static final int EAST_SLOT = 21;
    private static final int WEST_SLOT = 19;
    private static final int UP_SLOT = 14;
    private static final int DOWN_SLOT = 32;

    private static int maxDistance;
    private static int pushItemTick;
    private final HashMap<Location, Integer> TICKER_MAP = new HashMap<>();

    public LineTransferVanillaPusher(ItemGroup itemGroup,
                                     SlimefunItemStack item,
                                     RecipeType recipeType,
                                     ItemStack[] recipe,
                                     String configKey
    ) {
        super(itemGroup, item, recipeType, recipe, NodeType.PUSHER);
        for (int slot : getInputSlots()) {
            this.getSlotsToDrop().add(slot);
        }
        loadConfiguration(configKey);
    }

    private void loadConfiguration(String itemId) {
        FileConfiguration config = Networks.getInstance().getConfig();

        int defaultMaxDistance = 32;
        int defaultGrabItemTick = 1;

        maxDistance = config.getInt("items." + itemId + ".max-distance", defaultMaxDistance);
        pushItemTick = config.getInt("items." + itemId + ".pushitem-tick", defaultGrabItemTick);
    }


    @Override
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        super.onTick(blockMenu, block);

        if (blockMenu == null) {
            return;
        }
        final Location location = blockMenu.getLocation();

        if (pushItemTick != 1) {
            int tickCounter = getTickCounter(location);
            tickCounter = (tickCounter + 1) % pushItemTick;

            if (tickCounter == 0) {
                tryPushItem(blockMenu);
            }

            updateTickCounter(location, tickCounter);
        } else {
            tryPushItem(blockMenu);
        }
    }

    private int getTickCounter(Location location) {
        final Integer ticker = TICKER_MAP.get(location);
        if (ticker == null) {
            TICKER_MAP.put(location, 0);
            return 0;
        }
        return ticker;
    }

    private void updateTickCounter(Location location, int tickCounter) {
        TICKER_MAP.put(location, tickCounter);
    }

    private void tryPushItem(@Nonnull BlockMenu blockMenu) {
        final NodeDefinition definition = NetworkStorage.getNode(blockMenu.getLocation());

        if (definition == null || definition.getNode() == null) {
            return;
        }

        final NetworkRoot root = definition.getNode().getRoot();
        final BlockFace direction = getCurrentDirection(blockMenu);

        // Fix for early vanilla pusher release
        final Block block = blockMenu.getBlock();
        final String ownerUUID = StorageCacheUtils.getData(block.getLocation(), OWNER_KEY);
        if (ownerUUID == null) {
            return;
        }
        final UUID uuid = UUID.fromString(ownerUUID);
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        // dirty fix
        Block targetBlock = block.getRelative(direction);
        for (int d = 0; d <= maxDistance; d++) {
            // 如果方块是空气，退出
            if (targetBlock.getType() == Material.AIR) {
                break;
            }

            final BlockState blockState = targetBlock.getState();

            if (!(blockState instanceof InventoryHolder holder)) {
                return;
            }

            if (Networks.getInstance().getMCVersion().isAtLeast(MCVersion.MC1_21)) {
                if (blockState instanceof CrafterInventory) {
                    return;
                }
            }

            for (int slot : getInputSlots()) {
                final ItemStack templateItem = blockMenu.getItemInSlot(slot);

                if (templateItem == null || templateItem.getType() == Material.AIR) {
                    continue;
                }

                ItemStack template = StackUtils.getAsQuantity(templateItem, 1);

                // dirty fix
                try {
                    if (!Slimefun.getProtectionManager().hasPermission(offlinePlayer, targetBlock, Interaction.INTERACT_BLOCK)) {
                        return;
                    }
                } catch (NullPointerException ex) {
                    return;
                }

                final Inventory inventory = holder.getInventory();

                boolean wildChests = Networks.getSupportedPluginManager().isWildChests();
                boolean isChest = wildChests && WildChestsAPI.getChest(targetBlock.getLocation()) != null;

                sendDebugMessage(block.getLocation(), "WildChests 已安装：" + wildChests);
                sendDebugMessage(block.getLocation(), "该方块是否被 WildChest 判断为方块：" + isChest);

                if (inventory instanceof FurnaceInventory furnace) {
                    handleFurnace(root, template, furnace);
                } else if (inventory instanceof BrewerInventory brewer) {
                    handleBrewingStand(root, template, brewer);
                } else if (wildChests && isChest) {
                    sendDebugMessage(block.getLocation(), "WildChest 测试失败！");
                    return;
                } else if (InvUtils.fits(holder.getInventory(), template)) {
                    sendDebugMessage(block.getLocation(), "WildChest 测试成功。");
                    for (ItemStack targetItem : inventory.getContents()) {
                        if (targetItem == null || targetItem.getType() == Material.AIR) {
                            final ItemStack stack = root.getItemStack(new ItemRequest(template, template.getMaxStackSize()));
                            if (stack == null) {
                                break;
                            }
                            holder.getInventory().addItem(stack);
                            break;
                        } else if (StackUtils.itemsMatch(targetItem, template)) {
                            int canAdd = template.getMaxStackSize() - targetItem.getAmount();
                            if (canAdd > 0) {
                                final ItemStack stack = root.getItemStack(new ItemRequest(template, canAdd));
                                if (stack == null) {
                                    break;
                                }
                                holder.getInventory().addItem(stack);
                                break;
                            }
                        }
                    }
                }
            }
            targetBlock = targetBlock.getRelative(direction);
        }
    }

    private void handleFurnace(@Nonnull NetworkRoot root, @Nonnull ItemStack template, @Nonnull FurnaceInventory furnace) {
        if (template.getType().isFuel()
                && (furnace.getFuel() == null || furnace.getFuel().getType() == Material.AIR)
        ) {
            final ItemStack stack = root.getItemStack(new ItemRequest(template, template.getMaxStackSize()));
            if (stack == null) {
                return;
            }
            furnace.setFuel(stack.clone());
            stack.setAmount(0);
        } else if (!template.getType().isFuel() && furnace.getSmelting() == null || furnace.getSmelting().getType() == Material.AIR) {
            final ItemStack stack = root.getItemStack(new ItemRequest(template, template.getMaxStackSize()));
            if (stack == null) {
                return;
            }
            furnace.setSmelting(stack.clone());
            stack.setAmount(0);
        }
    }

    private void handleBrewingStand(@Nonnull NetworkRoot root, @Nonnull ItemStack template, @Nonnull BrewerInventory brewer) {
        if (template.getType() == Material.BLAZE_POWDER) {
            if (brewer.getFuel() == null || brewer.getFuel().getType() == Material.AIR) {
                final ItemStack stack = root.getItemStack(new ItemRequest(template.clone(), template.getMaxStackSize()));
                if (stack == null) {
                    return;
                }
                brewer.setFuel(stack.clone());
                stack.setAmount(0);
            } else if (brewer.getIngredient() == null || brewer.getIngredient().getType() == Material.AIR) {
                if (brewer.getIngredient() == null || brewer.getIngredient().getType() == Material.AIR) {
                    final ItemStack stack = root.getItemStack(new ItemRequest(template.clone(), template.getMaxStackSize()));
                    if (stack == null) {
                        return;
                    }
                    brewer.setIngredient(stack.clone());
                    stack.setAmount(0);
                }
            }
        } else if (template.getType() == Material.POTION) {
            for (int i = 0; i < 3; i++) {
                final ItemStack stackInSlot = brewer.getContents()[i];
                if (stackInSlot == null || stackInSlot.getType() == Material.AIR) {
                    final ItemStack[] contents = brewer.getContents();
                    final ItemStack stack = root.getItemStack(new ItemRequest(template.clone(), template.getMaxStackSize()));
                    if (stack == null) {
                        return;
                    }
                    contents[i] = stack.clone();
                    brewer.setContents(contents);
                    stack.setAmount(0);
                    return;
                }
            }
        } else if (brewer.getIngredient() == null || brewer.getIngredient().getType() == Material.AIR) {
            final ItemStack stack = root.getItemStack(new ItemRequest(template.clone(), template.getMaxStackSize()));
            if (stack == null) {
                return;
            }
            brewer.setIngredient(stack.clone());
            stack.setAmount(0);
        }
    }

    @Nonnull
    @Override
    protected int[] getBackgroundSlots() {
        return BACKGROUND_SLOTS;
    }

    @Override
    public int getNorthSlot() {
        return NORTH_SLOT;
    }

    @Override
    public int getSouthSlot() {
        return SOUTH_SLOT;
    }

    @Override
    public int getEastSlot() {
        return EAST_SLOT;
    }

    @Override
    public int getWestSlot() {
        return WEST_SLOT;
    }

    @Override
    public int getUpSlot() {
        return UP_SLOT;
    }

    @Override
    public int getDownSlot() {
        return DOWN_SLOT;
    }

    @Override
    public boolean runSync() {
        return true;
    }

    @Override
    public int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.MAROON, 1);
    }

    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(6);
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aTransferring data",
                "",
                "&7[&aMaximum distance&7]&f: &6" + maxDistance + " blocks",
                "&7[&aPush rate&7]&f:&7 Every &6" + pushItemTick + " SfTick &7Push once"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aParameter",
                "&7Default mode: &6First Stop",
                "&cUnchangable mode",
                "&7Default quantity: &664",
                "&cUnchangable mode"
        ));
        displayRecipes.add(new CustomItemStack(Material.BOOK,
                "&aFeature",
                "",
                "&eLine pusher will push an item through",
                "&ea line of machines it facing."
        ));
        return displayRecipes;
    }
}
