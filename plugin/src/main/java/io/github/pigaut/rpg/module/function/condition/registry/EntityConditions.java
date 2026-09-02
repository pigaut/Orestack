package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.entity.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class EntityConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("ENTITY_IS_MOB", (Line<Condition>) line ->
                new EntityIsMob(plugin));

        conditions.addLoader("ENTITY_TYPE_EQUALS", (Line<Condition>) line ->
                new EntityTypeEquals(line.getAllRequired(1, EntityType.class)));

    }

}