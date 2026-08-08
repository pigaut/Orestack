package io.github.pigaut.rpg.module.skill;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.skill.level.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.settings.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.*;

public class SkillPlaceholders {

    public static void registerAll(@NotNull RpgMakerPlugin plugin) {
        PlaceholderRegistry placeholders = plugin.getPlaceholders();

        RpgSettings settings = plugin.getSettings();
        ProgressBar skillProgressBar = settings.getSkillProgressBar();

        placeholders.register("skills_count", context -> {
            PlayerData playerData = context.playerData();
            if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                return null;
            }
            return rpgPlayerData.getSkills().size();
        });

        placeholders.register("skills_maxed", context -> {
            PlayerData playerData = context.playerData();
            if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
                return null;
            }
            return rpgPlayerData.getSkillsMaxed();
        });

        placeholders.register("skills_maxed_progress", context -> {
            PlayerData playerData = context.playerData();
            if (playerData instanceof RpgPlayerData rpgPlayerData) {
                int skillsMaxed = rpgPlayerData.getSkillsMaxed();
                int skillCount = rpgPlayerData.getSkillCount();
                return Percentage.asDouble(skillsMaxed, skillCount);
            }
            return null;
        });

        placeholders.register("skills_maxed_progress_bar", context -> {
            PlayerData playerData = context.playerData();
            if (playerData instanceof RpgPlayerData rpgPlayerData) {
                int skillsMaxed = rpgPlayerData.getSkillsMaxed();
                int skillCount = rpgPlayerData.getSkillCount();
                int percentage = Percentage.asInteger(skillsMaxed, skillCount);
                return skillProgressBar.getBarByProgress(percentage);
            }
            return null;
        });

        for (SkillTemplate skillTemplate : plugin.getSkillTemplates().getAll()) {
            String skillName = skillTemplate.getName();

            placeholders.register(skillName + "_skill_description", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    if (skill != null) {
                        return skill.getDescription();
                    }
                }
                return null;
            });

            placeholders.register(skillName + "_skill_level", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    if (skill != null && skill.isFirstLevelUnlocked()) {
                        return skill.getCurrentLevel() + 1;
                    }
                }
                return null;
            });

            placeholders.register(skillName + "_skill_next_level", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    return skill != null ? skill.getNextLevel() + 1 : null;
                }
                return null;
            });

            placeholders.register(skillName + "_skill_exp", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    return skill != null ? skill.getTotalExp() : null;
                }
                return null;
            });

            placeholders.register(skillName + "_skill_exp_required", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    return skill != null ? skill.getNextLevelExp() : null;
                }
                return null;
            });

            placeholders.register(skillName + "_skill_exp_left", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    return skill != null ? skill.getExpToNextLevel() : null;
                }
                return null;
            });

            placeholders.register(skillName + "_skill_progress", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    if (skill != null) {
                        return Percentage.asDouble(skill.getTotalExp(), skill.getNextLevelExp());
                    }
                }
                return null;
            });

            placeholders.register(skillName + "_skill_progress_bar", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    if (skill != null) {
                        int percentage = Percentage.asInteger(skill.getTotalExp(), skill.getNextLevelExp());
                        return skillProgressBar.getBarByProgress(percentage);
                    }
                }
                return null;
            });

            placeholders.register(skillName + "_skill_rewards", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    Skill skill = rpgPlayerData.getSkill(skillName);
                    return skill != null ? skill.getNextLevelRewards() : null;
                }
                return null;
            });

            for (int i = 0; i <= skillTemplate.getMaxLevel(); i++) {
                int index = i;
                int level = index + 1;

                long levelUpExp = skillTemplate.getLevel(index).getExpRequired();

                placeholders.register(skillName + "_skill_level_" + level, context -> {
                    return level;
                });

                placeholders.register(skillName + "_skill_level_" + level + "_exp_required", context -> {
                    return levelUpExp;
                });

                placeholders.register(skillName + "_skill_level_" + level + "_exp_left", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        Skill skill = rpgPlayerData.getSkill(skillName);
                        if (skill != null) {
                            int totalExp = skill.getTotalExp();
                            return Math.max(0, levelUpExp - totalExp);
                        }
                    }
                    return null;
                });

                placeholders.register(skillName + "_skill_level_" + level + "_progress", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        Skill skill = rpgPlayerData.getSkill(skillName);
                        if (skill != null) {
                            return Percentage.asDouble(skill.getTotalExp(), levelUpExp);
                        }
                    }
                    return null;
                });

                placeholders.register(skillName + "_skill_level_" + level + "_progress_bar", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        Skill skill = rpgPlayerData.getSkill(skillName);
                        if (skill != null) {
                            int percentage = Percentage.asInteger(skill.getTotalExp(), levelUpExp);
                            return skillProgressBar.getBarByProgress(percentage);
                        }
                    }
                    return null;
                });

                placeholders.register(skillName + "_skill_level_" + level + "_rewards", context -> {
                    PlayerData playerData = context.playerData();
                    if (playerData instanceof RpgPlayerData rpgPlayerData) {
                        Skill skill = rpgPlayerData.getSkill(skillName);
                        if (skill != null) {
                            SkillLevel skillLevel = skill.getLevel(index);
                            return skillLevel.getRewards();
                        }
                    }
                    return null;
                });
            }
        }

        for (String groupName : plugin.getSkillTemplates().getAllGroups()) {
            placeholders.register(groupName + "_skills_count", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    return rpgPlayerData.getSkillCount(groupName);
                }
                return null;
            });

            placeholders.register(groupName + "_skills_maxed", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    return rpgPlayerData.getSkillsMaxed(groupName);
                }
                return null;
            });

            placeholders.register(groupName + "_skills_maxed_progress", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    int skillsMaxed = rpgPlayerData.getSkillsMaxed(groupName);
                    int skillCount = rpgPlayerData.getSkillCount(groupName);
                    return Percentage.asDouble(skillsMaxed, skillCount);
                }
                return null;
            });

            placeholders.register(groupName + "_skills_maxed_progress_bar", context -> {
                PlayerData playerData = context.playerData();
                if (playerData instanceof RpgPlayerData rpgPlayerData) {
                    int skillsMaxed = rpgPlayerData.getSkillsMaxed(groupName);
                    int skillCount = rpgPlayerData.getSkillCount(groupName);
                    int percentage = Percentage.asInteger(skillsMaxed, skillCount);
                    return skillProgressBar.getBarByProgress(percentage);
                }
                return null;
            });
        }

        // Skill placeholders (no player)
        placeholders.register("skill_name", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getName() : null;
        });

        placeholders.register("skill_description", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getDescription() : null;
        });

        placeholders.register("skill_level", context -> {
            Skill skill = context.get(Skill.class);
            if (skill != null && skill.isFirstLevelUnlocked()) {
                return skill.getCurrentLevel() + 1;
            }
            return null;
        });

        placeholders.register("skill_previous_level", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getPreviousLevel() + 1 : null;
        });

        placeholders.register("skill_next_level", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getNextLevel() + 1 : null;
        });

        placeholders.register("skill_exp", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getTotalExp() : null;
        });

        placeholders.register("skill_exp_required", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getNextLevelExp() : null;
        });

        placeholders.register("skill_exp_left", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getExpToNextLevel() : null;
        });

        placeholders.register("skill_progress", context -> {
            Skill skill = context.get(Skill.class);
            if (skill != null) {
                return Percentage.asDouble(skill.getTotalExp(), skill.getNextLevelExp());
            }
            return null;
        });

        placeholders.register("skill_progress_bar", context -> {
            Skill skill = context.get(Skill.class);
            if (skill != null) {
                int percentage = Percentage.asInteger(skill.getTotalExp(), skill.getNextLevelExp());
                return skillProgressBar.getBarByProgress(percentage);
            }
            return null;
        });

        placeholders.register("skill_rewards", context -> {
            Skill skill = context.get(Skill.class);
            return skill != null ? skill.getNextLevelRewards() : null;
        });

        for (int i = 0; i < 1000; i++) {
            int index = i;
            int level = index + 1;

            placeholders.register("skill_level_" + level, context -> {
                return level;
            });

            placeholders.register("skill_level_" + level + "_exp_required", context -> {
                Skill skill = context.get(Skill.class);
                if (skill != null && index <= skill.getMaxLevel()) {
                    return skill.getLevel(index).getExpRequired();
                }
                return null;
            });

            placeholders.register("skill_level_" + level + "_exp_left", context -> {
                Skill skill = context.get(Skill.class);
                if (skill != null && index <= skill.getMaxLevel()) {
                    long expRequirement = skill.getLevel(index).getExpRequired();
                    return Math.max(0, expRequirement - skill.getTotalExp());
                }
                return null;
            });

            placeholders.register("skill_level_" + level + "_progress", context -> {
                Skill skill = context.get(Skill.class);
                if (skill != null && index <= skill.getMaxLevel()) {
                    return Percentage.asDouble(skill.getTotalExp(), skill.getLevel(index).getExpRequired());
                }
                return null;
            });

            placeholders.register("skill_level_" + level + "_progress_bar", context -> {
                Skill skill = context.get(Skill.class);
                if (skill != null && index <= skill.getMaxLevel()) {
                    int percentage = Percentage.asInteger(skill.getTotalExp(), skill.getLevel(index).getExpRequired());
                    return skillProgressBar.getBarByProgress(percentage);
                }
                return null;
            });

            placeholders.register("skill_level_" + level + "_rewards", context -> {
                Skill skill = context.get(Skill.class);
                if (skill != null && index <= skill.getMaxLevel()) {
                    return skill.getLevel(index).getRewards();
                }
                return null;
            });
        }
    }

}
