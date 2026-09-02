package io.github.pigaut.rpg.module.function.condition.block;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockTypeEquals implements BlockCondition.Predicate {

    private final Set<Material> blockTypes;

    public BlockTypeEquals(@NotNull Set<Material> blockTypes) {
        this.blockTypes = Set.copyOf(blockTypes);
    }

    @Override
    public boolean test(@NotNull Block block) {
        return blockTypes.contains(block.getType());
    }

}
