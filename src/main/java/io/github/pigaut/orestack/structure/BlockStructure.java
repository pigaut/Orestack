package io.github.pigaut.orestack.structure;

import org.bukkit.*;
import org.bukkit.block.*;

import java.util.*;

public interface BlockStructure {

    boolean matchBlocks(Location origin);

    List<Block> getBlocks(Location origin);

    void createBlocks(Location origin);

    void removeBlocks(Location origin);

}
