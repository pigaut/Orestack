package io.github.pigaut.rpg.module.function.condition.player.state;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface PlayerStateCondition extends Condition {

    @Nullable Boolean evaluate(@NotNull PlayerState playerState);

    @Override
    default @Nullable Boolean isMet(@NotNull Context context) {
        PlayerState playerState = context.playerState();
        return playerState != null ? evaluate(playerState) : null;
    }

}
