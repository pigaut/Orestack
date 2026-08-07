package io.github.pigaut.rpg.core.progressbar;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ProgressBarLoader implements ConfigLoader<ProgressBar> {

    private final EnhancedPlugin plugin;

    public ProgressBarLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid progress bar";
    }

    @Override
    public @NotNull ProgressBar loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String barId = scalar.toString();

        for (ProgressBar progressBar : plugin.getSettings().getProgressBars()) {
            if (progressBar.getId().equalsIgnoreCase(barId)) {
                return progressBar;
            }
        }

        throw new InvalidConfigException(scalar, "Could not find progress bar with name: " + barId);
    }

    @Override
    public @NotNull ProgressBar loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String barId = section.getKey();

        Map<Integer, String> barByHealthPercentage = new HashMap<>();
        for (KeyedScalar nestedScalar : section.getNestedScalars()) {
            Amount progressPercent = nestedScalar.getKeyAs(Amount.class)
                    .require(Requirements.amountBetween(0, 100))
                    .orThrow();

            String healthBar = nestedScalar.toString(ColorUtil.FORMATTER);

            for (int i = 0; i <= 100; i++) {
                if (progressPercent.match(i)) {
                    barByHealthPercentage.put(i, healthBar);
                }
            }
        }

        return new ProgressBar(barId, barByHealthPercentage);
    }
}
