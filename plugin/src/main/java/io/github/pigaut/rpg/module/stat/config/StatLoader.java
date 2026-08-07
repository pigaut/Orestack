package io.github.pigaut.rpg.module.stat.config;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.jetbrains.annotations.*;

public class StatLoader implements ConfigLoader<Stat> {

    private final EnhancedPlugin plugin;

    public StatLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid stat";
    }

    @Override
    public @NotNull Stat loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String statName = scalar.toString(CaseStyle.SNAKE);

        Stat stat = plugin.getStats().get(statName);
        if (stat == null) {
            throw new InvalidConfigException(scalar, "Could not find stat with name: " + statName);
        }

        return stat;
    }

}
