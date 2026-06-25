package io.github.pigaut.orestack.core.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.collection.tier.*;
import io.github.pigaut.orestack.core.action.collection.*;
import io.github.pigaut.orestack.core.action.collection.OpenCollectionMenu;
import io.github.pigaut.orestack.core.action.gate.*;
import io.github.pigaut.orestack.core.action.generator.*;
import io.github.pigaut.orestack.core.condition.collection.*;
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
import io.github.pigaut.voxel.data.function.action.menu.*;
import io.github.pigaut.voxel.data.function.condition.*;
import io.github.pigaut.voxel.data.function.condition.config.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.*;

public class OrestackConfigurator extends PluginConfigurator {

    public OrestackConfigurator(@NotNull OrestackPlugin plugin) {
        super(plugin);

        addLoader(ToolDamage.class, new ToolDamageLoader());

        addLoader(GeneratorTemplate.class, new GeneratorTemplateLoader(plugin));
        addLoader(GeneratorPhase.class, new GeneratorPhaseLoader(plugin));

        addLoader(GateTemplate.class, new GateLoader(plugin));
        addLoader(GatePhase.class, new GatePhaseLoader(plugin));

        addLoader(CollectionTemplate.class, new CollectionTemplateLoader(plugin));
        addLoader(CollectionTier.class, new CollectionTierLoader());

        ConditionLoader conditions = getConditionLoader();
        ActionLoader actions = getActionLoader();

        // Generator Actions start
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

        // Gate actions start
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
        // Gate actions end


        // Collection conditions start
        conditions.addLoader("HAS_UNLOCKED_COLLECTION", (Line<Condition>) line ->
                new PlayerHasUnlockedCollection(line.getRequired(1, CollectionTemplate.class)));

        conditions.addLoader("HAS_COLLECTION_TIER", (Line<Condition>) line ->
                new PlayerCollectionTierEquals(
                        line.getRequired(1, Amount.class),
                        line.getRequired("collection", CollectionTemplate.class)
                ));

        conditions.addLoader("COLLECTION_NAME_EQUALS", (Line<Condition>) line ->
                new CollectionNameEquals(line.getRequiredString(1)));

        conditions.addLoader("COLLECTION_IS_UNLOCKED", (Line<Condition>) line ->
                new CollectionIsUnlocked());

        conditions.addLoader("COLLECTION_HAS_REWARDS", (Line<Condition>) line ->
                new CollectionHasRewards());

        conditions.addLoader("COLLECTION_TIER_EQUALS", (Line<Condition>) line ->
                new CollectionTierEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("COLLECTION_TIER_IS_COMPLETED", (Line<Condition>) line ->
                new CollectionTierEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        conditions.addLoader("COLLECTION_MAX_TIER_EQUALS", (Line<Condition>) line ->
                new CollectionMaxTierEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("COLLECTION_HAS_TIER", (Line<Condition>) line ->
                new CollectionMaxTierEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));
        // Collection conditions end

        // Collection actions start
        actions.addLoader("INCREMENT_COLLECTION", (Line<Action>) line ->
                new IncrementItemCollection(
                        line.getRequired(1, CollectionTemplate.class),
                        line.get("amount", Amount.class).withDefault(Amount.ONE)
                ));

        actions.addLoader("DECREMENT_COLLECTION", (Line<Action>) line ->
                new DecrementItemCollection(
                        line.getRequired(1, CollectionTemplate.class),
                        line.get("amount", Amount.class).withDefault(Amount.ONE)
                ));

        // Collection actions end

        // Menu actions
        actions.addLoader("OPEN_MENU", (Line<Action>) line -> {
            String menuName = line.getRequiredString(1);

            if (line.hasFlag("collection")) {
                String collectionName = line.getRequiredString("collection");
                return new OpenCollectionMenu(plugin, menuName, collectionName);
            }

            return new OpenMenu(plugin, menuName);
        });


        // Collection condition Aliases
        conditions.addAliases("HAS_UNLOCKED_COLLECTION", "PLAYER_HAS_UNLOCKED_COLLECTION");
        conditions.addAliases("HAS_COLLECTION_TIER", "PLAYER_HAS_COLLECTION_TIER");

        // Collection action aliases
        actions.addAliases("INCREMENT_COLLECTION", "INCREMENT_ITEM_COLLECTION", "INCREASE_COLLECTION", "INCREASE_ITEM_COLLECTION");
        actions.addAliases("DECREMENT_COLLECTION", "DECREMENT_ITEM_COLLECTION", "DECREASE_COLLECTION", "DECREASE_ITEM_COLLECTION");

    }

}
