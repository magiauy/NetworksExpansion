package com.ytdd9527.networksexpansion.utils;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

@UtilityClass
public class NetworksVersionedEnchantment {
    public static final Enchantment GLOW;
    public static final Enchantment LUCK_OF_THE_SEA;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();
        GLOW = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5) ? Enchantment.POWER : getKey("ARROW_DAMAGE");
        LUCK_OF_THE_SEA = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5) ? Enchantment.LUCK_OF_THE_SEA : getKey("LUCK");
    }

    @Nullable
    private static Enchantment getKey(@Nonnull String key) {
        try {
            Field field = Enchantment.class.getDeclaredField(key);
            return (Enchantment) field.get((Object) null);
        } catch (Exception ignored) {
            return null;
        }
    }
}
