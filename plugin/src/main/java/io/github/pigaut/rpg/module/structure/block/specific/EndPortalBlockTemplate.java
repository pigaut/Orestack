package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class EndPortalBlockTemplate extends AbstractBlockTemplate<EndPortalFrame> {

    private final boolean eye;

    public EndPortalBlockTemplate(@NotNull Material type, boolean eye) {
        super(type, EndPortalFrame.class);
        this.eye = eye;
    }

    @Override
    public boolean matchBlockData(@NotNull EndPortalFrame blockData, @NotNull Rotation rotation) {
        return blockData.hasEye() == eye;
    }

    @Override
    public void updateBlockData(@NotNull EndPortalFrame blockData, @NotNull Rotation rotation) {
        blockData.setEye(eye);
    }
}
