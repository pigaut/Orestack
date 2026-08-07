package io.github.pigaut.rpg.module.structure.global;

import io.github.pigaut.rpg.module.structure.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

import io.github.pigaut.rpg.core.transform.Rotation;

public class Structure implements Iterable<GlobalBlock> {

    private final StructureTemplate template;
    protected final Location origin;
    protected final Rotation rotation;
    private final List<GlobalBlock> realBlocks;

    public Structure(@NotNull StructureTemplate template, @NotNull Location origin, @NotNull Rotation rotation) {
        this.template = template;
        this.origin = origin;
        this.rotation = rotation;
        this.realBlocks = template.createBlocks(origin, rotation);
    }

    public StructureTemplate getTemplate() {
        return template;
    }

    public boolean hasMultipleBlocks() {
        return template.hasMultipleBlocks();
    }

    public boolean isPlaced() {
        for (GlobalBlock realBlock : realBlocks) {
            if (!realBlock.isPlaced()) {
                return false;
            }
        }
        return true;
    }

    public void place() {
        for (GlobalBlock realBlock : realBlocks) {
            realBlock.place();
        }
    }

    public void remove() {
        for (GlobalBlock realBlock : realBlocks) {
            realBlock.remove();
        }
    }

    public @NotNull Structure replace(@NotNull StructureTemplate structureTemplate) {
        Set<Location> newBlockLocations = structureTemplate.getOccupiedLocations(origin, rotation);
        for (Block oldBlock : getOccupiedBlocks()) {
            Location oldBlockLocation = oldBlock.getLocation();
            if (!newBlockLocations.contains(oldBlockLocation)) {
                oldBlock.setType(Material.AIR, false);
            }
        }
        return structureTemplate.place(origin, rotation);
    }

    public Set<Block> getOccupiedBlocks() {
        return template.getOccupiedBlocks(origin, rotation);
    }

    public Material getMostCommonMaterial() {
        return template.getMostCommonMaterial();
    }

    @Override
    public @NotNull Iterator<GlobalBlock> iterator() {
        return realBlocks.iterator();
    }

}
