package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultipleFacingBlockTemplate extends AbstractBlockTemplate<MultipleFacing> {

    private final Set<BlockFace> facingDirections;

    public MultipleFacingBlockTemplate(@NotNull Material type, Set<BlockFace> facingDirections) {
        super(type, MultipleFacing.class);
        this.facingDirections = facingDirections;
    }

    @Override
    public boolean matchBlockData(@NotNull MultipleFacing blockData, @NotNull Rotation rotation) {
        return blockData.getFaces().equals(rotation.translateBlockFaces(facingDirections));
    }

    @Override
    public void updateBlockData(@NotNull MultipleFacing blockData, @NotNull Rotation rotation) {
        for (BlockFace allowedFace : rotation.translateBlockFaces(blockData.getAllowedFaces())) {
            blockData.setFace(allowedFace, facingDirections.contains(allowedFace));
        }
    }
}
