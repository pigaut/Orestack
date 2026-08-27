package io.github.pigaut.rpg.player.state.base;

import io.github.pigaut.rpg.player.data.base.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface EnhancedPlayerState {

    @NotNull
    EnhancedPlugin getPlugin();

    @NotNull
    EnhancedPlayerData getPlayerData();

    @NotNull
    UUID getUniqueId();

    @NotNull
    String getName();

    @Nullable
    Player asPlayer();

    @Nullable
    OfflinePlayer asOfflinePlayer();

}
