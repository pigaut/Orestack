package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.generator.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class GeneratorConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("GENERATOR_NAME_EQUALS", (ConfigLoader.Line<Condition>) line ->
                new GeneratorNameEquals(line.getRequiredString(1)));

    }

}
