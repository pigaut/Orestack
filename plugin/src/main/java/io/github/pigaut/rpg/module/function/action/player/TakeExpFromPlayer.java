package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class TakeExpFromPlayer implements PlayerAction.Executor {

    private final Amount amount;

    public TakeExpFromPlayer(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player) {
        player.giveExp(-amount.intValue());
    }

}
