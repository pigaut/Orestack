package io.github.pigaut.orestack.core.placeholder;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.health.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.placeholder.*;
import io.github.pigaut.voxel.data.collection.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.yaml.util.*;
import org.jetbrains.annotations.*;

public class OrestackPlaceholders {

    public static void registerAll(@NotNull OrestackPlugin plugin, @NotNull PlaceholderRegistry placeholders) {
        placeholders.register("generator", context -> {
            GeneratorTemplate generatorTemplate = context.get(GeneratorTemplate.class);
            if (generatorTemplate != null) {
                return generatorTemplate.getName();
            }

            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getName() : null;
        });

        placeholders.register("generator_phase", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getState().getCurrentPhase() : null;
        });

        placeholders.register("generator_phases", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getMaxPhase() + 1 : null;
        });

        placeholders.register("generator_state", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getPhase().getState() : null;
        });

        placeholders.register("generator_rotation", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getRotation() : null;
        });

        placeholders.register("generator_world", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getWorld().getName() : null;
        });

        placeholders.register("generator_x", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getBlockX() : null;
        });

        placeholders.register("generator_y", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getBlockY() : null;
        });

        placeholders.register("generator_z", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? generator.getOrigin().getBlockZ() : null;
        });

        placeholders.register("phase_timer", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatCompact(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_full", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatFull(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_hours", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toHours(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_minutes", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toMinutes(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("phase_timer_seconds", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toSeconds(generator.getTicksToNextPhase()) : null;
        });

        placeholders.register("generator_timer", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatCompact(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_full", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.formatFull(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_hours", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toHours(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_minutes", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toMinutes(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_timer_seconds", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? Ticks.toSeconds(generator.getTicksToRegrownPhase()) : null;
        });

        placeholders.register("generator_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getGeneratorHealthInt(generator) : null;
        });

        placeholders.register("generator_max_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getGeneratorMaxHealthInt(generator) : null;
        });

        placeholders.register("phase_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getPhaseHealthInt(generator) : null;
        });

        placeholders.register("phase_max_health", context -> {
            Generator generator = context.get(Generator.class);
            return generator != null ? HealthUtil.getPhaseMaxHealthInt(generator) : null;
        });

        OrestackSettings settings = plugin.getSettings();
        for (ProgressBar progressBar : settings.getHealthBars()) {
            placeholders.register("generator_health_bar:" + progressBar.getId(), context -> {
                Generator generator = context.get(Generator.class);
                if (generator == null) {
                    return null;
                }
                Integer healthPercentage = HealthUtil.getHealthPercentage(generator);
                if (healthPercentage == null) {
                    return null;
                }
                return progressBar.getBarByProgress(healthPercentage);
            });

            placeholders.register("phase_health_bar:" + progressBar.getId(), context -> {
                Generator generator = context.get(Generator.class);
                if (generator == null) {
                    return null;
                }
                Integer healthPercentage = HealthUtil.getPhaseHealthPercentage(generator);
                if (healthPercentage == null) {
                    return null;
                }
                return progressBar.getBarByProgress(healthPercentage);
            });
        }

        for (ProgressBar progressBar : settings.getGrowthBars()) {
            placeholders.register("generator_growth_bar:" + progressBar.getId(), context -> {
                Generator generator = context.get(Generator.class);
                if (generator == null) {
                    return null;
                }
                int growth = generator.getState().getTotalElapsedTicks();
                int totalGrowthTime = generator.getTemplate().getTotalGrowthTime();

                double ratio = (double) growth / totalGrowthTime;
                int percent = (int) Math.round(ratio * 100.0);
                percent = Math.max(0, Math.min(100, percent));
                return progressBar.getBarByProgress(percent);
            });

            placeholders.register("phase_growth_bar:" + progressBar.getId(), context -> {
                Generator generator = context.get(Generator.class);
                if (generator == null) {
                    return null;
                }
                int growth = generator.getState().getElapsedTicksInPhase();
                int phaseGrowthTime = generator.getPhase().getGrowthTimeInTicks();

                double ratio = (double) growth / phaseGrowthTime;
                int percent = (int) Math.round(ratio * 100.0);
                percent = Math.max(0, Math.min(100, percent));
                return progressBar.getBarByProgress(percent);
            });
        }

        // Player Collection placeholders
        for (CollectionTemplate collectionTemplate : plugin.getCollectionTemplates().getAll()) {
            String collectionName = collectionTemplate.getName();
            placeholders.register(collectionName + "_collection_amount_left", context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null) {
                    return null;
                }
                Collection collection = playerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getAmountToNextTier();
            });

            placeholders.register(collectionName + "_collection_progress", context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null) {
                    return null;
                }
                Collection collection = playerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getCollectedAmount();
            });

            placeholders.register(collectionName + "_collection_progress_percent", context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null) {
                    return null;
                }
                Collection collection = playerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }

                double percentage = ((double) collection.getCollectedAmount() / collection.getNextTierAmount()) * 100;
                double cappedPercentage = Math.min(100.0, percentage);
                return String.format("%.1f", cappedPercentage);
            });

            for (ProgressBar progressBar : plugin.getSettings().getGrowthBars()) {
                placeholders.register(collectionName + "_collection_progress_bar:" + progressBar.getId(), context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData == null) {
                        return null;
                    }
                    Collection collection = playerData.getItemCollection(collectionName);
                    if (collection == null) {
                        return null;
                    }

                    double ratio = (double) collection.getCollectedAmount() / collection.getNextTierAmount();
                    int percent = (int) Math.round(ratio * 100.0);
                    percent = Math.max(0, Math.min(100, percent));
                    return progressBar.getBarByProgress(percent);
                });
            }

            placeholders.register(collectionName + "_collection_tier_up_requirement", context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null) {
                    return null;
                }
                Collection collection = playerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getNextTierAmount();
            });

            placeholders.register(collectionName + "_collection_current_tier", context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null) {
                    return null;
                }
                Collection collection = playerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getCurrentTier() + 1;
            });

            placeholders.register(collectionName + "_collection_next_tier", context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null) {
                    return null;
                }
                Collection collection = playerData.getItemCollection(collectionName);
                if (collection == null) {
                    return null;
                }
                return collection.getNextTier() + 1;
            });

            for (int tierIndex = 0; tierIndex <= collectionTemplate.getMaxTier(); tierIndex++) {
                final int tierUpAmount = collectionTemplate.getTier(tierIndex).getAmount();

                placeholders.register(collectionName + "_collection_tier_" + (tierIndex + 1) + "_requirement", context -> {
                    return tierUpAmount;
                });

                placeholders.register(collectionName + "_collection_tier_" + (tierIndex + 1) + "_progress_percent", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData == null) {
                        return null;
                    }
                    Collection collection = playerData.getItemCollection(collectionName);
                    if (collection == null) {
                        return null;
                    }

                    double percentage = ((double) collection.getCollectedAmount() / tierUpAmount) * 100;
                    double cappedPercentage = Math.min(100.0, percentage);
                    return String.format("%.1f", cappedPercentage);
                });

                for (ProgressBar progressBar : plugin.getSettings().getGrowthBars()) {
                    placeholders.register(collectionName + "_collection_tier_" + (tierIndex + 1) + "_progress_bar:" + progressBar.getId(), context -> {
                        PlayerData playerData = context.playerData();
                        if (playerData == null) {
                            return null;
                        }
                        Collection collection = playerData.getItemCollection(collectionName);
                        if (collection == null) {
                            return null;
                        }

                        double ratio = (double) collection.getCollectedAmount() / tierUpAmount;
                        int percent = (int) Math.round(ratio * 100.0);
                        percent = Math.max(0, Math.min(100, percent));
                        return progressBar.getBarByProgress(percent);
                    });
                }

            }
        }

        // Player collection category placeholders
        for (String groupName : plugin.getCollectionTemplates().getAllGroups()) {
            placeholders.register("collection_unlocked:" + groupName, context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null)
                    return null;

                int collectionsUnlocked = 0;
                for (Collection collection : playerData.getItemCollections()) {
                    String group = collection.getGroup();
                    if (group == null || !group.equals(groupName)) {
                        if (collection.getCurrentTier() > -1) {
                            collectionsUnlocked++;
                        }
                    }
                }

                return collectionsUnlocked;
            });


            placeholders.register("collection_unlocked_percent:" + groupName, context -> {
                PlayerData playerData = context.playerData();
                if (playerData == null) {
                    return null;
                }
                int collectionsCount = 0;
                int collectionsUnlocked = 0;
                for (Collection collection : playerData.getItemCollections()) {
                    String group = collection.getGroup();
                    if (group == null || !group.equals(groupName)) {
                        collectionsCount++;
                        if (collection.getCurrentTier() > -1) {
                            collectionsUnlocked++;
                        }
                    }
                }

                double percentage = ((double) collectionsUnlocked / collectionsCount) * 100;
                return String.format("%.1f", percentage);
            });

            for (ProgressBar progressBar : plugin.getSettings().getGrowthBars()) {
                placeholders.register("collection_unlocked:" + groupName + "_progress_bar:" + progressBar.getId(), context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData == null) {
                        return null;
                    }
                    int collectionsCount = 0;
                    int collectionsUnlocked = 0;
                    for (Collection collection : playerData.getItemCollections()) {
                        String group = collection.getGroup();
                        if (group == null || !group.equals(groupName)) {
                            collectionsCount++;
                            if (collection.getCurrentTier() > -1) {
                                collectionsUnlocked++;
                            }
                        }
                    }

                    double ratio = (double) collectionsUnlocked / collectionsCount;
                    int percent = (int) Math.round(ratio * 100.0);
                    percent = Math.max(0, Math.min(100, percent));
                    return progressBar.getBarByProgress(percent);
                });
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
                return collection.getTier().getRewards();
            });

            placeholders.register("collection_tier", context -> {
                Collection collection = context.get(Collection.class);
                if (collection == null) {
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



            for (ProgressBar progressBar : plugin.getSettings().getGrowthBars()) {
                placeholders.register("collection_tier_" + (tierIndex + 1) + "_progress_bar:" + progressBar.getId(), context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData == null) {
                        return null;
                    }
                    Collection collection = playerData.getItemCollection(collectionName);
                    if (collection == null) {
                        return null;
                    }

                    double ratio = (double) collection.getCollectedAmount() / tierUpAmount;
                    int percent = (int) Math.round(ratio * 100.0);
                    percent = Math.max(0, Math.min(100, percent));
                    return progressBar.getBarByProgress(percent);
                });
            }

        }



    }
}
