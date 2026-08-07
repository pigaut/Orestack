package io.github.pigaut.rpg.module.function.condition.player;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerIsSneaking implements PlayerCondition {

    @Override
    public Boolean evaluate(@NotNull Player player) {
        return player.isSneaking();
    }

}
