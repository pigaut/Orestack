package io.github.pigaut.orestack.collection.template;

import io.github.pigaut.orestack.collection.tier.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.plugin.manager.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionTemplate implements Identifiable {

    private final String name;
    private final String group;
    private final ItemStack item;
    private final List<CollectionTier> collectionTiers;
    private final Function onUnlock;
    private final Function onLock;

    public CollectionTemplate(@NotNull String name, @Nullable String group,
                              @NotNull ItemStack item, @NotNull List<CollectionTier> tiers,
                              @Nullable Function onUnlock, @Nullable Function onLock) {
        this.name = name;
        this.group = group;
        this.item = item;
        this.collectionTiers = tiers;
        this.onUnlock = onUnlock;
        this.onLock = onLock;
    }

    public @NotNull ItemStack getItem() {
        return item.clone();
    }

    public boolean matchItem(@NotNull ItemStack item) {
        return this.item.isSimilar(item);
    }

    public int getMaxTier() {
        return collectionTiers.size() - 1;
    }

    public @NotNull CollectionTier getTier(int tier) {
        return collectionTiers.get(tier);
    }

    public @Nullable CollectionTier getTierByAmount(int collectedAmount) {
        for (int i = collectionTiers.size() - 1; i >= 0; i--) {
            CollectionTier collectionTier = collectionTiers.get(i);
            if (collectedAmount >= collectionTier.getAmount()) {
                return collectionTier;
            }
        }
        return null;
    }

    public @NotNull List<CollectionTier> getCollectionTiers() {
        return new ArrayList<>(collectionTiers);
    }

    public @Nullable Function getOnUnlock() {
        return onUnlock;
    }

    public @Nullable Function getOnLock() {
        return onLock;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }
}
