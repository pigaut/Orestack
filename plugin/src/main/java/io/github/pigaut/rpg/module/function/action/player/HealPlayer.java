package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class HealPlayer implements PlayerAction.Executor {

    private final Amount amount;

    public HealPlayer(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player) {
        PlayerUtil.heal(player, amount.doubleValue());
    }

}
