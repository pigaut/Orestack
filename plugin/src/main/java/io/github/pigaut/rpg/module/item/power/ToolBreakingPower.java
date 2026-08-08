package io.github.pigaut.rpg.module.item.power;

import org.jetbrains.annotations.*;

public class ToolBreakingPower {

    private final BreakingPower breakingPower;
    private final String display;
    private final int amount;

    public ToolBreakingPower(@NotNull BreakingPower breakingPower, @NotNull String display, int amount) {
        this.breakingPower = breakingPower;
        this.display = display;
        this.amount = amount;
    }

    public @NotNull String getDisplay() {
        return display;
    }

    public @NotNull BreakingPower getType() {
        return breakingPower;
    }

    public int getAmount() {
        return amount;
    }

}
