package io.github.pigaut.orestack.core.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.core.action.gate.*;
import io.github.pigaut.orestack.core.action.generator.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.config.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.orestack.generator.config.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.health.*;
import io.github.pigaut.orestack.health.config.*;
import io.github.pigaut.voxel.core.config.*;
import io.github.pigaut.voxel.data.function.action.*;
import io.github.pigaut.voxel.data.function.condition.config.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.*;

public class OrestackConfigurator extends PluginConfigurator {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);

        addLoader(GeneratorTemplate.class, new GeneratorLoader(plugin));
        addLoader(GeneratorPhase.class, new GeneratorPhaseLoader(plugin));

        addLoader(GateTemplate.class, new GateLoader(plugin));
        addLoader(GatePhase.class, new GatePhaseLoader(plugin));

        addLoader(ToolDamage.class, new ToolDamageLoader());

        ConditionLoader conditions = getConditionLoader();
        ActionLoader actions = getActionLoader();

        // Generator Actions
        actions.addLoader("KEEP_STAGE", (Line<Action>) line ->
                new GeneratorKeepPhaseAction());

        actions.addLoader("KEEP_PHASE", (Line<Action>) line ->
                new GeneratorKeepPhaseAction());

        actions.addLoader("NEXT_STAGE", (Line<Action>) line ->
                new GeneratorGrowAction());

        actions.addLoader("NEXT_PHASE", (Line<Action>) line ->
                new GeneratorGrowAction());

        actions.addLoader("GROW_GENERATOR", (Line<Action>) line ->
                new GeneratorGrowAction());

        actions.addLoader("PREVIOUS_STAGE", (Line<Action>) line ->
                new GeneratorHarvestAction());

        actions.addLoader("PREVIOUS_PHASE", (Line<Action>) line ->
                new GeneratorHarvestAction());

        actions.addLoader("HARVEST_GENERATOR", (Line<Action>) line ->
                new GeneratorHarvestAction());

        actions.addLoader("REGROW_GENERATOR", (Line<Action>) line ->
                new GeneratorRegrowAction());

        actions.addLoader("SET_STAGE", (Line<Action>) line ->
                new GeneratorSetPhaseAction(line.getRequiredInteger(1) - 1,
                        line.getBoolean("growing").withDefault(true)));

        actions.addLoader("SET_GENERATOR_STAGE", (Line<Action>) line ->
                new GeneratorSetPhaseAction(line.getRequiredInteger(1) - 1,
                        line.getBoolean("growing").withDefault(true)));

        actions.addLoader("SET_PHASE", (Line<Action>) line ->
                new GeneratorSetPhaseAction(line.getRequiredInteger(1) - 1,
                        line.getBoolean("growing").withDefault(true)));

        actions.addLoader("SET_GENERATOR_PHASE", (Line<Action>) line ->
                new GeneratorSetPhaseAction(line.getRequiredInteger(1) - 1,
                        line.getBoolean("growing").withDefault(true)));

        actions.addLoader("DAMAGE_GENERATOR", (Line<Action>) line -> {
            ConfigOptional<Amount> amount = line.get(1, Amount.class);
            if (!amount.existsInConfig()) {
                return new DamageGeneratorWithTool();
            }
            return new DamageGeneratorAction(amount.withDefault(Amount.ONE));
        });

        actions.addLoader("DAMAGE_GENERATOR_WITH_TOOL", (Line<Action>) line ->
                new DamageGeneratorWithTool());

        // Gate actions
        actions.addLoader("OPEN_GATE", (Line<Action>) line ->
                new GateOpenAction());

        actions.addLoader("CLOSE_GATE", (Line<Action>) line ->
                new GateCloseAction());

        actions.addLoader("REPLACE_GATE", (Line<Action>) line ->
                new ReplaceGateAction(plugin, line.getRequiredString(1)));

        actions.addLoader("DAMAGE_GATE", (Line<Action>) line -> {
            ConfigOptional<Amount> amount = line.get(1, Amount.class);
            if (!amount.existsInConfig()) {
                return new DamageGateWithTool(plugin);
            }
            return new DamageGateAction(amount.withDefault(Amount.ONE));
        });

        actions.addLoader("DAMAGE_GATE_WITH_TOOL", (Line<Action>) line ->
                new DamageGateWithTool(plugin));

    }

}
