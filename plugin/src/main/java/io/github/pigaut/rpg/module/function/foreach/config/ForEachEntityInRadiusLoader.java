package io.github.pigaut.rpg.module.function.foreach.config;

import io.github.pigaut.rpg.module.function.foreach.type.*;
import io.github.pigaut.rpg.module.function.foreach.type.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ForEachEntityInRadiusLoader implements ConfigLoader.Line<ForEachEntityInRadius> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid for-each source";
    }

    @Override
    public @NotNull ForEachEntityInRadius loadFromLine(ConfigLine line) throws InvalidConfigException {
        Amount radius = line.get("radius", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(5));

        Amount entityLimit = line.get("limit|entityLimit", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(100));

        return new ForEachEntityInRadius(radius, entityLimit);
    }

}
