package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import org.bukkit.block.*;
import org.bukkit.event.*;

public class GeneratorMineEvent extends PlayerGeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Block blockMined;
    private boolean keepStage;

    public GeneratorMineEvent(OrestackPlayer player, Generator generator, Block blockMined, boolean keepStage) {
        super(player, generator);
        this.blockMined = blockMined;
        this.keepStage = keepStage;
    }

    public Block getBlockMined() {
        return blockMined;
    }

    public boolean isKeepStage() {
        return keepStage;
    }

    public void setKeepStage(boolean keepStage) {
        this.keepStage = keepStage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
