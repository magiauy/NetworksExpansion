package com.ytdd9527.networksexpansion.core.util;

import com.ytdd9527.networksexpansion.core.guide.SurvivalGuideImpl;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@UtilityClass
public class GuideUtil {
    @Getter
    private static final SurvivalGuideImpl guide = new SurvivalGuideImpl();

    @ParametersAreNonnullByDefault
    public static void openMainMenuAsync(Player player, int selectedPage) {
        if (!PlayerProfile.get(player, profile -> Slimefun.runSync(() -> openMainMenu(profile, selectedPage)))) {
            Slimefun.getLocalization().sendMessage(player, "messages.opening-guide");
        }
    }

    @ParametersAreNonnullByDefault
    public static void openMainMenu(PlayerProfile profile, int selectedPage) {
        getGuide().openMainMenu(profile, selectedPage);
    }
}
