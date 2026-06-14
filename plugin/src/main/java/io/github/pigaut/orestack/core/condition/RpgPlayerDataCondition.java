package io.github.pigaut.orestack.core.condition;

import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.voxel.data.function.condition.player.data.*;
import io.github.pigaut.voxel.player.data.*;
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
