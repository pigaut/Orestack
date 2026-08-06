package io.github.pigaut.rpg.player.data;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.recipe.*;
import org.jetbrains.annotations.*;

public class RpgPlayerDataManger extends PlayerDataManager<SimpleRpgPlayerData> {

    public RpgPlayerDataManger(@NotNull RpgMakerPlugin plugin) {
        super(plugin, player -> new SimpleRpgPlayerData(player.getUniqueId()));
        addDataRepository(new UnlockedRecipesRepository<>(plugin));
        addDataRepository(new PlayerCollectionRepository(plugin));
        addDataRepository(new PlayerSkillRepository(plugin));
    }

}
