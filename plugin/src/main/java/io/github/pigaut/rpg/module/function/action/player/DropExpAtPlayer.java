package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropExpAtPlayer implements Action {

    private final EnhancedPlugin plugin;
    private final Amount expAmount;
    private final @Nullable Amount orbAmount;
    private final boolean experience;

    public DropExpAtPlayer(EnhancedPlugin plugin, Amount expAmount, @Nullable Amount orbAmount, boolean experience) {
        this.plugin = plugin;
        this.expAmount = expAmount;
        this.orbAmount = orbAmount;
        this.experience = experience;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player == null) {
            return;
        }

        Location location = player.getLocation();
        Integer totalOrbs = orbAmount != null ? orbAmount.intValue() : null;
        Amount totalExp = expAmount;

        ItemStack tool = context.tool();
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

}
