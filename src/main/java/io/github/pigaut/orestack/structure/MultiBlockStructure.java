package io.github.pigaut.orestack.structure;

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
    public List<Block> getBlocks(Location origin) {
        return structures.stream()
                .flatMap(component -> component.getBlocks(origin).stream())
                .toList();
    }

    @Override
    public void removeBlocks(Location origin) {
        for (BlockStructure structure : structures) {
            structure.removeBlocks(origin);
        }
    }

    @Override
    public boolean matchBlocks(Location origin) {
        for (BlockStructure component : structures) {
            if (!component.matchBlocks(origin)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void createBlocks(Location origin) {
        for (BlockStructure component : structures) {
            component.createBlocks(origin);
        }
    }

}
