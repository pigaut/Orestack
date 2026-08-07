package io.github.pigaut.rpg.module.function.condition.block;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockTypeEquals implements BlockCondition {

    private final List<Material> validBlockTypes;

    public BlockTypeEquals(List<Material> validBlockTypes) {
        this.validBlockTypes = validBlockTypes;
    }

    @Override
    public Boolean evaluate(@NotNull Block block) {
        final Material blockType = block.getType();
        for (Material validBlockType : validBlockTypes) {
            if (blockType == validBlockType) {
                return true;
            }
        }
        return false;
    }

}
