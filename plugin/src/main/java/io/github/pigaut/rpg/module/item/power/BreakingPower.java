package io.github.pigaut.rpg.module.item.power;

import io.github.pigaut.rpg.module.function.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BreakingPower {

    private final String name;
    private final String display;
    private final Map<Material, Integer> breakingPowersByBlock;
    private final Function onInsufficientPower;

    public BreakingPower(@NotNull String name, @NotNull String display,
                         @NotNull Map<Material, Integer> breakingPowersByBlock,
                         @Nullable Function onInsufficientPower) {
        this.name = name;
        this.display = display;
        this.breakingPowersByBlock = breakingPowersByBlock;
        this.onInsufficientPower = onInsufficientPower;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getDisplay() {
        return display;
    }

    public boolean containsBlock(@NotNull Material block) {
        return breakingPowersByBlock.containsKey(block);
    }

    public int getAmount(@NotNull Material block) {
        return breakingPowersByBlock.getOrDefault(block, 1);
    }

    public @NotNull Map<Material, Integer> getAmountByBlock() {
        return new HashMap<>(breakingPowersByBlock);
    }

    public @Nullable Function getOnInsufficientPower() {
        return onInsufficientPower;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BreakingPower that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
