package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class WallBlockTemplate extends AbstractBlockTemplate<Wall> {

    private final boolean up;
    private final Map<BlockFace, Wall.Height> heightByFace;

    public WallBlockTemplate(@NotNull Material type, boolean up, @NotNull Map<BlockFace, Wall.Height> heightByFace) {
        super(type, Wall.class);
        this.up = up;
        this.heightByFace = heightByFace;
    }

    @Override
    public boolean matchBlockData(@NotNull Wall blockData, @NotNull Rotation rotation) {
        if (blockData.isUp() != up) {
            return false;
        }

        for (BlockFace wallFace : BlockUtil.WALL_FACES) {
            BlockFace rotatedFace = rotation.translateBlockFace(wallFace);
            Wall.Height expected = heightByFace.getOrDefault(rotatedFace, Wall.Height.NONE);
            if (blockData.getHeight(rotatedFace) != expected) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void updateBlockData(@NotNull Wall blockData, @NotNull Rotation rotation) {
        blockData.setUp(up);
        for (BlockFace wallFace : BlockUtil.WALL_FACES) {
            BlockFace rotatedFace = rotation.translateBlockFace(wallFace);
            blockData.setHeight(wallFace, heightByFace.getOrDefault(rotatedFace, Wall.Height.NONE));
        }
    }

}
