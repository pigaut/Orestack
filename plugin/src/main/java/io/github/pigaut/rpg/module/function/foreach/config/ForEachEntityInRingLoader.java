package io.github.pigaut.rpg.module.function.foreach.config;

import io.github.pigaut.rpg.module.function.foreach.type.*;
import io.github.pigaut.rpg.module.function.foreach.type.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class ForEachEntityInRingLoader implements ConfigLoader.Line<ForEachEntityInRing> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid for-each source";
    }

    @Override
    public @NotNull ForEachEntityInRing loadFromLine(ConfigLine line) throws InvalidConfigException {
        Amount diameter = line.get("diameter", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(10));

        Amount thickness = line.get("thickness", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(2));

        Amount entityLimit = line.get("limit|entityLimit", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(Amount.fixed(100));

        return new ForEachEntityInRing(diameter, thickness, entityLimit);
    }

}
