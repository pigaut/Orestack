package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.voxel.player.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GeneratorDestroyEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerState damager;

    public GeneratorDestroyEvent(@Nullable PlayerState damager, @NotNull  Generator generator) {
        super(generator);
        this.damager = damager;
    }

    public GeneratorDestroyEvent(@Nullable OrestackPlayer damager, @NotNull  Generator generator) {
        super(generator);
        this.damager = damager;
    }

    public @Nullable PlayerState getDamager() {
        return damager;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
