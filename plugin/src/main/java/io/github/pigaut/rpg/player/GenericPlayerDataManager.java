package io.github.pigaut.rpg.player;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class GenericPlayerDataManager extends PlayerDataManager<SimplePlayerData> {

    public GenericPlayerDataManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, player -> new SimplePlayerData(player.getUniqueId()));
    }

}
