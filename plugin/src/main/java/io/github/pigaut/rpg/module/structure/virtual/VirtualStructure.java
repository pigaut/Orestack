package io.github.pigaut.rpg.module.structure.virtual;

import com.google.common.collect.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VirtualStructure {

    private final StructureTemplate template;
    private final Location origin;
    private final Rotation rotation;
    private final Multimap<ChunkPosition, VirtualBlock> virtualBlocksByChunk;
    private final Set<UUID> viewers = new HashSet<>();

    public VirtualStructure(@NotNull StructureTemplate template, @NotNull Location origin, @NotNull Rotation rotation) {
        this.template = template;
        this.origin = origin;
        this.rotation = rotation;
        this.virtualBlocksByChunk = template.createVirtualBlocks(origin, rotation);
    }

    public @NotNull Location getOrigin() {
        return origin;
    }

    public @NotNull Rotation getRotation() {
        return rotation;
    }

    public boolean hasViewers() {
        return !viewers.isEmpty();
    }

    public boolean isViewer(@NotNull Player player) {
        return viewers.contains(player.getUniqueId());
    }

    public void send(@NotNull Player player, @NotNull ChunkPosition chunkPosition) {
        for (VirtualBlock virtualBlock : virtualBlocksByChunk.get(chunkPosition)) {
            virtualBlock.send(player);
        }
    }

    public void sendAll(@NotNull Player player) {
        for (VirtualBlock virtualBlock : virtualBlocksByChunk.values()) {
            virtualBlock.send(player);
        }
    }

    public void addViewer(@NotNull Player player) {
        viewers.add(player.getUniqueId());
        sendAll(player);
    }

    public void removeViewer(@NotNull Player player) {
        viewers.remove(player.getUniqueId());
        for (VirtualBlock virtualBlock : virtualBlocksByChunk.values()) {
            virtualBlock.remove(player);
        }
    }

    public void removeViewer(@NotNull UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            removeViewer(player);
        } else {
            viewers.remove(playerId);
        }
    }

    public @NotNull StructureTemplate getTemplate() {
        return template;
    }

    public @NotNull Set<UUID> getViewers() {
        return new HashSet<>(viewers);
    }

    public @NotNull Set<Block> getOccupiedBlocks() {
        return template.getOccupiedBlocks(origin, rotation);
    }

    public @Nullable VirtualBlock getVirtualBlock(@NotNull World world, int x, int y, int z) {
        for (VirtualBlock virtualBlock : virtualBlocksByChunk.get(ChunkPosition.of(world, x, z))) {
            if (LocationUtil.matches(virtualBlock.getLocation(), world, x, y, z)) {
                return virtualBlock;
            }
        }
        return null;
    }

    public @Nullable VirtualBlock getVirtualBlock(@NotNull Location location) {
        for (VirtualBlock virtualBlock : virtualBlocksByChunk.get(ChunkPosition.fromLocation(location))) {
            if (virtualBlock.getLocation().equals(location)) {
                return virtualBlock;
            }
        }
        return null;
    }

}
