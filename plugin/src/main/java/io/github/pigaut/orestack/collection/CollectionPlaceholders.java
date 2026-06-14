package io.github.pigaut.orestack.collection;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.collection.tier.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.core.placeholder.*;
import io.github.pigaut.voxel.core.progressbar.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.voxel.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CollectionPlaceholders {

    public static void registerAll(@NotNull OrestackPlugin plugin) {
        PlaceholderRegistry placeholders = plugin.getPlaceholders();

        OrestackSettings settings = plugin.getSettings();
        placeholders.register("collections_unlocked", context -> {
            PlayerData playerData = context.playerData();
            if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                return null;
            }

            int collectionsUnlocked = 0;
            for (Collection collection : rpgPlayerData.getItemCollections()) {
                if (collection.isUnlocked()) {
                    collectionsUnlocked++;
                }
            }
            return collectionsUnlocked;
        });

        placeholders.register("collections_count", context -> {
            PlayerData playerData = context.playerData();
            if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                return null;
            }
            return rpgPlayerData.getItemCollections().size();
        });

        placeholders.register("collections_unlocked_percent", context -> {
            PlayerData playerData = context.playerData();
            if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                return null;
            }
            Set<Collection> collections = rpgPlayerData.getItemCollections();
            int collectionsUnlocked = 0;
            for (Collection collection : collections) {
                if (collection.isUnlocked()) {
                    collectionsUnlocked++;
                }
            }
            double percentage = ((double) collectionsUnlocked / collections.size()) * 100;
            return String.format("%.1f", percentage);
        });

        for (ProgressBar progressBar : plugin.getSettings().getProgressBars()) {
            placeholders.register("collections_progress_bar:" + progressBar.getId(), context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Set<Collection> collections = rpgPlayerData.getItemCollections();
                int collectionsUnlocked = 0;
                for (Collection collection : collections) {
                    if (collection.isUnlocked()) {
                        collectionsUnlocked++;
                    }
                }

                int percentage = Percentage.of(collectionsUnlocked, collections.size());
                return progressBar.getBarByProgress(percentage);
            });
        }

        for (CollectionTemplate collectionTemplate : plugin.getCollectionTemplates().getAll()) {
            String collectionName = collectionTemplate.getName();
            placeholders.register(collectionName + "_collection_amount_left", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Collection collection = rpgPlayerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getAmountToNextTier();
            });

            placeholders.register(collectionName + "_collection_progress", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Collection collection = rpgPlayerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getCollectedAmount();
            });

            placeholders.register(collectionName + "_collection_progress_percent", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Collection collection = rpgPlayerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }

                return Percentage.of(collection.getCollectedAmount(), collection.getNextTierAmount());
            });

            for (ProgressBar progressBar : plugin.getSettings().getProgressBars()) {
                placeholders.register(collectionName + "_collection_progress_bar:" + progressBar.getId(), context -> {
                    PlayerData playerData = context.playerData();
                    if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                        return null;
                    }
                    Collection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection == null) {
                        return null;
                    }

                    int percentage = Percentage.of(collection.getCollectedAmount(), collection.getNextTierAmount());
                    return progressBar.getBarByProgress(percentage);
                });
            }

            placeholders.register(collectionName + "_collection_tier_up_requirement", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Collection collection = rpgPlayerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getNextTierAmount();
            });

            placeholders.register(collectionName + "_collection_tier", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Collection collection = rpgPlayerData.getItemCollection(collectionName);
                if (collection == null || !collection.isFirstTierUnlocked()) {
                    return null;
                }
                return collection.getCurrentTier() + 1;
            });

            placeholders.register(collectionName + "_collection_next_tier", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Collection collection = rpgPlayerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getNextTier() + 1;
            });

            for (int tierIndex = 0; tierIndex <= collectionTemplate.getMaxTier(); tierIndex++) {
                int tierUpAmount = collectionTemplate.getTier(tierIndex).getAmount();

                placeholders.register(collectionName + "_collection_tier_" + (tierIndex + 1) + "_requirement", context -> {
                    return tierUpAmount;
                });

                placeholders.register(collectionName + "_collection_tier_" + (tierIndex + 1) + "_progress_percent", context -> {
                    PlayerData playerData = context.playerData();
                    if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                        return null;
                    }
                    Collection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection == null) {
                        return null;
                    }
                    return Percentage.asString(collection.getCollectedAmount(), tierUpAmount);
                });

                for (ProgressBar progressBar : plugin.getSettings().getProgressBars()) {
                    placeholders.register(collectionName + "_collection_tier_" + (tierIndex + 1) + "_progress_bar:" + progressBar.getId(), context -> {
                        PlayerData playerData = context.playerData();
                        if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                            return null;
                        }
                        Collection collection = rpgPlayerData.getItemCollection(collectionName);
                        if (collection == null) {
                            return null;
                        }
                        int percentage = Percentage.of(collection.getCollectedAmount(), tierUpAmount);
                        return progressBar.getBarByProgress(percentage);
                    });
                }

            }
        }

        for (String groupName : plugin.getCollectionTemplates().getAllGroups()) {
            placeholders.register(groupName + "_collections_unlocked", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                int collectionsUnlocked = 0;
                for (Collection collection : rpgPlayerData.getItemCollections()) {
                    String group = collection.getGroup();
                    if (group != null && group.equals(groupName) && collection.isUnlocked()) {
                        collectionsUnlocked++;
                    }
                }

                return collectionsUnlocked;
            });

            placeholders.register(groupName + "_collections_count", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                int collectionsCount = 0;
                for (Collection collection : rpgPlayerData.getItemCollections()) {
                    String group = collection.getGroup();
                    if (group != null && group.equals(groupName)) {
                        collectionsCount++;
                    }
                }

                return collectionsCount;
            });

            placeholders.register(groupName + "_collections_unlocked_percent", context -> {
                PlayerData playerData = context.playerData();
                if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                    return null;
                }
                Set<Collection> collections = rpgPlayerData.getItemCollections();
                int collectionsUnlocked = 0;
                for (Collection collection : rpgPlayerData.getItemCollections()) {
                    String group = collection.getGroup();
                    if (group != null && group.equals(groupName) && collection.isUnlocked()) {
                        collectionsUnlocked++;
                    }
                }

                return Percentage.asString(collectionsUnlocked, collections.size());
            });

            for (ProgressBar progressBar : plugin.getSettings().getProgressBars()) {
                placeholders.register(groupName + "_collections_progress_bar:" + progressBar.getId(), context -> {
                    PlayerData playerData = context.playerData();
                    if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                        return null;
                    }
                    Set<Collection> collections = rpgPlayerData.getItemCollections();
                    int collectionsUnlocked = 0;
                    for (Collection collection : collections) {
                        String group = collection.getGroup();
                        if (group != null && group.equals(groupName) && collection.isUnlocked()) {
                            collectionsUnlocked++;
                        }
                    }
                    int percentage = Percentage.of(collectionsUnlocked, collections.size());
                    return progressBar.getBarByProgress(percentage);
                });
            }
        }

        // Collection placeholders (no player)
        placeholders.register("collection_name", context -> {
            Collection collection = context.get(Collection.class);
            if (collection == null) {
                return null;
            }
            return collection.getName();
        });

        placeholders.register("collection_rewards", context -> {
            Collection collection = context.get(Collection.class);
            if (collection == null) {
                return null;
            }
            CollectionTier tier = collection.getTier();
            if (tier == null) {
                return null;
            }
            return tier.getRewards();
        });

        placeholders.register("collection_tier", context -> {
            Collection collection = context.get(Collection.class);
            if (collection == null || !collection.isFirstTierUnlocked()) {
                return null;
            }
            return collection.getCurrentTier() + 1;
        });

        placeholders.register("collection_previous_tier", context -> {
            Collection collection = context.get(Collection.class);
            if (collection == null) {
                return null;
            }
            return collection.getCurrentTier();
        });

        placeholders.register("collection_progress", context -> {
            Collection collection = context.get(Collection.class);
            if (collection == null) {
                return null;
            }
            return collection.getCollectedAmount();
        });

        placeholders.register("collection_progress_percent", context -> {
            Collection collection = context.get(Collection.class);
            if (collection == null) {
                return null;
            }
            return Percentage.asString(collection.getCollectedAmount(), collection.getNextTierAmount());
        });

        ProgressBar collectionProgressBar = settings.getCollectionProgressBar();
        for (int i = 0; i < 100; i++) {
            int tierIndex = i;

            placeholders.register("collection_tier_" + (tierIndex + 1) + "_rewards", context -> {
                Collection collection = context.get(Collection.class);
                if (collection == null || tierIndex > collection.getMaxTier()) {
                    return null;
                }
                return collection.getTier(tierIndex).getRewards();
            });

            placeholders.register("collection_tier_" + (tierIndex + 1) + "_requirement", context -> {
                Collection collection = context.get(Collection.class);
                if (collection == null || tierIndex > collection.getMaxTier()) {
                    return null;
                }
                return collection.getTier(tierIndex).getAmount();
            });

            placeholders.register("collection_tier_" + (tierIndex + 1) + "_progress_percent", context -> {
                Collection collection = context.get(Collection.class);
                if (collection == null || tierIndex > collection.getMaxTier()) {
                    return null;
                }
                return Percentage.asString(collection.getCollectedAmount(), collection.getTier(tierIndex).getAmount());
            });

            placeholders.register("collection_tier_" + (tierIndex + 1) + "_progress_bar", context -> {
                Collection collection = context.get(Collection.class);
                if (collection == null || tierIndex > collection.getMaxTier()) {
                    return null;
                }

                int percentage = Percentage.of(collection.getCollectedAmount(), collection.getTier(tierIndex).getAmount());
                return collectionProgressBar.getBarByProgress(percentage);
            });
        }
    }

}
