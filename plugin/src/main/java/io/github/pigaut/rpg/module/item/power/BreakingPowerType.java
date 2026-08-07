package io.github.pigaut.rpg.module.item.power;

import org.jetbrains.annotations.*;

import java.util.*;

public class BreakingPowerType {

    private final @NotNull String name;

    public BreakingPowerType(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BreakingPowerType powerType)) return false;
        return Objects.equals(name, powerType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}

