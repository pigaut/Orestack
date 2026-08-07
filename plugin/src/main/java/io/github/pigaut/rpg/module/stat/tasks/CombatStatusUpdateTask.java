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

public class CombatStatusUpdateTask extends PluginRunnable {

    public CombatStatusUpdateTask(@NotNull EnhancedPlugin plugin) {
        super(plugin);
    }

    public void start() {
        runTaskTimer(60);
    }

    @Override
    public void run() {
        Settings settings = plugin.getSettings();
        long combatDurationMillis = settings.getCombatDuration().toMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerState playerState = plugin.getPlayerState(player);

            if (!playerState.isInCombat()) {
                continue;
            }

            Long lastCombatInteraction = playerState.getLastCombatInteraction();
            if (lastCombatInteraction == null) {
                playerState.setInCombat(false);
                continue;
            }

            if (System.currentTimeMillis() - lastCombatInteraction >= combatDurationMillis) {
                playerState.setInCombat(false);
            }
        }
    }

}
