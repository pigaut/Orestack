package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GiveExpToPlayer implements PlayerToolAction {

    private final EnhancedPlugin plugin;
    private final Amount amount;
    private final boolean experience;

    public GiveExpToPlayer(EnhancedPlugin plugin, Amount amount, boolean experience) {
        this.plugin = plugin;
        this.amount = amount;
        this.experience = experience;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull ItemStack tool) {
        Amount totalExp = amount;
        if (experience) {
            double expMultiplier = plugin.getSettings().getExperienceMultiplier(tool).doubleValue();
            totalExp = totalExp.transform(amount -> amount * expMultiplier);
        }

        player.giveExp(totalExp.intValue());
    }

}
