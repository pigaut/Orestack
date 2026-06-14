package io.github.pigaut.orestack.player.state;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.player.state.*;
import org.jetbrains.annotations.*;

public class OrestackPlayerStateManager extends PlayerStateManager<RpgPlayerState> {

    public OrestackPlayerStateManager(@NotNull OrestackPlugin plugin) {
        super(plugin, player -> new RpgPlayerState(plugin, player));
    }

}
