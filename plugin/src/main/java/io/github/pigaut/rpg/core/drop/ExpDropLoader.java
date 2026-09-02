package io.github.pigaut.rpg.core.drop;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ExpDropLoader implements ConfigLoader.Line<ExpDrop> {

    private final EnhancedPlugin plugin;

    public ExpDropLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid exp drop";
    }

    @Override
    public @NotNull ExpDrop loadFromLine(ConfigLine line) throws InvalidConfigException {
        Amount expAmount;
        if (line.size() <= 1) {
            expAmount = line.getRequired(0, Amount.class);
        } else {
            expAmount = line.getRequired(1, Amount.class);
        }

        Amount orbAmount = line.get("orbs|orbAmount|orbCount", Amount.class)
                .withDefault(null);

        boolean experience = line.getBoolean("experience|applyExperience")
                .withDefault(true);

        return new ExpDrop(plugin, expAmount, orbAmount, experience);
    }

}