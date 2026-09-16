package io.github.pigaut.rpg.plugin.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AbstractRegistry<T> implements Registry<T> {

    private final CaseStyle caseStyle;

    private final Map<String, T> valuesByName = new HashMap<>();

    public AbstractRegistry() {
        this(CaseStyle.CONSTANT);
    }

    public AbstractRegistry(@NotNull CaseStyle caseStyle) {
        this.caseStyle = caseStyle;
    }

    @Override
    public boolean contains(@NotNull String name) {
        return valuesByName.containsKey(caseStyle.format(name));
    }

    @Override
    public @Nullable T get(@NotNull String name) {
        return valuesByName.get(caseStyle.format(name));
    }

    @Override
    public void register(@NotNull String name, @NotNull T value) {
        valuesByName.put(caseStyle.format(name), value);
    }

    @Override
    public void unregister(@NotNull String name) {
        valuesByName.remove(caseStyle.format(name));
    }

    @Override
    public void registerAlias(@NotNull String name, @NotNull String... aliases) {
        T value = get(name);
        if (value == null) {
            throw new IllegalArgumentException("Could not find value with name: " + name);
        }

        for (String alias : aliases) {
            register(alias, value);
        }
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
