package io.github.pigaut.rpg.config;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class PluginNotInstalledLoader<T> implements ConfigLoader<T> {

    private final String pluginName;
    private final @Nullable T defaultValue;

    public PluginNotInstalledLoader(@NotNull String pluginName, @Nullable T defaultValue) {
        this.pluginName = pluginName;
        this.defaultValue = defaultValue;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid dependency";
    }

    @Override
    public @NotNull T loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        if (defaultValue != null) {
            ConfigRoot root = scalar.getRoot();
            root.collectWarning(new InvalidConfigException(scalar, pluginName + " plugin is not installed"));
            return defaultValue;
        }
        throw new InvalidConfigException(scalar, pluginName + " plugin is not installed");
    }

    @Override
    public @NotNull T loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (defaultValue != null) {
            ConfigRoot root = section.getRoot();
            root.collectWarning(new InvalidConfigException(section, pluginName + " plugin is not installed"));
            return defaultValue;
        }
        throw new InvalidConfigException(section, pluginName + " plugin is not installed");
    }

    @Override
    public @NotNull T loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        if (defaultValue != null) {
            ConfigRoot root = sequence.getRoot();
            root.collectWarning(new InvalidConfigException(sequence, pluginName + " plugin is not installed"));
            return defaultValue;
        }
        throw new InvalidConfigException(sequence, pluginName + " plugin is not installed");
    }
}
