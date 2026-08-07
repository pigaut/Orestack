package io.github.pigaut.rpg.module.function.action.server;

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

public class DropExp implements Action {

    private final EnhancedPlugin plugin;
    private final Amount expAmount;
    private final @Nullable Amount orbAmount;
    private final Location location;
    private final boolean experience;

    public DropExp(EnhancedPlugin plugin, Amount expAmount, @Nullable Amount orbAmount,
                   World world, double x, double y, double z, boolean experience) {
        this.plugin = plugin;
        this.expAmount = expAmount;
        this.orbAmount = orbAmount;
        this.experience = experience;
        this.location = new Location(world, x, y, z);
    }

    @Override
    public void execute(@NotNull Context context) {
        Integer totalOrbs = orbAmount != null ? orbAmount.intValue() : null;

        Player player = context.player();
        ItemStack tool = context.tool();
        if (player == null || tool == null) {
            ExpUtil.dropExp(location, expAmount, totalOrbs);
            return;
        }

        Amount totalExp = expAmount;
        if (experience) {
            double expMultiplier = plugin.getSettings().getExperienceMultiplier(tool).doubleValue();
            totalExp = totalExp.transform(amount -> amount * expMultiplier);
        }

        ExpUtil.dropExp(location, totalExp, totalOrbs);
    }

}
