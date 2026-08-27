package io.github.pigaut.rpg.player.data;

import io.github.pigaut.rpg.player.data.base.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerDataFactory<T extends EnhancedPlayerData> {

    @NotNull
    T create(@NotNull Player player);

}