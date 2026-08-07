package io.github.pigaut.rpg.config.misc;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class BlockRangeLoader implements ConfigLoader.Line<BlockRange> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid block range";
    }

    @Override
    public @NotNull BlockRange loadFromLine(ConfigLine line) throws InvalidConfigException {
        Amount rangeX = line.get("rangeX", Amount.class).withDefault(Amount.ZERO);
        Amount rangeY = line.get("rangeY", Amount.class).withDefault(Amount.ZERO);
        Amount rangeZ = line.get("rangeZ", Amount.class).withDefault(Amount.ZERO);
        return new BlockRange(rangeX, rangeY, rangeZ);
    }

    @Override
    public @NotNull BlockRange loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        Amount rangeX = section.get("x", Amount.class).withDefault(Amount.ZERO);
        Amount rangeY = section.get("y", Amount.class).withDefault(Amount.ZERO);
        Amount rangeZ = section.get("z", Amount.class).withDefault(Amount.ZERO);
        return new BlockRange(rangeX, rangeY, rangeZ);
    }

}
