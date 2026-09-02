package io.github.pigaut.rpg.module.function.action.protagonist.cooldown;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.function.action.protagonist.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class AddProtagonistCooldown implements Action {

    private final String name;
    private final Delay duration;

    public AddProtagonistCooldown(@NotNull String name, @NotNull Delay duration) {
        this.name = name;
        this.duration = duration;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        if (context.isPlayerProtagonist()) {
            PlayerState playerState = context.playerState();
            if (playerState != null) {
                playerState.addCooldown(name, duration);
                return FunctionResponse.NONE;
            }
        }
        else {
            Mob mob = context.mob();
            if (mob != null) {
                mob.addCooldown(name, duration);
                return FunctionResponse.NONE;
            }
        }

        return new FunctionError("Function trigger does not support player/mob protagonist");
    }

}
