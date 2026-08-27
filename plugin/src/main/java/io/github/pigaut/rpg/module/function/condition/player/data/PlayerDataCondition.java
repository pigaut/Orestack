package io.github.pigaut.rpg.module.function.condition.player.data;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.player.data.base.*;
import org.jetbrains.annotations.*;

public interface PlayerDataCondition extends Condition {

    @Nullable
    Boolean evaluate(@NotNull EnhancedPlayerData playerData);

    @Override
    @Nullable
    default Boolean isMet(@NotNull Context context) {
        EnhancedPlayerData playerData = context.playerData();
        if (playerData == null) {
            return null;
        }
        return evaluate(playerData);
    }

}
