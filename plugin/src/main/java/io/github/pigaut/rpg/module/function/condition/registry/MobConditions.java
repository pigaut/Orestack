package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.mob.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class MobConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("MOB_HAS_FLAG", (Line<Condition>) line ->
                new MobHasFlag(line.getRequiredString(1)));

        conditions.addLoader("MOB_HEALTH_EQUALS", (Line<Condition>) line ->
                new MobHealthEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("MOB_HEALTH_ABOVE", (Line<Condition>) line ->
                new MobHealthEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        conditions.addLoader("MOB_HEALTH_BELOW", (Line<Condition>) line ->
                new MobHealthEquals(Amount.lessThanOrEqual(line.getRequiredInteger(1))));

        conditions.addLoader("MOB_IS_FULL_HEALTH", (Line<Condition>) line ->
                new MobIsFullHealth());

        conditions.addLoader("MOB_NAME_EQUALS", (Line<Condition>) line ->
                new MobNameEquals(line.getRequiredString(1)));

        conditions.addLoader("ATTACKER_COUNT_EQUALS", (Line<Condition>) line ->
                new AttackerCountEquals(line.getRequired(1, Amount.class)));

        conditions.addLoader("ATTACKER_COUNT_ABOVE", (Line<Condition>) line ->
                new AttackerCountEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        conditions.addLoader("ATTACKER_COUNT_BELOW", (Line<Condition>) line ->
                new AttackerCountEquals(Amount.lessThanOrEqual(line.getRequiredInteger(1))));
    }

}