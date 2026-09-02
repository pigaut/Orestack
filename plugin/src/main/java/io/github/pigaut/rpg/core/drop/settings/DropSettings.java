package io.github.pigaut.rpg.core.drop.settings;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface DropSettings {

    @NotNull
    List<DropLocation> getItemDropLocationPriority();

    @NotNull
    List<DropLocation> getExpDropLocationPriority();

    boolean isFortuneDrop(@NotNull Material material);

    boolean isMiningFortuneDrop(@NotNull Material material);

    boolean isLootingDrop(@NotNull Material material);

    boolean isAutoSmelt();

    boolean isTelepathy();

    boolean isExperience();

    @Nullable
    Material getSilkDrop(@NotNull Material originalDrop);

    @Nullable
    Material getSmeltedDrop(@NotNull Material originalDrop);

    double getSmeltChance(@NotNull ItemStack tool);

    double getTelepathyChance(@NotNull ItemStack tool);

    @NotNull
    Amount getExperienceMultiplier(@NotNull ItemStack tool);

}
