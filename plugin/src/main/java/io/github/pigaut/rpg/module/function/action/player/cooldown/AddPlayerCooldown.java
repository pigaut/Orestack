package io.github.pigaut.rpg.module.function.action.player.cooldown;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class AddPlayerCooldown implements PlayerStateAction.Executor {

    private final String name;
    private final Delay duration;

    public AddPlayerCooldown(@NotNull String name, @NotNull Delay duration) {
        this.name = name;
        this.duration = duration;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.addCooldown(name, duration);
    }

}
