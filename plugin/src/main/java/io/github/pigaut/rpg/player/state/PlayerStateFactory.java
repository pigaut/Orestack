package io.github.pigaut.rpg.player.state;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerStateFactory<T extends PlayerState> {

    @NotNull
    T create(Player player);

}
