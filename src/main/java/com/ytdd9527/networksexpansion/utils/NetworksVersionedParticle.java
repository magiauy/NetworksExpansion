package com.ytdd9527.networksexpansion.utils;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import org.bukkit.Particle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

@UtilityClass
public class NetworksVersionedParticle {
    public static final Particle DUST;
    public static final Particle EXPLOSION;
    public static final Particle SMOKE;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();
        DUST = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5) ? Particle.DUST : getKey("REDSTONE");
        EXPLOSION = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5) ? Particle.EXPLOSION : getKey("EXPLOSION_LARGE");
        SMOKE = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5) ? Particle.SMOKE : getKey("SMOKE_NORMAL");
    }

    @Nullable
    private static Particle getKey(@Nonnull String key) {
        try {
            Field field = Particle.class.getDeclaredField(key);
            return (Particle) field.get((Object) null);
        } catch (Exception ignored) {
            return null;
        }
    }
}
