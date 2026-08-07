package io.github.pigaut.rpg.plugin.registry;

import org.jetbrains.annotations.*;

import java.util.*;

public interface Registry<T> {

    boolean contains(@NotNull String name);

    @Nullable T get(@NotNull String name);

    void register(@NotNull String name, @NotNull T value);

    void unregister(@NotNull String name);

    @NotNull Collection<T> getAll();

    @NotNull Collection<String> getAllNames();

    void clear();

}