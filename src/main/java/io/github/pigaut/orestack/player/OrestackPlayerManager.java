package io.github.pigaut.orestack.player;

import io.github.pigaut.voxel.player.*;

public class OrestackPlayerManager extends PlayerManager<OrestackPlayer> {

    public OrestackPlayerManager() {
        super(OrestackPlayer::new);
    }

}
