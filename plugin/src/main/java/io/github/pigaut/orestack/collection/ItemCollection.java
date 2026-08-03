package io.github.pigaut.orestack.collection;

import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.collection.tier.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemCollection {

    private final CollectionTemplate template;
    private int currentTier = -1;
    private int collectedAmount;

    public ItemCollection(@NotNull CollectionTemplate template, int collectedAmount) {
        this.template = template;
        this.collectedAmount = collectedAmount;
        for (int i = template.getMaxTier(); i >= 0; i--) {
            CollectionTier collectionTier = template.getTier(i);
            if (collectedAmount >= collectionTier.getAmountRequired()) {
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

    public int getPreviousTier() {
        return currentTier <= 0 ? currentTier : currentTier - 1;
    }

    public boolean matchItem(@NotNull ItemStack item) {
        return template.matchItem(item);
    }

    public @NotNull ItemStack getItem() {
        return template.getItem();
    }

    public int getTotalAmount() {
        return collectedAmount;
    }

    public int getNextTierAmount() {
        CollectionTier nextTier = template.getTier(currentTier >= template.getMaxTier() ? currentTier : currentTier + 1);
        return nextTier.getAmountRequired();
    }

    public int getAmountToNextTier() {
        int nextTierAmount = getNextTierAmount();
        return nextTierAmount - collectedAmount;
    }

    public void increaseAmount(@NotNull Context context, int amount) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        boolean unlockedCollection = collectedAmount == 0;
        this.collectedAmount += amount;

        context = context.with(ItemCollection.class, this);

        if (unlockedCollection) {
            Function onUnlock = template.getOnUnlock();
            if (onUnlock != null) {
                onUnlock.run(context);
            }
        }

        CollectionTier nextTier;
        while (currentTier + 1 <= template.getMaxTier()
                && collectedAmount >= (nextTier = template.getTier(currentTier + 1)).getAmountRequired()) {

            currentTier++;

            Function onTierUp = template.getOnTierUp();
            if (onTierUp != null) {
                onTierUp.run(context);
            }

            Function onCompletion = nextTier.getOnCompletion();
            if (onCompletion != null) {
                onCompletion.run(context);
            }
        }
    }

    public void decreaseAmount(@NotNull Context context, int amount) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");
        this.collectedAmount = Math.max(0, this.collectedAmount - amount);

        context = context.with(ItemCollection.class, this);

        while (currentTier >= 0 && collectedAmount < template.getTier(currentTier).getAmountRequired()) {
            CollectionTier lostTier = template.getTier(currentTier);

            currentTier--;

            Function onTierDown = template.getOnTierDown();
            if (onTierDown != null) {
                onTierDown.run(context);
            }

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

    public @Nullable List<String> getNextTierRewards() {
        if (currentTier > getMaxTier()) {
            return null;
        }
        CollectionTier nextTier = getTier(currentTier + 1);
        return nextTier.getRewards();
    }

}
