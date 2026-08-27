package io.github.pigaut.rpg.module.structure.settings;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface StructureSettings {

    boolean isKeepBlocksOnRemove();

    boolean isRestoreBlocksOnRemove();

    boolean isDamageOverflow();

    boolean isEfficiencyDamageMultiplier();

    boolean isReducedCooldownDamage();

    @NotNull
    Amount getToolDamage(@NotNull Material toolType, @NotNull Material blockType);

    double getStructureDamage(@NotNull Player player, @NotNull Block block);

}
