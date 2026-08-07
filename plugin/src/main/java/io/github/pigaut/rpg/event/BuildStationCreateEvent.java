package io.github.pigaut.rpg.event;

import io.github.pigaut.rpg.core.buildstation.*;
import io.github.pigaut.rpg.core.buildstation.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BuildStationCreateEvent extends CancellableEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final BuildStation buildStation;
    private final Set<Location> occupiedBlocks;

    public BuildStationCreateEvent(@NotNull BuildStation buildStation, Set<Location> occupiedBlocks) {
        this.buildStation = buildStation;
        this.occupiedBlocks = occupiedBlocks;
    }

    public @NotNull BuildStation getBuildStation() {
        return buildStation;
    }

    public Set<Location> getOccupiedBlocks() {
        return occupiedBlocks;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
