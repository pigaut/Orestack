package io.github.pigaut.orestack.generator.phase;

import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.data.structure.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorPhase {

    private final GrowthState state;
    private final StructureTemplate structure;
    private final List<Material> decorativeBlocks;
    private final boolean dropItems;
    private final boolean dropExp;
    private final Amount toolDamage;
    private final boolean idle;
    private final int growthTime;
    private final @Nullable Double growthChance;
    private final @Nullable Double health;
    private final int clickCooldown;
    private final int hitCooldown;
    private final int harvestCooldown;
    private final @Nullable HologramTemplate hologramTemplate;
    private final @Nullable Function onBreak;
    private final @Nullable Function onGrowth;
    private final @Nullable Function onClick;
    private final @Nullable Function onHit;
    private final @Nullable Function onHarvest;
    private final @Nullable Function onDestroy;

    public GeneratorPhase(@NotNull GrowthState state, @NotNull StructureTemplate structure, List<Material> decorativeBlocks,
                          boolean dropItems, boolean dropExp, Amount toolDamage, boolean idle, int growthTime, @Nullable Double growthChance,
                          @Nullable Double health, int clickCooldown, int hitCooldown, int harvestCooldown, @Nullable HologramTemplate hologramTemplate,
                          @Nullable Function onBreak, @Nullable Function onGrowth, @Nullable Function onClick,
                          @Nullable Function onHit, @Nullable Function onHarvest, @Nullable Function onDestroy) {
        this.state = state;
        this.structure = structure;
        this.decorativeBlocks = decorativeBlocks;
        this.dropItems = dropItems;
        this.dropExp = dropExp;
        this.toolDamage = toolDamage;
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
        this.hologramTemplate = hologramTemplate;
    }

    public boolean growsInstantly() {
        return growthTime == 0;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public @NotNull StructureTemplate getStructureTemplate() {
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

    public Amount getToolDamage() {
        return toolDamage;
    }

    public @Nullable Double getMaxHealth() {
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

    public GrowthState getState() {
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

    public @Nullable HologramTemplate getHologramTemplate() {
        return hologramTemplate;
    }

    @Override
    public String toString() {
        return "GeneratorPhase{" +
                "state=" + state +
                ", structure=" + structure +
                ", decorativeBlocks=" + decorativeBlocks +
                ", dropItems=" + dropItems +
                ", dropExp=" + dropExp +
                ", toolDamage=" + toolDamage +
                ", idle=" + idle +
                ", growthTime=" + growthTime +
                ", growthChance=" + growthChance +
                ", health=" + health +
                ", clickCooldown=" + clickCooldown +
                ", hitCooldown=" + hitCooldown +
                ", harvestCooldown=" + harvestCooldown +
                ", hologramTemplate=" + hologramTemplate +
                ", onBreak=" + onBreak +
                ", onGrowth=" + onGrowth +
                ", onClick=" + onClick +
                ", onHit=" + onHit +
                ", onHarvest=" + onHarvest +
                ", onDestroy=" + onDestroy +
                '}';
    }

}
