package io.github.pigaut.rpg.module.function.condition.config;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class NotConditionLoader implements ConfigLoader<NotCondition> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid if-not condition";
    }

    @Override
    public @NotNull NotCondition loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        return new NotCondition(scalar.getRequired(Condition.class));
    }

    @Override
    public @NotNull NotCondition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new NotCondition(sequence.getRequired(AndCondition.class));
    }

}
