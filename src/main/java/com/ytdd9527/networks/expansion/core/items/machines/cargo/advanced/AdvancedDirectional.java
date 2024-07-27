package com.ytdd9527.networks.expansion.core.items.machines.cargo.advanced;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import com.ytdd9527.networks.expansion.core.enums.TransportMode;
import com.ytdd9527.networks.expansion.utils.TextUtil;
import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.network.NodeType;
import io.github.sefiraat.networks.slimefun.network.NetworkDirectional;
import io.github.sefiraat.networks.utils.NetworkUtils;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.guizhanss.guizhanlib.minecraft.helper.MaterialHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AdvancedDirectional extends NetworkDirectional {

    protected static final String DIRECTION = "direction";
    protected static final String OWNER_KEY = "uuid";
    protected static final String LIMIT_KEY = "transport_limit";
    protected static final String TRANSPORT_MODE_KEY = "transport_mode";
    private static final int NORTH_SLOT = 12;
    private static final int SOUTH_SLOT = 30;
    private static final int EAST_SLOT = 22;
    private static final int WEST_SLOT = 20;
    private static final int UP_SLOT = 15;
    private static final int DOWN_SLOT = 33;
    private static final Set<BlockFace> VALID_FACES = EnumSet.of(
            BlockFace.UP,
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    );

    private static final CustomItemStack MINUS_ICON = new CustomItemStack(
            Material.RED_CONCRETE, TextUtil.colorRandomString("减少数量")
    );

    private static final CustomItemStack SHOW_ICON = new CustomItemStack(
            Material.GOLD_BLOCK,
            Theme.NOTICE + "数量",
            Theme.NOTICE + "当前数量: 64"
    );

    private static final CustomItemStack ADD_ICON = new CustomItemStack(
            Material.GREEN_CONCRETE, TextUtil.colorRandomString("增加数量")
    );

    private static final CustomItemStack TRANSPORT_MODE_ICON = new CustomItemStack(
            Material.GREEN_CONCRETE,
            TextUtil.colorRandomString("运输模式"),
            TextUtil.colorRandomString("当前模式：") + TextUtil.colorRandomString("无")
    );
    private static final Map<Location, BlockFace> SELECTED_DIRECTION_MAP = new HashMap<>();
    private static final Map<Location, Integer> NETWORK_NUMBER_MAP = new HashMap<>();
    private static final Map<Location, TransportMode> NETWORK_TRANSPORT_MODE_MAP = new HashMap<>();
    private final ItemStack showIconClone;
    private final ItemStack transportModeIconClone;
    public TransportMode transportMode = TransportMode.FIRST_STOP;
    NetworkDirectional instance = this;

    protected AdvancedDirectional(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, NodeType type) {
        super(itemGroup, item, recipeType, recipe, type);
        this.showIconClone = SHOW_ICON.clone();
        this.transportModeIconClone = TRANSPORT_MODE_ICON.clone();

        addItemHandler(
                new BlockTicker() {
                    @Override
                    public boolean isSynchronized() {
                        return runSync();
                    }

                    @Override
                    public void tick(Block block, SlimefunItem slimefunItem, SlimefunBlockData data) {
                        onTick(data.getBlockMenu(), block);
                    }

                    @Override
                    public void uniqueTick() {
                        onUniqueTick();
                    }
                }
        );
    }

    @Nonnull
    public static ItemStack getDirectionalSlotPane(@Nonnull BlockFace blockFace, @Nonnull SlimefunItem slimefunItem, boolean active) {
        final ItemStack displayStack = new CustomItemStack(
                slimefunItem.getItem(),
                Theme.PASSIVE + "设置朝向: " + blockFace.name() + " (" + ChatColor.stripColor(slimefunItem.getItemName()) + ")"
        );
        final ItemMeta itemMeta = displayStack.getItemMeta();
        if (active) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemMeta.setLore(List.of(
                Theme.CLICK_INFO + "左键点击: " + Theme.PASSIVE + "设置朝向",
                Theme.CLICK_INFO + "Shift+左键点击: " + Theme.PASSIVE + "打开目标方块"
        ));
        displayStack.setItemMeta(itemMeta);
        return displayStack;
    }

    @Nonnull
    public static ItemStack getDirectionalSlotPane(@Nonnull BlockFace blockFace, @Nonnull Material blockMaterial, boolean active) {
        if (blockMaterial.isItem() && !blockMaterial.isAir()) {
            final ItemStack displayStack = new CustomItemStack(
                    blockMaterial,
                    Theme.PASSIVE + "设置朝向 " + blockFace.name() + " (" + MaterialHelper.getName(blockMaterial) + ")"
            );
            final ItemMeta itemMeta = displayStack.getItemMeta();
            if (active) {
                itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.setLore(List.of(
                    Theme.CLICK_INFO + "左键点击: " + Theme.PASSIVE + "设置朝向",
                    Theme.CLICK_INFO + "Shift+左键点击: " + Theme.PASSIVE + "打开目标方块"
            ));
            displayStack.setItemMeta(itemMeta);
            return displayStack;
        } else {
            Material material = active ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
            return new CustomItemStack(
                    material,
                    ChatColor.GRAY + "设置朝向: " + blockFace.name()
            );
        }
    }

    @Nullable
    public static BlockFace getSelectedFace(@Nonnull Location location) {
        return SELECTED_DIRECTION_MAP.get(location);
    }

    public void updateGui(@Nullable BlockMenu blockMenu) {
        if (blockMenu == null || !blockMenu.hasViewer()) {
            return;
        }

        BlockFace direction = getCurrentDirection(blockMenu);

        for (BlockFace blockFace : VALID_FACES) {
            final Block block = blockMenu.getBlock().getRelative(blockFace);
            final SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(block.getLocation());
            if (slimefunItem != null) {
                switch (blockFace) {
                    case NORTH ->
                            blockMenu.replaceExistingItem(getNorthSlot(), getDirectionalSlotPane(blockFace, slimefunItem, blockFace == direction));
                    case SOUTH ->
                            blockMenu.replaceExistingItem(getSouthSlot(), getDirectionalSlotPane(blockFace, slimefunItem, blockFace == direction));
                    case EAST ->
                            blockMenu.replaceExistingItem(getEastSlot(), getDirectionalSlotPane(blockFace, slimefunItem, blockFace == direction));
                    case WEST ->
                            blockMenu.replaceExistingItem(getWestSlot(), getDirectionalSlotPane(blockFace, slimefunItem, blockFace == direction));
                    case UP ->
                            blockMenu.replaceExistingItem(getUpSlot(), getDirectionalSlotPane(blockFace, slimefunItem, blockFace == direction));
                    case DOWN ->
                            blockMenu.replaceExistingItem(getDownSlot(), getDirectionalSlotPane(blockFace, slimefunItem, blockFace == direction));
                    default -> throw new IllegalStateException("意外的值: " + blockFace);
                }
            } else {
                final Material material = block.getType();
                switch (blockFace) {
                    case NORTH ->
                            blockMenu.replaceExistingItem(getNorthSlot(), getDirectionalSlotPane(blockFace, material, blockFace == direction));
                    case SOUTH ->
                            blockMenu.replaceExistingItem(getSouthSlot(), getDirectionalSlotPane(blockFace, material, blockFace == direction));
                    case EAST ->
                            blockMenu.replaceExistingItem(getEastSlot(), getDirectionalSlotPane(blockFace, material, blockFace == direction));
                    case WEST ->
                            blockMenu.replaceExistingItem(getWestSlot(), getDirectionalSlotPane(blockFace, material, blockFace == direction));
                    case UP ->
                            blockMenu.replaceExistingItem(getUpSlot(), getDirectionalSlotPane(blockFace, material, blockFace == direction));
                    case DOWN ->
                            blockMenu.replaceExistingItem(getDownSlot(), getDirectionalSlotPane(blockFace, material, blockFace == direction));
                    default -> throw new IllegalStateException("意外的值: " + blockFace);
                }
            }
        }
    }

    @Nonnull
    protected BlockFace getCurrentDirection(@Nonnull BlockMenu blockMenu) {
        BlockFace direction = SELECTED_DIRECTION_MAP.get(blockMenu.getLocation().clone());

        if (direction == null) {
            direction = BlockFace.valueOf(StorageCacheUtils.getData(blockMenu.getLocation(), DIRECTION));
            SELECTED_DIRECTION_MAP.put(blockMenu.getLocation().clone(), direction);
        }
        return direction;
    }

    @OverridingMethodsMustInvokeSuper
    protected void onTick(@Nullable BlockMenu blockMenu, @Nonnull Block block) {
        addToRegistry(block);
        updateGui(blockMenu);
    }

    protected void onUniqueTick() {
        super.onUniqueTick();
    }

    @Override
    public void onPlace(@Nonnull BlockPlaceEvent event) {
        NetworkStorage.removeNode(event.getBlock().getLocation());
        var blockData = StorageCacheUtils.getBlock(event.getBlock().getLocation());
        blockData.setData(OWNER_KEY, event.getPlayer().getUniqueId().toString());
        blockData.setData(DIRECTION, BlockFace.SELF.name());
        blockData.setData(LIMIT_KEY, String.valueOf(getMaxLimit()));
        blockData.setData(TRANSPORT_MODE_KEY, String.valueOf(TransportMode.FIRST_STOP));
        NetworkUtils.applyConfig(instance, blockData.getBlockMenu(), event.getPlayer());
    }

    @Override
    public void postRegister() {
        new BlockMenuPreset(this.getId(), this.getItemName()) {

            @Override
            public void init() {
                drawBackground(getBackgroundSlots());

                if (getOtherBackgroundSlots() != null && getOtherBackgroundStack() != null) {
                    drawBackground(getOtherBackgroundStack(), getOtherBackgroundSlots());
                }

                addItem(getNorthSlot(), getDirectionalSlotPane(BlockFace.NORTH, Material.AIR, false), (player, i, itemStack, clickAction) -> false);
                addItem(getSouthSlot(), getDirectionalSlotPane(BlockFace.SOUTH, Material.AIR, false), (player, i, itemStack, clickAction) -> false);
                addItem(getEastSlot(), getDirectionalSlotPane(BlockFace.EAST, Material.AIR, false), (player, i, itemStack, clickAction) -> false);
                addItem(getWestSlot(), getDirectionalSlotPane(BlockFace.WEST, Material.AIR, false), (player, i, itemStack, clickAction) -> false);
                addItem(getUpSlot(), getDirectionalSlotPane(BlockFace.UP, Material.AIR, false), (player, i, itemStack, clickAction) -> false);
                addItem(getDownSlot(), getDirectionalSlotPane(BlockFace.DOWN, Material.AIR, false), (player, i, itemStack, clickAction) -> false);
                addItem(getAddSlot(), getAddIcon(), (p, i, itemStack, clickAction) -> false);
                addItem(getMinusSlot(), getMinusIcon(), (p, i, itemStack, clickAction) -> false);
                addItem(getShowSlot(), getShowIcon(), (p, i, itemStack, clickAction) -> false);
                if (getTransportModeSlot() != -1) {
                    addItem(getTransportModeSlot(), getTransportModeIcon(), (p, i, itemStack, clickAction) -> false);
                }
            }

            @Override
            public void newInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block b) {
                final BlockFace direction;
                final Location location = blockMenu.getLocation();
                final String string = StorageCacheUtils.getData(location, DIRECTION);
                final String rawLimit = StorageCacheUtils.getData(location, LIMIT_KEY);
                final String rawMode = StorageCacheUtils.getData(location, TRANSPORT_MODE_KEY);

                if (string == null) {
                    // This likely means a block was placed before I made it directional
                    direction = BlockFace.SELF;
                    StorageCacheUtils.setData(location, DIRECTION, BlockFace.SELF.name());
                } else {
                    direction = BlockFace.valueOf(string);
                }
                SELECTED_DIRECTION_MAP.put(location.clone(), direction);

                int limit;
                if (rawLimit == null) {
                    limit = getMaxLimit();
                } else {
                    limit = Integer.parseInt(rawLimit);
                }
                NETWORK_NUMBER_MAP.put(location.clone(), limit);

                TransportMode mode;
                if (rawMode == null) {
                    mode = TransportMode.FIRST_STOP;
                    StorageCacheUtils.setData(location, TRANSPORT_MODE_KEY, String.valueOf(mode));
                } else {
                    mode = TransportMode.valueOf(rawMode);
                }
                NETWORK_TRANSPORT_MODE_MAP.put(location.clone(), mode);

                blockMenu.addMenuClickHandler(getNorthSlot(), (player, i, itemStack, clickAction) ->
                        directionClick(player, clickAction, blockMenu, BlockFace.NORTH));
                blockMenu.addMenuClickHandler(getSouthSlot(), (player, i, itemStack, clickAction) ->
                        directionClick(player, clickAction, blockMenu, BlockFace.SOUTH));
                blockMenu.addMenuClickHandler(getEastSlot(), (player, i, itemStack, clickAction) ->
                        directionClick(player, clickAction, blockMenu, BlockFace.EAST));
                blockMenu.addMenuClickHandler(getWestSlot(), (player, i, itemStack, clickAction) ->
                        directionClick(player, clickAction, blockMenu, BlockFace.WEST));
                blockMenu.addMenuClickHandler(getUpSlot(), (player, i, itemStack, clickAction) ->
                        directionClick(player, clickAction, blockMenu, BlockFace.UP));
                blockMenu.addMenuClickHandler(getDownSlot(), (player, i, itemStack, clickAction) ->
                        directionClick(player, clickAction, blockMenu, BlockFace.DOWN));

                blockMenu.addMenuClickHandler(getShowSlot(), (player, i, itemStack, clickAction) -> false);

                blockMenu.addMenuClickHandler(getAddSlot(), (p, slot, item, action) ->
                        addClick(location, action));
                blockMenu.addMenuClickHandler(getMinusSlot(), (p, slot, item, action) ->
                        minusClick(location, action));

                if (getTransportModeSlot() != -1) {
                    blockMenu.addMenuClickHandler(getTransportModeSlot(), (p, slot, item, action) ->
                            toggleTransportMode(location));
                }

                updateShowIcon(location);
                updateTransportModeIcon(location);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return this.getSlimefunItem().canUse(player, false)
                        && Slimefun.getProtectionManager().hasPermission(player, block.getLocation(), Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return getInputSlots();
                } else {
                    return getOutputSlots();
                }
            }
        };
    }

    @ParametersAreNonnullByDefault
    public boolean directionClick(Player player, ClickAction action, BlockMenu blockMenu, BlockFace blockFace) {
        if (action.isShiftClicked()) {
            openDirection(player, blockMenu, blockFace);
        } else {
            setDirection(blockMenu, blockFace);
        }
        return false;
    }

    @ParametersAreNonnullByDefault
    public void openDirection(Player player, BlockMenu blockMenu, BlockFace blockFace) {
        super.openDirection(player, blockMenu, blockFace);
    }

    @ParametersAreNonnullByDefault
    public void setDirection(BlockMenu blockMenu, BlockFace blockFace) {
        SELECTED_DIRECTION_MAP.put(blockMenu.getLocation().clone(), blockFace);
        StorageCacheUtils.setData(blockMenu.getBlock().getLocation(), DIRECTION, blockFace.name());
    }

    @ParametersAreNonnullByDefault
    public boolean minusClick(Location location, ClickAction action) {
        int n = 1;
        if (action.isRightClicked()) {
            n = 8;
        }
        if (action.isShiftClicked()) {
            n = 64;
        }
        minusNumber(location, n);
        return false;
    }

    @ParametersAreNonnullByDefault
    public boolean addClick(Location location, ClickAction action) {
        int n = 1;
        if (action.isRightClicked()) {
            n = 8;
        }
        if (action.isShiftClicked()) {
            n = 64;
        }
        Networks.getInstance().getLogger().info("@addClick 1 current number: " + getCurrentNumber(location));
        addNumber(location, n);
        Networks.getInstance().getLogger().info("@addClick 2 current number: " + getCurrentNumber(location));
        return false;
    }

    @Nonnull
    protected abstract int[] getBackgroundSlots();

    @Nullable
    protected int[] getOtherBackgroundSlots() {
        return null;
    }

    @Nullable
    protected CustomItemStack getOtherBackgroundStack() {
        return null;
    }

    public int getNorthSlot() {
        return NORTH_SLOT;
    }

    public int getSouthSlot() {
        return SOUTH_SLOT;
    }

    public int getEastSlot() {
        return EAST_SLOT;
    }

    public int getWestSlot() {
        return WEST_SLOT;
    }

    public int getUpSlot() {
        return UP_SLOT;
    }

    public int getDownSlot() {
        return DOWN_SLOT;
    }

    public int[] getItemSlots() {
        return new int[0];
    }

    public int[] getInputSlots() {
        return new int[0];
    }

    public int[] getOutputSlots() {
        return new int[0];
    }

    protected Particle.DustOptions getDustOptions() {
        return super.getDustOptions();
    }

    protected void showParticle(@Nonnull Location location, @Nonnull BlockFace blockFace) {
        final Vector faceVector = blockFace.getDirection().clone().multiply(-1);
        final Vector pushVector = faceVector.clone().multiply(2);
        final Location displayLocation = location.clone().add(0.5, 0.5, 0.5).add(faceVector);
        location.getWorld().spawnParticle(Particle.REDSTONE, displayLocation, 0, pushVector.getX(), pushVector.getY(), pushVector.getZ(), getDustOptions());
    }

    public ItemStack getShowIcon() {
        return this.showIconClone;
    }

    public CustomItemStack getMinusIcon() {
        return MINUS_ICON;
    }

    public CustomItemStack getAddIcon() {
        return ADD_ICON;
    }

    public CustomItemStack getTransportModeIcon() {
        return TRANSPORT_MODE_ICON;
    }


    public int getCurrentNumber(Location location) {
        Integer number = NETWORK_NUMBER_MAP.get(location.clone());
        if (number == null) {
            number = Integer.parseInt(StorageCacheUtils.getData(location, LIMIT_KEY));
            NETWORK_NUMBER_MAP.put(location.clone(), number);
        }
        return number;
    }

    public void setCurrentNumber(Location location, int number) {
        NETWORK_NUMBER_MAP.put(location.clone(), number);
        StorageCacheUtils.setData(location, LIMIT_KEY, Integer.toString(number));
    }

    public TransportMode getCurrentTransportMode(Location location) {
        TransportMode mode = NETWORK_TRANSPORT_MODE_MAP.get(location.clone());
        if (mode == null) {
            mode = TransportMode.valueOf(StorageCacheUtils.getData(location, TRANSPORT_MODE_KEY));
            NETWORK_TRANSPORT_MODE_MAP.put(location.clone(), mode);
        }
        return mode;
    }

    public void setTransportMode(Location location, TransportMode mode) {
        NETWORK_TRANSPORT_MODE_MAP.put(location.clone(), mode);
        StorageCacheUtils.setData(location, TRANSPORT_MODE_KEY, String.valueOf(mode));
    }

    public boolean toggleTransportMode(Location location) {
        TransportMode mode = getCurrentTransportMode(location);
        setTransportMode(location, mode.next());
        updateTransportModeIcon(location);
        return false;
    }

    public void minusNumber(Location location, int number) {
        int currentNumber = getCurrentNumber(location);
        if (currentNumber - number >= 1) {
            setCurrentNumber(location, (currentNumber - number));
        } else {
            setCurrentNumber(location, getMaxLimit() - (number - currentNumber));
        }
        updateShowIcon(location);
    }

    public void addNumber(Location location, int number) {
        int currentNumber = getCurrentNumber(location);
        int newNumber = currentNumber + number;
        if (comeMaxLimit(newNumber)) {
            newNumber = newNumber - getMaxLimit();
        }
        setCurrentNumber(location, newNumber);
        updateShowIcon(location);
    }

    public abstract boolean comeMaxLimit(int currentNumber);

    public abstract int getMaxLimit();

    public void updateShowIcon(Location location) {

        ItemMeta itemMeta = this.showIconClone.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.set(0, Theme.NOTICE + "当前数量: " + getCurrentNumber(location));
        itemMeta.setLore(lore);
        this.showIconClone.setItemMeta(itemMeta);

        BlockMenu blockMenu = StorageCacheUtils.getMenu(location);
        if (blockMenu != null) {
            blockMenu.replaceExistingItem(getShowSlot(), this.showIconClone);
        }
    }

    public void updateTransportModeIcon(Location location) {
        ItemMeta itemMeta = this.transportModeIconClone.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.set(0, Theme.NOTICE + "当前模式: " + Theme.MECHANISM + getCurrentTransportMode(location).getName());
        itemMeta.setLore(lore);
        this.transportModeIconClone.setItemMeta(itemMeta);

        BlockMenu blockMenu = StorageCacheUtils.getMenu(location);
        if (blockMenu != null) {
            int slot = getTransportModeSlot();
            if (slot != -1) {
                blockMenu.replaceExistingItem(slot, this.transportModeIconClone);
            }
        }
    }

    protected abstract int getMinusSlot();

    protected abstract int getShowSlot();

    protected abstract int getAddSlot();

    protected abstract int getTransportModeSlot();
}
