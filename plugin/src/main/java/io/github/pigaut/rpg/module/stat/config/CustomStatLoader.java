package io.github.pigaut.rpg.module.stat.config;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CustomStatLoader implements ConfigLoader<CustomStat> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid custom stat";
    }

    @Override
    public @NotNull CustomStat loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        double baseValue = section.getDouble("base").withDefault(0d);
        Map<Stat, LeveledStatModifier> subModifiers = new HashMap<>();

        ConfigSection modifierSection = section.getSectionOrCreate("stats");
        for (KeyedScalar modifierScalar : modifierSection.getNestedScalars()) {
            Stat stat = modifierScalar.getKeyAs(Stat.class).orThrow();
            LeveledStatModifier modifier = modifierScalar.getRequired(LeveledStatModifier.class);
            subModifiers.put(stat, modifier);
        }

        return new CustomStat(baseValue, subModifiers);
    }

}
