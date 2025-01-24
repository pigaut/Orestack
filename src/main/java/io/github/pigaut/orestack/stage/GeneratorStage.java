package io.github.pigaut.orestack.stage;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.function.interact.block.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.meta.placeholder.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorStage implements PlaceholderSupplier {

    private final Generator generator;
    private final GeneratorState state;
    private final Material resource;
    private final @Nullable Integer age;
    private final @Nullable BlockFace facingDirection;
    private final boolean dropItems;
    private final int growthTime;
    private final @Nullable Double growthChance;
    private final @Nullable Function onBreak;
    private final @Nullable Function onGrowth;
    private final @Nullable BlockClickFunction onClick;
    private final @Nullable Hologram hologram;

    public GeneratorStage(@NotNull Generator generator, @NotNull GeneratorState stageType, @NotNull Material resource,
                          @Nullable Integer age, @Nullable BlockFace facingDirection, boolean dropItems, int growthTime,
                          @Nullable Double growthChance, @Nullable Function onBreak, @Nullable Function onGrowth,
                          @Nullable BlockClickFunction onClick, @Nullable Hologram hologram) {
        this.generator = generator;
        this.state = stageType;
        this.resource = resource;
        this.age = age;
        this.facingDirection = facingDirection;
        this.dropItems = dropItems;
        this.growthTime = growthTime;
        this.growthChance = growthChance;
        this.onBreak = onBreak;
        this.onGrowth = onGrowth;
        this.onClick = onClick;
        this.hologram = hologram;
    }

    public @NotNull Generator getGenerator() {
        return generator;
    }

    public @NotNull Material getBlockType() {
        return resource;
    }

    public @NotNull Material getItemType() {
        return Crops.isCrop(resource) ? Crops.getCropItem(resource) : resource;
    }

    public @Nullable Integer getAge() {
        return age;
    }

    public @Nullable BlockFace getFacingDirection() {
        return facingDirection;
    }

    public boolean isDropItems() {
        return dropItems;
    }

    public boolean shouldGrow() {
        return growthChance == null || Probability.test(growthChance);
    }

    public int getGrowthTime() {
        return growthTime;
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

    public boolean matchBlock(BlockData blockData) {
        return blockData.getMaterial() == resource
                && (age == null || ((Ageable) blockData).getAge() == age)
                && (facingDirection == null || ((Directional) blockData).getFacing() == facingDirection);
    }

    public void updateBlock(Block block) {
        block.setType(resource);
        if (age != null) {
            final Ageable ageable = (Ageable) block.getBlockData();
            ageable.setAge(age);
            block.setBlockData(ageable);
        }
        if (facingDirection != null) {
            final Directional directional = (Directional) block.getBlockData();
            directional.setFacing(facingDirection);
            block.setBlockData(directional);
        }
    }

    @Override
    public @NotNull Placeholder[] getPlaceholders() {
        return new Placeholder[] {
                Placeholder.of("%generator%", generator.getName()),
                Placeholder.of("%generator_stage%", generator.indexOf(this)),
                Placeholder.of("%generator_stages%", generator.getStages()),
                Placeholder.of("%generator_state%", state.toString().toLowerCase()),
                Placeholder.of("%generator_age%", age),
                Placeholder.of("%generator_growth_seconds%", growthTime / 20),
                Placeholder.of("%generator_growth_minutes%", growthTime / 1200)
        };
    }

    @Override
    public String toString() {
        return "GeneratorStage{" +
                "generator=" + generator.getName() +
                ", state=" + state +
                ", resource=" + resource +
                ", age=" + age +
                ", facingDirection=" + facingDirection +
                ", dropItems=" + dropItems +
                ", growthTime=" + growthTime +
                ", growthChance=" + growthChance +
                ", onBreak=" + onBreak +
                ", onGrowth=" + onGrowth +
                ", onClick=" + onClick +
                '}';
    }

}
