package io.github.sefiraat.networks.slimefun.tools;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.sefiraat.networks.network.NetworkRoot;
import io.github.sefiraat.networks.slimefun.network.NetworkController;
import io.github.sefiraat.networks.utils.Theme;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class NetworkProbe extends SlimefunItem implements CanCooldown {

    private static final MessageFormat MESSAGE_FORMAT = new MessageFormat("{0}{1}: {2}{3}", Locale.ROOT);

    public NetworkProbe(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

    }

    @Override
    public void preRegister() {
        addItemHandler((ItemUseHandler) this::onUse);
    }

    protected void onUse(PlayerRightClickEvent e) {
        final Optional<Block> optional = e.getClickedBlock();
        if (optional.isPresent()) {
            final Block block = optional.get();
            final Player player = e.getPlayer();
            if (canBeUsed(player, e.getItem())) {
                var blockData = StorageCacheUtils.getBlock(block.getLocation());
                if (blockData == null) {
                    return;
                }

                var slimefunItem = SlimefunItem.getById(blockData.getSfId());
                if (slimefunItem instanceof NetworkController) {
                    e.cancel();
                    displayToPlayer(block, player);
                    putOnCooldown(e.getItem());
                }
            }
        }
    }

    private void displayToPlayer(@Nonnull Block block, @Nonnull Player player) {
        final NetworkRoot root = NetworkController.getNetworks().get(block.getLocation());
        if (root != null) {
            final int bridges = root.getBridges().size();
            final int monitors = root.getMonitors().size();
            final int importers = root.getImporters().size();
            final int exporters = root.getExporters().size();
            final int grids = root.getGrids().size();
            final int cells = root.getCells().size();
            final int wipers = root.getWipers().size();
            final int grabbers = root.getGrabbers().size();
            final int pushers = root.getPushers().size();
            final int cutters = root.getCutters().size();
            final int pasters = root.getPasters().size();
            final int vacuums = root.getVacuums().size();
            final int purgers = root.getPurgers().size();
            final int crafters = root.getCrafters().size();
            final int powerNodes = root.getPowerNodes().size();
            final int powerDisplays = root.getPowerDisplays().size();
            final int encoders = root.getEncoders().size();
            final int wirelessTransmitters = root.getWirelessTransmitters().size();
            final int wirelessReceivers = root.getWirelessReceivers().size();
            final int powerOutlets = root.getPowerOutlets().size();
            final int greedyBlocks = root.getGreedyBlocks().size();

            final int advancedImporters = root.getAdvancedImporters().size();
            final int advancedExporters = root.getAdvancedExporters().size();
            final int advancedGreedyBlocks = root.getAdvancedGreedyBlocks().size();
            final int advancedPurgers = root.getAdvancedPurgers().size();
            final int transferPushers = root.getTransferPushers().size();
            final int transferGrabbers = root.getTransferGrabbers().size();
            final int transfers = root.getTransfers().size();
            final int lineTransferVanillaPushers = root.getLineTransferVanillaPushers().size();
            final int lineTransferVanillaGrabbers = root.getLineTransferVanillaGrabbers().size();
            final int inputOnlyMonitor = root.getInputOnlyMonitors().size();
            final int outputOnlyMonitor = root.getOutputOnlyMonitors().size();

            final Map<ItemStack, Long> allNetworkItems = root.getAllNetworkItemsLongType();
            final int distinctItems = allNetworkItems.size();


            long totalItems = allNetworkItems.values().stream().mapToLong(integer -> integer).sum();

            final String nodeCount = root.getNodeCount() >= root.getMaxNodes()
                    ? Theme.ERROR + String.valueOf(root.getNodeCount()) + "+"
                    : String.valueOf(root.getNodeCount());

            final ChatColor c = Theme.CLICK_INFO.getColor();
            final ChatColor p = Theme.SUCCESS.getColor();

            player.sendMessage("------------------------------");
            player.sendMessage("       Network Summary        ");
            player.sendMessage("------------------------------");

            player.sendMessage(formatter("Bridges", bridges));
            player.sendMessage(formatter("Monitors", monitors));
            player.sendMessage(formatter("Importers", importers));
            player.sendMessage(formatter("Exporters", exporters));
            player.sendMessage(formatter("Grids", grids));
            player.sendMessage(formatter("Cells", cells));
            player.sendMessage(formatter("Wipers", wipers));
            player.sendMessage(formatter("Grabbers", grabbers));
            player.sendMessage(formatter("Pushers", pushers));
            player.sendMessage(formatter("Purgers", purgers));
            player.sendMessage(formatter("Crafters", crafters));
            player.sendMessage(formatter("Power Nodes", powerNodes));
            player.sendMessage(formatter("Power Displays", powerDisplays));
            player.sendMessage(formatter("Encoders", encoders));
            player.sendMessage(formatter("Cutters", cutters));
            player.sendMessage(formatter("Pasters", pasters));
            player.sendMessage(formatter("Vacuums", vacuums));
            player.sendMessage(formatter("Wireless Transmitters", wirelessTransmitters));
            player.sendMessage(formatter("Wireless Receivers", wirelessReceivers));
            player.sendMessage(formatter("Power Outlets", powerOutlets));
            player.sendMessage(formatter("Greedy Blocks", greedyBlocks));
            player.sendMessage("-----------------------------------------");
            player.sendMessage("        Network Expansion - Summary      ");
            player.sendMessage("-----------------------------------------");
            player.sendMessage(formatter("Line Pusher", chainPushers));
            player.sendMessage(formatter("Line Grabber", chainGrabbers));
            player.sendMessage(formatter("Line Transfer", chainDispatchers));
            player.sendMessage(formatter("Vanilla Line Pusher", chainVanillaPushers));
            player.sendMessage(formatter("Vanilla Line Grabber", chainVanillaGrabbers));
            player.sendMessage(formatter("Advanced Importers", advancedImporters));
            player.sendMessage(formatter("Advanced Exporters", advancedExporters));
            player.sendMessage("-----------------------------------------");
            player.sendMessage(formatter("Distinct Items", distinctItems));
            player.sendMessage(formatter("Total Items", totalItems));
            player.sendMessage("-----------------------------------------");
            player.sendMessage(formatter("Total Nodes", nodeCount + "/" + root.getMaxNodes()));
            if (root.isOverburdened()) {
                player.sendMessage(Theme.ERROR + "Warning: " + Theme.PASSIVE +
                                        "Your network has reached or exceeded the maximum node limit. " +
                                       "Nodes beyond the limit will not function, which nodes these are " +
                                       "may not always be the same. Reduce your total nodes."                );
            }
        }
    }

    @Override
    public int cooldownDuration() {
        return 10;
    }

    public String formatter(String name, long count) {
        return MESSAGE_FORMAT.format(new Object[]{Theme.CLICK_INFO.getColor(), name, Theme.SUCCESS.getColor(), count}, new StringBuffer(), null).toString();
    }

    public String formatter(String name, String s) {
        return MESSAGE_FORMAT.format(new Object[]{Theme.CLICK_INFO.getColor(), name, Theme.SUCCESS.getColor(), s}, new StringBuffer(), null).toString();
    }
}
