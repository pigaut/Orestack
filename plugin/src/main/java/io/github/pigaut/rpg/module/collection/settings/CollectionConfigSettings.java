package io.github.pigaut.rpg.module.collection.settings;

import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionConfigSettings implements CollectionSettings {

    private final EnhancedPlugin plugin;

    private Set<ItemSpawnReason> collectionSources;
    private ProgressBar collectionProgressBar;

    public CollectionConfigSettings(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfiguration(@NotNull ConfigSection config) {
        collectionSources = new HashSet<>(config.getAll("collection-item-sources", ItemSpawnReason.class)
                .withDefault(List.of()));

        collectionProgressBar = config.get("collection-progress-bar", ProgressBar.class)
                .withDefault(ProgressBar.EMPTY);
    }

    @Override
    public @NotNull Set<ItemSpawnReason> getCollectionSources() {
        return new HashSet<>(collectionSources);
    }

    @Override
    public boolean isCollectionSourceEnabled(@NotNull ItemSpawnReason source) {
        return collectionSources.contains(source);
    }

    @Override
    public @NotNull ProgressBar getCollectionProgressBar() {
        return collectionProgressBar;
    }

}
