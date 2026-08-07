package io.github.pigaut.rpg.plugin;

import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.logging.*;
import java.util.regex.*;

/**
 * @author Timeout
 * Modified by Pigaut
 */
public class PluginLogger {

    private static final Logger LOGGER = Bukkit.getLogger();
    private static final String COLOR_PATTERN = "\u001b[38;5;%dm";
    private static final String FORMAT_PATTERN = "\u001b[%dm";

    private final EnhancedPlugin plugin;
    private final String prefix;
    private final char altColorChar = '&';

    public PluginLogger(EnhancedPlugin plugin) {
        this.plugin = plugin;
        this.prefix = "[" + plugin.getName() + "]";
    }

    public void info(@NotNull String message) {
        LOGGER.log(Level.INFO, prefix + convertStringMessage("&a " + message));
    }

    public void warning(@NotNull String message) {
        LOGGER.log(Level.WARNING, prefix + convertStringMessage("&c " + message));
    }

    public void severe(@NotNull String message) {
        LOGGER.log(Level.SEVERE, prefix + convertStringMessage("&4 " + message));
    }

    public void log(@NotNull Level level, @NotNull String message) {
        LOGGER.log(level, () -> prefix + " " + convertStringMessage(message));
    }

    private String convertStringMessage(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        boolean colored = plugin.getSettings().isColoredConsole();

        String messageCopy = String.copyValueOf(message.toCharArray()) + ConsoleColor.RESET.ansiColor;
        Matcher matcher = Pattern.compile("(" + altColorChar + "[0-9a-fk-orA-FK-OR])").matcher(message);
        while (matcher.find()) {
            String result = matcher.group(1);
            if (colored) {
                ConsoleColor color = ConsoleColor.getColorByCode(result.charAt(1));
                messageCopy = messageCopy.replace(result, color.getAnsiColor());
            }
            else {
                messageCopy = messageCopy.replace(result, "");
            }
        }

        return messageCopy;
    }

    private enum ConsoleColor {

        BLACK('0', COLOR_PATTERN, 0),
        DARK_GREEN('2', COLOR_PATTERN, 2),
        DARK_RED('4', COLOR_PATTERN, 1),
        GOLD('6', COLOR_PATTERN, 172),
        DARK_GREY('8', COLOR_PATTERN, 8),
        GREEN('a', COLOR_PATTERN, 10),
        RED('c', COLOR_PATTERN, 9),
        YELLOW('e', COLOR_PATTERN, 11),
        DARK_BLUE('1', COLOR_PATTERN, 4),
        DARK_AQUA('3', COLOR_PATTERN, 30),
        DARK_PURPLE('5', COLOR_PATTERN, 54),
        GRAY('7', COLOR_PATTERN, 246),
        BLUE('9', COLOR_PATTERN, 4),
        AQUA('b', COLOR_PATTERN, 51),
        LIGHT_PURPLE('d', COLOR_PATTERN, 13),
        WHITE('f', COLOR_PATTERN, 15),
        STRIKETHROUGH('m', FORMAT_PATTERN, 9),
        ITALIC('o', FORMAT_PATTERN, 3),
        BOLD('l', FORMAT_PATTERN, 1),
        UNDERLINE('n', FORMAT_PATTERN, 4),
        RESET('r', FORMAT_PATTERN, 0);


        private char bukkitColor;
        private String ansiColor;

        ConsoleColor(char bukkitColor, String pattern, int ansiCode) {
            this.bukkitColor = bukkitColor;
            this.ansiColor = String.format(pattern, ansiCode);
        }

        public static ConsoleColor getColorByCode(char code) {
            for(ConsoleColor color: values()) {
                if(color.bukkitColor == code) return color;
            }
            throw new IllegalArgumentException("Color with code " + code + " does not exists");
        }

        public String getAnsiColor() {
            return ansiColor;
        }
    }
}
