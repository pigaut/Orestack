package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.registry.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ActionRegistry extends AbstractRegistry<ConfigLoader<Action>> implements ConfigLoader<Action> {

    private final EnhancedPlugin plugin;

    public ActionRegistry(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid action";
    }

    @Override
    public @NotNull Action loadFromScalar(@NotNull ConfigScalar scalar) throws InvalidConfigException {
        ConfigLine line = scalar.toLine();
        String actionName = line.getRequiredString(0);

        ConfigLoader<? extends Action> loader = get(actionName);
        if (loader == null) {
            throw new InvalidConfigException(line,
                    "Could not find action with name: " + CaseFormatter.toCamelCase(actionName));
        }

        Action action = loader.loadFromScalar(scalar);

        Integer repetitions = line.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = line.get("interval|period", Delay.class)
                .check(repetitions != null, "Repetitions must be set to use interval delay")
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            action = new PeriodicAction(plugin, action, interval, repetitions);
        } else if (repetitions != null) {
            action = new RepeatedAction(action, repetitions);
        }

        Integer delay = line.get("delay", Delay.class)
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            action = new DelayedAction(plugin, action, delay);
        }

        Double chance = line.getDouble("chance")
                .require(Requirements.between(0, 1))
                .withDefault(null);

        if (chance != null) {
            action = new ChanceAction(action, chance);
        }

        return action;
    }

    @Override
    public @NotNull Action loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        List<Action> actions = sequence.getAllRequired(Action.class);
        if (actions.isEmpty()) {
            return Action.EMPTY;
        }
        if (actions.size() == 1) {
            return actions.get(0);
        }
        return new MultiAction(actions);
    }

}
