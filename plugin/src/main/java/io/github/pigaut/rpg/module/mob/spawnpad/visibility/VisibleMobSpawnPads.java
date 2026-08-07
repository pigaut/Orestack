package io.github.pigaut.rpg.module.mob.spawnpad.visibility;

import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VisibleMobSpawnPads {

    private final EnhancedPlugin plugin;
    private final UUID playerId;

    private final Set<Location> currentlyVisible = new HashSet<>();
    private Task replaceTask;

    public VisibleMobSpawnPads(@NotNull EnhancedPlugin plugin, @NotNull UUID playerId) {
        this.plugin = plugin;
        this.playerId = playerId;
    }

    public void start() {
        replaceTask = plugin.getScheduler().runTaskTimer(1, 5, () -> {
            Player player = Bukkit.getPlayer(playerId);
            if (player == null) {
                stop();
                return;
            }

            Location center = player.getLocation();
            Set<Location> newPadsInRange = new HashSet<>();
            double radiusSquared = 50.0 * 50.0;

            for (MobSpawnPad mobSpawnPad : plugin.getMobSpawnPads().getAll(player.getWorld())) {
                if (!mobSpawnPad.isAllowVisible()) {
                    continue;
                }
                Location blockLocation = mobSpawnPad.getLocation();
                if (blockLocation.distanceSquared(center) <= radiusSquared) {
                    newPadsInRange.add(blockLocation);

                    if (!currentlyVisible.contains(blockLocation)) {
                        BlockData blockData = Bukkit.createBlockData(Material.SPAWNER);
                        player.sendBlockChange(blockLocation, blockData);
                        currentlyVisible.add(blockLocation);
                    }
                }
            }

            Iterator<Location> it = currentlyVisible.iterator();
            while (it.hasNext()) {
                Location location = it.next();
                if (!newPadsInRange.contains(location)) {
                    player.sendBlockChange(location, location.getBlock().getBlockData());
                    it.remove();
                }
            }
        });
    }

    public void stop() {
        if (replaceTask != null) {
            replaceTask.cancel();
            replaceTask = null;
        }

        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            currentlyVisible.clear();
            return;
        }

        for (Location location : currentlyVisible) {
            player.sendBlockChange(location, location.getBlock().getBlockData());
        }

        currentlyVisible.clear();
    }

}
