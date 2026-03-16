package io.github.pigaut.orestack.health.config;

import io.github.pigaut.orestack.health.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class HealthBarLoader implements ConfigLoader<HealthBar> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid health bar";
    }

    @Override
    public @NotNull HealthBar loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String id = section.getKey();
        Map<Amount, String> barsByHealth = new HashMap<>();

        for (KeyedScalar nestedScalar : section.getNestedScalars()) {
            Amount health = nestedScalar.getKeyAs(Amount.class)
                    .map(amount -> amount.transform(value -> value / 100))
                    .orThrow();

            String bar = nestedScalar.toString(StringColor.FORMATTER);
            barsByHealth.put(health, bar);
        }

        return new HealthBar(id, barsByHealth);
    }
}
