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
        actions.addLoader("KEEP_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorKeepPhaseAction());

        actions.addLoader("KEEP_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorKeepPhaseAction());

        actions.addLoader("NEXT_STAGE", (ConfigLoader.Line<Action>) line ->
                new GeneratorGrowAction());

        actions.addLoader("NEXT_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorGrowAction());

        actions.addLoader("GROW_GENERATOR", (ConfigLoader.Line<Action>) line ->
                new GeneratorGrowAction());

        actions.addLoader("PREVIOUS_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorHarvestAction());

        actions.addLoader("HARVEST_GENERATOR", (ConfigLoader.Line<Action>) line ->
                new GeneratorHarvestAction());

        actions.addLoader("REGROW_GENERATOR", (ConfigLoader.Line<Action>) line ->
                new GeneratorRegrowAction());

        actions.addLoader("SET_GENERATOR_PHASE", (ConfigLoader.Line<Action>) line ->
                new GeneratorSetPhaseAction(line.getRequiredInteger(1) - 1,
                        line.getBoolean("growing").withDefault(true)));

        actions.addLoader("DAMAGE_GENERATOR", (ConfigLoader.Line<Action>) line -> {
            ConfigOptional<Amount> amount = line.get(1, Amount.class);
            if (!amount.existsInConfig()) {
                return new DamageGeneratorWithTool();
            }
            return new DamageGeneratorAction(amount.withDefault(Amount.ONE));
        });

        actions.addLoader("DAMAGE_GENERATOR_WITH_TOOL", (ConfigLoader.Line<Action>) line ->
                new DamageGeneratorWithTool());

        actions.addAliases("SET_GENERATOR_PHASE", "SET_PHASE");

    }

}
