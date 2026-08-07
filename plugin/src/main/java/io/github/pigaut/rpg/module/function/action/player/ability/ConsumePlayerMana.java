package io.github.pigaut.rpg.module.function.action.player.ability;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class ConsumePlayerMana implements PlayerStateAction {

    private final Amount amount;

    public ConsumePlayerMana(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.setMana(playerState.getMana() - amount.intValue());
    }

}
