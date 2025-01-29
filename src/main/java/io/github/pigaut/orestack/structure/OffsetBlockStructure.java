package io.github.pigaut.orestack.structure;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OffsetBlockStructure extends SingleBlockStructure {

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
    public List<Block> getBlocks(Location origin) {
        return List.of(origin.clone().add(offsetX, offsetY, offsetZ).getBlock());
    }

    @Override
    public boolean matchBlocks(Location origin) {
        return super.matchBlocks(origin.clone().add(offsetX, offsetY, offsetZ));
    }

    @Override
    public void createBlocks(Location origin) {
        super.createBlocks(origin.clone().add(offsetX, offsetY, offsetZ));
    }

    @Override
    public void removeBlocks(Location origin) {
        super.removeBlocks(origin.clone().add(offsetX, offsetY, offsetZ));
    }
}
