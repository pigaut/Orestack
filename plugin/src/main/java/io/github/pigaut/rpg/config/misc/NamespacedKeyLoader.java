package io.github.pigaut.rpg.config.misc;

import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class NamespacedKeyLoader implements ConfigLoader<NamespacedKey> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid namespace";
    }

    @Override
    public @NotNull NamespacedKey loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String key = scalar.toString(CaseStyle.SNAKE);

        if (key.isEmpty()) {
            throw new InvalidConfigException(scalar, "String must not be empty");
        }

        NamespacedKey namespacedKey = NamespacedKey.fromString(key, null);
        if (namespacedKey == null) {
            throw new InvalidConfigException(scalar, "Could not create namespace from: " + key);
        }

        return namespacedKey;
    }

}
