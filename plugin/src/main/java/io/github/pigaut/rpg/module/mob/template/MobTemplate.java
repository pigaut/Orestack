package io.github.pigaut.rpg.module.mob.template;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.module.mob.disguise.*;
import io.github.pigaut.rpg.module.mob.faction.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.module.mob.disguise.*;
import io.github.pigaut.rpg.module.mob.faction.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobTemplate implements Identifiable {

    private final EnhancedPlugin plugin;
    private final String name;
    private final String group;

    private final EntityType entity;
    private final MobOptions options;

    private final @Nullable MobFaction faction;
    private final @Nullable MobBossBarTemplate bossBarTemplate;
    private final @Nullable MobDisguiseTemplate disguiseTemplate;

    private final boolean defaultItemDrops;
    private final boolean defaultExpDrops;

    private final @Nullable List<ItemDrop> itemDrops;
    private final @Nullable Amount expDrops;

    private final @Nullable Delay attackCooldown;

    private final boolean leashing;
    private final boolean renaming;
    private final boolean sunburn;
    private final boolean dealDamage;
    private final boolean randomEquipment;
    private final boolean slimeSplit;

    private final String playerSlainMessage;

    private final Function onSpawn;
    private final Function onDamaged;
    private final Function onHealthChange;
    private final Function onDeath;

    private final Function onTarget;
    private final Function onAttack;
    private final Function onKill;
    private final Function onPlayerKill;

    private final Function onJump;
    private final Function onLand;

    public MobTemplate(EnhancedPlugin plugin, String name, String group,
                       @NotNull EntityType entity, @NotNull MobOptions options, @Nullable MobFaction faction,
                       @Nullable MobBossBarTemplate bossBarTemplate, @Nullable MobDisguiseTemplate disguiseTemplate,
                       boolean defaultItemDrops, boolean defaultExpDrops,
                       @Nullable List<ItemDrop> itemDrops, @Nullable Amount expDrops, @Nullable Delay attackCooldown,
                       boolean preventLeashing, boolean preventRenaming, boolean preventSunburn,
                       boolean preventAttackDamage, boolean preventRandomEquipment, boolean slimeSplit,
                       String playerSlainMessage,
                       Function onSpawn, Function onDamaged, Function onHealthChange, Function onDeath,
                       Function onTarget, Function onAttack, Function onKill, Function onPlayerKill, Function onJump, Function onLand) {
        this.plugin = plugin;
        this.name = name;
        this.group = group;
        this.entity = entity;
        this.options = options;
        this.faction = faction;
        this.bossBarTemplate = bossBarTemplate;
        this.disguiseTemplate = disguiseTemplate;
        this.defaultItemDrops = defaultItemDrops;
        this.defaultExpDrops = defaultExpDrops;
        this.itemDrops = itemDrops;
        this.expDrops = expDrops;
        this.attackCooldown = attackCooldown;
        this.leashing = preventLeashing;
        this.renaming = preventRenaming;
        this.sunburn = preventSunburn;
        this.dealDamage = preventAttackDamage;
        this.randomEquipment = preventRandomEquipment;
        this.slimeSplit = slimeSplit;
        this.playerSlainMessage = playerSlainMessage;
        this.onSpawn = onSpawn;
        this.onDamaged = onDamaged;
        this.onHealthChange = onHealthChange;
        this.onDeath = onDeath;
        this.onTarget = onTarget;
        this.onAttack = onAttack;
        this.onKill = onKill;
        this.onPlayerKill = onPlayerKill;
        this.onJump = onJump;
        this.onLand = onLand;
    }

    public @NotNull EnhancedPlugin getPlugin() {
        return plugin;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable String getGroup() {
        return group;
    }

    public @NotNull EntityType getEntityType() {
        return entity;
    }

    public @NotNull MobOptions getOptions() {
        return options;
    }

    public @Nullable MobFaction getFaction() {
        return faction;
    }

    public @Nullable MobBossBarTemplate getBossBarTemplate() {
        return bossBarTemplate;
    }

    public @Nullable MobDisguiseTemplate getDisguiseTemplate() {
        return disguiseTemplate;
    }

    public double getMaxHealth() {
        return options.getMaxHealth();
    }

    public boolean isDefaultItemDrops() {
        return defaultItemDrops;
    }

    public boolean isDefaultExpDrops() {
        return defaultExpDrops;
    }

    public @Nullable List<ItemDrop> getItemDrops() {
        return itemDrops != null ? new ArrayList<>(itemDrops) : null;
    }

    public @Nullable Amount getExpDrops() {
        return expDrops;
    }

    public @Nullable Delay getAttackCooldown() {
        return attackCooldown;
    }

    public boolean isLeashing() {
        return leashing;
    }

    public boolean isRenaming() {
        return renaming;
    }

    public boolean isSunburn() {
        return sunburn;
    }

    public boolean isDealDamage() {
        return dealDamage;
    }

    public boolean isRandomEquipment() {
        return randomEquipment;
    }

    public boolean isSlimeSplit() {
        return slimeSplit;
    }

    public @Nullable String getPlayerSlainMessage() {
        return playerSlainMessage;
    }

    public @Nullable Function getOnSpawn() {
        return onSpawn;
    }

    public @Nullable Function getOnDamaged() {
        return onDamaged;
    }

    public @Nullable Function getOnHealthChange() {
        return onHealthChange;
    }

    public @Nullable Function getOnDeath() {
        return onDeath;
    }

    public @Nullable Function getOnTarget() {
        return onTarget;
    }

    public @Nullable Function getOnAttack() {
        return onAttack;
    }

    public @Nullable Function getOnKill() {
        return onKill;
    }

    public @Nullable Function getOnPlayerKill() {
        return onPlayerKill;
    }

    public @Nullable Function getOnJump() {
        return onJump;
    }

    public @Nullable Function getOnLand() {
        return onLand;
    }

    public @NotNull Mob spawn(@NotNull Location location) {
        return spawn(location, null);
    }

    public @NotNull Mob spawn(@NotNull Location location, @Nullable MobSpawnPad spawnPad) {
        if (disguiseTemplate != null) {
            disguiseTemplate.applyToNextEntity();
        }

        LivingEntity spawnedEntity;
        if (Server.getVersion() >= Version.V1_17_1) {
            spawnedEntity = (LivingEntity) location.getWorld().spawnEntity(location, entity, randomEquipment);
        } else {
            spawnedEntity = (LivingEntity) location.getWorld().spawnEntity(location, entity);
        }

        options.apply(spawnedEntity);

        Mob mob = new Mob(plugin, spawnedEntity, this, spawnPad);
        plugin.getMobs().register(mob);

        if (bossBarTemplate != null) {
            mob.setBossBarVisible(true);
        }

        if (onSpawn != null) {
            onSpawn.run(Context.fromMob(plugin, mob));
        }

        return mob;
    }

}
