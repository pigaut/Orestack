package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerHasExp implements PlayerCondition {

    private final Amount exp;

    public PlayerHasExp(Amount exp) {
        this.exp = exp;
    }

    @Override
    public Boolean evaluate(@NotNull Player player) {
        return exp.match(player.getTotalExperience());
    }

}
