package io.github.pigaut.orestack.structure;

import org.bukkit.*;
import org.bukkit.block.*;
import io.github.pigaut.voxel.util.Rotation;

import java.util.*;

public interface BlockStructure {

    boolean matchBlocks(Location origin, Rotation rotation);

    List<Block> getBlocks(Location origin, Rotation rotation);

    void createBlocks(Location origin, Rotation rotation);

    void removeBlocks(Location origin, Rotation rotation);

}
