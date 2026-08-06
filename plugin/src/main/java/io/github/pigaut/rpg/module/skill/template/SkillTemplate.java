package io.github.pigaut.rpg.module.skill.template;

import io.github.pigaut.rpg.module.skill.exp.*;
import io.github.pigaut.rpg.module.skill.level.*;
import io.github.pigaut.rpg.module.skill.exp.*;
import io.github.pigaut.rpg.module.skill.level.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.skill.exp.*;
import io.github.pigaut.rpg.module.skill.level.*;
import io.github.pigaut.rpg.plugin.manager.*;
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
    private final @Nullable Function onLevelUp;
    private final @Nullable Function onLevelDown;
    private final @Nullable Function onExpEarn;

    private final @Nullable ExpYieldFunction blockBreakExp;
    private final @Nullable ExpYieldFunction eggCollectExp;
    private final @Nullable ExpYieldFunction milkCowExp;
    private final @Nullable ExpYieldFunction shearSheepExp;
    private final @Nullable ExpYieldFunction enchantItemExp;
    private final @Nullable ExpYieldFunction brewPotionExp;

    public SkillTemplate(@NotNull String name, @Nullable String group,
                         @NotNull ItemStack icon, List<String> description,
                         @NotNull List<SkillLevel> skillLevels,
                         @Nullable Function onUnlock, @Nullable Function onLock,
                         @Nullable Function onLevelUp, @Nullable Function onLevelDown, @Nullable Function onExpEarn,
                         @Nullable ExpYieldFunction blockBreakExp, @Nullable ExpYieldFunction eggCollectExp,
                         @Nullable ExpYieldFunction milkCowExp, @Nullable ExpYieldFunction shearSheepExp,
                         @Nullable ExpYieldFunction enchantItemExp, @Nullable ExpYieldFunction brewPotionExp) {
        this.name = name;
        this.group = group;
        this.icon = icon;
        this.description = List.copyOf(description);
        this.skillLevels = List.copyOf(skillLevels);
        this.onUnlock = onUnlock;
        this.onLock = onLock;
        this.onLevelUp = onLevelUp;
        this.onLevelDown = onLevelDown;
        this.onExpEarn = onExpEarn;
        this.blockBreakExp = blockBreakExp;
        this.eggCollectExp = eggCollectExp;
        this.milkCowExp = milkCowExp;
        this.shearSheepExp = shearSheepExp;
        this.enchantItemExp = enchantItemExp;
        this.brewPotionExp = brewPotionExp;
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
        return description != null ? new ArrayList<>(description) : null;
    }

    public @NotNull List<SkillLevel> getSkillLevels() {
        return skillLevels;
    }

    public int getMaxLevel() {
        return skillLevels.size() - 1;
    }

    public @NotNull SkillLevel getLevel(int level) {
        return skillLevels.get(level);
    }

    public @Nullable Function getOnUnlock() {
        return onUnlock;
    }

    public @Nullable Function getOnLock() {
        return onLock;
    }

    public @Nullable Function getOnLevelUp() {
        return onLevelUp;
    }

    public @Nullable Function getOnLevelDown() {
        return onLevelDown;
    }

    public @Nullable Function getOnExpEarn() {
        return onExpEarn;
    }

    public @Nullable ExpYieldFunction getBlockBreakExp() {
        return blockBreakExp;
    }

    public @Nullable ExpYieldFunction getEggCollectExp() {
        return eggCollectExp;
    }

    public @Nullable ExpYieldFunction getMilkCowExp() {
        return milkCowExp;
    }

    public @Nullable ExpYieldFunction getShearSheepExp() {
        return shearSheepExp;
    }

    public @Nullable ExpYieldFunction getEnchantItemExp() {
        return enchantItemExp;
    }

    public @Nullable ExpYieldFunction getBrewPotionExp() {
        return brewPotionExp;
    }

    public long getExpRequiredForLevel(int level) {
        SkillLevel skillLevel = getLevel(level);
        return skillLevel.getExpRequired();
    }

    public int getLevelForExp(long totalExp) {
        int level = 0;
        for (SkillLevel skillLevel : skillLevels) {
            if (totalExp >= skillLevel.getExpRequired()) {
                level++;
            } else {
                break;
            }
        }
        return level;
    }

    public long getExpIntoCurrentLevel(int totalExp) {
        int currentLevel = getLevelForExp(totalExp);
        if (currentLevel == 0) {
            return totalExp;
        }
        long previousLevelExp = skillLevels.get(currentLevel - 1).getExpRequired();
        return totalExp - previousLevelExp;
    }

    public long getExpRequiredForNextLevel(int totalExp) {
        int currentLevel = getLevelForExp(totalExp);
        if (currentLevel >= skillLevels.size()) {
            return -1;
        }
        long nextLevelExp = skillLevels.get(currentLevel).getExpRequired();
        long previousLevelExp = currentLevel == 0 ? 0 : skillLevels.get(currentLevel - 1).getExpRequired();
        return nextLevelExp - previousLevelExp;
    }

}
