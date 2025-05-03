package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import org.bukkit.block.*;
import org.bukkit.event.*;

public class GeneratorMineEvent extends PlayerGeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Block blockMined;
    public boolean resetStage = false;

    public GeneratorMineEvent(OrestackPlayer player, Generator generator, Block blockMined) {
        super(player, generator);
        this.blockMined = blockMined;
    }

    public Block getBlockMined() {
        return blockMined;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
