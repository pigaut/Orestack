package io.github.pigaut.rpg.listener.stat;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.event.item.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import java.util.*;

public class StatEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public StatEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExpChange(PlayerExpChangeEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        Player player = event.getPlayer();
        int amount = event.getAmount();

        PlayerState playerState = plugin.getPlayerState(player);
        amount = (int) (amount * playerState.getExpGainMultiplier());
        event.setAmount(amount);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEquipmentChange(PlayerEquipmentChangeEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        PlayerState playerState = plugin.getPlayerState(event.getPlayer());
        playerState.refreshStats(event.getSlot());
        playerState.updateStatusBar();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        if (!(event.getDamager() instanceof Player damager)) {
            return;
        }

        PlayerState playerState = plugin.getPlayerState(damager);
        playerState.setInCombat(true);

        double damage = 0;
        if (!plugin.getItems().hasStat(PlayerUtil.getTool(damager), BaseStats.DAMAGE)) {
            damage = event.getDamage();
        }

        damage += playerState.getAttackDamage();

        double critChance = playerState.getCritChance();
        if (Probability.test(critChance)) {
            damage *= playerState.getCritDamageMultiplier();
        }

        event.setDamage(damage);
    }

    private static final Set<EntityDamageEvent.DamageCause> DEFENSE_IGNORED_CAUSES = EnumSet.of(
            EntityDamageEvent.DamageCause.KILL,
            EntityDamageEvent.DamageCause.WORLD_BORDER,
            EntityDamageEvent.DamageCause.VOID,
            EntityDamageEvent.DamageCause.SUICIDE,
            EntityDamageEvent.DamageCause.CUSTOM
    );

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamaged(EntityDamageEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        PlayerState playerState = plugin.getPlayerState(victim);
        playerState.setInCombat(true);

        double damage = event.getDamage();
        EntityDamageEvent.DamageCause cause = event.getCause();

        if (!DEFENSE_IGNORED_CAUSES.contains(cause)) {
            damage *= plugin.getSettings().getDamageMultiplier(cause);
            damage *= StatsUtil.getDefenseDamageReduction(playerState.getDefense());
        }

        event.setDamage(0);
        playerState.setHealth((int) (playerState.getHealth() - damage));
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

    @EventHandler(priority = EventPriority.LOW)
    public void onRespawn(PlayerRespawnEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        PlayerState playerState = plugin.getPlayerState(event.getPlayer());
        playerState.resetHealth();
    }

}
