package io.github.pigaut.rpg.listener.player;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerLifecycleListener implements Listener {

    private final EnhancedPlugin plugin;

    public PlayerLifecycleListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayersData().registerPlayer(player);
        plugin.getPlayersState().registerPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayersData().unregisterPlayer(player);
        plugin.getPlayersState().unregisterPlayer(player);
    }

}
