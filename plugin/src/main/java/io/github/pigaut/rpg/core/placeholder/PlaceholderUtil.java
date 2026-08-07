package io.github.pigaut.rpg.core.placeholder;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.regex.*;

public class PlaceholderUtil {

    public static @NotNull String parseAll(@NotNull EnhancedPlugin plugin, @NotNull String text) {
        return parseAll(Context.fromPlugin(plugin), text);
    }

    public static @NotNull ItemStack parseAll(@NotNull EnhancedPlugin plugin, @NotNull ItemStack item) {
        return parseAll(Context.fromPlugin(plugin), item);
    }

    private static final String SPLIT_LINE_CHARACTER = "\u001F";
    private static final Pattern SPLIT_LINE_PATTERN = Pattern.compile("\u001F");

    public static @NotNull String parseAll(@NotNull Context context, @NotNull String text) {
        PlaceholdersHook placeholderAPI = Server.getPlaceholderAPIHook();
        if (placeholderAPI != null) {
            text = placeholderAPI.setPlaceholders(context.player(), text);
        }

        int placeholderStart = text.indexOf('{');
        if (placeholderStart == -1) {
            return text;
        }

        int length = text.length();
        StringBuilder builder = new StringBuilder(length + (length >> 3));
        int cursor = 0;

        while (placeholderStart != -1) {
            // Append everything before the first placeholder start '{'
            if (placeholderStart > cursor) {
                builder.append(text, cursor, placeholderStart);
            }

            // Find placeholder end '}'
            int placeholderEnd = text.indexOf('}', placeholderStart + 1);
            if (placeholderEnd == -1) {
                builder.append(text, placeholderStart, length);
                return builder.toString();
            }

            // Extract the placeholder id between '{' and '}' and resolve it
            String placeholderId = text.substring(placeholderStart + 1, placeholderEnd);
            String replacement = context.resolvePlaceholder(placeholderId);

            if (replacement != null) {
                if (replacement.contains(SPLIT_LINE_CHARACTER)) {
                    String prefix = builder.toString();
                    String suffix = text.substring(placeholderEnd + 1);
                    String[] lines = SPLIT_LINE_PATTERN.split(replacement, -1);

                    StringJoiner joiner = new StringJoiner(SPLIT_LINE_CHARACTER);
                    for (String line : lines) {
                        joiner.add(prefix + line + suffix);
                    }
                    return joiner.toString();
                }

                builder.append(replacement);
            } else {
                builder.append(text, placeholderStart, placeholderEnd + 1);
            }

            cursor = placeholderEnd + 1;
            placeholderStart = text.indexOf('{', cursor);
        }

        // Append everything after the last placeholder end '}'
        if (cursor < length) {
            builder.append(text, cursor, length);
        }

        return builder.toString();
    }

    public static @NotNull List<String> parseAll(@NotNull Context context, @NotNull List<String> elements) {
        List<String> parsedElements = new ArrayList<>();

        for (String element : elements) {
            String parsedElement = parseAll(context, element);
            if (parsedElement.equals("%%EMPTY_LIST%%")) {
                continue;
            }

            String[] lines = SPLIT_LINE_PATTERN.split(parsedElement, -1);
            for (String line : lines) {
                parsedElements.add(line);
            }
        }

        return parsedElements;
    }

    public static @NotNull ItemStack parseAll(@NotNull Context context, @NotNull ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            parseAll(context, meta);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static @NotNull ItemMeta parseAll(@NotNull EnhancedPlugin plugin, @NotNull ItemMeta meta) {
        return parseAll(Context.fromPlugin(plugin), meta);
    }

    public static @NotNull ItemMeta parseAll(@NotNull Context context, @NotNull ItemMeta meta) {
        if (meta.hasDisplayName()) {
            meta.setDisplayName(parseAll(context, meta.getDisplayName()));
        }

        if (meta.hasLore()) {
            meta.setLore(parseAll(context, meta.getLore()));
        }

        return meta;
    }

}
