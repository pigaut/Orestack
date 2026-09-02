package io.github.pigaut.rpg.module.function.condition.config;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class NorConditionLoader implements ConfigLoader<NorCondition> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid if-none condition";
    }

    @Override
    public @NotNull NorCondition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new NorCondition(sequence.getAllRequired(Condition.class));
    }

}
