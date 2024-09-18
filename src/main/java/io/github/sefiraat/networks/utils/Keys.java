package io.github.sefiraat.networks.utils;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.sefiraat.networks.Networks;
import io.github.sefiraat.networks.managers.SupportedPluginManager;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;

@Data
@UtilityClass
public class Keys {


    public static final NamespacedKey ON_COOLDOWN = newKey("cooldown");
    public static final NamespacedKey CARD_INSTANCE = newKey("ntw_card");
    public static final NamespacedKey QUANTUM_STORAGE_INSTANCE = newKey("quantum_storage");
    public static final NamespacedKey BLUEPRINT_INSTANCE = newKey("ntw_blueprint");
    public static final NamespacedKey BLUEPRINT_INSTANCE2 = customNewKey("networks", "ntw_blueprint");
    public static final NamespacedKey BLUEPRINT_INSTANCE3 = customNewKey("networks-changed", "ntw_blueprint");
    public static final NamespacedKey FACE = newKey("face");
    public static final NamespacedKey ITEM = newKey("item");
    public static final NamespacedKey AMOUNT = newKey("amount");
    public static final NamespacedKey TRANSFER_MODE = newKey("transfer_mode");
    public static final NamespacedKey STORAGE_UNIT_UPGRADE_TABLE = newKey("storage_upgrade_table");
    public static final NamespacedKey STORAGE_UNIT_UPGRADE_TABLE_MODEL = newKey("storage_upgrade_table_model");
    public static final NamespacedKey NETWORKSKEY = newKey("networkskey");
    public static final NamespacedKey ITEM_MOVER_ITEM = newKey("item_mover_item");
    public static final NamespacedKey ITEM_MOVER_AMOUNT = newKey("item_mover_amount");

    public static NamespacedKey INFINITY_DISPLAY = null;

    static {
        if (SupportedPluginManager.getInstance().isInfinityExpansion()) {
            INFINITY_DISPLAY = InfinityExpansion.createKey("display");
        }
    }


    @Nonnull
    public static NamespacedKey newKey(@Nonnull String key) {
        return new NamespacedKey(Networks.getInstance(), key);
    }

    @Nonnull
    public static NamespacedKey customNewKey(@Nonnull String namespace, @Nonnull String key) {
        return new NamespacedKey(namespace, key);
    }
}
