package io.github.pigaut.rpg.module.function.action.player.ability;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class RemoveCooldown implements PlayerStateAction {

    private final String name;

    public RemoveCooldown(String name) {
        this.name = name;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        playerState.removeCooldown(name);
    }

}
