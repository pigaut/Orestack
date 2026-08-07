package io.github.pigaut.rpg.module.function.foreach;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.line.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ForEachSourceRegistry implements ConfigLoader<ForEachSource> {

    private final Map<String, ConfigLoader<? extends ForEachSource<?>>> forEachSourceLoaders = new HashMap<>();

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid for-each source";
    }

    @Override
    public @NotNull ForEachSource<?> loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        ConfigLine line = scalar.toLine();
        String sourceName = line.getRequiredString(0, CaseStyle.SNAKE);
        ConfigLoader<? extends ForEachSource<?>> sourceLoader = get(sourceName);
        if (sourceLoader == null) {
            throw new InvalidConfigException(scalar, "Could not find for-each source with name: " + sourceName);
        }
        return sourceLoader.loadFromScalar(scalar);
    }

    public boolean contains(@NotNull String name) {
        return forEachSourceLoaders.containsKey(name);
    }

    public @Nullable ConfigLoader<? extends ForEachSource<?>> get(@NotNull String name) {
        return forEachSourceLoaders.get(name);
    }

    public <T extends ForEachSource<?>> void register(@NotNull String name, @NotNull ConfigLoader<T> sourceLoader) {
        forEachSourceLoaders.put(name, sourceLoader);
    }

    public void register(@NotNull String name, @NotNull ForEachSource<?> source) {
        forEachSourceLoaders.put(name, new ConfigLoader<>() {
            @Override
            public String getErrorDescription() {
                return "invalid for-each source";
            }

            @Override
            public @NotNull ForEachSource<?> loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
                return source;
            }
        });
    }

    public void unregister(@NotNull String name) {
        forEachSourceLoaders.remove(name);
    }

    public @NotNull List<ConfigLoader<? extends ForEachSource<?>>> getAll() {
        return new ArrayList<>(forEachSourceLoaders.values());
    }

    public @NotNull List<String> getAllNames() {
        return new ArrayList<>(forEachSourceLoaders.keySet());
    }

    public void clear() {
        forEachSourceLoaders.clear();
    }

}
