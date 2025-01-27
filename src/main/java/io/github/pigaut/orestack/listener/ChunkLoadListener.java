package io.github.pigaut.orestack.listener;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.*;
import io.github.pigaut.voxel.hologram.display.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.world.*;

public class ChunkLoadListener implements Listener {

    private final OrestackPlugin plugin;

    public ChunkLoadListener(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        for (Generator generator : plugin.getGenerators().getAllGenerators()) {
            if (SpigotLibs.areChunksEqual(chunk, generator.getOrigin().getChunk())) {
                final HologramDisplay hologram = generator.getCurrentHologram();
                if (hologram != null && !hologram.exists()) {
                    hologram.spawn();
                }
            }
        }
    }

}
