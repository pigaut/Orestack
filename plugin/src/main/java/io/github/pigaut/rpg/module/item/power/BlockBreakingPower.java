package io.github.pigaut.rpg.module.item.power;

import io.github.pigaut.rpg.module.function.*;
import org.jetbrains.annotations.*;

public class BlockBreakingPower {

    private final BreakingPower breakingPower;
    private final int amount;

    public BlockBreakingPower(@NotNull BreakingPower breakingPower, int amount) {
        this.breakingPower = breakingPower;
        this.amount = amount;
    }

    public @NotNull BreakingPower getType() {
        return breakingPower;
    }

    public int getAmount() {
        return amount;
    }

    public @NotNull String getName() {
        return breakingPower.getName();
    }

    public @NotNull String getDisplay() {
        return breakingPower.getDisplay();
    }

    public @Nullable Function getOnWrongTool() {
        return breakingPower.getOnWrongTool();
    }

    public @Nullable Function getOnInsufficientPower() {
        return breakingPower.getOnInsufficientPower();
    }

}
