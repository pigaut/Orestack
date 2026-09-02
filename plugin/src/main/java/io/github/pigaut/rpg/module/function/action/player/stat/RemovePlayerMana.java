package io.github.pigaut.rpg.module.function.action.player.stat;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class RemovePlayerMana implements PlayerStateAction.Executor {

    private final Amount amount;

    public RemovePlayerMana(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.setMana(playerState.getMana() - amount.intValue());
    }

}
