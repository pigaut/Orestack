package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.core.structure.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorStage {

    private final GeneratorTemplate generator;
    private final GeneratorState state;
    private final BlockStructure structure;
    private final List<Material> decorativeBlocks;
    private final boolean dropItems;
    private final boolean dropExp;
    private final boolean idle;
    private final int growthTime;
    private final @Nullable Double growthChance;
    private final @Nullable Double health;
    private final int clickCooldown;
    private final int hitCooldown;
    private final int harvestCooldown;
    private final @Nullable Hologram hologram;
    private final @Nullable Function onBreak;
    private final @Nullable Function onGrowth;
    private final @Nullable Function onClick;
    private final @Nullable Function onHit;
    private final @Nullable Function onHarvest;
    private final @Nullable Function onDestroy;

    public GeneratorStage(@NotNull GeneratorTemplate generator, @NotNull GeneratorState state,
                          @NotNull BlockStructure structure, List<Material> decorativeBlocks, boolean dropItems,
                          boolean dropExp, boolean idle, int growthTime, @Nullable Double growthChance, @Nullable Double health,
                          int clickCooldown, int hitCooldown, int harvestCooldown, @Nullable Hologram hologram,
                          @Nullable Function onBreak, @Nullable Function onGrowth, @Nullable Function onClick,
                          @Nullable Function onHit, @Nullable Function onHarvest, @Nullable Function onDestroy) {
        this.generator = generator;
        this.state = state;
        this.structure = structure;
        this.decorativeBlocks = decorativeBlocks;
        this.dropItems = dropItems;
        this.dropExp = dropExp;
        this.idle = idle;
        this.growthTime = growthTime;
        this.growthChance = growthChance;
        this.health = health;
        this.clickCooldown = clickCooldown;
        this.hitCooldown = hitCooldown;
        this.harvestCooldown = harvestCooldown;
        this.onBreak = onBreak;
        this.onGrowth = onGrowth;
        this.onClick = onClick;
        this.onHit = onHit;
        this.onHarvest = onHarvest;
        this.onDestroy = onDestroy;
        this.hologram = hologram;
    }

    public @NotNull GeneratorTemplate getGenerator() {
        return generator;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public @NotNull BlockStructure getStructure() {
        return structure;
    }

    public List<Material> getDecorativeBlocks() {
        return new ArrayList<>(decorativeBlocks);
    }

    public boolean isDropItems() {
        return dropItems;
    }

    public boolean isDropExp() {
        return dropExp;
    }

    public @Nullable Double getHealth() {
        return health;
    }

    public int getClickCooldown() {
        return clickCooldown;
    }

    public int getHarvestCooldown() {
        return harvestCooldown;
    }

    public int getHitCooldown() {
        return hitCooldown;
    }

    public boolean isIdle() {
        return idle;
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

    public @Nullable Function getClickFunction() {
        return onClick;
    }

    public @Nullable Function getHitFunction() {
        return onHit;
    }

    public @Nullable Function getHarvestFunction() {
        return onHarvest;
    }

    public @Nullable Function getDestroyFunction() {
        return onDestroy;
    }

    public @Nullable Hologram getHologram() {
        return hologram;
    }

    @Override
    public String toString() {
        return "GeneratorStage{" +
                "generator=" + generator.getName() +
                ", state=" + state +
                ", structure=" + structure +
                ", growthTime=" + growthTime +
                ", growthChance=" + growthChance +
                ", onBreak=" + onBreak +
                ", onGrowth=" + onGrowth +
                ", onClick=" + onClick +
                ", hologram=" + hologram +
                '}';
    }

}
