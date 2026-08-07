package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerHasExpLevel implements PlayerCondition {

    private final Amount level;

    public PlayerHasExpLevel(Amount level) {
        this.level = level;
    }

    @Override
    public Boolean evaluate(@NotNull Player player) {
        return level.match(player.getLevel());
    }

}
