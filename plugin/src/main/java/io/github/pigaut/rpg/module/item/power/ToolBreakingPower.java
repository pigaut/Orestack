package io.github.pigaut.rpg.module.item.power;

import org.jetbrains.annotations.*;

public class ToolBreakingPower {

    private final BreakingPower breakingPower;
    private final int amount;

    public ToolBreakingPower(@NotNull BreakingPower breakingPower, int amount) {
        this.breakingPower = breakingPower;
        this.amount = amount;
    }

    public @NotNull String getName() {
        return breakingPower.getName();
    }

    public @NotNull BreakingPower getType() {
        return breakingPower;
    }

    public int getAmount() {
        return amount;
    }

}
