package io.github.pigaut.rpg.player.data;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerDataFactory<T extends PlayerData> {

    @NotNull
    T create(@NotNull Player player);

}