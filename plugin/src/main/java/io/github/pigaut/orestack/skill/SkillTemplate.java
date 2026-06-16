package io.github.pigaut.orestack.skill;

import org.jetbrains.annotations.*;

import java.util.*;

public class SkillTemplate {

    private final String name;
    private final @Nullable String group;
    private final List<SkillLevel> skillLevels;

    public SkillTemplate(String name, @Nullable String group, List<SkillLevel> skillLevels) {
        this.name = name;
        this.group = group;
        this.skillLevels = List.copyOf(skillLevels);
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getGroup() {
        return group;
    }

    public @NotNull List<SkillLevel> getSkillLevels() {
        return skillLevels;
    }

    public int getMaxLevel() {
        return skillLevels.size();
    }

    public @NotNull SkillLevel getLevel(int level) {
        return skillLevels.get(level - 1);
    }

    public int getExpRequiredForLevel(int level) {
        SkillLevel skillLevel = getLevel(level);
        return skillLevel != null ? skillLevel.getExpAmount() : -1;
    }

    public int getLevelForExp(int totalExp) {
        int level = 0;
        for (SkillLevel skillLevel : skillLevels) {
            if (totalExp >= skillLevel.getExpAmount()) {
                level++;
            } else {
                break;
            }
        }
        return level;
    }

    public int getExpIntoCurrentLevel(int totalExp) {
        int currentLevel = getLevelForExp(totalExp);
        if (currentLevel == 0) {
            return totalExp;
        }
        int previousLevelExp = skillLevels.get(currentLevel - 1).getExpAmount();
        return totalExp - previousLevelExp;
    }

    public int getExpRequiredForNextLevel(int totalExp) {
        int currentLevel = getLevelForExp(totalExp);
        if (currentLevel >= skillLevels.size()) {
            return -1;
        }
        int nextLevelExp = skillLevels.get(currentLevel).getExpAmount();
        int previousLevelExp = currentLevel == 0 ? 0 : skillLevels.get(currentLevel - 1).getExpAmount();
        return nextLevelExp - previousLevelExp;
    }

}
