package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class PlayerHasFlag implements PlayerStateCondition.Predicate {

    private final String flag;

    public PlayerHasFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public boolean test(@NotNull PlayerState playerState) {
        return playerState.hasFlag(flag);
    }

}
