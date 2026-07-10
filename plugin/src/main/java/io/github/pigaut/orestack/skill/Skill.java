package io.github.pigaut.orestack.skill;

import io.github.pigaut.orestack.skill.level.*;
import io.github.pigaut.orestack.skill.template.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.player.state.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Skill {

    private final SkillTemplate template;
    private int currentLevel;
    private int totalExp;

    public Skill(@NotNull SkillTemplate template, int totalExp) {
        this.template = template;
        this.totalExp = totalExp;
        this.currentLevel = template.getLevelForExp(totalExp);
    }

    public @NotNull String getName() {
        return template.getName();
    }

    public @Nullable String getGroup() {
        return template.getGroup();
    }

    public @NotNull SkillTemplate getTemplate() {
        return template;
    }

    public int getMaxLevel() {
        return template.getMaxLevel();
    }

    public boolean isMaxLevel() {
        return currentLevel >= getMaxLevel();
    }

    public @NotNull ItemStack getIcon() {
        return template.getIcon();
    }

    public @Nullable List<String> getDescription() {
        return template.getDescription();
    }

    public boolean isFirstLevelUnlocked() {
        return currentLevel >= 1;
    }

    public @Nullable SkillLevel getLevel() {
        return currentLevel >= 1 ? template.getLevel(currentLevel) : null;
    }

    public @NotNull SkillLevel getLevel(int level) {
        return template.getLevel(level);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getNextLevel() {
        return currentLevel >= template.getMaxLevel() ? currentLevel : currentLevel + 1;
    }

    public int getNextLevelExp() {
        return template.getExpRequiredForLevel(getNextLevel());
    }

    public @Nullable List<String> getNextLevelRewards() {
        SkillLevel skillLevel = getLevel();
        return skillLevel != null ? skillLevel.getRewards() : null;
    }

    public int getTotalExp() {
        return totalExp;
    }

    public int getExpToNextLevel() {
        return Math.max(0, getNextLevelExp() - totalExp);
    }

    public void increaseExp(@NotNull Context context, int amount) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");

        PlayerState playerState = context.playerState();
        if (playerState == null) {
            return;
        }

        this.totalExp += amount;
        context = context.with(Skill.class, this);

        SkillLevel nextLevel;
        while (currentLevel + 1 <= template.getMaxLevel()
                && totalExp >= (nextLevel = template.getLevel(currentLevel + 1)).getExpRequirement()) {

            currentLevel++;

            SkillStats skillStats = nextLevel.getStats();
            skillStats.applyAll(playerState, this);

            Function onProgression = nextLevel.getOnProgression();
            if (onProgression != null) {
                onProgression.run(context);
            }
        }
    }

    public void decreaseExp(@NotNull Context context, int amount) {
        Preconditions.checkArgument(amount > 0, "Amount must be positive");

        PlayerState playerState = context.playerState();
        if (playerState == null) {
            return;
        }

        totalExp = Math.max(0, totalExp - amount);
        context = context.with(Skill.class, this);

        while (currentLevel >= 1 && totalExp < template.getLevel(currentLevel).getExpRequirement()) {
            SkillLevel lostLevel = template.getLevel(currentLevel);

            currentLevel--;

            SkillLevel newLevel = template.getLevel(currentLevel);
            SkillStats skillStats = newLevel.getStats();
            skillStats.applyAll(playerState, this);

            Function onRegression = lostLevel.getOnRegression();
            if (onRegression != null) {
                onRegression.run(context);
            }
        }
    }

}