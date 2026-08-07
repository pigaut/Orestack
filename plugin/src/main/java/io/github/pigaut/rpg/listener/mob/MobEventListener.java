package io.github.pigaut.rpg.listener.mob;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.event.mob.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.event.mob.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

import java.util.*;

public class MobEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public MobEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onAttack(MobAttackEvent event) {
        Mob mob = event.getMob();
        if (!mob.isDealDamage()) {
            event.setCancelled(true);
        }
        Function onAttack = mob.getOnAttack();
        if (onAttack != null) {
            onAttack.run(Context.fromMobAndEnemy(plugin, mob, event.getEnemy(), event));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHealthChange(MobHealthChangeEvent event) {
        Mob mob = event.getMob();

        MobBossBar mobBossBar = mob.getBossBar();
        if (mobBossBar != null) {
            mobBossBar.update();
        }

        Function onHealthChange = mob.getOnHealthChange();
        if (onHealthChange != null) {
            onHealthChange.run(Context.fromMob(plugin, mob));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDamage(MobDamageEvent event) {
        Mob mob = event.getMob();
        Function onDamaged = mob.getOnDamaged();
        if (onDamaged != null) {
            onDamaged.run(Context.fromMobAndEnemy(plugin, mob, event.getEnemy(), event));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onKill(MobKillEvent event) {
        Mob mob = event.getMob();
        Entity victim = event.getEnemy();

        if (victim instanceof Player playerVictim) {
            PlayerState playerState = plugin.getPlayerState(playerVictim);
            playerState.setMobKiller(mob);
        }

        Function onKill = mob.getOnKill();
        if (onKill != null) {
            onKill.run(Context.fromMobAndEnemy(plugin, mob, victim, event));
        }

        if (victim instanceof Player) {
            Function onPlayerKill = mob.getOnPlayerKill();
            if (onPlayerKill != null) {
                onPlayerKill.run(Context.fromMobAndEnemy(plugin, mob, victim, event));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDeath(MobDeathEvent event) {
        Mob mob = event.getMob();
        Context context = Context.fromMobAndEnemy(plugin, mob, event.getEnemy(), event);

        Function onDeath = mob.getOnDeath();
        if (onDeath != null) {
            onDeath.run(context);
            if (event.isCancelled()) {
                return;
            }
        }

        Location location = LocationUtil.centered(mob.getLocation());
        List<ItemDrop> itemDrops = mob.getItemDrops();
        if (itemDrops != null) {
            for (ItemDrop itemDrop : itemDrops) {
                itemDrop.spawn(location, context, ItemSpawnReason.MOB_DROPS);
            }
        }

        Amount expDrops = mob.getExpDrops();
        if (expDrops != null) {
            ExpUtil.dropExp(location, expDrops.intValue());
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onTarget(MobTargetEvent event) {
        Mob mob = event.getMob();
        Function onTarget = mob.getOnTarget();
        if (onTarget != null) {
            onTarget.run(Context.fromMobAndEnemy(plugin, mob, event.getTarget(), event));
        }
    }

}
