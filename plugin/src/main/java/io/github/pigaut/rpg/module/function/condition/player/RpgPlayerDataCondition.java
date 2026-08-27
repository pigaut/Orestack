package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.module.function.condition.player.data.*;
import io.github.pigaut.rpg.player.data.base.*;
import org.jetbrains.annotations.*;

public interface RpgPlayerDataCondition extends PlayerDataCondition {

    @Nullable
    Boolean evaluate(@NotNull PlayerData playerData);

    @Override
    default @Nullable Boolean evaluate(@NotNull EnhancedPlayerData playerData) {
        if (playerData instanceof PlayerData rpgPlayerData) {
            return evaluate(rpgPlayerData);
        }
        return null;
    }

}
