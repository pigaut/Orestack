package io.github.pigaut.rpg.plugin.manager.config;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.section.*;

import io.github.pigaut.yaml.util.*;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.*;

public class ManagerConfigDataLoader<T extends Identifiable> {

    private final EnhancedPlugin plugin;
    private final ConfigBackedManager<T> manager;
    private final Class<T> dataType;

    private String directory;
    private String prefix;
    private ConfigType configType = ConfigType.ANY;
    private KeyExtractor<T> keyExtractor;
    private final List<String> ignoredFiles = new ArrayList<>();

    public ManagerConfigDataLoader(@NotNull EnhancedPlugin plugin, @NotNull ConfigBackedManager<T> manager, @NotNull Class<T> dataType) {
        this.plugin = plugin;
        this.manager = manager;
        this.dataType = dataType;
        this.prefix = CaseFormatter.toTitleCase(manager.getClass()).replace(" Manager", "");
        this.keyExtractor = (section, key) -> List.of(section.getRequired(key, dataType));
    }

    public ManagerConfigDataLoader<T> directory(@NotNull String directory) {
        this.directory = directory;
        return this;
    }

    public ManagerConfigDataLoader<T> prefix(@NotNull String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ManagerConfigDataLoader<T> configType(@NotNull ConfigType configType) {
        this.configType = configType;
        return this;
    }

    public ManagerConfigDataLoader<T> extractor(@NotNull KeyExtractor<T> keyExtractor) {
        this.keyExtractor = keyExtractor;
        return this;
    }

    @FunctionalInterface
    public interface KeyExtractor<T> {
        @NotNull Collection<T> extract(@NotNull ConfigSection section, @NotNull String key) throws ConfigException;
    }

    public ManagerConfigDataLoader<T> ignore(@NotNull String... fileNames) {
        Collections.addAll(this.ignoredFiles, fileNames);
        return this;
    }

    public @NotNull ErrorCollector loadFromConfigFiles() {
        if (directory == null) {
            throw new IllegalStateException("Cannot load configuration: Directory has not been set.");
        }

        if (configType == null) {
            throw new IllegalStateException("Cannot load configuration: Load Strategy has not been set.");
        }

        if (configType == ConfigType.SECTION_KEY) {
            return loadSectionKeys();
        }

        return switch (configType) {
            case SCALAR -> loadStandardFiles(file -> YamlConfig.loadScalar(file, plugin.getConfigurator(), prefix));
            case SECTION -> loadStandardFiles(file -> YamlConfig.loadSection(file, plugin.getConfigurator(), prefix));
            case SEQUENCE -> loadStandardFiles(file -> YamlConfig.loadSequence(file, plugin.getConfigurator(), prefix));
            case ANY -> loadStandardFiles(file -> YamlConfig.loadConfig(file, plugin.getConfigurator(), prefix));
            default -> throw new IllegalStateException("Unhandled load strategy: " + configType);
        };
    }

    @FunctionalInterface
    private interface ConfigFileLoader {
        @NotNull ConfigRoot load(@NotNull File file) throws ConfigException;
    }

    private @NotNull ErrorCollector loadStandardFiles(@NotNull ConfigFileLoader configFileLoader) {
        ErrorCollector errorCollector = new SimpleErrorCollector();
        List<File> files = plugin.getFiles(directory);

        for (File file : files) {
            if (ignoredFiles.contains(file.getName())) {
                continue;
            }

            ConfigRoot root;
            T foundValue;
            try {
                root = configFileLoader.load(file);
                plugin.getSettings().applyConfigShortcuts(root);
                foundValue = root.getRequired(dataType);
                errorCollector.collectAll(root);
            } catch (ConfigException e) {
                errorCollector.collectError(e);
                continue;
            }

            try {
                manager.add(foundValue);
            } catch (DuplicateElementException e) {
                errorCollector.collectError(new InvalidConfigException(root, e.getMessage()));
            }
        }

        return errorCollector;
    }

    private @NotNull ErrorCollector loadSectionKeys() {
        if (directory == null) {
            throw new IllegalStateException("Cannot load configuration: Directory has not been set.");
        }

        List<File> files = plugin.getFiles(directory);

        ErrorCollector errorCollector = new SimpleErrorCollector();
        for (File file : files) {
            if (ignoredFiles.contains(file.getName())) {
                continue;
            }

            RootSection section = YamlConfig.loadSectionOrEmpty(file, plugin.getConfigurator(), prefix);
            plugin.getSettings().applyConfigShortcuts(section);

            try {
                for (String key : section.getKeys()) {
                    for (T foundValue : keyExtractor.extract(section, key)) {
                        manager.add(foundValue);
                    }
                }
            } catch (ConfigException e) {
                errorCollector.collectError(e);
            } catch (DuplicateElementException e) {
                errorCollector.collectError(new InvalidConfigException(section, e.getMessage()));
            }

            errorCollector.collectAll(section);
        }

        return errorCollector;
    }
}
