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

public class ManaRegenTask extends PluginRunnable {

    public ManaRegenTask(@NotNull EnhancedPlugin plugin) {
        super(plugin);
    }

    public void start() {
        int manaRegenInterval = plugin.getSettings().getManaRegenInterval();
        if (manaRegenInterval > 0) {
            runTaskTimer(manaRegenInterval);
        }
    }

    @Override
    public void run() {
        Settings settings = plugin.getSettings();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead()) {
                continue;
            }

            PlayerState playerState = plugin.getPlayerState(player);
            if (playerState.isMaxMana()) {
                continue;
            }

            boolean inCombat = playerState.isInCombat();
            if (!inCombat || settings.isRegenManaDuringCombat()) {
                playerState.setMana(playerState.getMana() + playerState.getManaRegen());
            }
        }
    }

}