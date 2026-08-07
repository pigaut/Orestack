package io.github.pigaut.rpg.plugin.manager;

import org.jetbrains.annotations.*;

import java.util.*;

public interface Container<T extends Identifiable> {

    boolean contains(@NotNull String name);

    boolean containsGroup(@NotNull String group);

    @Nullable T get(@NotNull String name);

    void add(@NotNull T value) throws DuplicateElementException;

    void remove(@NotNull String name);

    @NotNull List<T> getAll();

    @NotNull List<T> getAll(@NotNull String group);

    void removeAll(@NotNull String group);

    @NotNull List<String> getAllNames();

    @NotNull List<String> getAllNames(String group);

    @NotNull List<String> getAllGroups();

    void clear();

}
