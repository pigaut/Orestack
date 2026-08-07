package io.github.pigaut.rpg.module.structure.virtual;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VirtualStructureManager extends Manager {

    private final Set<VirtualStructure> virtualStructures = new HashSet<>();

    public VirtualStructureManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void disable() {
        virtualStructures.clear();
    }

    public boolean isSupported() {
        return Server.isPaper() && Server.getVersion() >= Version.V1_19_3 && Server.isPluginLoaded("packetevents");
    }

    public void register(@NotNull VirtualStructure virtualStructure) {
        Preconditions.checkState(Server.isPaper(), "Virtual structures can only be registered on paper servers.");
        virtualStructures.add(virtualStructure);
    }

    public void unregister(@NotNull VirtualStructure fakeStructure) {
        virtualStructures.remove(fakeStructure);
    }

    public Collection<VirtualStructure> getAll() {
        return new ArrayList<>(virtualStructures);
    }

    public @Nullable BlockData getVirtualBlockData(@NotNull Player player, @NotNull World world, int x, int y, int z) {
        for (VirtualStructure structure : virtualStructures) {
            if (!structure.isViewer(player)) {
                continue;
            }

            VirtualBlock virtualBlock = structure.getVirtualBlock(world, x, y, z);
            if (virtualBlock != null) {
                return virtualBlock.getBlockData();
            }
        }
        return null;
    }

    public @Nullable BlockData getVirtualBlockData(@NotNull Location location) {
        for (VirtualStructure structure : virtualStructures) {
            VirtualBlock virtualBlock = structure.getVirtualBlock(location);
            if (virtualBlock != null) {
                return virtualBlock.getBlockData();
            }
        }
        return null;
    }

    // To be optimized probably
    public boolean isStructure(@NotNull Location location) {
        for (VirtualStructure structure : virtualStructures) {
            for (Block block : structure.getOccupiedBlocks()) {
                if (block.getLocation().equals(location)) {
                    return true;
                }
            }
        }
        return false;
    }

}
