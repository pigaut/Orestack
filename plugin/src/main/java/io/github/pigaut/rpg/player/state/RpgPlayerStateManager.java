package io.github.pigaut.rpg.player.state;

import io.github.pigaut.rpg.*;
import org.jetbrains.annotations.*;

public class RpgPlayerStateManager extends PlayerStateManager<RpgPlayerState> {

    public RpgPlayerStateManager(@NotNull RpgMakerPlugin plugin) {
        super(plugin, player -> new RpgPlayerState(plugin, player));
    }

}
