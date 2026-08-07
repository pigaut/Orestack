package io.github.pigaut.rpg.module.stat.tasks;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class HealthRegenTask extends PluginRunnable {

    public HealthRegenTask(@NotNull EnhancedPlugin plugin) {
        super(plugin);
    }

    public void start() {
        int healthRegenInterval = plugin.getSettings().getHealthRegenInterval();
        if (healthRegenInterval > 0) {
            runTaskTimer(healthRegenInterval);
        }
    }

    @Override
    public void run() {
        Settings settings = plugin.getSettings();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerState playerState = plugin.getPlayerState(player);
            if (playerState.isMaxHealth()) {
                continue;
            }

            boolean inCombat = playerState.isInCombat();
            if (!inCombat || settings.isRegenHealthDuringCombat()) {
                playerState.setHealth(playerState.getHealth() + playerState.getHealthRegen());
            }
        }
    }

}
