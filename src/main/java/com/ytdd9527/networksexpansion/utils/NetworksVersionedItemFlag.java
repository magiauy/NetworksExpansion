package com.ytdd9527.networksexpansion.utils;

import com.ytdd9527.networksexpansion.api.enums.MCVersion;
import io.github.sefiraat.networks.Networks;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemFlag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

@UtilityClass
public class NetworksVersionedItemFlag {
    public static final ItemFlag HIDE_ADDITIONAL_TOOLTIP;

    static {
        MCVersion version = Networks.getInstance().getMCVersion();
        HIDE_ADDITIONAL_TOOLTIP = version.isAtLeast(MCVersion.MC1_20_5) ? ItemFlag.HIDE_ADDITIONAL_TOOLTIP : getKey("HIDE_POTION_EFFECTS");

    }

    @Nullable
    private static ItemFlag getKey(@Nonnull String key) {
        try {
            Field field = ItemFlag.class.getDeclaredField(key);
            return (ItemFlag) field.get((Object) null);
        } catch (Exception ignored) {
            return null;
        }
    }
}
