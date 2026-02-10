package io.github.pigaut.orestack.advertise;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.*;

public class AdvertisementListener implements Listener {

    private final OrestackPlugin plugin;

    public AdvertisementListener(@NotNull OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(plugin.getPermission("advertise"))) {
            return;
        }

        String premiumReminder = plugin.getAdvertisements().getPremiumReminder();
        if (!premiumReminder.isEmpty()) {
            plugin.getScheduler().runTaskLater(Amount.ranged(200, 600).getInteger(), () -> {
                Chat.send(player, premiumReminder);
            });
        }
    }

}
