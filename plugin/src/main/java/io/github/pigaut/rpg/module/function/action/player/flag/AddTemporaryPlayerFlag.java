package io.github.pigaut.rpg.module.function.action.player.flag;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class AddTemporaryPlayerFlag implements PlayerStateAction.Executor {

    private final String flag;
    private final Delay duration;

    public AddTemporaryPlayerFlag(@NotNull String flag, @NotNull Delay duration) {
        this.flag = flag;
        this.duration = duration;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.addTemporaryFlag(flag, duration);
    }

}
