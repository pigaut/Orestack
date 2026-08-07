package io.github.pigaut.rpg.listener.player;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

public class PlayerStatListener implements Listener {

    private final EnhancedPlugin plugin;

    public PlayerStatListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        if (event.getDamager() instanceof Player damager) {
            PlayerState playerState = plugin.getPlayerState(damager);
            playerState.setInCombat(true);

            int damage = playerState.getAttackDamage();
            event.setDamage(damage);
        }

        if (event.getEntity() instanceof Player victim) {
            PlayerState playerState = plugin.getPlayerState(victim);
            playerState.setInCombat(true);

            int damage = StatsUtil.calculateDamage(event.getDamage(), playerState.getDefense());
            event.setDamage(damage);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttackMonitor(EntityDamageByEntityEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        if (event.getEntity() instanceof Player victim) {
            PlayerState playerState = plugin.getPlayerState(victim);
            int newHealth = (int) (playerState.getHealth() - event.getDamage());
            playerState.setHealth(newHealth);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        PlayerState playerState = plugin.getPlayerState(event.getEntity());
        playerState.resetHealth();
    }

}
