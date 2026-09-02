package io.github.pigaut.rpg.listener.mob;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.event.mob.*;
import io.github.pigaut.rpg.event.player.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

public class EntityEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public EntityEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityDamageByPlayer(EntityDamageByPlayerEvent event) {
        Player player = event.getPlayer();
        LivingEntity victim = event.getVictim();
        double damage = event.getDamage();

        Mob mobVictim = plugin.getMob(victim);
        if (mobVictim != null) {
            mobVictim.addAttacker(player, damage);

            // Player kills the mob
            if (victim.getHealth() <= damage) {
                MobDeathEvent mobDeathEvent = new MobDeathEvent(mobVictim, player);
                Server.callEvent(mobDeathEvent);
                event.setCancelled(mobDeathEvent.isCancelled());
            }
            // Player doesn't kill the mob
            else {
                MobDamageEvent mobDamageEvent = new MobDamageEvent(mobVictim, player, damage);
                Server.callEvent(mobDamageEvent);
                event.setDamage(mobDamageEvent.getDamage());
                event.setCancelled(mobDamageEvent.isCancelled());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }

        if (event.getDamager() instanceof Player) {
            return;
        }

        LivingEntity damager = EntityUtil.getDamagerEntity(event.getDamager());
        if (damager == null) {
            return;
        }

        double damage = event.getDamage();
        double finalDamage = event.getFinalDamage();

        Mob mobDamager = plugin.getMob(damager);
        if (mobDamager != null) {
            Delay attackCooldown = mobDamager.getAttackCooldown();
            if (attackCooldown != null) {
                if (mobDamager.hasFlag("plugin:mob_attack_cooldown")) {
                    event.setCancelled(true);
                    return;
                }
                mobDamager.addTemporaryFlag("plugin:mob_attack_cooldown", attackCooldown);
            }

            // Mob kills the victim
            if (victim.getHealth() <= finalDamage) {
                MobKillEvent mobKillEvent = new MobKillEvent(mobDamager, victim);
                Server.callEvent(mobKillEvent);
                event.setCancelled(mobKillEvent.isCancelled());
            }
            // Mob doesn't kill the victim
            else {
                MobAttackEvent mobAttackEvent = new MobAttackEvent(mobDamager, victim, damage);
                Server.callEvent(mobAttackEvent);
                event.setDamage(mobAttackEvent.getDamage());
                event.setCancelled(mobAttackEvent.isCancelled());
            }
        }

        Mob mobVictim = plugin.getMob(victim);
        if (mobVictim != null && !event.isCancelled()) {
            // Damager kills the mob
            if (victim.getHealth() <= finalDamage) {
                MobDeathEvent mobDeathEvent = new MobDeathEvent(mobVictim, damager);
                Server.callEvent(mobDeathEvent);
                event.setCancelled(mobDeathEvent.isCancelled());
            }
            // Damager doesn't kill the mob
            else {
                MobDamageEvent mobDamageEvent = new MobDamageEvent(mobVictim, damager, damage);
                Server.callEvent(mobDamageEvent);
                event.setDamage(mobDamageEvent.getDamage());
                event.setCancelled(mobDamageEvent.isCancelled());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityHealthChange(EntityDamageEvent event) {
        Mob mob = plugin.getMob(event.getEntity());
        if (mob == null) {
            return;
        }

        if (event.getFinalDamage() == 0) {
            return;
        }

        plugin.getScheduler().runTaskLater(1, () -> {
            MobHealthChangeEvent mobHealthChangeEvent = new MobHealthChangeEvent(mob);
            Server.callEvent(mobHealthChangeEvent);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Mob mob = plugin.getMob(event.getEntity());
        if (mob == null) {
            return;
        }

        if (!mob.isDefaultItemDrops()) {
            event.getDrops().clear();
        }

        if (!mob.isDefaultExpDrops()) {
            event.setDroppedExp(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerState playerState = plugin.getPlayerState(player);
        Mob mobKiller = playerState.getMobKiller();
        if (mobKiller == null) {
            if (!plugin.getSettings().isShowDeathMessages()) {
                event.setDeathMessage(null);
            }
            return;
        }

        playerState.setMobKiller(null);
        Context context = Context.fromMobAndPlayer(plugin, mobKiller, player);

        // Custom slain message per mob
        String slainMessage = mobKiller.getPlayerSlainMessage();
        if (slainMessage != null) {
            String parsedSlainMessage = PlaceholderUtil.parseAll(context, slainMessage);
            event.setDeathMessage(parsedSlainMessage);
            return;
        }

        // Generic slain messages are disabled
        if (!plugin.getSettings().isShowSlainMessages()) {
            event.setDeathMessage(null);
            return;
        }

        // Generic slain message for all mobs
        String parsedSlainMessage = PlaceholderUtil.parseAll(context, plugin.getSettings().getPlayerSlainMessage());
        event.setDeathMessage(parsedSlainMessage);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        Mob mob = plugin.getMob(event.getEntity());
        if (mob != null) {
            MobTargetEvent mobTargetEvent = new MobTargetEvent(mob, event.getTarget());
            Server.callEvent(mobTargetEvent);
            event.setCancelled(mobTargetEvent.isCancelled());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityLeash(PlayerLeashEntityEvent event) {
        Mob mob = plugin.getMob(event.getEntity());
        if (mob == null) {
            return;
        }

        if (!mob.isLeashing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntitySunCombust(EntityCombustEvent event) {
        if (event instanceof EntityCombustByBlockEvent || event instanceof EntityCombustByEntityEvent) {
            return;
        }

        Mob mob = plugin.getMob(event.getEntity());
        if (mob == null) {
            return;
        }

        if (!mob.isSunburn()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSlimeSplit(SlimeSplitEvent event) {
        Mob mob = plugin.getMob(event.getEntity());
        if (mob == null) {
            return;
        }

        if (!mob.isSlimeSplit()) {
            event.setCancelled(true);
        }
    }

}
