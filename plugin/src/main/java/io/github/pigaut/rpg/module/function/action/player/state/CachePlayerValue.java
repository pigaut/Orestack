package io.github.pigaut.rpg.module.function.action.player.state;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class CachePlayerValue implements PlayerStateAction.Executor {

    private final String id;
    private final Object value;

    public CachePlayerValue(String id, Object value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.saveCache(id, value);
    }

}
