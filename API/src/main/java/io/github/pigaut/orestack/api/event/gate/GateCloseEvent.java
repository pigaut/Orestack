package io.github.pigaut.orestack.api.event.gate;

import org.bukkit.*;
import org.bukkit.event.*;

public class GateCloseEvent extends GateTransitionEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GateCloseEvent(Location origin, String gate, int phase) {
        super(origin, gate, phase, false);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
