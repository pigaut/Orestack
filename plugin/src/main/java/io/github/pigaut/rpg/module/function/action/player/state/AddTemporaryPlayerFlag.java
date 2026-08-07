package io.github.pigaut.rpg.module.function.action.player.state;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class AddTemporaryPlayerFlag implements PlayerStateAction {

    private final String flag;
    private final int duration;

    public AddTemporaryPlayerFlag(String flag, int duration) {
        this.flag = flag;
        this.duration = duration;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.addTemporaryFlag(flag, duration);
    }

}
