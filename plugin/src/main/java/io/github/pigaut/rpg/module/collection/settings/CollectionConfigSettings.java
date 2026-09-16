package io.github.pigaut.rpg.module.collection.settings;

import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.event.drop.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionConfigSettings implements CollectionSettings {

    private final EnhancedPlugin plugin;
    private final Settings settings;

    public CollectionConfigSettings(@NotNull EnhancedPlugin plugin, @NotNull Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    private Set<ItemSpawnReason> collectionSources;
    private ProgressBar collectionProgressBar;

    public void loadConfiguration(@NotNull ConfigSection config) {
        collectionSources = new HashSet<>(config.getAll("collection-item-sources", ItemSpawnReason.class)
                .withDefault(List.of()));

        collectionProgressBar = config.get("collection-progress-bar", ProgressBar.class)
                .withDefault(ProgressBar.EMPTY);
    }

    @Override
    public @NotNull Set<ItemSpawnReason> getCollectionSources() {
        settings.checkLoaded(collectionSources);
        return new HashSet<>(collectionSources);
    }

    @Override
    public boolean isCollectionSourceEnabled(@NotNull ItemSpawnReason source) {
        settings.checkLoaded(collectionSources);
        return collectionSources.contains(source);
    }

    @Override
    public @NotNull ProgressBar getCollectionProgressBar() {
        settings.checkLoaded(collectionProgressBar);
        return collectionProgressBar;
    }

}
