package io.github.pigaut.rpg.module.function.condition.config;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class NegativeConditionLoader implements ConfigLoader<NegativeCondition> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid negative condition";
    }

    @Override
    public @NotNull NegativeCondition loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        return new NegativeCondition.Simple(scalar.getRequired(Condition.class));
    }

    @Override
    public @NotNull NegativeCondition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new NegativeCondition.Multi(sequence.getAllRequired(Condition.class));
    }

}
