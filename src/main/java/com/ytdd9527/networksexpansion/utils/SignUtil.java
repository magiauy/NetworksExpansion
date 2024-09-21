package com.ytdd9527.networksexpansion.utils;


import io.github.sefiraat.networks.Networks;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@UtilityClass
public class SignUtil {
    public static final BlockFace[] VALID_SIGN_FACES = new BlockFace[]{
            BlockFace.UP
    };

    public static final BlockFace[] VALID_WALL_SIGN_FACES = new BlockFace[]{
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST
    };
    @Nullable
    public static Sign getSignByBlock(@Nonnull Block block) {
        if (SlimefunTag.SIGNS.isTagged(block.getType()) || SlimefunTag.WALL_SIGNS.isTagged(block.getType())) {
            return (Sign) block.getState();
        }

        return null;
    }

    @Nonnull
    public static Sign setSignText(@Nonnull Sign sign, boolean lock, @Nullable String... lines) {
        // Sign range: 0-3
        if (lines == null || lines.length == 0) {
            return sign;
        }

        for (int i = 0; i < Math.min(lines.length, 4); i++) {
            if (lines[i] == null) {
                continue;
            }
            sign.setLine(i, lines[i]);
        }

        if (lock) {
            sign.setWaxed(true);
        }

        sign.update();
        return sign;
    }

    public static void addSignTextAround(@Nonnull Block block, boolean lock, @Nullable String... lines) {
        if (lines == null || lines.length == 0) {
            return;
        }

        Sign sign = null;
        if (Networks.getSlimefunTickCount() % 20 == 0) {
            for (BlockFace face : VALID_SIGN_FACES) {
                Block aroundRelative = block.getRelative(face);
                if (SlimefunTag.SIGNS.isTagged(aroundRelative.getType())) {
                    sign = (Sign) aroundRelative.getState();
                    break;
                }
            }

            for (BlockFace face : VALID_WALL_SIGN_FACES) {
                Block aroundRelative = block.getRelative(face);
                if (SlimefunTag.SIGNS.isTagged(aroundRelative.getType())) {
                    sign = (Sign) aroundRelative.getState();
                    break;
                }
            }
        }
        if (sign == null) {
            return;
        }

        setSignText(sign, lock, lines);
    }
}
