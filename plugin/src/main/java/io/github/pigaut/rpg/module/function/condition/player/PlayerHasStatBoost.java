package io.github.pigaut.rpg.module.function.condition.player;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class PlayerHasStatBoost implements PlayerStateCondition.Predicate {

    private final Stat stat;
    private final String id;

    public PlayerHasStatBoost(@NotNull Stat stat, @NotNull String id) {
        this.stat = stat;
        this.id = id;
    }

    @Override
    public boolean test(@NotNull PlayerState playerState) {
        PlayerStat playerStat = playerState.getStat(stat);
        if (playerStat == null) {
            return false;
        }
        return playerStat.hasBoost(id);
    }

}
