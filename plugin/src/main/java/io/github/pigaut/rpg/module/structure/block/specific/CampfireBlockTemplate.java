package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Campfire;
import org.jetbrains.annotations.*;

public class CampfireBlockTemplate extends AbstractBlockTemplate<Campfire> {

    private final BlockFace direction;
    private final boolean lit;
    private final boolean signalFire;

    public CampfireBlockTemplate(@NotNull Material type, @NotNull BlockFace direction,
                                 boolean lit, boolean signalFire) {
        super(type, Campfire.class);
        this.direction = direction;
        this.lit = lit;
        this.signalFire = signalFire;
    }

    @Override
    public boolean matchBlockData(@NotNull Campfire blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(direction) &&
                blockData.isLit() == lit &&
                blockData.isSignalFire() == signalFire;
    }

    @Override
    public void updateBlockData(@NotNull Campfire blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(direction));
        blockData.setLit(lit);
        blockData.setSignalFire(signalFire);
    }
}
