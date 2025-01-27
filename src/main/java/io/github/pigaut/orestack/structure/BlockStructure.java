package io.github.pigaut.orestack.structure;

import org.bukkit.*;
import org.bukkit.block.*;

import java.util.*;

public interface BlockStructure {

    List<Block> getBlocks(Location origin);

    void removeBlocks(Location origin);

    boolean matchBlocks(Location origin);

    void createBlocks(Location origin);

}
