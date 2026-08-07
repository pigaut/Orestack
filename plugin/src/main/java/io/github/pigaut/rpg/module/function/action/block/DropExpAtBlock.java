package io.github.pigaut.rpg.module.function.action.block;

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
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropExpAtBlock implements Action {

    private final EnhancedPlugin plugin;
    private final Amount expAmount;
    private final @Nullable Amount orbAmount;
    private final boolean experience;

    public DropExpAtBlock(EnhancedPlugin plugin, Amount expAmount, @Nullable Amount orbAmount, boolean experience) {
        this.plugin = plugin;
        this.expAmount = expAmount;
        this.orbAmount = orbAmount;
        this.experience = experience;
    }

    @Override
    public void execute(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return;
        }

        Location location = block.getLocation().add(0.5, 0.5, 0.5);
        Integer totalOrbs = orbAmount != null ? orbAmount.intValue() : null;

        ItemStack tool = context.tool();
        if (tool == null) {
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
