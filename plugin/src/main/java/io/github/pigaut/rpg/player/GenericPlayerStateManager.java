package io.github.pigaut.rpg.player;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class GenericPlayerStateManager extends PlayerStateManager<GenericPlayerState> {

    public GenericPlayerStateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, player -> new GenericPlayerState(plugin, player));
    }

}
