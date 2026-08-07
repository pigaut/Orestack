package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class TurtleEggBlockTemplate extends AbstractBlockTemplate<TurtleEgg> {

    private final int eggs;

    public TurtleEggBlockTemplate(@NotNull Material type, int eggs) {
        super(type, TurtleEgg.class);
        this.eggs = eggs;
    }

    @Override
    public boolean matchBlockData(@NotNull TurtleEgg blockData, @NotNull Rotation rotation) {
        return blockData.getEggs() == eggs;
    }

    @Override
    public void updateBlockData(@NotNull TurtleEgg blockData, @NotNull Rotation rotation) {
        blockData.setEggs(eggs);
    }
}
