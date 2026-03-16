package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.stage.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import io.github.pigaut.voxel.core.structure.Structure;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
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

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, @NotNull Rotation rotation, int stage) throws GeneratorOverlapException {
        Generator generator = new Generator(template, origin, rotation);
        plugin.getGenerators().registerGenerator(generator);
        GeneratorUtil.init(generator, stage);
        return generator;
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin, @NotNull Rotation rotation) throws GeneratorOverlapException {
        return create(template, origin, rotation, template.getMaxStage());
    }

    public static @NotNull Generator create(@NotNull GeneratorTemplate template, @NotNull Location origin) throws GeneratorOverlapException {
        return create(template, origin, Rotation.NONE);
    }

    public @NotNull String getName() {
        return template.getName();
    }

    public @NotNull GeneratorStage getStage() {
        return template.getStage(state.getCurrentStage());
    }

    public void setStage(int stage) {
        GeneratorUtil.setStage(this, stage);
    }

    public @NotNull GeneratorState getState() {
        return state;
    }

    public @NotNull GeneratorStage getStage(int stage) {
        return template.getStage(stage);
    }

    public boolean isFullyGrown() {
        return state.getCurrentStage() >= template.getMaxStage();
    }

    public boolean isDepleted() {
        return state.getCurrentStage() <= 0;
    }

    public int getMaxStage() {
        return template.getMaxStage();
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
        GeneratorStage stage = getStage();
        return stage.getStructureTemplate().getOccupiedBlocks(origin, rotation);
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
            template.getLastStage().getStructureTemplate().place(origin, rotation);
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
        GeneratorUtil.setStage(this, template.getMaxStage());
    }

    public void damage(@NotNull PlayerState damageDealer, double amount) {
        GeneratorUtil.damage(this, damageDealer, amount);
    }

    public @Nullable Integer getNextGrowthStage(int growthAmount) {
        int stageIndex = state.getCurrentStage() + growthAmount;

        int maxStage = getMaxStage();
        if (stageIndex >= maxStage) {
            return maxStage;
        }

        GeneratorStage currentStage = this.getStage();
        for (int i = stageIndex; i < maxStage; i++) {
            GeneratorStage nextStage = template.getStage(i);
            if (nextStage.growsInstantly()) {
                continue;
            }

            Double growthChance = nextStage.getGrowthChance();
            if (growthChance != null && !Probability.test(growthChance)) {
                if (currentStage.getState().isHarvestable()) {
                    return null;
                }
                continue;
            }
            return i;
        }

        return maxStage;
    }

    public int getNextHarvestingStage() {
        int stageIndex = state.getCurrentStage();
        GeneratorStage stage;
        do {
            stageIndex--;
            stage = template.getStage(stageIndex);
        }
        while (stage.getState().isTransitional());
        return stageIndex;
    }

}
