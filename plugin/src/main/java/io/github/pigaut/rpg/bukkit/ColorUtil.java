package io.github.pigaut.rpg.bukkit;


import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.regex.*;

public class ColorUtil {

    public static final StringFormatter FORMATTER = ColorUtil::translateColors;

    public static @NotNull String parseAll(@NotNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String translateColorsAndStyle(String string) {
        return ColorUtil.translateColors(CaseStyle.translateTagStyle(string));
    }

    public static String translateColors(String string) {
        return translateColorCodes(translateHexColors(string));
    }

    public static String translateColorCodes(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String translateHexColors(String string) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            String hexCode = string.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&" + c);
            }

            string = string.replace(hexCode, builder.toString());
            matcher = pattern.matcher(string);
        }
        return string;
    }

    public static boolean startsWithColor(@NotNull String string) {
        return !string.isEmpty() && string.charAt(0) == ChatColor.COLOR_CHAR;
    }

}
