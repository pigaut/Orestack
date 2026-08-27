package io.github.pigaut.rpg.player.data.base;

import org.jetbrains.annotations.*;

import java.util.*;

public interface EnhancedPlayerData {

    @NotNull
    UUID getUniqueId();

    boolean isLoaded();

    void setLoaded(boolean loaded);

    void runWhenLoaded(@NotNull Runnable task);

}
