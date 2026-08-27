package io.github.pigaut.rpg.listener.stat;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.event.item.*;
import io.github.pigaut.rpg.event.player.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;

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
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) ||
                !(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (!plugin.getSettings().isStats()) {
            EntityDamageByPlayerEvent damageByPlayerEvent = new EntityDamageByPlayerEvent(damager, victim,
                    event.getDamage(), 1, event.isCritical());
            Server.callEvent(damageByPlayerEvent);

            if (damageByPlayerEvent.isCancelled()) {
                event.setCancelled(true);
            }
            event.setDamage(damageByPlayerEvent.getDamage());
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
        double critMultiplier = 1;
        if (Probability.test(critChance)) {
            critMultiplier = playerState.getCritDamageMultiplier();
        }

        if (critMultiplier != 1) {
            damage *= critMultiplier;
        }

        EntityDamageByPlayerEvent damageByPlayerEvent = new EntityDamageByPlayerEvent(damager, victim,
                damage, critMultiplier, event.isCritical());
        Server.callEvent(damageByPlayerEvent);

        if (damageByPlayerEvent.isCancelled()) {
            event.setCancelled(true);
        }
        event.setDamage(damageByPlayerEvent.getDamage());
    }

    private static final Set<DamageCause> IGNORED_DAMAGE_CAUSES = EnumSet.of(
            DamageCause.KILL,
            DamageCause.WORLD_BORDER,
            DamageCause.VOID,
            DamageCause.SUICIDE,
            DamageCause.CUSTOM
    );

    private static final Set<DamageCause> REPEATING_DAMAGE_CAUSES = EnumSet.of(
            DamageCause.LAVA,
            DamageCause.FIRE,
            DamageCause.FIRE_TICK,
            DamageCause.DROWNING,
            DamageCause.STARVATION,
            DamageCause.POISON,
            DamageCause.WITHER,
            DamageCause.FREEZE,
            DamageCause.CRAMMING,
            DamageCause.SUFFOCATION,
            DamageCause.HOT_FLOOR,
            DamageCause.CAMPFIRE,
            DamageCause.DRYOUT
    );

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamaged(EntityDamageEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        DamageCause cause = event.getCause();
        if (REPEATING_DAMAGE_CAUSES.contains(cause)) {
            PlayerState playerState = plugin.getPlayerState(victim);
            long now = System.currentTimeMillis();
            long last = playerState.getLastDamageTime();
            long cooldownMillis = 500;

            if (now - last < cooldownMillis) {
                event.setDamage(0);
                return;
            }

            playerState.setLastDamageTime(now);
        }

        PlayerState playerState = plugin.getPlayerState(victim);
        playerState.setInCombat(true);

        double damage = event.getDamage();

        if (!IGNORED_DAMAGE_CAUSES.contains(cause)) {
            damage *= plugin.getSettings().getDamageMultiplier(cause);
            damage *= StatUtil.getDefenseDamageReduction(playerState.getDefense());
        }

        event.setDamage(0);
        playerState.setHealth((int) (playerState.getHealth() - damage));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onRespawn(PlayerRespawnEvent event) {
        if (!plugin.getSettings().isStats()) {
            return;
        }

        PlayerState playerState = plugin.getPlayerState(event.getPlayer());
        playerState.resetHealth();
        playerState.resetMana();
    }

}
