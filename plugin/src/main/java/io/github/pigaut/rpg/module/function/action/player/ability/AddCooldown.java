package io.github.pigaut.rpg.module.function.action.player.ability;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class AddCooldown implements PlayerStateAction {

    private final String name;
    private final Delay duration;

    public AddCooldown(@NotNull String name, @NotNull Delay duration) {
        this.name = name;
        this.duration = duration;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        int ticksDuration = duration.toTicks();
        playerState.addCooldown(name, ticksDuration);
    }

}
