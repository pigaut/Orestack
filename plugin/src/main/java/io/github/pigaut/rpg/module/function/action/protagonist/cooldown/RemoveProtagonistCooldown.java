package io.github.pigaut.rpg.module.function.action.protagonist.cooldown;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class RemoveProtagonistCooldown implements Action {

    private final String name;

    public RemoveProtagonistCooldown(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        if (context.isPlayerProtagonist()) {
            PlayerState playerState = context.playerState();
            if (playerState != null) {
                playerState.removeCooldown(name);
                return FunctionResponse.NONE;
            }
        }
        else {
            Mob mob = context.mob();
            if (mob != null) {
                mob.removeCooldown(name);
                return FunctionResponse.NONE;
            }
        }
        return new FunctionError("Function trigger does not support player/mob protagonist");
    }

}
