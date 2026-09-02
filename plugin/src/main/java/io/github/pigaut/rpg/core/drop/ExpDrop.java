package io.github.pigaut.rpg.core.drop;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ExpDrop {

    private final EnhancedPlugin plugin;
    private final Amount expAmount;
    private final @Nullable Amount orbAmount;
    private final boolean experience;

    public ExpDrop(@NotNull EnhancedPlugin plugin, @NotNull Amount expAmount,
                    @Nullable Amount orbAmount, boolean experience) {
        this.plugin = plugin;
        this.expAmount = expAmount;
        this.orbAmount = orbAmount;
        this.experience = experience;
    }

    public void spawn(@NotNull Location location, @Nullable ItemStack tool) {
        Integer totalOrbs = orbAmount != null ? orbAmount.intValue() : null;
        Amount totalExp = expAmount;

        if (tool == null) {
            ExpUtil.dropExp(location, totalExp, totalOrbs);
            return;
        }

        if (experience) {
            double expMultiplier = plugin.getSettings().getExperienceMultiplier(tool).doubleValue();
            totalExp = totalExp.transform(amount -> amount * expMultiplier);
        }

        ExpUtil.dropExp(location, totalExp, totalOrbs);
    }

    public void give(@NotNull Player player) {
        give(player, null);
    }

    public void give(@NotNull Player player, @Nullable ItemStack tool) {
        Amount totalExp = expAmount;

        if (tool != null && experience) {
            double expMultiplier = plugin.getSettings().getExperienceMultiplier(tool).doubleValue();
            totalExp = totalExp.transform(amount -> amount * expMultiplier);
        }

        if (plugin.getSettings().isStats()) {
            PlayerState playerState = plugin.getPlayerState(player);
            totalExp = totalExp.transform(amount -> amount * playerState.getExpGainMultiplier());
        }

        player.giveExp(totalExp.intValue());
    }

}