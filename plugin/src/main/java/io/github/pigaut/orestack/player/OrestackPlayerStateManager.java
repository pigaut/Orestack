package io.github.pigaut.orestack.player;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.player.*;
import org.jetbrains.annotations.*;

public class OrestackPlayerStateManager extends PlayerStateManager<OrestackPlugin, OrestackPlayer> {

    public OrestackPlayerStateManager(@NotNull OrestackPlugin plugin) {
        super(plugin, OrestackPlayer::new);
    }

}
