package io.github.pigaut.rpg.module.stat.custom;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CustomStat extends PlayerStat {

    private final Map<Stat, LeveledStatModifier> subModifiers;

    public CustomStat(double base, @NotNull Map<Stat, LeveledStatModifier> subModifiers) {
        super(base);
        this.subModifiers = Map.copyOf(subModifiers);
    }

    public @Nullable LeveledStatModifier getStatModifier(@NotNull Stat type) {
        return subModifiers.get(type);
    }

    public double getSubStat(@NotNull Stat type) {
        LeveledStatModifier modifier = subModifiers.get(type);
        if (modifier == null) {
            return 0;
        }
        int level = (int) getTotal();
        return modifier.getAmountAtLevel(level);
    }

}
