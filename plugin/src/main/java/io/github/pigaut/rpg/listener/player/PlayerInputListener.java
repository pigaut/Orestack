package io.github.pigaut.rpg.listener.player;

import io.github.pigaut.rpg.player.input.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.player.input.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerInputListener implements Listener {

    private final EnhancedPlugin plugin;

    public PlayerInputListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        PlayerState player = plugin.getPlayerState(event.getPlayer());
        if (player.isAwaitingInput(InputSource.CHAT)) {
            event.setCancelled(true);
            plugin.getScheduler().runTask(() ->
                    player.submitInput(event.getMessage()));
        }
    }

}
