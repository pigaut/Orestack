package io.github.pigaut.rpg.module.function.condition.player.state;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class PlayerHasStatBoost implements PlayerStateCondition {

    private final Stat stat;
    private final String id;

    public PlayerHasStatBoost(@NotNull Stat stat, @NotNull String id) {
        this.stat = stat;
        this.id = id;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull PlayerState playerState) {
        PlayerStat playerStat = playerState.getStat(stat);
        if (playerStat == null) {
            return null;
        }
        return playerStat.hasBoost(id);
    }

}
