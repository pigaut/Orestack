package io.github.pigaut.orestack.player;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.voxel.plugin.*;
import org.jetbrains.annotations.*;

public class OrestackPlayerDataManger extends PlayerDataManager<GenericPlayerData> {

    public OrestackPlayerDataManger(@NotNull OrestackPlugin plugin) {
        super(plugin, player -> new GenericPlayerData(player.getUniqueId()));
        addDataRepository(new UnlockedRecipesRepository(plugin));
        addDataRepository(new PlayerCollectionRepository(plugin));
    }

}
