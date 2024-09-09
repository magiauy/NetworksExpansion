package io.github.sefiraat.networks.managers;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.google.common.base.Preconditions;
import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedItem;
import io.github.sefiraat.networks.Networks;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;

public class SupportedPluginManager {

    @Getter
    private static SupportedPluginManager instance;

    private final @Getter boolean infinityExpansion;
    private final @Getter boolean fluffyMachines;
    private final @Getter boolean netheopoiesis;
    private final @Getter boolean slimeHud;
    private final @Getter boolean roseStacker;
    private final @Getter boolean wildStacker;

    private @Getter RoseStackerAPI roseStackerAPI;
    // region First Tick Only Registrations
    private @Getter boolean mcMMO;
    private @Getter boolean wildChests;

    // endregion

    public SupportedPluginManager() {
        Preconditions.checkArgument(instance == null, "Cannot instantiate class");
        instance = this;
        this.infinityExpansion = Bukkit.getPluginManager().isPluginEnabled("InfinityExpansion");
        this.fluffyMachines = Bukkit.getPluginManager().isPluginEnabled("FluffyMachines");
        this.netheopoiesis = Bukkit.getPluginManager().isPluginEnabled("Netheopoiesis");
        this.slimeHud = Bukkit.getPluginManager().isPluginEnabled("SlimeHUD");

        this.roseStacker = Bukkit.getPluginManager().isPluginEnabled("RoseStacker");
        if (roseStacker) {
            this.roseStackerAPI = RoseStackerAPI.getInstance();
        }

        this.wildStacker = Bukkit.getPluginManager().isPluginEnabled("WildStacker");
        Networks.getInstance()
                .getServer()
                .getScheduler()
                .runTaskLater(Networks.getInstance(), this::firstTickRegistrations, 1);
    }

    public static int getStackAmount(Item item) {
        if (getInstance().isWildStacker()) {
            return WildStackerAPI.getItemAmount(item);
        } else if (getInstance().isRoseStacker()) {
            StackedItem stackedItem = getInstance().getRoseStackerAPI().getStackedItem(item);
            return stackedItem == null ? item.getItemStack().getAmount() : stackedItem.getStackSize();
        } else {
            return item.getItemStack().getAmount();
        }
    }

    public static void setStackAmount(Item item, int amount) {
        if (getInstance().isWildStacker()) {
            WildStackerAPI.getStackedItem(item).setStackAmount(amount, true);
        } else if (getInstance().isRoseStacker()) {
            StackedItem stackedItem = getInstance().getRoseStackerAPI().getStackedItem(item);
            if (stackedItem != null) {
                stackedItem.setStackSize(amount);
            }
        } else {
            item.getItemStack().setAmount(amount);
        }
    }

    private void firstTickRegistrations() {
        this.wildChests = Bukkit.getPluginManager().isPluginEnabled("WildChests");
        this.mcMMO = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
    }
}
