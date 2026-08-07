package io.github.pigaut.rpg.module.function.condition.player.data;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.player.data.*;
import org.jetbrains.annotations.*;

public interface PlayerDataCondition extends Condition {

    @Nullable
    Boolean evaluate(@NotNull PlayerData playerData);

    @Override
    @Nullable
    default Boolean evaluate(@NotNull Context context) {
        PlayerData playerData = context.playerData();
        if (playerData == null) {
            return null;
        }
        return evaluate(playerData);
    }

}
