package io.github.pigaut.rpg.config.misc;

import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.map.*;
import org.jetbrains.annotations.*;

public class BlockRangeMapper implements ConfigMapper<BlockRange> {

    @Override
    public @NotNull FieldType getDefaultMappingType() {
        return FieldType.SECTION;
    }

    @Override
    public void mapToSection(@NotNull ConfigSection section, @NotNull BlockRange value) {
        section.set("x", value.rangeX);
        section.set("y", value.rangeY);
        section.set("z", value.rangeZ);
    }

}
