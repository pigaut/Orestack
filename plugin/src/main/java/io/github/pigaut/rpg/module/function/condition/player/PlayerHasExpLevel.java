package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerHasExpLevel implements PlayerCondition.Predicate {

    private final Amount level;

    public PlayerHasExpLevel(Amount level) {
        this.level = level;
    }

    @Override
    public boolean test(@NotNull Player player) {
        return level.match(player.getLevel());
    }

}
