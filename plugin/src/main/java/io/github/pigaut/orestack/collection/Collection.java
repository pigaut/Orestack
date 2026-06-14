package io.github.pigaut.orestack.collection;

import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.collection.tier.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class Collection {

    private final CollectionTemplate template;
    private int currentTier = -1;
    private int collectedAmount;

    public Collection(@NotNull CollectionTemplate template, int collectedAmount) {
        this.template = template;
        this.collectedAmount = collectedAmount;
        for (int i = template.getMaxTier(); i >= 0; i--) {
            CollectionTier collectionTier = template.getTier(i);
            if (collectedAmount >= collectionTier.getAmount()) {
                currentTier = i;
                break;
            }
        }
    }

    public @NotNull String getName() {
        return template.getName();
    }

    public @Nullable String getGroup() {
        return template.getGroup();
    }

    public @NotNull CollectionTemplate getTemplate() {
        return template;
    }

    public boolean isUnlocked() {
        return collectedAmount > 0;
    }

    public boolean isFirstTierUnlocked() {
        return currentTier >= 0;
    }

    public @Nullable CollectionTier getTier() {
        return currentTier >= 0 ? template.getTier(currentTier) : null;
    }

    public @NotNull CollectionTier getTier(int tier) {
        return template.getTier(tier);
    }

    public int getCurrentTier() {
        return currentTier;
    }

    public int getNextTier() {
        return currentTier >= template.getMaxTier() ? currentTier : currentTier + 1;
    }

    public boolean matchItem(@NotNull ItemStack item) {
        return template.matchItem(item);
    }

    public @NotNull ItemStack getItem() {
        return template.getItem();
    }

    public int getCollectedAmount() {
        return collectedAmount;
    }

    public int getNextTierAmount() {
        CollectionTier nextTier = template.getTier(currentTier >= template.getMaxTier() ? currentTier : currentTier + 1);
        return nextTier.getAmount();
    }

    public int getAmountToNextTier() {
        int nextTierAmount = getNextTierAmount();
        return nextTierAmount - collectedAmount;
    }

    public void increaseAmount(@NotNull Context context, int amount) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        boolean unlockedCollection = collectedAmount == 0;
        this.collectedAmount += amount;

        context = context.with(Collection.class, this);

        if (unlockedCollection) {
            Function onUnlock = template.getOnUnlock();
            if (onUnlock != null) {
                onUnlock.run(context);
            }
        }

        CollectionTier nextTier;
        while (currentTier + 1 <= template.getMaxTier()
                && collectedAmount >= (nextTier = template.getTier(currentTier + 1)).getAmount()) {

            currentTier++;

            Function onCompletion = nextTier.getOnCompletion();
            if (onCompletion != null) {
                onCompletion.run(context);
            }
        }
    }

    public void decreaseAmount(@NotNull Context context, int amount) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        this.collectedAmount = Math.max(0, this.collectedAmount - amount);

        context = context.with(Collection.class, this);

        while (currentTier >= 0 && collectedAmount < template.getTier(currentTier).getAmount()) {
            CollectionTier lostTier = template.getTier(currentTier);

            currentTier--;

            Function onRegression = lostTier.getOnRegression();
            if (onRegression != null) {
                onRegression.run(context);
            }
        }

        if (collectedAmount == 0) {
            Function onLock = template.getOnLock();
            if (onLock != null) {
                onLock.run(context);
            }
        }
    }

    public int getMaxTier() {
        return template.getMaxTier();
    }

}
