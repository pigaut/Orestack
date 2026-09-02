package io.github.pigaut.rpg.module.function.action.player.stat;

import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class ClearPlayerStatBoosts implements PlayerStateAction.Executor {

    private final Stat stat;

    public ClearPlayerStatBoosts(Stat stat) {
        this.stat = stat;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        PlayerStat playerStat = playerState.getStat(stat);
        if (playerStat != null) {
            playerStat.clearBoosts();
        }
    }
}
