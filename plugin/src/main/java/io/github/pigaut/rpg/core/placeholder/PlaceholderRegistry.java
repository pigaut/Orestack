package io.github.pigaut.rpg.core.placeholder;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlaceholderRegistry {

    private final EnhancedPlugin plugin;

    private final Map<String, PlaceholderResolver> placeholdersById = new HashMap<>();
    private final String listSeparator = Character.toString('\u001F');

    public PlaceholderRegistry(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public @Nullable String resolvePlaceholder(@NotNull String placeholder, @NotNull Context context) {
        String placeholderId = placeholder;
        CaseStyle caseStyle = null;
        NumberStyle numberStyle = null;

        int index = placeholder.lastIndexOf('_');
        if (index != -1) {
            String tag = placeholder.substring(index + 1);
            caseStyle = CaseStyle.getByName(tag);
            numberStyle = NumberStyle.getByName(tag);

            if (caseStyle != null || numberStyle != null) {
                placeholderId = placeholder.substring(0, index);
            }
        }

        Object value;

        PlaceholderResolver supplier = placeholdersById.get(placeholderId);
        if (supplier != null) {
            value = supplier.resolve(context);
        } else if (context.containsPlaceholder(placeholderId)) {
            value = context.placeholder(placeholderId);
        } else {
            return null;
        }

        if (value == null) {
            value = plugin.getSettings().getPlaceholderFallback(placeholderId);
            if (value == null) {
                return "";
            }
        }

        if (value instanceof Collection<?> elements) {
            if (elements.isEmpty()) {
                return "%%EMPTY_LIST%%";
            }
            StringJoiner joiner = new StringJoiner(listSeparator);
            for (Object element : elements) {
                joiner.add(element.toString());
            }
            value = joiner.toString();
        }

        if (value instanceof Double && numberStyle == null) {
            numberStyle = NumberStyle.DECIMALS_0;
        }

        if (caseStyle != null) {
            return caseStyle.format(value.toString());
        }

        if (numberStyle != null && value instanceof Number numberValue) {
            return numberStyle.format(numberValue);
        }

        return value.toString();
    }

    public boolean contains(@NotNull String id) {
        return placeholdersById.containsKey(id);
    }

    public void register(@NotNull String id, @NotNull PlaceholderResolver supplier) {
        if (placeholdersById.containsKey(id)) {
            throw new IllegalStateException("Placeholder already registered: " + id);
        }
        placeholdersById.put(id, supplier);
    }

    public void unregister(@NotNull String id) {
        placeholdersById.remove(id);
    }

    public void clear() {
        placeholdersById.clear();
    }

    public void registerAlias(@NotNull String id, @NotNull String alias) {
        PlaceholderResolver resolver = placeholdersById.get(id);
        if (resolver == null) {
            throw new IllegalStateException("No placeholder registered under id: " + id);
        }
        if (placeholdersById.containsKey(alias)) {
            throw new IllegalStateException("Placeholder already registered: " + alias);
        }
        placeholdersById.put(alias, resolver);
    }

    public void registerAliases(@NotNull String id, @NotNull String... aliases) {
        for (String alias : aliases) {
            registerAlias(id, alias);
        }
    }

}
