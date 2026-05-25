package io.github.pigaut.orestack.player;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.player.state.*;
import org.jetbrains.annotations.*;

public class OrestackPlayerStateManager extends PlayerStateManager<OrestackPlayer> {

    public OrestackPlayerStateManager(@NotNull OrestackPlugin plugin) {
        super(plugin, player -> new OrestackPlayer(plugin, player));
    }

}
