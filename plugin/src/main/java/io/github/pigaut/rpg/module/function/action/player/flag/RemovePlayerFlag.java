package io.github.pigaut.rpg.module.function.action.player.flag;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class RemovePlayerFlag implements PlayerStateAction.Executor {

    private final String flag;

    public RemovePlayerFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.removeFlag(flag);
    }

}
