package io.github.pigaut.rpg.module.function.condition.block;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockTypeEquals implements BlockCondition {

    private final Set<Material> validBlockTypes;

    public BlockTypeEquals(Set<Material> validBlockTypes) {
        this.validBlockTypes = Set.copyOf(validBlockTypes);
    }

    @Override
    public Boolean evaluate(@NotNull Block block) {
        return validBlockTypes.contains(block.getType());
    }

}
