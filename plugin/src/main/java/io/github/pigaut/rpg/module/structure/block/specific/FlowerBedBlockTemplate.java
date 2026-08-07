package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class FlowerBedBlockTemplate extends AbstractBlockTemplate<FlowerBed> {

    private final int flowerAmount;

    public FlowerBedBlockTemplate(@NotNull Material type, int flowerAmount) {
        super(type, FlowerBed.class);
        this.flowerAmount = flowerAmount;
    }

    @Override
    public boolean matchBlockData(@NotNull FlowerBed blockData, @NotNull Rotation rotation) {
        return blockData.getFlowerAmount() == flowerAmount;
    }

    @Override
    public void updateBlockData(@NotNull FlowerBed blockData, @NotNull Rotation rotation) {
        blockData.setFlowerAmount(flowerAmount);
    }
}
