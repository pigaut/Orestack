package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class OrientableBlockTemplate extends AbstractBlockTemplate<Orientable> {

    private final Axis axis;

    public OrientableBlockTemplate(@NotNull Material type, @NotNull Axis axis) {
        super(type, Orientable.class);
        this.axis = axis;
    }

    @Override
    public boolean matchBlockData(@NotNull Orientable blockData, @NotNull Rotation rotation) {
        return blockData.getAxis() == rotation.translateAxis(axis);
    }

    @Override
    public void updateBlockData(@NotNull Orientable blockData, @NotNull Rotation rotation) {
        blockData.setAxis(rotation.translateAxis(axis));
    }

}
