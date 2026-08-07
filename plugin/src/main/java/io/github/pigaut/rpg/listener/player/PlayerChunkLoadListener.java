package io.github.pigaut.rpg.listener.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.structure.virtual.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.structure.virtual.*;
import io.github.pigaut.rpg.plugin.*;
import io.papermc.paper.event.packet.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

// Paper only
public class PlayerChunkLoadListener implements Listener {

    private final EnhancedPlugin plugin;

    public PlayerChunkLoadListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(PlayerChunkLoadEvent event) {
        Player player = event.getPlayer();
        ChunkPosition chunkCoords = ChunkPosition.fromChunk(event.getChunk());
        for (VirtualStructure virtualStructure : plugin.getVirtualStructures().getAll()) {
            if (!virtualStructure.isViewer(player)) {
                continue;
            }

            virtualStructure.send(player, chunkCoords);
        }
    }

}
