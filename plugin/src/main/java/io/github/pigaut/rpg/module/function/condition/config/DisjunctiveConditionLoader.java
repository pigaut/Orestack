package io.github.pigaut.rpg.module.function.condition.config;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class DisjunctiveConditionLoader implements ConfigLoader<DisjunctiveCondition> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid disjunctive condition";
    }

    @Override
    public @NotNull DisjunctiveCondition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new DisjunctiveCondition(sequence.getAllRequired(Condition.class));
    }

}
