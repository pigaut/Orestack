package io.github.pigaut.rpg.module.stat.modifier;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StatModifier {

    public static final StatModifier ZERO = new StatModifier(0, StatOperation.ADD);

    private final double value;
    private final StatOperation type;

    public StatModifier(double value, @NotNull StatOperation type) {
        this.value = value;
        this.type = type;
    }

    public static StatModifier add(double value) {
        return new StatModifier(value, StatOperation.ADD);
    }

    public @NotNull StatOperation getOperation() {
        return type;
    }

    public double getAmount() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StatModifier that)) return false;
        return Double.compare(value, that.value) == 0 && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    @Override
    public String toString() {
        return "StatModifier{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }

}