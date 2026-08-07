package io.github.pigaut.rpg.module.function.foreach.config;

import io.github.pigaut.rpg.module.function.foreach.type.*;
import io.github.pigaut.rpg.module.function.foreach.type.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ForEachEntityInFrontLoader implements ConfigLoader.Line<ForEachEntityInFront> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid for-each source";
    }

    @Override
    public @NotNull ForEachEntityInFront loadFromLine(ConfigLine line) throws InvalidConfigException {
        Amount length = line.get("length", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(5));

        Amount width = line.get("width", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(3));

        Amount entityLimit = line.get("limit|entityLimit", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(100));

        return new ForEachEntityInFront(length, width, entityLimit);
    }

}
