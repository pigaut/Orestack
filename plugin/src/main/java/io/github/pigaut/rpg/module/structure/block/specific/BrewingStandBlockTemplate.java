package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BrewingStandBlockTemplate extends AbstractBlockTemplate<BrewingStand> {

    private final Set<Integer> bottles;

    public BrewingStandBlockTemplate(@NotNull Material type, @NotNull Set<Integer> bottles) {
        super(type, BrewingStand.class);
        this.bottles = bottles;
    }

    @Override
    public boolean matchBlockData(@NotNull BrewingStand blockData, @NotNull Rotation rotation) {
        for (int i = 0; i < blockData.getMaximumBottles(); i++) {
            if (blockData.hasBottle(i) != bottles.contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateBlockData(@NotNull BrewingStand blockData, @NotNull Rotation rotation) {
        for (int i = 0; i < blockData.getMaximumBottles(); i++) {
            blockData.setBottle(i, bottles.contains(i));
        }
    }
}
