package io.github.pigaut.rpg.module.stat;

import io.github.pigaut.rpg.module.stat.modifier.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StatUtil {

    public static final double MINING_EFFICIENCY_MULTIPLIER = 20;
    public static final double MOVEMENT_SPEED_MULTIPLIER = 100;

    public static double getDefenseDamageReduction(int defense) {
        return (double) 100 / (defense + 100);
    }

    public static int calculateDamage(double damage, int defense) {
        double damageReduction = (double) 100 / (defense + 100);
        return (int) (damage * damageReduction);
    }

    public static double calculateStatTotal(double base, @NotNull Collection<StatModifier> modifiers) {
        double total = base;

        double addSum = 0;
        for (StatModifier modifier : modifiers) {
            if (modifier.getOperation() == StatOperation.ADD) {
                addSum += modifier.getAmount();
            }
        }
        total += addSum;

        double scaleSum = 0;
        for (StatModifier modifier : modifiers) {
            if (modifier.getOperation() == StatOperation.SCALE) {
                scaleSum += modifier.getAmount();
            }
        }
        total += base * scaleSum;

        for (StatModifier modifier : modifiers) {
            if (modifier.getOperation() == StatOperation.MULTIPLY) {
                total *= (1 + modifier.getAmount());
            }
        }

        return total;
    }

    public static double calculateStatTotal(double base, @NotNull Map<LeveledStatModifier, Integer> modifierLevels) {
        double total = base;

        double addSum = 0;
        for (var entry : modifierLevels.entrySet()) {
            LeveledStatModifier modifier = entry.getKey();
            int level = entry.getValue();
            if (modifier.getOperation() == StatOperation.ADD) {
                addSum += modifier.getAmountAtLevel(level);
            }
        }
        total += addSum;

        double scaleSum = 0;
        for (var entry : modifierLevels.entrySet()) {
            LeveledStatModifier modifier = entry.getKey();
            int level = entry.getValue();
            if (modifier.getOperation() == StatOperation.SCALE) {
                scaleSum += modifier.getAmountAtLevel(level);
            }
        }
        total += base * scaleSum;

        for (var entry : modifierLevels.entrySet()) {
            LeveledStatModifier modifier = entry.getKey();
            int level = entry.getValue();
            if (modifier.getOperation() == StatOperation.MULTIPLY) {
                total *= (1 + modifier.getAmountAtLevel(level));
            }
        }

        return total;
    }

}
