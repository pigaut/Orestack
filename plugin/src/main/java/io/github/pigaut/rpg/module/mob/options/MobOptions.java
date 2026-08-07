package io.github.pigaut.rpg.module.mob.options;

import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.module.mob.goal.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.module.mob.goal.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.material.*;
import org.jetbrains.annotations.*;

public class MobOptions {

    private final String displayName;

    private final double maxHealth;
    private final Amount health;

    private final @Nullable Double speed;
    private final @Nullable Double damage;
    private final @Nullable Double knockbackResistance;

    private final @Nullable ItemStack helmet;
    private final @Nullable ItemStack chestplate;
    private final @Nullable ItemStack leggings;
    private final @Nullable ItemStack boots;
    private final @Nullable ItemStack mainHand;
    private final @Nullable ItemStack offHand;

    private final boolean alwaysShowName;
    private final int arrowsInBody;
    private final boolean baby;
    private final @Nullable DyeColor color;
    private final boolean collidable;
    private final boolean canPickupItems;
    private final boolean glowing;
    private final boolean gravity;
    private final boolean hasAi;
    private final boolean invulnerable;
    private final boolean invisible;
    private final int noDamageTicks;
    private final boolean silent;

    private final @Nullable Integer size;

    private final @Nullable MobGoalsTemplate mobGoalsTemplate;

    public MobOptions(@NotNull String displayName, double maxHealth, @NotNull Amount health,
                      @Nullable Double speed, @Nullable Double damage, @Nullable Double knockbackResistance,
                      @Nullable ItemStack helmet, @Nullable ItemStack chestplate, @Nullable ItemStack leggings,
                      @Nullable ItemStack boots, @Nullable ItemStack mainHand, @Nullable ItemStack offHand,
                      boolean alwaysShowName, int arrowsInBody, boolean baby, @Nullable DyeColor color, boolean collidable,
                      boolean canPickupItems, boolean glowing, boolean gravity, boolean hasAi, boolean invulnerable,
                      boolean invisible, int noDamageTicks, boolean silent,
                      @Nullable Integer size, @Nullable MobGoalsTemplate mobGoalsTemplate) {
        this.displayName = displayName;
        this.health = health;
        this.maxHealth = maxHealth;
        this.speed = speed;
        this.damage = damage;
        this.knockbackResistance = knockbackResistance;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.alwaysShowName = alwaysShowName;
        this.arrowsInBody = arrowsInBody;
        this.baby = baby;
        this.color = color;
        this.collidable = collidable;
        this.canPickupItems = canPickupItems;
        this.glowing = glowing;
        this.gravity = gravity;
        this.hasAi = hasAi;
        this.invulnerable = invulnerable;
        this.invisible = invisible;
        this.noDamageTicks = noDamageTicks;
        this.silent = silent;
        this.size = size;
        this.mobGoalsTemplate = mobGoalsTemplate;
    }

    public @NotNull String getDisplayName() {
        return displayName;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public @NotNull Amount getHealth() {
        return health;
    }

    public @Nullable Double getSpeed() {
        return speed;
    }

    public @Nullable Double getDamage() {
        return damage;
    }

    public @Nullable Double getKnockbackResistance() {
        return knockbackResistance;
    }

    public void apply(@NotNull LivingEntity entity) {
        entity.setCustomName(displayName);

        EntityEquipment equipment = entity.getEquipment();
        if (equipment != null) {
            equipment.setHelmet(helmet, true);
            equipment.setChestplate(chestplate, true);
            equipment.setLeggings(leggings, true);
            equipment.setBoots(boots, true);
            equipment.setItemInMainHand(mainHand, true);
            equipment.setItemInOffHand(offHand, true);
        }

        entity.setArrowsInBody(arrowsInBody);
        entity.setCustomNameVisible(alwaysShowName);
        entity.setCollidable(collidable);
        entity.setCanPickupItems(canPickupItems);
        entity.setGlowing(glowing);
        entity.setGravity(gravity);
        entity.setAI(hasAi);
        entity.setInvulnerable(invulnerable);
        entity.setInvisible(invisible);
        entity.setMaximumNoDamageTicks(noDamageTicks);
        entity.setPersistent(false);
        entity.setRemoveWhenFarAway(true);
        entity.setSilent(silent);

        if (baby) {
            if (entity instanceof Ageable ageable) {
                ageable.setBaby();
            }
            if (entity instanceof Breedable breedable) {
                breedable.setAgeLock(true);
            }
        }

        if (color != null && entity instanceof Colorable colorable) {
            colorable.setColor(color);
        }

        // Max health
        AttributeInstance maxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.setBaseValue(maxHealth);
        }

        // Starting health
        entity.setHealth(health.doubleValue());

        if (speed != null) {
            AttributeInstance speedAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute != null) {
                speedAttribute.setBaseValue(speed);
            }
        }

        if (damage != null) {
            AttributeInstance attackDamageAttribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamageAttribute != null) {
                attackDamageAttribute.setBaseValue(damage);
            }
        }

        if (knockbackResistance != null) {
            AttributeInstance knockbackAttribute = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if (knockbackAttribute != null) {
                knockbackAttribute.setBaseValue(knockbackResistance);
            }
        }

        if (size != null) {
            if (entity instanceof Slime slime) {
                slime.setSize(size);
            } else if (entity instanceof Phantom phantom) {
                phantom.setSize(size);
            }
        }

        if (mobGoalsTemplate != null) {
            mobGoalsTemplate.apply(entity);
        }
    }

}
