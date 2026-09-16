package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.generator.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class GeneratorActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        // Brain damage
        actions.register("KEEP_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorKeepPhaseAction());

        actions.register("KEEP_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorKeepPhaseAction());

        actions.register("NEXT_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorGrowAction());

        actions.register("NEXT_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorGrowAction());

        actions.register("GROW_GENERATOR", (ConfigLoader.Line<Action>) line ->
                new GeneratorGrowAction());

        actions.register("PREVIOUS_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorHarvestAction());

        actions.register("HARVEST_GENERATOR", (ConfigLoader.Line<Action>) line ->
                new GeneratorHarvestAction());

        actions.register("REGROW_GENERATOR", (ConfigLoader.Line<Action>) line ->
                new GeneratorRegrowAction());

        actions.register("SET_GENERATOR_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorSetPhaseAction(line.getRequiredInteger(1) - 1,
                        line.getBoolean("growing").withDefault(true)));

        actions.register("DAMAGE_GENERATOR", (ConfigLoader.Line<Action>) line -> {
            ConfigOptional<Amount> amount = line.get(1, Amount.class);
            if (!amount.existsInConfig()) {
                return new DamageGeneratorWithTool();
            }
            return new DamageGeneratorAction(amount.withDefault(Amount.ONE));
        });

        actions.register("DAMAGE_GENERATOR_WITH_TOOL", (ConfigLoader.Line<Action>) line ->
                new DamageGeneratorWithTool());

        actions.registerAlias("SET_GENERATOR_PHASE", "SET_PHASE");

    }

}
