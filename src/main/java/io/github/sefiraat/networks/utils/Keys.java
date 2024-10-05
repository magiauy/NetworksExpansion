package io.github.sefiraat.networks.utils;

import io.github.sefiraat.networks.Networks;
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
    public static final NamespacedKey networkskey = newKey("networkskey");


    @Nonnull
    public static NamespacedKey newKey(@Nonnull String key) {
        return new NamespacedKey(Networks.getInstance(), key);
    }

    @Nonnull
    public static NamespacedKey customNewKey(@Nonnull String namespace, @Nonnull String key) {
        return new NamespacedKey(namespace, key);
    }
}
