package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;

public abstract class PlayerGeneratorEvent extends GeneratorEvent {

    private final OrestackPlayer player;

    protected PlayerGeneratorEvent(OrestackPlayer player, Generator generator) {
        super(generator);
        this.player = player;
    }

    public OrestackPlayer getPlayer() {
        return player;
    }

}
