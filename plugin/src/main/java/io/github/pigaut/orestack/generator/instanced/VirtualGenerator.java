package io.github.pigaut.orestack.generator.instanced;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.exception.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.data.structure.virtual.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VirtualGenerator {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    private final GeneratorTemplate template;
    private final Location origin;
    private final Rotation rotation;
    private final List<VirtualStructure> structures = new ArrayList<>();
    private final Map<UUID, InstancedGenerator> instances = new HashMap<>();

    private VirtualGenerator(GeneratorTemplate template, Location origin, Rotation rotation) {
        this.template = template;
        this.origin = origin;
        this.rotation = rotation;
    }

    public static @NotNull VirtualGenerator create(@NotNull GeneratorTemplate template, @NotNull Location origin, @NotNull Rotation rotation) throws GeneratorOverlapException, VirtualGeneratorUnsupportedException {
        VirtualGenerator generator = new VirtualGenerator(template, origin, rotation);
        plugin.getGenerators().registerGenerator(generator);
        for (GeneratorPhase phase : template.getPhases()) {
            generator.structures.add(phase.getStructureTemplate().project(origin, rotation));
        }
        plugin.getScheduler().runTaskLater(5, () -> Bukkit.getOnlinePlayers().forEach(generator::addViewer));
        return generator;
    }

    public static @NotNull VirtualGenerator create(@NotNull GeneratorTemplate template, @NotNull Location origin) throws GeneratorOverlapException, VirtualGeneratorUnsupportedException {
        return create(template, origin, Rotation.NONE);
    }

    public @NotNull GeneratorTemplate getTemplate() {
        return template;
    }

    public @NotNull Location getOrigin() {
        return origin;
    }

    public @NotNull Rotation getRotation() {
        return rotation;
    }

    public @NotNull VirtualStructure getVirtualStructure(int phase) {
        return structures.get(phase);
    }

    public @NotNull Set<Block> getAllOccupiedSolidBlocks() {
        return template.getOccupiedSolidBlocks(origin, rotation);
    }

    public @NotNull Set<Block> getAllOccupiedBlocks() {
        return template.getOccupiedBlocks(origin, rotation);
    }

    public int getMaxPhase() {
        return template.getMaxPhase();
    }

    public boolean isViewer(@NotNull Player player) {
        return instances.containsKey(player.getUniqueId());
    }

    public @Nullable InstancedGenerator getInstance(@NotNull Player player) {
        return instances.get(player.getUniqueId());
    }

    public @NotNull InstancedGenerator addViewer(@NotNull Player player) {
        return instances.computeIfAbsent(player.getUniqueId(), playerId -> {
            InstancedGenerator generator = new InstancedGenerator(playerId, this, origin, rotation);
            generator.setPhase(template.getMaxPhase(), true);
            return generator;
        });
    }

    public void removeViewer(@NotNull Player player) {
        removeViewer(player.getUniqueId());
    }

    public void removeViewer(UUID playerId) {
        InstancedGenerator removed = instances.remove(playerId);
        if (removed != null) {
            VirtualStructure structure = getVirtualStructure(removed.getState().getCurrentPhase());
            structure.removeViewer(playerId);
        }
    }

    public void cleanupAll() {
        instances.forEach((playerId, generator) -> {
            generator.removeBlocks();
            generator.cancelGrowth();
        });
    }

    public void removeAll() {
        cleanupAll();
        instances.clear();
        plugin.getGenerators().unregisterGenerator(this);
    }
}
