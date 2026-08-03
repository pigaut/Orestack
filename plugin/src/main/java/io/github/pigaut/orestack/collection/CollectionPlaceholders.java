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

public class CollectionPlaceholders {

    public static void registerAll(@NotNull OrestackPlugin plugin) {
        PlaceholderRegistry placeholders = plugin.getPlaceholders();

        OrestackSettings settings = plugin.getSettings();
        ProgressBar collectionProgressBar = settings.getCollectionProgressBar();

        placeholders.register("collections_count", context -> {
            PlayerData playerData = context.playerData();
            if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                return null;
            }
            return rpgPlayerData.getItemCollections().size();
        });

        placeholders.register("collections_unlocked", context -> {
            PlayerData playerData = context.playerData();
            if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                return null;
            }
            return rpgPlayerData.getCollectionsUnlocked();
        });

        placeholders.register("collections_unlocked_progress", context -> {
            PlayerData playerData = context.playerData();
            if (playerData instanceof RpgPlayerData rpgPlayerData) {
                int collectionsUnlocked = rpgPlayerData.getCollectionsUnlocked();
                int collectionCount = rpgPlayerData.getCollectionCount();
                return Percentage.asDouble(collectionsUnlocked, collectionCount);
            }
            return null;
        });

        placeholders.register("collections_unlocked_progress_bar", context -> {
            PlayerData playerData = context.playerData();
            if (playerData instanceof RpgPlayerData rpgPlayerData) {
                int collectionsUnlocked = rpgPlayerData.getCollectionsUnlocked();
                int collectionCount = rpgPlayerData.getCollectionCount();
                int percentage = Percentage.asInteger(collectionsUnlocked, collectionCount);
                return collectionProgressBar.getBarByProgress(percentage);
            }
            return null;
        });

        for (CollectionTemplate collectionTemplate : plugin.getCollectionTemplates().getAll()) {
            String collectionName = collectionTemplate.getName();

            placeholders.register(collectionName + "_collection_tier", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection != null && collection.isFirstTierUnlocked()) {
                        return collection.getCurrentTier() + 1;
                    }
                }
                return null;
            });

            placeholders.register(collectionName + "_collection_next_tier", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection != null) {
                        return collection.getNextTier() + 1;
                    }
                }
                return null;
            });

            placeholders.register(collectionName + "_collection_amount", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection != null) {
                        return collection.getTotalAmount();
                    }
                }
                return null;
            });

            placeholders.register(collectionName + "_collection_amount_required", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection != null) {
                        return collection.getNextTierAmount();
                    }
                }
                return null;
            });

            placeholders.register(collectionName + "_collection_amount_left", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection != null) {
                        return collection.getAmountToNextTier();
                    }
                }
                return null;
            });

            placeholders.register(collectionName + "_collection_progress", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection != null) {
                        return Percentage.asDouble(collection.getTotalAmount(), collection.getNextTierAmount());
                    }
                }
                return null;
            });

            placeholders.register(collectionName + "_collection_progress_bar", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    if (collection != null) {
                        int percentage = Percentage.asInteger(collection.getTotalAmount(), collection.getNextTierAmount());
                        return collectionProgressBar.getBarByProgress(percentage);
                    }
                }
                return null;
            });

            placeholders.register(collectionName + "_collection_rewards", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                    return collection != null ? collection.getNextTierRewards() : null;
                }
                return null;
            });

            for (int i = 0; i <= collectionTemplate.getMaxTier(); i++) {
                int index = i;
                int tier = index + 1;

                int tierUpAmount = collectionTemplate.getTier(index).getAmountRequired();

                placeholders.register(collectionName + "_collection_tier_" + tier, context -> {
                    return tier;
                });

                placeholders.register(collectionName + "_collection_tier_" + tier + "_amount_required", context -> {
                    return tierUpAmount;
                });

                placeholders.register(collectionName + "_collection_tier_" + tier + "_amount_left", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                        if (collection != null) {
                            int totalAmount = collection.getTotalAmount();
                            return Math.max(0, tierUpAmount - totalAmount);
                        }
                    }
                    return null;
                });

                placeholders.register(collectionName + "_collection_tier_" + tier + "_progress", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                        if (collection != null) {
                            return Percentage.asDouble(collection.getTotalAmount(), tierUpAmount);
                        }
                    }
                    return null;
                });

                placeholders.register(collectionName + "_collection_tier_" + tier + "_progress_bar", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                        if (collection != null) {
                            int percentage = Percentage.asInteger(collection.getTotalAmount(), tierUpAmount);
                            return collectionProgressBar.getBarByProgress(percentage);
                        }
                    }
                    return null;
                });

                placeholders.register(collectionName + "_collection_tier_" + tier + "_rewards", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
                        if (collection != null) {
                            CollectionTier collectionTier = collection.getTier(index);
                            return collectionTier.getRewards();
                        }
                    }
                    return null;
                });
            }
        }

        for (String groupName : plugin.getCollectionTemplates().getAllGroups()) {
            placeholders.register(groupName + "_collections_count", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    return rpgPlayerData.getCollectionCount(groupName);
                }
                return null;
            });

            placeholders.register(groupName + "_collections_unlocked", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    return rpgPlayerData.getCollectionsUnlocked();
                }
                return null;
            });

            placeholders.register(groupName + "_collections_unlocked_progress", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    int collectionsUnlocked = rpgPlayerData.getCollectionsUnlocked(groupName);
                    int collectionCount = rpgPlayerData.getCollectionCount(groupName);
                    return Percentage.asDouble(collectionsUnlocked, collectionCount);
                }
                return null;
            });

            placeholders.register(groupName + "_collections_unlocked_progress_bar", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    int collectionsUnlocked = rpgPlayerData.getCollectionsUnlocked(groupName);
                    int collectionCount = rpgPlayerData.getCollectionCount(groupName);
                    int percentage = Percentage.asInteger(collectionsUnlocked, collectionCount);
                    return collectionProgressBar.getBarByProgress(percentage);
                }
                return null;
            });
        }

        // Collection placeholders (no player)
        placeholders.register("collection_name", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            return collection != null ? collection.getName() : null;
        });

        placeholders.register("collection_tier", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            if (collection != null && collection.isFirstTierUnlocked()) {
                return collection.getCurrentTier() + 1;
            }
            return null;
        });

        placeholders.register("collection_previous_tier", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            return collection != null ? collection.getPreviousTier() : null;
        });

        placeholders.register("collection_amount", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            return collection != null ? collection.getTotalAmount() : null;
        });

        placeholders.register("collection_amount_required", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            return collection != null ? collection.getNextTierAmount() : null;
        });

        placeholders.register("collection_amount_left", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            return collection != null ? collection.getAmountToNextTier() : null;
        });

        placeholders.register("collection_progress", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            return collection != null ? Percentage.asDouble(collection.getTotalAmount(), collection.getNextTierAmount()) : null;
        });

        placeholders.register("collection_progress_bar", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            if (collection != null) {
                int percentage = Percentage.asInteger(collection.getTotalAmount(), collection.getNextTierAmount());
                return collectionProgressBar.getBarByProgress(percentage);
            }
            return null;
        });

        placeholders.register("collection_rewards", context -> {
            ItemCollection collection = context.get(ItemCollection.class);
            return collection != null ? collection.getNextTierRewards() : null;
        });

        for (int i = 0; i < 100; i++) {
            int index = i;
            int tier = index + 1;

            placeholders.register("collection_tier_" + tier, context -> {
                return tier;
            });

            placeholders.register("collection_tier_" + tier + "_amount_required", context -> {
                ItemCollection collection = context.get(ItemCollection.class);
                if (collection != null && index <= collection.getMaxTier()) {
                    return collection.getTier(index).getAmountRequired();
                }
                return null;
            });

            placeholders.register("collection_tier_" + tier + "_amount_left", context -> {
                ItemCollection collection = context.get(ItemCollection.class);
                if (collection != null && index <= collection.getMaxTier()) {
                    long amountRequired = collection.getTier(index).getAmountRequired();
                    return Math.max(0, amountRequired - collection.getTotalAmount());
                }
                return null;
            });

            placeholders.register("collection_tier_" + tier + "_progress", context -> {
                ItemCollection collection = context.get(ItemCollection.class);
                if (collection != null && index <= collection.getMaxTier()) {
                    return Percentage.asDouble(collection.getTotalAmount(), collection.getTier(index).getAmountRequired());
                }
                return null;
            });

            placeholders.register("collection_tier_" + tier + "_progress_bar", context -> {
                ItemCollection collection = context.get(ItemCollection.class);
                if (collection != null && index <= collection.getMaxTier()) {
                    int percentage = Percentage.asInteger(collection.getTotalAmount(), collection.getTier(index).getAmountRequired());
                    return collectionProgressBar.getBarByProgress(percentage);
                }
                return null;
            });

            placeholders.register("collection_tier_" + tier + "_rewards", context -> {
                ItemCollection collection = context.get(ItemCollection.class);
                if (collection != null && index <= collection.getMaxTier()) {
                    return collection.getTier(index).getRewards();
                }
                return null;
            });
        }
    }

}
