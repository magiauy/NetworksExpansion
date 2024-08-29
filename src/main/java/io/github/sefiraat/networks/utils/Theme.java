package io.github.sefiraat.networks.utils;

import com.ytdd9527.networksexpansion.utils.TextUtil;

import io.github.mooy1.infinityexpansion.items.storage.Storage;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Getter
public enum Theme {
    GOLD(ChatColor.GOLD, ""),
    WHITE(ChatColor.WHITE, ""),
    AQUA(ChatColor.AQUA, ""),
    WARNING(ChatColor.YELLOW, "Warning"),
    ERROR(ChatColor.RED, "Error"),
    NOTICE(ChatColor.WHITE, "Notice"),
    PASSIVE(ChatColor.GRAY, ""),
    SUCCESS(ChatColor.GREEN, "Success"),
    MAIN(ChatColor.of("#21588f"), "Alone"),
    CLICK_INFO(ChatColor.of("#e4ed32"), "Click here"),
    RESEARCH(ChatColor.of("#a60e03"), "Research"),
    CRAFTING(ChatColor.of("#dbcea9"), "Crafting Material"),
    MACHINE(ChatColor.of("#3295a8"), "Machine"),
    STORAGE(ChatColor.of("#3295a8"), "Storage"),
    BLUEPRINTBLANK(ChatColor.of("#3295a8"), "Blueprint Blank"),

    TOOL(ChatColor.of("#6b32a8"), "Tool"),
    MECHANISM(ChatColor.of("#3295a8"), "Mechanism"),
    FUEL(ChatColor.of("#112211"), "Fossil Fuel"),
    MATERIAL_CLASS(ChatColor.of("#a4c2ba"), "Material Class"),
    RECIPE_TYPE(ChatColor.of("#ffe89c"), "Recipe Type"),


    GUIDE(ChatColor.of("#444444"), "Guide");


    private static final Theme[] cachedValues = values();
    private final ChatColor color;
    private final String loreLine;

    @ParametersAreNonnullByDefault
    Theme(ChatColor color, String loreLine) {
        this.color = color;
        this.loreLine = loreLine;

    }

    /**
     * Gets a SlimefunItemStack with a pre-populated lore and name with themed colors.
     *
     * @param id        The ID for the new {@link SlimefunItemStack}
     * @param itemStack The vanilla {@link ItemStack} used to base the {@link SlimefunItemStack} on
     * @param themeType The {@link Theme} {@link ChatColor} to apply to the {@link SlimefunItemStack} name
     * @param name      The name to apply to the {@link SlimefunItemStack}
     * @param lore      The lore lines for the {@link SlimefunItemStack}. Lore is book-ended with empty strings.
     * @return Returns the new {@link SlimefunItemStack}
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack themedSlimefunItemStack(String id, ItemStack itemStack, Theme themeType, String name, String... lore) {
        ChatColor passiveColor = Theme.PASSIVE.getColor();
        List<String> finalLore = new ArrayList<>();
        finalLore.add("");
        for (String s : lore) {
            finalLore.add(passiveColor + s);
        }
        finalLore.add("");
        finalLore.add(applyThemeToString(Theme.CLICK_INFO, themeType.getLoreLine()));
        return new SlimefunItemStack(
                id,
                itemStack,
                Theme.applyThemeToString(themeType, name),
                finalLore.toArray(new String[finalLore.size() - 1])
        );
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack tsItem(String id, ItemStack itemStack, Theme themeType, String name, String... lore) {
        List<String> finalLore = new ArrayList<>(Arrays.stream(lore).toList());
        finalLore.add("");
        finalLore.add(TextUtil.colorPseudorandomString("此物品即将被删除,请更换最新的物品，将在一个月后彻底删除旧物品"));
        finalLore.add(applyThemeToString(Theme.SUCCESS, themeType.getLoreLine()));
        return new SlimefunItemStack(
                id,
                itemStack,
                Theme.applyThemeToString(themeType, name),
                finalLore.toArray(new String[finalLore.size() - 1])
        );
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack Random(
            String id,
            ItemStack itemStack,
            Theme themeType,
            String name,
            String... lore
    ) {
        String coloredName = TextUtil.colorPseudorandomString(name);
        ChatColor passiveColor = Theme.PASSIVE.getColor();
        List<String> finalLore = new ArrayList<>();
        finalLore.add("");
        for (String s : lore) {
            finalLore.add(passiveColor + s);
        }
        finalLore.add(applyThemeToString(Theme.SUCCESS, themeType.getLoreLine()));
        return new SlimefunItemStack(
                id,
                itemStack,
                coloredName,
                finalLore.toArray(new String[0])
        );
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack Random(
            String id,
            String texture,
            Theme themeType,
            String name,
            String... lore
    ) {
        String coloredName = TextUtil.colorPseudorandomString(name);
        ChatColor passiveColor = Theme.PASSIVE.getColor();
        List<String> finalLore = new ArrayList<>();
        finalLore.add("");
        for (String s : lore) {
            finalLore.add(passiveColor + s);
        }
        finalLore.add(applyThemeToString(Theme.SUCCESS, themeType.getLoreLine()));
        return new SlimefunItemStack(
                id,
                texture,
                coloredName,
                finalLore.toArray(new String[0])
        );
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static SlimefunItemStack model(
            String id,
            ItemStack itemStack,
            Theme themeType,
            String name,
            String... lore
    ) {
        String coloredName = TextUtil.colorPseudorandomString(name);
        ChatColor passiveColor = Theme.PASSIVE.getColor();
        List<String> finalLore = new ArrayList<>();
        finalLore.add("");
        for (String s : lore) {
            finalLore.add(passiveColor + s);
        }
        finalLore.add(applyThemeToString(Theme.SUCCESS, themeType.getLoreLine()));
        return new SlimefunItemStack(
                id + "_MODEL",
                itemStack,
                coloredName + " &f(&aModel&f)",
                finalLore.toArray(new String[0])
        );
    }

    /**
     * Applies the theme color to a given string
     *
     * @param themeType The {@link Theme} to apply the color from
     * @param string    The string to apply the color to
     * @return Returns the string provides preceded by the color
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static String applyThemeToString(Theme themeType, String string) {
        return themeType.getColor() + string;
    }

    /**
     * Gets an ItemStack with a pre-populated lore and name with themed colors.
     *
     * @param material  The {@link Material} used to base the {@link ItemStack} on
     * @param themeType The {@link Theme} {@link ChatColor} to apply to the {@link ItemStack} name
     * @param name      The name to apply to the {@link ItemStack}
     * @param lore      The lore lines for the {@link ItemStack}. Lore is book-ended with empty strings.
     * @return Returns the new {@link ItemStack}
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static ItemStack themedItemStack(Material material, Theme themeType, String name, String... lore) {
        ChatColor passiveColor = Theme.PASSIVE.getColor();
        List<String> finalLore = new ArrayList<>();
        finalLore.add("");
        for (String s : lore) {
            finalLore.add(passiveColor + s);
        }
        finalLore.add("");
        finalLore.add(applyThemeToString(Theme.CLICK_INFO, themeType.getLoreLine()));
        return new CustomItemStack(
                material,
                Theme.applyThemeToString(themeType, name),
                finalLore.toArray(new String[finalLore.size() - 1])
        );
    }

    @Nonnull
    public Particle.DustOptions getDustOptions(float size) {
        return new Particle.DustOptions(
                Color.fromRGB(
                        color.getColor().getRed(),
                        color.getColor().getGreen(),
                        color.getColor().getBlue()
                ),
                size
        );
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum class should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return this.color.toString();
    }


}
