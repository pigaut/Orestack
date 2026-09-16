package io.github.pigaut.rpg.module.function.condition.config;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class AndConditionLoader implements ConfigLoader<AndCondition> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid if condition";
    }

    @Override
    public @NotNull AndCondition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new AndCondition(sequence.getAllRequired(Condition.class));
    }

}
