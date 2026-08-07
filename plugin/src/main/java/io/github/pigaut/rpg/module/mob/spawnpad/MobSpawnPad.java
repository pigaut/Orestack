package io.github.pigaut.rpg.module.mob.spawnpad;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class MobSpawnPad {

    private final EnhancedPlugin plugin;

    private final MobTemplate mobTemplate;
    private final Location location;
    private final Delay spawnDelay;
    private final double activationRange;
    private final double spawnRange;

    private @Nullable Task spawnTask;
    private @Nullable UUID mobId;
    private boolean allowVisible = true;

    public MobSpawnPad(EnhancedPlugin plugin, @NotNull MobTemplate mobTemplate, @NotNull Location location,
                       @Nullable Delay spawnDelay, double spawnRange, double activationRange) {
        this.plugin = plugin;
        this.mobTemplate = mobTemplate;
        this.location = location;
        this.spawnDelay = spawnDelay;
        this.spawnRange = spawnRange;
        this.activationRange = activationRange;
    }

    public boolean isAllowVisible() {
        return allowVisible;
    }

    public void setAllowVisible(boolean allowVisible) {
        this.allowVisible = allowVisible;
    }

    public @NotNull MobTemplate getMobTemplate() {
        return mobTemplate;
    }

    public @NotNull World getWorld() {
        return location.getWorld();
    }

    public @NotNull Location getLocation() {
        return location.clone();
    }

    public @Nullable Delay getSpawnDelay() {
        return spawnDelay;
    }

    public double getActivationRange() {
        return activationRange;
    }

    public double getSpawnRange() {
        return spawnRange;
    }

    public @Nullable UUID getMobId() {
        return mobId;
    }

    public @Nullable Mob getMob() {
        return mobId != null ? plugin.getMob(mobId) : null;
    }

    public void remove() {
        Mob mob = getMob();
        if (mob != null) {
            mob.remove();
        }
        cancelSpawning();
        plugin.getMobSpawnPads().unregister(this);
    }

    public boolean shouldSpawn() {
        return !hasMob() && !isSpawning() && isActivated();
    }

    public boolean isSpawning() {
        return spawnTask != null;
    }

    public void cancelSpawning() {
        if (spawnTask != null) {
            if (!spawnTask.isCancelled()) {
                spawnTask.cancel();
            }
            spawnTask = null;
        }
    }

    public boolean isActivated() {
        return !LocationUtil.getNearbyPlayers(location, activationRange).isEmpty();
    }

    public boolean hasMob() {
        return mobId != null && Bukkit.getEntity(mobId) != null;
    }

    public void spawnMob() {
        if (!shouldSpawn()) {
            return;
        }

        Location spawnLocation = location.clone();
        if (spawnRange > 0) {
            ThreadLocalRandom random = ThreadLocalRandom.current();

            double offsetX = random.nextDouble(-spawnRange, spawnRange);
            double offsetZ = random.nextDouble(-spawnRange, spawnRange);

            spawnLocation.add(offsetX, 0, offsetZ);

            int highestY = spawnLocation.getWorld().getHighestBlockYAt(spawnLocation);
            if (Math.abs(location.getY() - highestY) <= spawnRange) {
                spawnLocation.setY(highestY + 1);
            }
        }

        if (spawnDelay == null) {
            Mob spawnedMob = mobTemplate.spawn(spawnLocation, this);
            mobId = spawnedMob.getEntityId();
            return;
        }

        spawnTask = plugin.getScheduler().runTaskLater(spawnDelay.toTicks(), () -> {
            Mob spawnedMob = mobTemplate.spawn(spawnLocation, this);
            mobId = spawnedMob.getEntityId();
            spawnTask = null;
        });
    }

}
