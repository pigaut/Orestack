package io.github.pigaut.rpg.module.item.power;

import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BreakingPower {

    private final String name;
    private final BlockMatcherMap<Integer> breakingPowersByBlock;
    private final Function onWrongTool;
    private final Function onInsufficientPower;

    public BreakingPower(@NotNull String name,
                         @NotNull BlockMatcherMap<Integer> breakingPowersByBlock,
                         @Nullable Function onWrongTool,
                         @Nullable Function onInsufficientPower) {
        this.name = name;
        this.breakingPowersByBlock = breakingPowersByBlock;
        this.onWrongTool = onWrongTool;
        this.onInsufficientPower = onInsufficientPower;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable BlockBreakingPower fromBlock(@NotNull Block block) {
        Integer amount = breakingPowersByBlock.get(block);
        if (amount == null) {
            return null;
        }
        return new BlockBreakingPower(this, amount);
    }

    public @Nullable Function getOnWrongTool() {
        return onWrongTool;
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
