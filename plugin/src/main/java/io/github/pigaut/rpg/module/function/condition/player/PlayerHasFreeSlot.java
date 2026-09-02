package io.github.pigaut.rpg.module.function.condition.player;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerHasFreeSlot implements PlayerCondition.Predicate {

    @Override
    public boolean test(@NotNull Player player) {
        return player.getInventory().firstEmpty() != -1;
    }

}
