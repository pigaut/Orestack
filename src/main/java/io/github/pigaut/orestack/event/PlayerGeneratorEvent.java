package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import org.bukkit.block.*;

public abstract class PlayerGeneratorEvent extends GeneratorEvent {

    private final OrestackPlayer player;

    protected PlayerGeneratorEvent(OrestackPlayer player, Block block, Generator generator, GeneratorStage stage) {
        super(block, generator, stage);
        this.player = player;
    }

    public OrestackPlayer getPlayer() {
        return player;
    }

}
