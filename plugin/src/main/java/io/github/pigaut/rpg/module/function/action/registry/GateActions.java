package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.gate.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class GateActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("OPEN_GATE", (ConfigLoader.Line<Action>) line ->
                new GateOpenAction());

        actions.addLoader("CLOSE_GATE", (ConfigLoader.Line<Action>) line ->
                new GateCloseAction());

        actions.addLoader("REPLACE_GATE", (ConfigLoader.Line<Action>) line ->
                new ReplaceGateAction(plugin, line.getRequiredString(1)));

        actions.addLoader("DAMAGE_GATE", (ConfigLoader.Line<Action>) line -> {
            ConfigOptional<Amount> amount = line.get(1, Amount.class);
            if (!amount.existsInConfig()) {
                return new DamageGateWithTool(plugin);
            }
            return new DamageGateAction(amount.withDefault(Amount.ONE));
        });

        actions.addLoader("DAMAGE_GATE_WITH_TOOL", (ConfigLoader.Line<Action>) line ->
                new DamageGateWithTool(plugin));

    }

}
