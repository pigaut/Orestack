package io.github.pigaut.rpg.module.function.condition.player.ability;

import io.github.pigaut.rpg.module.function.condition.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.condition.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class PlayerHasCooldown implements PlayerStateCondition {

    private final String name;

    public PlayerHasCooldown(String name) {
        this.name = name;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull PlayerState playerState) {
        return playerState.hasCooldown(name);
    }

}
