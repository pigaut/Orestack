package io.github.pigaut.orestack.player.data;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.voxel.module.recipe.*;
import io.github.pigaut.voxel.player.data.*;
import org.jetbrains.annotations.*;

public class RpgPlayerDataManger extends PlayerDataManager<SimpleRpgPlayerData> {

    public RpgPlayerDataManger(@NotNull OrestackPlugin plugin) {
        super(plugin, player -> new SimpleRpgPlayerData(player.getUniqueId()));
        addDataRepository(new UnlockedRecipesRepository<>(plugin));
        addDataRepository(new PlayerCollectionRepository(plugin));
        addDataRepository(new PlayerSkillRepository(plugin));
    }

}
