package io.github.pigaut.orestack.generator.instanced;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.data.structure.virtual.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InstancedGenerator extends BasicGenerator {

    private final UUID playerId;
    private final VirtualGenerator generator;
    private VirtualStructure structure;

    InstancedGenerator(UUID playerId, VirtualGenerator generator, Location origin, Rotation rotation) {
        super(generator.getTemplate(), origin, rotation);
        this.playerId = playerId;
        this.generator = generator;
    }

    public @NotNull UUID getPlayerId() {
        return playerId;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public @NotNull VirtualStructure getStructure() {
        return structure;
    }

    public void removeBlocks() {
        Player player = Bukkit.getPlayer(playerId);
        if (structure != null && player != null) {
            structure.removeViewer(player);
        }
    }

    public void setStructure(@NotNull VirtualStructure newStructure, @NotNull Player player) {
        if (structure != null) {
            structure.removeViewer(player);
        }
        this.structure = newStructure;
        newStructure.addViewer(player);
    }

    @Override
    public void cleanup() {
        cancelGrowth();
        removeBlocks();
    }

    @Override
    public void remove() {
        generator.removeAll();
    }

    @Override
    public void mineBlock(@NotNull Player player, @NotNull Block block, int expToDrop) {
        GeneratorUtil.callGeneratorMineEvent(this, state.getCurrentPhase(), player, block, expToDrop);
        structure.sendAll(player);
    }

    @Override
    public void setPhase(int phaseIndex, boolean growing) {
        if (phaseIndex < 0 || phaseIndex > getMaxPhase()) {
            return;
        }

        Player player = Bukkit.getPlayer(playerId);
        if (player == null) {
            return;
        }

        GeneratorPhase newPhase = getPhase(phaseIndex);
        Context context = Context.builder()
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withBlock(origin.getBlock())
                .with(Generator.class, this)
                .build();

        state.setCurrentPhase(phaseIndex);
        state.setHealth(newPhase.getMaxHealth());
        setStructure(generator.getVirtualStructure(phaseIndex), player);

        if (growing) {
            Function growthFunction = newPhase.getGrowthFunction();
            if (growthFunction != null) {
                growthFunction.run(context);
            }
        }
    }

}
