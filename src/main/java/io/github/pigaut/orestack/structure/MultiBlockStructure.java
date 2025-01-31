package io.github.pigaut.orestack.structure;

import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiBlockStructure implements BlockStructure {

    private final List<BlockStructure> structures;

    public MultiBlockStructure(@NotNull List<@NotNull BlockStructure> structures) {
        this.structures = structures;
    }

    @Override
    public boolean matchBlocks(Location origin, Rotation rotation) {
        for (BlockStructure component : structures) {
            if (!component.matchBlocks(origin, rotation)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Block> getBlocks(Location origin, Rotation rotation) {
        return structures.stream()
                .flatMap(component -> component.getBlocks(origin, rotation).stream())
                .toList();
    }

    @Override
    public void createBlocks(Location origin, Rotation rotation) {
        for (BlockStructure component : structures) {
            component.createBlocks(origin, rotation);
        }
    }

    @Override
    public void removeBlocks(Location origin, Rotation rotation) {
        for (BlockStructure structure : structures) {
            structure.removeBlocks(origin, rotation);
        }
    }

}
