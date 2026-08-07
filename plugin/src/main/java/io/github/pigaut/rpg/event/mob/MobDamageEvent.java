package io.github.pigaut.rpg.event.mob;

import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.yaml.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class MobDamageEvent extends MobEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private double damage;

    public MobDamageEvent(@NotNull Mob mob, @NotNull Entity enemy, double damage) {
        super(mob, enemy);
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        Preconditions.checkArgument(damage >= 0, "Damage must be positive");
        this.damage = damage;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
