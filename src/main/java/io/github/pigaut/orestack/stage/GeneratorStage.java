package io.github.pigaut.orestack.stage;

import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.function.interact.block.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.placeholder.*;
import org.jetbrains.annotations.*;

public class GeneratorStage implements PlaceholderSupplier {

    private final GeneratorTemplate generator;
    private final GeneratorState state;
    private final BlockStructure structure;
    private final boolean dropItems;
    private final @Nullable Integer expToDrop;
    private final boolean regrow;
    private final int growthTime;
    private final @Nullable Double growthChance;
    private final @Nullable Function onBreak;
    private final @Nullable Function onGrowth;
    private final @Nullable BlockClickFunction onClick;
    private final @Nullable Hologram hologram;

    public GeneratorStage(@NotNull GeneratorTemplate generator, @NotNull GeneratorState state,
                          @NotNull BlockStructure structure, boolean dropItems, @Nullable Integer expToDrop,
                          boolean regrow, int growthTime, @Nullable Double growthChance,
                          @Nullable Function onBreak, @Nullable Function onGrowth,
                          @Nullable BlockClickFunction onClick, @Nullable Hologram hologram) {
        this.generator = generator;
        this.state = state;
        this.structure = structure;
        this.dropItems = dropItems;
        this.expToDrop = expToDrop;
        this.regrow = regrow;
        this.growthTime = growthTime;
        this.growthChance = growthChance;
        this.onBreak = onBreak;
        this.onGrowth = onGrowth;
        this.onClick = onClick;
        this.hologram = hologram;
    }

    public @NotNull GeneratorTemplate getGenerator() {
        return generator;
    }

    public boolean isDropItems() {
        return dropItems;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public @Nullable Integer getExpToDrop() {
        return expToDrop;
    }

    public @NotNull BlockStructure getStructure() {
        return structure;
    }

    public GeneratorState getState() {
        return state;
    }

    public @Nullable Double getGrowthChance() {
        return growthChance;
    }

    public @Nullable Function getBreakFunction() {
        return onBreak;
    }

    public @Nullable Function getGrowthFunction() {
        return onGrowth;
    }

    public @Nullable BlockClickFunction getClickFunction() {
        return onClick;
    }

    public @Nullable Hologram getHologram() {
        return hologram;
    }

    public boolean shouldRegrow() {
        return regrow;
    }

    @Override
    public @NotNull Placeholder[] getPlaceholders() {
        return new Placeholder[]{
                Placeholder.of("%generator%", generator.getName()),
                Placeholder.of("%generator_stage%", generator.indexOfStage(this)),
                Placeholder.of("%generator_stages%", generator.getMaxStage()),
                Placeholder.of("%generator_state%", state.toString().toLowerCase()),
                Placeholder.of("%generator_growth_seconds%", growthTime / 20),
                Placeholder.of("%generator_growth_minutes%", growthTime / 1200)
        };
    }

    @Override
    public String toString() {
        return "GeneratorStage{" + "generator=" + generator.getName() + ", state=" + state + ", dropItems=" + dropItems + ", growthTime=" + growthTime + ", growthChance=" + growthChance + ", onBreak=" + onBreak + ", onGrowth=" + onGrowth + ", onClick=" + onClick + '}';
    }

}
