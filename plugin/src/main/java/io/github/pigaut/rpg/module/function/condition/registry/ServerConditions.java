package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.server.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.chance.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;


public class ServerConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("CHANCE", (Line<Condition>) line ->
                new TestChance(line.getRequired(1, Chance.class)));

        conditions.addLoader("ONLINE_PLAYERS", (Line<Condition>) line ->
                new OnlinePlayersCondition(line.getRequired(1, Amount.class)));

        conditions.addLoader("PLACEHOLDER_EQUALS", (Line<Condition>) line -> {
            String placeholder = line.getRequiredString("id|tag|placeholder|ph");
            Amount amount = line.get(1, Amount.class).orElse(null);
            if (amount != null) {
                return new PlaceholderEqualsAmount(placeholder, amount);
            }

            boolean ignoreCase = line.getBoolean("ignoreCase|ignore-case").withDefault(true);
            return new PlaceholderEqualsString(placeholder, line.getRequiredString(1), ignoreCase);
        });

    }

}