package io.github.pigaut.orestack.stage;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorStage {

    private final Generator generator;
    private final GeneratorState state;
    private final Material resource;
    private final @Nullable Integer age;
    private final @Nullable BlockFace facingDirection;
    private final boolean dropItems;
    private final @Nullable Integer growthTime;
    private final @Nullable Double growthChance;
    private final List<Function> onBreak;
    private final List<Function> onGrowth;
    private final List<BlockClickFunction> onClick;

    public GeneratorStage(@NotNull Generator generator, @NotNull GeneratorState stageType, @NotNull Material resource,
                          @Nullable Integer age, @Nullable BlockFace facingDirection, boolean dropItems, @Nullable Integer growthTime,
                          @Nullable Double growthChance, List<Function> onBreak, List<Function> onGrowth, List<BlockClickFunction> onClick) {
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
        return growthTime != null && growthChance == null || Probability.test(growthChance);
    }

    public @Nullable Integer getGrowthTime() {
        return growthTime;
    }

    public GeneratorState getState() {
        return state;
    }

    public @Nullable Double getGrowthChance() {
        return growthChance;
    }

    public List<Function> getOnHarvest() {
        return onBreak;
    }

    public List<Function> getOnGrowth() {
        return onGrowth;
    }

    public List<BlockClickFunction> getOnClick() {
        return onClick;
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
