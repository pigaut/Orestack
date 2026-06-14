package io.github.pigaut.orestack.player.data;

import io.github.pigaut.voxel.player.data.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import io.github.pigaut.orestack.collection.Collection;

import java.util.*;

public class SimpleRpgPlayerData extends SimplePlayerData implements RpgPlayerData {

    private Set<Collection> itemCollections = new HashSet<>();

    public SimpleRpgPlayerData(@NotNull UUID playerId) {
        super(playerId);
    }

    public @Nullable Collection getItemCollection(@NotNull ItemStack item) {
        for (Collection collection : itemCollections) {
            if (collection.matchItem(item)) {
                return collection;
            }
        }
        return null;
    }

    public @Nullable Collection getItemCollection(@NotNull String name) {
        for (Collection collection : itemCollections) {
            if (collection.getName().equalsIgnoreCase(name)) {
                return collection;
            }
        }
        return null;
    }

    public @NotNull Set<Collection> getItemCollections() {
        return new HashSet<>(itemCollections);
    }

    public void setItemCollections(@NotNull Set<Collection> itemCollections) {
        this.itemCollections = itemCollections;
    }

}
