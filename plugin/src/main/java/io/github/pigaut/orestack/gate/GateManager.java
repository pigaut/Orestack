package io.github.pigaut.orestack.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.exception.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class GateManager extends Manager {

    private final OrestackPlugin plugin;
    private final Set<Gate> gates = new HashSet<>();
    private final Map<Location, Gate> gateBlocks = new ConcurrentHashMap<>();
    private final Map<Gate, List<BlockState>> removedBlocks = new HashMap<>();

    public GateManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean isAutoSave() {
        return true;
    }

    @Override
    public void clear() {
        gates.clear();
        gateBlocks.clear();
    }

    @Override
    public void loadData() {
        GateRepository.loadGates();
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        for (Gate gate : gates) {
            gate.cleanup();
            List<BlockState> removedBlocks = this.removedBlocks.remove(gate);
            if (plugin.getSettings().isRestoreBlocksOnRemove() && removedBlocks != null) {
                for (BlockState removedBlock : removedBlocks) {
                    removedBlock.update(true, false);
                }
            }
        }

    }

    @Override
    public void saveData() {
        GateRepository.saveGates();
    }

    public void registerGate(String worldId, int x, int y, int z, String gateName, String rotationData, int phase, String transitionData) throws GateCreateException {
        World world = Bukkit.getWorld(UUID.fromString(worldId));
        if (world == null) {
            throw new GateCreateException(worldId, x, y, z, "world not found (deleted/renamed/failed to load)");
        }

        String worldName = world.getName();
        GateTemplate template = plugin.getGateTemplate(gateName);
        if (template == null) {
            throw new GateNotExistsException(worldName, x, y, z);
        }

        Location origin = new Location(world, x, y, z);
        Rotation rotation = ParseUtil.parseEnumOrNull(Rotation.class, rotationData);
        if (rotation == null) {
            rotation = Rotation.NONE;
            logger.warning(String.format("Failed to load rotation of gate at %s, %d, %d, %d. Default rotation (NONE) has been applied.",
                    worldName, x, y, z));
        }

        GateTransition transition = transitionData != null ?
                ParseUtil.parseEnumOrNull(GateTransition.class, transitionData) : null;

        if (transition == null) {
            transition = GateTransition.NONE;
            logger.warning(String.format("Failed to load transition of gate at %s, %d, %d, %d. Transition (none) has been applied.",
                    worldName, x, y, z));
        }

        int maxPhase = template.getMaxPhase();
        if (phase > maxPhase) {
            logger.warning(String.format("Failed to load phase of gate at %s, %d, %d, %d. Maximum phase (" + maxPhase + ") has been applied.",
                    worldName, x, y, z));
        }

        if (transition == GateTransition.NONE && (phase != 0 && phase != maxPhase)) {
            phase = 0;
            logger.warning(String.format("Failed to load transition/phase of gate at %s, %d, %d, %d. Minimum phase (0) has been applied.",
                    worldName, x, y, z));
        }

        for (Block block : template.getOccupiedBlocks(origin, rotation)) {
            if (isGate(block.getLocation())) {
                throw new GateOverlapException(worldName, x, y, z);
            }

            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                throw new GateConflictException(worldName, x, y, z);
            }
        }

        int finalPhase = Math.min(phase, template.getMaxPhase());
        Rotation finalRotation = rotation;
        GateTransition finalTransition = transition;
        plugin.getScheduler().runTask(() -> {
            try {
                Gate.create(template, origin, finalRotation, finalPhase, finalTransition);
            } catch (GateCreateException ignored) {
                //Block overlaps are checked before scheduling
            }
        });
    }

    public Collection<Gate> getAll() {
        return new ArrayList<>(gates);
    }

    public boolean isGate(@NotNull Location location) {
        return gateBlocks.containsKey(location);
    }

    public @Nullable Gate getGate(@NotNull Location location) {
        return gateBlocks.get(location);
    }

    public boolean isRegistered(@NotNull Gate gate) {
        return gates.contains(gate);
    }

    public void registerGate(@NotNull Gate gate) throws GateCreateException {
        GateTemplate template = gate.getTemplate();

        List<BlockState> removedBlocks = new ArrayList<>();
        for (Block block : template.getOccupiedBlocks(gate.getOrigin(), gate.getRotation())) {
            Location blockLocation = block.getLocation();

            if (isGate(block.getLocation())) {
                throw new GateOverlapException(blockLocation);
            }

            if (plugin.getGenerators().isGenerator(blockLocation)) {
                throw new GateConflictException(blockLocation);
            }

            removedBlocks.add(block.getState());
        }

        gates.add(gate);

        for (Block block : gate.getOccupiedBlocks()) {
            gateBlocks.put(block.getLocation(), gate);
        }

        if (plugin.getSettings().isRestoreBlocksOnRemove()) {
            this.removedBlocks.put(gate, removedBlocks);
        }
    }

    public void unregisterGate(@NotNull Gate gate) {
        gates.remove(gate);

        for (Block block : gate.getOccupiedBlocks()) {
            gateBlocks.remove(block.getLocation());
        }

        List<BlockState> removedBlocks = this.removedBlocks.remove(gate);
        if (plugin.getSettings().isRestoreBlocksOnRemove() && removedBlocks != null) {
            for (BlockState removedBlock : removedBlocks) {
                removedBlock.update(true, false);
            }
        }
    }

}
