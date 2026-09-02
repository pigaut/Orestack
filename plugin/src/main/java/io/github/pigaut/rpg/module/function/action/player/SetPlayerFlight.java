package io.github.pigaut.rpg.module.function.action.player;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SetPlayerFlight implements PlayerAction.Executor {

    private final boolean flight;

    public SetPlayerFlight(boolean flight) {
        this.flight = flight;
    }

    @Override
    public void execute(@NotNull Player player) {
        player.setAllowFlight(flight);
    }

}
