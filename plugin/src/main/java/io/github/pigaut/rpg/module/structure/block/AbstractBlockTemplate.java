package io.github.pigaut.rpg.module.structure.block;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public abstract class AbstractBlockTemplate<T extends BlockData> extends BasicBlockTemplate {

    private final Class<T> blockDataClass;

    protected AbstractBlockTemplate(@NotNull Material type, @NotNull Class<T> blockDataClass) {
        super(type);
        Preconditions.checkArgument(MaterialUtil.isBlockData(type, blockDataClass), "type must be " + CaseFormatter.toTitleCase(blockDataClass));
        this.blockDataClass = blockDataClass;
    }

    public abstract boolean matchBlockData(@NotNull T blockData, @NotNull Rotation rotation);

    public abstract void updateBlockData(@NotNull T blockData, @NotNull Rotation rotation);

    public boolean hasBlockState() {
        return false;
    }

    public boolean matchBlockState(@NotNull BlockState blockState) {
        Preconditions.checkState(hasBlockState(), "Block template doesn't have a block state.");
        return false;
    }

    public void updateBlockState(@NotNull BlockState blockState) {
        Preconditions.checkState(hasBlockState(), "Block template doesn't have a block state.");
    }

    @Override
    public @NotNull BlockData createBlockData(@NotNull Rotation rotation) {
        T blockData = blockDataClass.cast(type.createBlockData());
        updateBlockData(blockData, rotation);
        return blockData;
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        Block block = location.getBlock();
        if (block.getType() != type) {
            return false;
        }

        T blockData = blockDataClass.cast(block.getBlockData());
        return matchBlockData(blockData, rotation);
    }

    @Override
    public void place(@NotNull Location location, @NotNull Rotation rotation) {
        Block block = location.getBlock();

        T blockData;
        if (block.getType() == type) {
            blockData = blockDataClass.cast(block.getBlockData());
        } else {
            block.setType(type, false);
            blockData = blockDataClass.cast(block.getBlockData());
        }

        if (!matchBlockData(blockData, rotation)) {
            updateBlockData(blockData, rotation);
            block.setBlockData(blockData, false);
        }

        if (!hasBlockState()) {
            return;
        }

        BlockState blockState = block.getState();
        if (!matchBlockState(blockState)) {
            updateBlockState(blockState);
            blockState.update(true, false);
        }
    }

}
