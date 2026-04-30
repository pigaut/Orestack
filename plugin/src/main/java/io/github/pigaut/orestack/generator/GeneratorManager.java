package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.generator.instanced.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class GeneratorManager extends Manager {

    private final OrestackPlugin plugin;

    private final Set<GlobalGenerator> globalGenerators = new HashSet<>();
    private final Map<Location, GlobalGenerator> globalGeneratorsByBlocks = new ConcurrentHashMap<>();

    private final Set<VirtualGenerator> virtualGenerators = new HashSet<>();
    private final Map<Location, VirtualGenerator> virtualGeneratorsByBlocks = new ConcurrentHashMap<>();

    private final Map<Object, List<BlockState>> removedBlocksByGenerator = new HashMap<>();

    public GeneratorManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        for (GlobalGenerator generator : globalGenerators) {
            generator.cleanup();

            List<BlockState> removedBlocks = removedBlocksByGenerator.remove(generator);
            if (plugin.getSettings().isRestoreBlocksOnRemove() && removedBlocks != null) {
                for (BlockState removedBlock : removedBlocks) {
                    removedBlock.update(true, false);
                }
            }
        }

        for (VirtualGenerator generator : virtualGenerators) {
            generator.cleanupAll();

            List<BlockState> removedBlocks = removedBlocksByGenerator.remove(generator);
            if (plugin.getSettings().isRestoreBlocksOnRemove() && removedBlocks != null) {
                for (BlockState removedBlock : removedBlocks) {
                    removedBlock.update(true, false);
                }
            }
        }

    }

    @Override
    public void loadData() {
        globalGenerators.clear();
        virtualGenerators.clear();
        globalGeneratorsByBlocks.clear();
        virtualGeneratorsByBlocks.clear();
        removedBlocksByGenerator.clear();
        GeneratorRepository.loadGenerators();
    }

    @Override
    public void saveData() {
        GeneratorRepository.saveGenerators();
    }

    @Override
    public boolean isAutoSave() {
        return true;
    }

    public void registerGenerator(String worldId, int x, int y, int z, String generatorName, String rotationData, int phase, boolean global) throws GeneratorCreateException {
        World world = Bukkit.getWorld(UUID.fromString(worldId));
        if (world == null) {
            throw new GeneratorCreateException(worldId, x, y, z, "world not found");
        }

        String worldName = world.getName();
        GeneratorTemplate template = plugin.getGeneratorTemplate(generatorName);
        if (template == null) {
            throw new GeneratorCreateException(worldName, x, y, z, "generator template not found");
        }

        Location origin = new Location(world, x, y, z);
        Rotation rotation = ParseUtil.parseEnumOrNull(Rotation.class, rotationData);
        if (rotation == null) {
            rotation = Rotation.NONE;
            logger.warning(String.format("Failed to load rotation of generator at %s, %d, %d, %d. Default rotation (NONE) has been applied.",
                    worldName, x, y, z));
        }

        int maxPhase = template.getMaxPhase();
        if (phase > maxPhase) {
            logger.warning(String.format("Failed to load phase of generator at %s, %d, %d, %d. Maximum phase (" + maxPhase + ") has been applied.",
                    worldName, x, y, z));
        }

        for (Block block : template.getOccupiedBlocks(origin, rotation)) {
            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException(world.getName(), x, y, z);
            }
        }

        if (!global && !plugin.getVirtualStructures().isSupported()) {
            throw new VirtualGeneratorUnsupportedException();
        }

        int finalPhase = Math.min(phase, template.getMaxPhase());
        Rotation finalRotation = rotation;
        plugin.getScheduler().runTask(() -> {
            try {
                if (global) {
                    GlobalGenerator.create(template, origin, finalRotation, finalPhase);
                } else {
                    VirtualGenerator.create(template, origin, finalRotation);
                }
            } catch (GeneratorOverlapException | VirtualGeneratorUnsupportedException ignored) {
                //Block overlaps are checked before scheduling
            }
        });
    }

    public Collection<GlobalGenerator> getAllGlobal() {
        return new ArrayList<>(globalGenerators);
    }

    public Collection<VirtualGenerator> getAllVirtual() {
        return new ArrayList<>(virtualGenerators);
    }

    public boolean isGenerator(@NotNull Location location) {
        return globalGeneratorsByBlocks.containsKey(location);
    }

    public boolean isGlobalGenerator(@NotNull Location location) {
        return globalGeneratorsByBlocks.containsKey(location);
    }

    public boolean isVirtualGenerator(@NotNull Location location) {
        return virtualGeneratorsByBlocks.containsKey(location);
    }

    public @Nullable GlobalGenerator getGlobalGenerator(@NotNull Location location) {
        return globalGeneratorsByBlocks.get(location);
    }

    public @Nullable VirtualGenerator getVirtualGenerator(@NotNull Location location) {
        return virtualGeneratorsByBlocks.get(location);
    }

    public @Nullable InstancedGenerator getPlayerGenerator(@NotNull Player player, @NotNull Location location) {
        VirtualGenerator virtualGenerator = getVirtualGenerator(location);
        if (virtualGenerator == null) {
            return null;
        }
        return virtualGenerator.getInstance(player);
    }

    public @Nullable Generator getGenerator(@Nullable Player player, @NotNull Location location) {
        if (globalGeneratorsByBlocks.containsKey(location)) {
            return globalGeneratorsByBlocks.get(location);
        }

        if (player == null) {
            return null;
        }

        VirtualGenerator generator = virtualGeneratorsByBlocks.get(location);
        return generator != null ? generator.getInstance(player) : null;
    }

    public void registerGenerator(@NotNull GlobalGenerator generator) throws GeneratorOverlapException {
        GeneratorTemplate template = generator.getTemplate();

        List<BlockState> removedBlocks = new ArrayList<>();
        for (Block block : template.getOccupiedBlocks(generator.getOrigin(), generator.getRotation())) {
            if (isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException();
            }
            removedBlocks.add(block.getState());
        }

        globalGenerators.add(generator);

        for (Block block : generator.getAllOccupiedBlocks()) {
            globalGeneratorsByBlocks.put(block.getLocation(), generator);
        }

        if (plugin.getSettings().isRestoreBlocksOnRemove()) {
            this.removedBlocksByGenerator.put(generator, removedBlocks);
        }
    }

    public void registerGenerator(@NotNull VirtualGenerator generator) throws GeneratorOverlapException {
        GeneratorTemplate template = generator.getTemplate();

        List<BlockState> removedBlocks = new ArrayList<>();
        for (Block block : template.getOccupiedBlocks(generator.getOrigin(), generator.getRotation())) {
            if (isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException();
            }
            removedBlocks.add(block.getState());
        }

        virtualGenerators.add(generator);

        for (Block block : generator.getAllOccupiedBlocks()) {
            virtualGeneratorsByBlocks.put(block.getLocation(), generator);
            block.setType(Material.AIR, false);
        }

        for (Block solidBlock : generator.getAllOccupiedSolidBlocks()) {
            solidBlock.setType(Material.BARRIER, false);
        }

        if (plugin.getSettings().isRestoreBlocksOnRemove()) {
            removedBlocksByGenerator.put(generator, removedBlocks);
        }
    }

    public void unregisterGenerator(@NotNull GlobalGenerator generator) {
        globalGenerators.remove(generator);
        
        for (Block block : generator.getAllOccupiedBlocks()) {
            globalGeneratorsByBlocks.remove(block.getLocation());
        }

        List<BlockState> removedBlocks = this.removedBlocksByGenerator.remove(generator);
        if (plugin.getSettings().isRestoreBlocksOnRemove() && removedBlocks != null) {
            for (BlockState removedBlock : removedBlocks) {
                removedBlock.update(true, false);
            }
        }
    }

    public void unregisterGenerator(@NotNull VirtualGenerator generator) {
        virtualGenerators.remove(generator);

        for (Block block : generator.getAllOccupiedBlocks()) {
            globalGeneratorsByBlocks.remove(block.getLocation());
        }

        List<BlockState> removedBlocks = this.removedBlocksByGenerator.remove(generator);
        if (plugin.getSettings().isRestoreBlocksOnRemove() && removedBlocks != null) {
            for (BlockState removedBlock : removedBlocks) {
                removedBlock.update(true, false);
            }
        }
    }

}
