package io.github.pigaut.rpg.bukkit;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static org.bukkit.Material.*;

public class Crops {

    private static final List<BlockFace> SURROUNDING_FACES = List.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

    public static List<Block> getCropsAttached(Block block) {
        return SURROUNDING_FACES.stream()
                .map(block::getRelative)
                .filter(attachedBlock -> {
                    final Material type = attachedBlock.getType();
                    return type == COCOA || type == SUGAR_CANE || type == CACTUS || type == BAMBOO;
                })
                .toList();
    }

    public static boolean hasCropAttached(Block block) {
        for (BlockFace face : SURROUNDING_FACES) {
            final Material type = block.getRelative(face).getType();
            if (type == COCOA || type == SUGAR_CANE || type == CACTUS || type == BAMBOO) {
                return true;
            }
        }
        return false;
    }

    public static @Nullable Block getCropAttached(Block block) {
        for (BlockFace face : SURROUNDING_FACES) {
            final Block attachedCrop = block.getRelative(face);
            final Material type = attachedCrop.getType();
            if (type == COCOA || type == SUGAR_CANE || type == CACTUS || type == BAMBOO) {
                return attachedCrop;
            }
        }
        return null;
    }

}
