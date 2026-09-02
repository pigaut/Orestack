package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.event.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class EventConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("CLICK_TYPE_EQUALS", (Line<Condition>) line ->
                new InteractionTypeEquals(
                        line.getAllRequired(1, InteractType.class),
                        line.getBoolean("shift|sneak|sneaking").withDefault(null)
                ));

        conditions.addLoader("DAMAGE_IS_CRITICAL", (Line<Condition>) line ->
                new DamageIsCritical());

        conditions.addLoader("DAMAGE_IS_FALLING_CRITICAL", (Line<Condition>) line ->
                new DamageIsFallingCritical());

        conditions.addAliases("DAMAGE_IS_CRITICAL", "DAMAGE_IS_CRIT");
        conditions.addAliases("DAMAGE_IS_FALLING_CRITICAL", "DAMAGE_IS_FALLING_CRIT");
    }

}