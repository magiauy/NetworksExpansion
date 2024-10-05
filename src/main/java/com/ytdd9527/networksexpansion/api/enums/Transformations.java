package com.ytdd9527.networksexpansion.api.enums;


import dev.sefiraat.sefilib.misc.RotationFace;
import dev.sefiraat.sefilib.misc.TransformationBuilder;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;

import javax.annotation.Nonnull;

public enum Transformations {

    BRIDGE_1(new TransformationBuilder()
            .scale(0.5f, 0.5f, 2f)
            .build()
    ),
    BRIDGE_3(new TransformationBuilder()
            .scale(-1.5f, 1.5f, 0.8f)
            .build()
    ),
    BRIDGE_2(new TransformationBuilder()
            .scale(2f, 1f, 1f)
            .secondRotation(RotationFace.TOP, 90)
            .build()
    ),
    TWO(new TransformationBuilder()
            .scale(1.9f, 2f, 1.9f)
            .build()
    ),
    NE_MODEL_CAPACITOR_5(new TransformationBuilder()
            .scale(1.5f, -4f, 1.5f)
            .build()
    ),
    CLOCHE_BASE(new TransformationBuilder()
            .scale(0.5f, 1f, 0.5f)
            .build()
    ),
    CLOCHE_GLASS(new TransformationBuilder()
            .scale(0.5f, 0.5f, 0.5f)
            .build()
    ),
    CLOCHE_DIRT(new TransformationBuilder()
            .scale(0.4f, 0.4f, 0.4f)
            .build()
    ),
    ;


    private final Transformation transformation;

    Transformations(@Nonnull Transformation transformation) {
        this.transformation = transformation;
    }

    public Transformation getTransformation() {
        return getTransformation(true);
    }

    public Transformation getTransformation(boolean itemDisplay) {
        // In 1.20+ the y axis of item displays are rotated by 180°
        // This corrects the visuals by rotating again
        if (itemDisplay && Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_20)) {
            return new Transformation(transformation.getTranslation(),
                    transformation.getLeftRotation(),
                    transformation.getScale(),
                    new Quaternionf(transformation.getRightRotation()).rotateY((float) Math.PI));
        }
        return transformation;
    }

}
