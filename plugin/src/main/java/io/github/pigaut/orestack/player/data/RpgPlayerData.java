package io.github.pigaut.orestack.player.data;

import io.github.pigaut.orestack.collection.Collection;
import io.github.pigaut.voxel.player.data.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;


public interface RpgPlayerData extends PlayerData {

    @NotNull
    Set<Collection> getItemCollections();

    @Nullable
    Collection getItemCollection(@NotNull String name);

    @Nullable
    Collection getItemCollection(@NotNull ItemStack item);

}
