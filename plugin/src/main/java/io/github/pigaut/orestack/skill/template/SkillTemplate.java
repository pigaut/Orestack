package io.github.pigaut.orestack.skill.template;

import io.github.pigaut.orestack.skill.level.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.data.function.evaluate.*;
import io.github.pigaut.voxel.plugin.manager.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillTemplate implements Identifiable {

    private final String name;
    private final @Nullable String group;

    private final ItemStack icon;
    private final List<String> description;

    private final List<SkillLevel> skillLevels;
    private final @Nullable Function onUnlock;
    private final @Nullable Function onLock;

    private final @Nullable AmountFunction blockBreakExp;
    private final @Nullable AmountFunction generatorHarvestExp;

    public SkillTemplate(@NotNull String name, @Nullable String group,
                         @NotNull ItemStack icon, List<String> description,
                         @NotNull List<SkillLevel> skillLevels,
                         @Nullable Function onUnlock, @Nullable Function onLock,
                         @Nullable AmountFunction blockBreakExp, @Nullable AmountFunction generatorHarvestExp) {
        this.name = name;
        this.group = group;
        this.icon = icon;
        this.description = List.copyOf(description);
        this.skillLevels = List.copyOf(skillLevels);
        this.onUnlock = onUnlock;
        this.onLock = onLock;
        this.blockBreakExp = blockBreakExp;
        this.generatorHarvestExp = generatorHarvestExp;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return icon;
    }

    public @Nullable List<String> getDescription() {
        return description;
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

    public @Nullable Function getOnUnlock() {
        return onUnlock;
    }

    public @Nullable Function getOnLock() {
        return onLock;
    }

    public int getExpRequiredForLevel(int level) {
        SkillLevel skillLevel = getLevel(level);
        return skillLevel.getExpRequirement();
    }

    public int getLevelForExp(int totalExp) {
        int level = 0;
        for (SkillLevel skillLevel : skillLevels) {
            if (totalExp >= skillLevel.getExpRequirement()) {
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
        int previousLevelExp = skillLevels.get(currentLevel - 1).getExpRequirement();
        return totalExp - previousLevelExp;
    }

    public int getExpRequiredForNextLevel(int totalExp) {
        int currentLevel = getLevelForExp(totalExp);
        if (currentLevel >= skillLevels.size()) {
            return -1;
        }
        int nextLevelExp = skillLevels.get(currentLevel).getExpRequirement();
        int previousLevelExp = currentLevel == 0 ? 0 : skillLevels.get(currentLevel - 1).getExpRequirement();
        return nextLevelExp - previousLevelExp;
    }

}
