package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.ChiseledBookshelf;
import org.jetbrains.annotations.*;

import java.util.*;

public class ChiseledBookshelfBlockTemplate extends AbstractBlockTemplate<ChiseledBookshelf> {

    private final BlockFace facing;
    private final Set<Integer> books;

    public ChiseledBookshelfBlockTemplate(@NotNull BlockFace facing, @NotNull Set<Integer> books) {
        super(Material.CHISELED_BOOKSHELF, ChiseledBookshelf.class);
        this.facing = facing;
        this.books = books;
    }

    @Override
    public boolean matchBlockData(@NotNull ChiseledBookshelf blockData, @NotNull Rotation rotation) {
        if (blockData.getFacing() != rotation.translateBlockFace(facing)) {
            return false;
        }

        for (int i = 0; i < blockData.getMaximumOccupiedSlots(); i++) {
            if (blockData.isSlotOccupied(i) != books.contains(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateBlockData(@NotNull ChiseledBookshelf blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        for (int i = 0; i < blockData.getMaximumOccupiedSlots(); i++) {
            blockData.setSlotOccupied(i, books.contains(i));
        }
    }
}
