package io.github.pigaut.rpg.module.stat.tasks;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.task.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class StatusBarUpdateTask extends PluginRunnable {

    public StatusBarUpdateTask(@NotNull EnhancedPlugin plugin) {
        super(plugin);
    }

    public void start() {
        runTaskTimer(3);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerState playerState = plugin.getPlayerState(player);
            PlayerUtil.sendActionBar(player, playerState.getStatusBar());
        }
    }

}