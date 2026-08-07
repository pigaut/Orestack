package io.github.pigaut.rpg.core.placeholder.custom;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CustomPlaceholders {

    private final Map<String, Object> placeholders;

    public CustomPlaceholders(Map<String, Object> customPlaceholders) {
        this.placeholders = customPlaceholders;
    }

    public @NotNull Context asContext(@NotNull EnhancedPlugin plugin) {
        return Context.builder(plugin).withPlaceholders(placeholders).build();
    }

    public @Nullable Object get(@NotNull String placeholder) {
        return placeholders.get(placeholder);
    }

    public @NotNull Map<String, Object> getAll() {
        return new HashMap<>(placeholders);
    }

}
