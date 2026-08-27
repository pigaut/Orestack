package io.github.pigaut.rpg.player.data;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.player.data.base.*;
import io.github.pigaut.rpg.player.data.repository.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class PlayerDataManager extends EnhancedPlayerDataManager<SimplePlayerData> {

    public PlayerDataManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, player -> new SimplePlayerData(player.getUniqueId()));
        addDataRepository(new UnlockedRecipesRepository<>(plugin));
        addDataRepository(new PlayerCollectionRepository(plugin));
        addDataRepository(new PlayerSkillRepository(plugin));
    }

}
