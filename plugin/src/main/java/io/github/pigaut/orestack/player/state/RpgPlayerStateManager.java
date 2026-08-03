package io.github.pigaut.orestack.player.state;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.player.state.*;
import org.jetbrains.annotations.*;

public class RpgPlayerStateManager extends PlayerStateManager<RpgPlayerState> {

    public RpgPlayerStateManager(@NotNull OrestackPlugin plugin) {
        super(plugin, player -> new RpgPlayerState(plugin, player));
    }

}
