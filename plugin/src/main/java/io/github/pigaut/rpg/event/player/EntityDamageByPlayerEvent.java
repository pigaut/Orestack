package io.github.pigaut.rpg.event.player;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class EntityDamageByPlayerEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final LivingEntity victim;
    private final double critMultiplier;
    private final boolean fallingCritDamage;
    private double damage;

    public EntityDamageByPlayerEvent(@NotNull Player player, @NotNull LivingEntity victim, double damage, double critMultiplier, boolean fallingCritDamage) {
        super(player);
        this.victim = victim;
        this.damage = damage;
        this.critMultiplier = critMultiplier;
        this.fallingCritDamage = fallingCritDamage;
    }

    public @NotNull LivingEntity getVictim() {
        return victim;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isCritDamage() {
        return critMultiplier != 1;
    }

    public double getCritMultiplier() {
        return critMultiplier;
    }

    public boolean isFallingCritDamage() {
        return fallingCritDamage;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}