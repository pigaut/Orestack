package io.github.pigaut.rpg.event;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public abstract class PlayerEvent extends CancellableEvent {

    private final Player player;

    protected PlayerEvent(@NotNull Player player) {
        this.player = player;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

}
