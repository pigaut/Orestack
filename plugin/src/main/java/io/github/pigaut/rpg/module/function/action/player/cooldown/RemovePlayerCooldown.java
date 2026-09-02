package io.github.pigaut.rpg.module.function.action.player.cooldown;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class RemovePlayerCooldown implements PlayerStateAction.Executor {

    private final String name;

    public RemovePlayerCooldown(String name) {
        this.name = name;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.removeCooldown(name);
    }

}
