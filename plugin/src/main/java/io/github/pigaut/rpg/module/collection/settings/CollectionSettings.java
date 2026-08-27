package io.github.pigaut.rpg.module.collection.settings;

import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.event.drop.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface CollectionSettings {

    @NotNull
    Set<ItemSpawnReason> getCollectionSources();

    boolean isCollectionSourceEnabled(@NotNull ItemSpawnReason source);

    @NotNull
    ProgressBar getCollectionProgressBar();

}
