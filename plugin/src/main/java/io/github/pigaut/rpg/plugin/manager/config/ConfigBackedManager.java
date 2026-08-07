package io.github.pigaut.rpg.plugin.manager.config;

import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public abstract class ConfigBackedManager<T extends Identifiable> extends ContainerBackedManager<T> implements ConfigBacked, Toggleable {

    private final Module module;
    private final ManagerConfigDataLoader<T> dataLoader;

    public ConfigBackedManager(@NotNull EnhancedJavaPlugin plugin, @NotNull Module module, @NotNull Class<T> dataType) {
        super(plugin);
        this.module = module;
        dataLoader = new ManagerConfigDataLoader<>(plugin, this, dataType);
        dataLoader.directory(module.getDirectory());
        dataLoader.configType(module.getConfigType());
    }

    @Override
    public @NotNull Module getModule() {
        return module;
    }

    protected void directory(@NotNull String directory) {
        dataLoader.directory(directory);
    }

    protected void configType(@NotNull ConfigType strategy) {
        dataLoader.configType(strategy);
    }

    protected void extractor(@NotNull ManagerConfigDataLoader.KeyExtractor<T> keyExtractor) {
        dataLoader.extractor(keyExtractor);
    }

    protected void prefix(@NotNull String prefix) {
        dataLoader.prefix(prefix);
    }

    protected void ignore(@NotNull String... fileNames) {
        dataLoader.ignore(fileNames);
    }

    @Override
    public @NotNull ErrorCollector loadConfigurationData() {
        return dataLoader.loadFromConfigFiles();
    }

    @Override
    public void reload() {
        reload(errorCollector -> ConfigErrorUtil.logAll(plugin, errorCollector.getErrors(), errorCollector.getWarnings()));
    }

    public void reload(Consumer<ErrorCollector> errorCollector) {
        disable();
        plugin.getScheduler().runTaskAsync(() -> {
            saveData();
            loadData();
            plugin.getScheduler().runTask(this::enable);
            errorCollector.accept(loadConfigurationData());
        });
    }

}
