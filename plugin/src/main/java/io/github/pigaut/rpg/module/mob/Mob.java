package io.github.pigaut.rpg.module.mob;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.core.flag.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.module.mob.faction.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.event.mob.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.core.flag.*;
import io.github.pigaut.rpg.event.mob.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.module.mob.faction.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Mob implements FlagHolder {

    private final EnhancedPlugin plugin;
    private final UUID entityId;
    private final MobTemplate mobTemplate;
    private final @Nullable MobSpawnPad spawnPad;
    private final Map<UUID, Double> damageByAttackers = new HashMap<>();
    private final Set<UUID> provokedBy = new HashSet<>();
    private final Set<String> flags = new HashSet<>();

    private @Nullable MobBossBar bossBar;
    private @Nullable UUID lastDamager;
    private @Nullable Location lastLocation;

    public Mob(EnhancedPlugin plugin, UUID entityId, MobTemplate mobTemplate, @Nullable MobSpawnPad spawnPad) {
        this.plugin = plugin;
        this.entityId = entityId;
        this.mobTemplate = mobTemplate;
        this.spawnPad = spawnPad;
    }

    public @NotNull UUID getEntityId() {
        return entityId;
    }

    public @Nullable LivingEntity getEntity() {
        return (LivingEntity) Bukkit.getEntity(entityId);
    }

    public @NotNull String getName() {
        return mobTemplate.getName();
    }

    public @NotNull MobTemplate getTemplate() {
        return mobTemplate;
    }

    public @NotNull MobOptions getOptions() {
        return mobTemplate.getOptions();
    }

    public @Nullable MobSpawnPad getSpawnPad() {
        return spawnPad;
    }

    public @Nullable MobFaction getFaction() {
        return mobTemplate.getFaction();
    }

    public void setProvokedBy(@NotNull UUID entityId) {
        provokedBy.add(entityId);
    }

    public boolean isProvokedBy(@NotNull UUID entityId) {
        return provokedBy.contains(entityId);
    }

    public void heal() {
        heal(mobTemplate.getMaxHealth());
    }

    public void heal(double amount) {
        LivingEntity entity = getEntity();
        if (entity == null || !entity.isValid()) {
            return;
        }

        double currentHealth = entity.getHealth();
        double maxHealth = getMaxHealth();

        double newHealth = Math.min(maxHealth, currentHealth + amount);
        if (newHealth != currentHealth) {
            entity.setHealth(newHealth);
            MobHealthChangeEvent mobHealthChangeEvent = new MobHealthChangeEvent(this);
            Server.callEvent(mobHealthChangeEvent);
        }
    }

    public void damage(@NotNull LivingEntity damager, double damage) {
        LivingEntity victim = getEntity();
        if (victim == null) {
            return;
        }

        double newHealth = Math.max(0, victim.getHealth() - damage);
        victim.setHealth(newHealth);

        if (damager instanceof Player) {
            lastDamager = damager.getUniqueId();
        }

        MobHealthChangeEvent mobHealthChangeEvent = new MobHealthChangeEvent(this);
        Server.callEvent(mobHealthChangeEvent);

        if (victim.isDead()) {
            MobDeathEvent mobDeathEvent = new MobDeathEvent(this, damager);
            Server.callEvent(mobDeathEvent);
        }
    }

    public void attack(@NotNull LivingEntity victim, double damage) {
        LivingEntity attacker = getEntity();
        if (attacker == null || victim.isDead()) {
            return;
        }

        Mob mobVictim = plugin.getMob(victim);
        if (mobVictim != null) {
            if (mobVictim.getTemplate().equals(mobTemplate)) {
                return;
            }
            EntityUtil.knockback(attacker, victim);
            mobVictim.damage(attacker, damage);
            return;
        }

        PlayerState playerVictim = plugin.getPlayerState(victim);
        if (playerVictim != null) {
            EntityUtil.knockback(attacker, victim);
            playerVictim.damage(attacker, damage);
            return;
        }

        boolean dealtDamage = EntityUtil.attack(attacker, victim, damage);
        if (dealtDamage && victim.getHealth() <= 0) {
            Server.callEvent(new MobKillEvent(this, victim));
        }
    }

    public int getAttackerCount() {
        return damageByAttackers.size();
    }

    public double getHealth() {
        LivingEntity entity = getEntity();
        return entity != null ? entity.getHealth() : 0;
    }

    public @Nullable Location getLocation() {
        Entity entity = getEntity();
        if (entity != null) {
            return entity.getLocation();
        }
        return lastLocation;
    }

    public @NotNull Collection<Player> getAttackers() {
        Set<Player> attackers = new HashSet<>();
        for (UUID entityId : damageByAttackers.keySet()) {
            Player player = Bukkit.getPlayer(entityId);
            if (player != null) {
                attackers.add(player);
            }
        }
        return attackers;
    }

    public void addAttacker(@NotNull Player player, double damage) {
        UUID playerId = player.getUniqueId();
        damageByAttackers.merge(playerId, damage, Double::sum);
        lastDamager = playerId;
    }

    public @Nullable MobBossBar getBossBar() {
        return bossBar;
    }

    public void setBossBarVisible(boolean visible) {
        if (!visible) {
            if (bossBar != null) {
                bossBar.stop();
                bossBar = null;
            }
            return;
        }

        MobBossBarTemplate bossBarTemplate = mobTemplate.getBossBarTemplate();
        if (bossBarTemplate != null) {
            bossBar = bossBarTemplate.create(this);
            bossBar.start();
        }
    }

    public @Nullable Player getLastDamager() {
        return lastDamager != null ? Bukkit.getPlayer(lastDamager) : null;
    }

    public @Nullable Player getTopDamager() {
        Player highestDamager = null;
        double maxDamage = 0.0;

        for (Map.Entry<UUID, Double> entry : damageByAttackers.entrySet()) {
            double damage = entry.getValue();
            if (damage <= maxDamage) {
                continue;
            }

            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                maxDamage = damage;
                highestDamager = player;
            }
        }

        return highestDamager;
    }

    public boolean isDefaultItemDrops() {
        return mobTemplate.isDefaultItemDrops();
    }

    public boolean isDefaultExpDrops() {
        return mobTemplate.isDefaultExpDrops();
    }

    public @Nullable List<ItemDrop> getItemDrops() {
        return mobTemplate.getItemDrops();
    }

    public @Nullable Amount getExpDrops() {
        return mobTemplate.getExpDrops();
    }

    public @Nullable Delay getAttackCooldown() {
        return mobTemplate.getAttackCooldown();
    }

    public double getMaxHealth() {
        return mobTemplate.getMaxHealth();
    }

    public boolean isLeashing() {
        return mobTemplate.isLeashing();
    }

    public boolean isRenaming() {
        return mobTemplate.isRenaming();
    }

    public boolean isSunburn() {
        return mobTemplate.isSunburn();
    }

    public boolean isDealDamage() {
        return mobTemplate.isDealDamage();
    }

    public boolean isRandomEquipment() {
        return mobTemplate.isRandomEquipment();
    }

    public boolean isSlimeSplit() {
        return mobTemplate.isSlimeSplit();
    }

    public @Nullable String getPlayerSlainMessage() {
        return mobTemplate.getPlayerSlainMessage();
    }

    public @Nullable Function getOnSpawn() {
        return mobTemplate.getOnSpawn();
    }

    public @Nullable Function getOnDamaged() {
        return mobTemplate.getOnDamaged();
    }

    public @Nullable Function getOnHealthChange() {
        return mobTemplate.getOnHealthChange();
    }

    public @Nullable Function getOnDeath() {
        return mobTemplate.getOnDeath();
    }

    public @Nullable Function getOnTarget() {
        return mobTemplate.getOnTarget();
    }

    public @Nullable Function getOnAttack() {
        return mobTemplate.getOnAttack();
    }

    public @Nullable Function getOnKill() {
        return mobTemplate.getOnKill();
    }

    public @Nullable Function getOnPlayerKill() {
        return mobTemplate.getOnPlayerKill();
    }

    public @Nullable Function getOnJump() {
        return mobTemplate.getOnJump();
    }

    public @Nullable Function getOnLand() {
        return mobTemplate.getOnLand();
    }

    @Override
    public boolean hasFlag(@NotNull String flag) {
        return flags.contains(flag);
    }

    @Override
    public @NotNull Collection<String> getFlags() {
        return new HashSet<>(flags);
    }

    @Override
    public void addFlag(@NotNull String flag) {
        flags.add(flag);
    }

    @Override
    public void addTemporaryFlag(@NotNull String flag, int ticks) {
        flags.add(flag);
        mobTemplate.getPlugin().getScheduler().runTaskLater(ticks, () -> flags.remove(flag));
    }

    @Override
    public void removeFlag(@NotNull String flag) {
        flags.remove(flag);
    }

    public boolean isValid() {
        Entity entity = getEntity();
        return entity != null && entity.isValid();
    }

    public void remove() {
        Entity entity = getEntity();
        if (entity != null) {
            lastLocation = entity.getLocation();
            entity.remove();
        }

        if (bossBar != null) {
            bossBar.stop();
            bossBar = null;
        }

        plugin.getMobs().unregister(entityId);
    }

}
