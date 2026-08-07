package io.github.pigaut.rpg.core.buildstation;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.specific.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.specific.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;

import java.util.*;

public class BuildStationStructureTemplate extends StructureTemplate {

    public BuildStationStructureTemplate(EnhancedPlugin plugin, int currentStructure, int xLength, int height, int zLength) {
        super(plugin, createBlocks(currentStructure, xLength, height, zLength));
    }

    private static Map<Offset, BlockTemplate> createBlocks(int currentStructure, int xLength, int height, int zLength) {
        Map<Offset, BlockTemplate> blockTemplatesByOffset = new HashMap<>();

        int halfX = xLength / 2;
        int halfZ = zLength / 2;

        int frameStartX = -halfX - 2;
        int frameEndX = halfX + 2;

        int frameStartZ = -halfZ - 2;
        int frameEndZ = halfZ + 2;

        BlockTemplate whiteConcrete = new BasicBlockTemplate(Material.WHITE_CONCRETE);
        BlockTemplate goldBlock = new BasicBlockTemplate(Material.GOLD_BLOCK);
        BlockTemplate whiteGlass = new BasicBlockTemplate(Material.WHITE_STAINED_GLASS);
        BlockTemplate redWool = new BasicBlockTemplate(Material.RED_WOOL);

        for (int x = frameStartX; x <= frameEndX; x++) {
            for (int z = frameStartZ; z <= frameEndZ; z++) {
                Offset floorOffset = new Offset(x, 0, z);
                Offset roofOffset = new Offset(x, height + 1, z);

                boolean isFrameOuter = (x == frameStartX || x == frameEndX || z == frameStartZ || z == frameEndZ);
                boolean isFrameInner = (x == frameStartX + 1 || x == frameEndX - 1 || z == frameStartZ + 1 || z == frameEndZ - 1);
                boolean isCenter = x == 0 && z == 0;

                if (isFrameOuter) {
                    BlockFace direction = BlockFace.NORTH;
                    Stairs.Shape stairShape = Stairs.Shape.STRAIGHT;

                    boolean north = z == frameEndZ;
                    if (north) {
                        direction = BlockFace.NORTH;
                    }

                    boolean south = z == frameStartZ;
                    if (south) {
                        direction = BlockFace.SOUTH;
                    }

                    boolean west = x == frameEndX;
                    if (west) {
                        direction = BlockFace.WEST;
                    }

                    boolean east = x == frameStartX;
                    if (east) {
                        direction = BlockFace.EAST;
                    }

                    if ((north && west) || (south && east)) {
                        stairShape = Stairs.Shape.OUTER_RIGHT;
                    }
                    else if ((north && east) || (south && west)) {
                        stairShape = Stairs.Shape.OUTER_LEFT;
                    }

                    BlockTemplate floorStairs = new StairsBlockTemplate(Material.QUARTZ_STAIRS, direction,
                            Bisected.Half.BOTTOM, stairShape);
                    blockTemplatesByOffset.put(floorOffset, floorStairs);

                    BlockTemplate roofStairs = new StairsBlockTemplate(Material.QUARTZ_STAIRS, direction,
                            Bisected.Half.TOP, stairShape);
                    blockTemplatesByOffset.put(roofOffset, roofStairs);

                }
                else if (isFrameInner) {
                    blockTemplatesByOffset.put(floorOffset, redWool);
                    blockTemplatesByOffset.put(roofOffset, redWool);
                }
                else if (isCenter) {
                    blockTemplatesByOffset.put(floorOffset, goldBlock);
                    blockTemplatesByOffset.put(roofOffset, whiteGlass);
                }
                else {
                    blockTemplatesByOffset.put(floorOffset, whiteConcrete);
                    blockTemplatesByOffset.put(roofOffset, whiteGlass);
                }
            }
        }

        BlockTemplate previousSign = WallSignBlockTemplate.builder()
                .type(Material.JUNGLE_WALL_SIGN)
                .lines("", "", "&lPrevious", "&8&l-----")
                .build();

        BlockTemplate infoSign = WallSignBlockTemplate.builder()
                .type(Material.JUNGLE_WALL_SIGN)
                .lines("&6&lStructure #" + currentStructure, "", "&2&lSave", "&a&l-----")
                .build();

        BlockTemplate nextSign = WallSignBlockTemplate.builder()
                .type(Material.JUNGLE_WALL_SIGN)
                .lines("", "", "&lNext", "&8&l-----")
                .build();

        BlockTemplate clearSign = WallSignBlockTemplate.builder()
                .type(Material.JUNGLE_WALL_SIGN)
                .lines("", "", "&4&lClear", "&c&l-----")
                .build();

        Offset previousSignOffset = new Offset(frameEndX - 1, 0, frameStartZ - 1);
        Offset infoSignOffset = new Offset(frameEndX - 2, 0, frameStartZ - 1);
        Offset nextSignOffset = new Offset(frameEndX - 3, 0, frameStartZ - 1);
        Offset clearSingOffset = new Offset(frameStartX + 1, 0, frameStartZ - 1);

        blockTemplatesByOffset.put(previousSignOffset, previousSign);
        blockTemplatesByOffset.put(infoSignOffset, infoSign);
        blockTemplatesByOffset.put(nextSignOffset, nextSign);
        blockTemplatesByOffset.put(clearSingOffset, clearSign);

        return blockTemplatesByOffset;
    }

}
