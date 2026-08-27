package io.github.pigaut.rpg.player.state;

import io.github.pigaut.rpg.player.state.base.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class PlayerStateManager extends EnhancedPlayerStateManager<PlayerState> {

    public PlayerStateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, player -> new SimplePlayerState(plugin, player));
    }

}
