package io.github.pigaut.rpg.module.stat.modifier;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.*;
import org.jetbrains.annotations.*;

public class LeveledStatModifier extends StatModifier {

    private final int levelInterval;

    public LeveledStatModifier(double amount, int levelInterval, @NotNull StatOperation operation) {
        super(amount, operation);
        this.levelInterval = levelInterval;
    }

    public int getLevelInterval() {
        return levelInterval;
    }

    public double getAmountAtLevel(int level) {
        return ((double) level / levelInterval) * getAmount();
    }

}