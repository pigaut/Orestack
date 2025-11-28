package io.github.pigaut.orestack.player;

import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.boot.*;
import org.jetbrains.annotations.*;

public class OrestackPlayerManager extends PlayerStateManager<OrestackPlayer> {

    public OrestackPlayerManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, OrestackPlayer::new);
    }

}
