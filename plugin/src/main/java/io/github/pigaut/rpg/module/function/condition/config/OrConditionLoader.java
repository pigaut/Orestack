package io.github.pigaut.rpg.module.function.condition.config;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class OrConditionLoader implements ConfigLoader<OrCondition> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid if-any condition";
    }

    @Override
    public @NotNull OrCondition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new OrCondition(sequence.getAllRequired(Condition.class));
    }

}
