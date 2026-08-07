package io.github.pigaut.rpg.config.itemstack;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.trim.*;
import org.jetbrains.annotations.*;

public class TrimPatternLoader implements ConfigLoader<TrimPattern> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid trim pattern";
    }

    @Override
    public @NotNull TrimPattern loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String trimName = scalar.toString();
        TrimPattern trimPattern = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(trimName));
        if (trimPattern == null) {
            throw new InvalidConfigException(scalar, "Could not find trim pattern with name: " + trimName);
        }
        return trimPattern;
    }

}
