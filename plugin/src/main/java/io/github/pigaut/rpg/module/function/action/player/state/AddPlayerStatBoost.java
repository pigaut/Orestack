package io.github.pigaut.rpg.module.function.action.player.state;

import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class AddPlayerStatBoost implements PlayerStateAction {

    private final EnhancedPlugin plugin;
    private final Stat stat;
    private final StatModifier modifier;
    private final Delay duration;
    private final @Nullable String id;

    public AddPlayerStatBoost(@NotNull EnhancedPlugin plugin, @NotNull Stat stat, @NotNull StatModifier modifier,
                              @NotNull Delay duration, @Nullable String id) {
        this.plugin = plugin;
        this.stat = stat;
        this.modifier = modifier;
        this.duration = duration;
        this.id = id;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        PlayerStat playerStat = playerState.getStat(stat);
        if (playerStat != null) {
            String id = this.id != null ? this.id : StringUtil.randomName();
            playerStat.setBoost(id, modifier);
            plugin.getScheduler().runTaskLater(duration.toTicks(), () -> {
                playerStat.removeBoost(id);
            });
        }
    }

}
