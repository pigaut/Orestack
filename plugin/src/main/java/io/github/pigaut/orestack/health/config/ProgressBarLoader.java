package io.github.pigaut.orestack.health.config;

import io.github.pigaut.orestack.health.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ProgressBarLoader implements ConfigLoader<ProgressBar> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid progress bar";
    }

    @Override
    public @NotNull ProgressBar loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String barId = section.getKey();

        Map<Integer, String> barByHealthPercentage = new HashMap<>();
        for (KeyedScalar nestedScalar : section.getNestedScalars()) {
            Amount progressPercent = nestedScalar.getKeyAs(Amount.class)
                    .require(Requirements.amountBetween(0, 100))
                    .orThrow();

            String healthBar = nestedScalar.toString(StringColor.FORMATTER);

            for (int i = 0; i <= 100; i++) {
                if (progressPercent.match(i)) {
                    barByHealthPercentage.put(i, healthBar);
                }
            }
        }

        return new ProgressBar(barId, barByHealthPercentage);
    }
}
