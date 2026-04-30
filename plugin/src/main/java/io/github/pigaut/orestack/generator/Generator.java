package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.transform.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface Generator {

    boolean isValid();

    boolean isFullyGrown();

    boolean isDepleted();

    void cleanup();

    void remove();

    @NotNull
    String getName();

    @NotNull
    GeneratorTemplate getTemplate();

    @NotNull
    Location getOrigin();

    @NotNull
    Rotation getRotation();

    int getMaxPhase();

    @NotNull
    GeneratorPhase getPhase(int phase);

    @NotNull
    GeneratorPhase getPhase();

    void setPhase(int phase, boolean growing);

    @NotNull
    GeneratorState getState();

    @NotNull
    Set<Block> getOccupiedBlocks();

    @NotNull
    Set<Block> getAllOccupiedBlocks();

    @Nullable
    Integer getNextGrowthPhase(int targetPhase);

    int getNextHarvestingPhase(int currentPhase);

    int getTicksToNextPhase();

    int getTicksToRegrownPhase();

    @Nullable
    Double getTotalHealth();

    @Nullable
    Double getHealth();

    void cancelGrowth();

    void mineBlock(@NotNull Player player, @NotNull Block block, int expToDrop);

    void interact(@NotNull Player player, @NotNull Block block);

    void damage(@NotNull Player player, @NotNull Context context, double damageAmount);

    void regrow();

    void grow();

    void grow(int amount);

    void harvest();

}
