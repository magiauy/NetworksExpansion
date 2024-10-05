package com.ytdd9527.networksexpansion.utils;

import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Final_ROOT
 * @since 2.0
 */
@UtilityClass
public class GuideUtil {
    private static final SurvivalGuideImpl survivalGuide = new SurvivalGuideImpl();
    private static final CheatGuideImpl cheatGuide = new CheatGuideImpl();

    @ParametersAreNonnullByDefault
    public static void openMainMenuAsync(Player player, SlimefunGuideMode mode, int selectedPage) {
        if (!PlayerProfile.get(player, profile -> Slimefun.runSync(() -> openMainMenu(player, profile, mode, selectedPage)))) {
            Slimefun.getLocalization().sendMessage(player, "messages.opening-guide");
        }
    }

    @ParametersAreNonnullByDefault
    public static void openMainMenu(Player player, PlayerProfile profile, SlimefunGuideMode mode, int selectedPage) {
        getGuide(player, mode).openMainMenu(profile, selectedPage);
    }

    public static SlimefunGuideImplementation getGuide(Player player, SlimefunGuideMode mode) {
        if (mode == SlimefunGuideMode.SURVIVAL_MODE) {
            return survivalGuide;
        }
        if (player.isOp() && mode == SlimefunGuideMode.CHEAT_MODE) {
            return cheatGuide;
        }

        return survivalGuide;
    }

    public static void removeLastEntry(@Nonnull GuideHistory guideHistory) {
        try {
            Method getLastEntry = guideHistory.getClass().getDeclaredMethod("getLastEntry", boolean.class);
            getLastEntry.setAccessible(true);
            getLastEntry.invoke(guideHistory, true);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
