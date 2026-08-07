package io.github.pigaut.rpg.module.item.power;

import org.jetbrains.annotations.*;

public class ToolBreakingPower {

    private final BreakingPower breakingPower;
    private final int amount;

    public ToolBreakingPower(BreakingPower breakingPower, int amount) {
        this.breakingPower = breakingPower;
        this.amount = amount;
    }

    public @NotNull BreakingPower getBreakingPower() {
        return breakingPower;
    }

    public int getAmount() {
        return amount;
    }

}
