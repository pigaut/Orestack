package io.github.pigaut.rpg.plugin.registry;

import org.jetbrains.annotations.*;

import java.util.*;

public class AbstractRegistry<T> implements Registry<T> {

    private final Map<String, T> valuesByName = new HashMap<>();

    @Override
    public boolean contains(@NotNull String name) {
        return valuesByName.containsKey(name);
    }

    @Override
    public @Nullable T get(@NotNull String name) {
        return valuesByName.get(name);
    }

    @Override
    public void register(@NotNull String name, @NotNull T value) {
        valuesByName.put(name, value);
    }

    @Override
    public void unregister(@NotNull String name) {
        valuesByName.remove(name);
    }

    @Override
    public @NotNull List<T> getAll() {
        return new ArrayList<>(valuesByName.values());
    }

    @Override
    public @NotNull List<String> getAllNames() {
        return new ArrayList<>(valuesByName.keySet());
    }

    @Override
    public void clear() {
        valuesByName.clear();
    }
}
