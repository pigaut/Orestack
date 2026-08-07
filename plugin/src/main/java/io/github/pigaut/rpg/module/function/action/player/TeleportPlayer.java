package io.github.pigaut.rpg.module.function.action.player;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class TeleportPlayer implements PlayerAction {

    private final Location destination;

    public TeleportPlayer(Location destination) {
        this.destination = destination;
    }

    @Override
    public void execute(@NotNull Player player) {
        player.teleport(destination);
    }

}
