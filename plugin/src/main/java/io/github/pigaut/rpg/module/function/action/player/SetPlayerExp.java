package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SetPlayerExp implements PlayerAction {

    private final Amount amount;

    public SetPlayerExp(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player) {
        player.setExp(amount.intValue());
    }

}
