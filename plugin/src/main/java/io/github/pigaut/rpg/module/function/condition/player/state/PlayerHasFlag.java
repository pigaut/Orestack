package io.github.pigaut.rpg.module.function.condition.player.state;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class PlayerHasFlag implements PlayerStateCondition {

    private final String flag;

    public PlayerHasFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public Boolean evaluate(@NotNull PlayerState playerState) {
        return playerState.hasFlag(flag);
    }

}
