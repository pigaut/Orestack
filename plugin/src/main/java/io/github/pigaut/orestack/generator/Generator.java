package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.structure.Structure;
import io.github.pigaut.voxel.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Generator {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    protected final GeneratorTemplate template;
    protected final Location origin;
    protected final Rotation rotation;
    private final GeneratorState state;

    public Generator(GeneratorTemplate template, Location origin, Rotation rotation) {
        this.template = template;
        this.origin = origin;
        this.rotation = rotation;
        this.state = new GeneratorState(this);
    }

    public Generator(GeneratorTemplate template, Location origin, Rotation rotation, GeneratorState state) {
        this.template = template;
        this.origin = origin;
        this.rotation = rotation;
        this.state = state;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, @NotNull Rotation rotation, int phase) throws GeneratorOverlapException {
        Generator generator = new Generator(template, origin, rotation);
        plugin.getGenerators().registerGenerator(generator);
        GeneratorUtil.init(generator, phase);
        return generator;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, @NotNull Rotation rotation) throws GeneratorOverlapException {
        return create(template, origin, rotation, template.getMaxPhase());
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin) throws GeneratorOverlapException {
        return create(template, origin, Rotation.NONE);
    }

    public @NotNull String getName() {
        return template.getName();
    }

    public @NotNull GeneratorPhase getPhase() {
        return template.getPhase(state.getCurrentPhase());
    }

    public void setPhase(int phase) {
        GeneratorUtil.setPhase(this, phase);
    }

    public @NotNull GeneratorState getState() {
        return state;
    }

    public @NotNull GeneratorPhase getPhase(int phase) {
        return template.getPhase(phase);
    }

    public boolean isFullyGrown() {
        return state.getCurrentPhase() >= template.getMaxPhase();
    }

    public boolean isDepleted() {
        return state.getCurrentPhase() <= 0;
    }

    public int getMaxPhase() {
        return template.getMaxPhase();
    }

    public @NotNull GeneratorTemplate getTemplate() {
        return template;
    }

    public @NotNull Location getOrigin() {
        return origin.clone();
    }

    public @NotNull Rotation getRotation() {
        return rotation;
    }

    public @NotNull Block getBlock() {
        return origin.getBlock();
    }

    public Set<Block> getOccupiedBlocks() {
        GeneratorPhase phase = getPhase();
        return phase.getStructureTemplate().getOccupiedBlocks(origin, rotation);
    }

    public Set<Block> getAllOccupiedBlocks() {
        return template.getOccupiedBlocks(origin, rotation);
    }

    public boolean isValid() {
        Structure structure = state.getStructure();

        for (Block block : structure.getOccupiedBlocks()) {
            Generator generator = plugin.getGenerator(block.getLocation());
            if (!this.equals(generator)) {
                return false;
            }
        }

        return structure.isPlaced();
    }

    public void remove() {
        state.cancelGrowthTask();
        state.removeBlocks();
        state.removeHologram();

        if (plugin.getSettings().isKeepBlocksOnRemove()) {
            template.getLastPhase().getStructureTemplate().place(origin, rotation);
        }

        plugin.getGenerators().unregisterGenerator(this);
    }

    public void harvest() {
        GeneratorUtil.harvest(this);
    }

    public void grow(int growthAmount) {
        GeneratorUtil.grow(this, growthAmount);
    }

    public void grow() {
        GeneratorUtil.grow(this, 1);
    }

    public void regrow() {
        GeneratorUtil.setPhase(this, template.getMaxPhase());
    }

    public void damage(@NotNull Player player, @NotNull Context context, double amount) {
        GeneratorUtil.damage(this, player, context, amount);
    }

    public int getTicksToNextPhase() {
        return state.getTicksToNextPhase();
    }

    public int getTicksToRegrownPhase() {
        return state.getTicksToRegrownPhase();
    }

    public @Nullable Double getTotalHealth() {
        return state.getHealth();
    }

    public @Nullable Double getHealth() {
        return state.getPhaseHealth();
    }

    public @Nullable Integer getNextGrowthPhase(int growthAmount) {
        int phaseIndex = state.getCurrentPhase() + growthAmount;

        int maxPhase = getMaxPhase();
        if (phaseIndex >= maxPhase) {
            return maxPhase;
        }

        GeneratorPhase currentPhase = this.getPhase();
        for (int i = phaseIndex; i < maxPhase; i++) {
            GeneratorPhase nextPhase = template.getPhase(i);
            if (nextPhase.growsInstantly()) {
                continue;
            }

            Double growthChance = nextPhase.getGrowthChance();
            if (growthChance != null && !Probability.test(growthChance)) {
                if (currentPhase.getState().isHarvestable()) {
                    return null;
                }
                continue;
            }
            return i;
        }

        return maxPhase;
    }

    public int getNextHarvestingPhase() {
        int phaseIndex = state.getCurrentPhase();
        GeneratorPhase phase;
        do {
            phaseIndex--;
            phase = template.getPhase(phaseIndex);
        }
        while (phase.getState().isTransitional());
        return phaseIndex;
    }

}
