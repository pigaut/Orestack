package io.github.pigaut.orestack.structure;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.voxel.util.Rotation;

import java.util.*;

public class OffsetBlockStructure extends OriginBlockStructure {

    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;

    public OffsetBlockStructure(Material type, @Nullable Integer age, @Nullable BlockFace direction, @Nullable Axis orientation, int offsetX, int offsetY, int offsetZ) {
        super(type, age, direction, orientation);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    @Override
    public List<Block> getBlocks(Location origin, Rotation rotation) {
        final Location offsetLocation = rotation.apply(origin.clone(), offsetX, offsetY, offsetZ);
        return List.of(offsetLocation.getBlock());
    }

    @Override
    public boolean matchBlocks(Location origin, Rotation rotation) {
        final Location offsetLocation = rotation.apply(origin.clone(), offsetX, offsetY, offsetZ);
        return super.matchBlocks(offsetLocation, rotation);
    }

    @Override
    public void createBlocks(Location origin, Rotation rotation) {
        final Location offsetLocation = rotation.apply(origin.clone(), offsetX, offsetY, offsetZ);
        super.createBlocks(offsetLocation, rotation);
    }

    @Override
    public void removeBlocks(Location origin, Rotation rotation) {
        final Location offsetLocation = rotation.apply(origin.clone(), offsetX, offsetY, offsetZ);
        super.removeBlocks(offsetLocation, rotation);
    }

}
