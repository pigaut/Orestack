package io.github.pigaut.rpg.module.mob.spawnpad;

import com.google.common.collect.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobSpawnPadManager extends Manager {

    private final Multimap<UUID, MobSpawnPad> mobSpawnPadsByWorld = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
    private final Queue<MobSpawnPad> spawnQueue = new ArrayDeque<>();
    private final Set<MobSpawnPad> queued = new HashSet<>();
    private Task checkTask;
    private Task spawnTask;

    public MobSpawnPadManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void clear() {
        mobSpawnPadsByWorld.clear();
    }

    @Override
    public void loadData() {
        MobSpawnPadRepository.loadMobSpawnPads(plugin);
    }

    @Override
    public void enable() {
        checkTask = plugin.getScheduler().runTaskTimer(40, () -> {
            for (MobSpawnPad mobSpawnPad : mobSpawnPadsByWorld.values()) {
                if (mobSpawnPad.shouldSpawn() && queued.add(mobSpawnPad)) {
                    spawnQueue.add(mobSpawnPad);
                }
            }
        });

        spawnTask = plugin.getScheduler().runTaskTimer(5, () -> {
            int spawned = 0;
            while (spawned < 5 && !spawnQueue.isEmpty()) {
                MobSpawnPad mobSpawnPad = spawnQueue.poll();
                queued.remove(mobSpawnPad);
                if (mobSpawnPad.shouldSpawn()) {
                    mobSpawnPad.spawnMob();
                    spawned++;
                }
            }
        });
    }

    @Override
    public void disable() {
        if (checkTask != null) {
            checkTask.cancel();
            checkTask = null;
        }
        if (spawnTask != null) {
            spawnTask.cancel();
            spawnTask = null;
        }
        spawnQueue.clear();
    }

    @Override
    public void saveData() {
        MobSpawnPadRepository.saveMobSpawnPads(plugin);
    }

    public @Nullable MobSpawnPad get(@NotNull Location location) {
        for (MobSpawnPad mobSpawnPad : mobSpawnPadsByWorld.values()) {
            if (mobSpawnPad.getLocation().equals(location)) {
                return mobSpawnPad;
            }
        }
        return null;
    }

    public @NotNull Collection<MobSpawnPad> getAll() {
        return new ArrayList<>(mobSpawnPadsByWorld.values());
    }

    public @NotNull Collection<MobSpawnPad> getAll(@NotNull World world) {
        return new ArrayList<>(mobSpawnPadsByWorld.get(world.getUID()));
    }

    public void register(@NotNull MobSpawnPad mobSpawnPad) {
        World world = mobSpawnPad.getWorld();
        mobSpawnPadsByWorld.put(world.getUID(), mobSpawnPad);
    }

    public void unregister(@NotNull MobSpawnPad mobSpawnPad) {
        World world = mobSpawnPad.getWorld();
        mobSpawnPadsByWorld.remove(world.getUID(), mobSpawnPad);
    }

    public void register(@NotNull String worldId, int x, int y, int z, @NotNull String mobName,
                         @Nullable String spawnDelayData, double spawnRange, double activationRange) throws MobSpawnPadCreateException {
        World world = Bukkit.getWorld(UUID.fromString(worldId));
        if (world == null) {
            throw new MobSpawnPadCreateException(worldId, x, y, z, "world not found");
        }

        String worldName = world.getName();
        MobTemplate mobTemplate = plugin.getMobTemplate(mobName);
        if (mobTemplate == null) {
            throw new MobSpawnPadCreateException(worldName, x, y, z, "mob template not found");
        }

        Delay spawnDelay = null;
        if (spawnDelayData != null) {
            spawnDelay = ParseUtil.parseDelayOrNull(spawnDelayData);
            if (spawnDelay == null) {
                logger.warning(String.format("Failed to load spawn delay of mob spawn pad at %s, %d, %d, %d. Default delay has been applied.",
                        worldName, x, y, z));
            }
        }

        Location location = new Location(world, x, y, z);
        MobSpawnPad mobSpawnPad = new MobSpawnPad(plugin, mobTemplate, location, spawnDelay, spawnRange, activationRange);
        register(mobSpawnPad);
    }

}
