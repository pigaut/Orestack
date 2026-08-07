package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class SmallDripLeafBlockTemplate extends AbstractBlockTemplate<SmallDripleaf> {

    private final BlockFace direction;
    private final Bisected.Half half;

    public SmallDripLeafBlockTemplate(@NotNull BlockFace direction, @NotNull Bisected.Half half) {
        super(Material.SMALL_DRIPLEAF, SmallDripleaf.class);
        this.direction = direction;
        this.half = half;
    }

    @Override
    public boolean matchBlockData(@NotNull SmallDripleaf blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(direction) &&
                blockData.getHalf() == half;
    }

    @Override
    public void updateBlockData(@NotNull SmallDripleaf blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(direction));
        blockData.setHalf(half);
    }

}
