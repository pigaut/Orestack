package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class PlayerHasCooldown implements PlayerStateCondition.Predicate {

    private final String name;

    public PlayerHasCooldown(String name) {
        this.name = name;
    }

    @Override
    public boolean test(@NotNull PlayerState playerState) {
        return playerState.hasCooldown(name);
    }

}
