package io.github.pigaut.rpg.module.structure;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.global.*;
import io.github.pigaut.rpg.module.structure.global.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.structure.global.Structure;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FoliaStructure extends Structure {

    private final EnhancedPlugin plugin;

    public FoliaStructure(@NotNull EnhancedPlugin plugin, @NotNull StructureTemplate template,
                          @NotNull Location origin, @NotNull Rotation rotation) {
        super(template, origin, rotation);
        this.plugin = plugin;
    }

    public void place() {
        for (GlobalBlock realBlock : this) {
            plugin.getRegionScheduler(realBlock.getLocation())
                    .runTask(realBlock::place);
        }
    }

    public void remove() {
        for (GlobalBlock realBlock : this) {
            plugin.getRegionScheduler(realBlock.getLocation())
                    .runTask(realBlock::remove);
        }
    }

    public @NotNull Structure replace(@NotNull StructureTemplate structureTemplate) {
        Set<Location> newBlockLocations = structureTemplate.getOccupiedLocations(origin, rotation);
        for (Block oldBlock : getOccupiedBlocks()) {
            Location oldBlockLocation = oldBlock.getLocation();
            if (!newBlockLocations.contains(oldBlockLocation)) {
                plugin.getRegionScheduler(oldBlockLocation).runTask(() -> {
                    oldBlock.setType(Material.AIR, false);
                });
            }
        }
        return structureTemplate.place(origin, rotation);
    }

}
