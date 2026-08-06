package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.module.function.condition.player.data.*;
import io.github.pigaut.rpg.module.function.condition.player.data.*;
import io.github.pigaut.rpg.player.data.*;
import org.jetbrains.annotations.*;

public interface RpgPlayerDataCondition extends PlayerDataCondition {

    @Nullable
    Boolean evaluate(@NotNull RpgPlayerData playerData);

    @Override
    default @Nullable Boolean evaluate(@NotNull PlayerData playerData) {
        if (playerData instanceof RpgPlayerData rpgPlayerData) {
            return evaluate(rpgPlayerData);
        }
        return null;
    }

}
