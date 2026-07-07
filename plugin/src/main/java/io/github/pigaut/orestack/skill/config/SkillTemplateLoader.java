package io.github.pigaut.orestack.skill.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.orestack.skill.level.*;
import io.github.pigaut.orestack.skill.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.data.function.*;
import io.github.pigaut.voxel.data.function.evaluate.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.sequence.*;
import net.objecthunter.exp4j.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillTemplateLoader implements ConfigLoader<SkillTemplate> {

    private final OrestackPlugin plugin;

    public SkillTemplateLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid skill";
    }

    @Override
    public @NotNull SkillTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String skillName = scalar.toString();
        SkillTemplate skillTemplate = plugin.getSkillTemplate(skillName);
        if (skillTemplate == null) {
            throw new InvalidConfigException(scalar, "Could not find skill with name: " + skillName);
        }
        return skillTemplate;
    }

    @Override
    public @NotNull SkillTemplate loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        if (!(sequence instanceof RootSequence root) || !root.hasFile()) {
            throw new InvalidConfigException(sequence, "Can only load collection from a root sequence");
        }

        String name = root.getName();
        String group = Group.byFile(root.getFile(), "skills", true);

        ConfigSection settingsSection = sequence.getRequiredSection(0);

        int maxLevel = settingsSection.getInteger("max-level")
                .requireOrThrow(Requirements.positive());

        Expression expFormula;
        try {
            String rawFormula = settingsSection.getRequiredString("exp-formula");
            expFormula = new ExpressionBuilder(rawFormula)
                    .variable("level")
                    .build();
        } catch (Exception e) {
            throw new InvalidConfigException(settingsSection, "exp-formula", "Could not parse exp formula");
        }

        List<SkillLevel> skillLevels = new ArrayList<>(maxLevel);
        for (int i = 0; i < maxLevel; i++) {
            skillLevels.add(new SkillLevel(0, SkillStats.EMPTY, List.of(), null, null));
        }

        int lastLevel = 0;
        SkillStats lastLevelStats = SkillStats.EMPTY;
        for (int i = 1; i < sequence.size(); i++) {
            ConfigSection levelSection = sequence.getRequiredSection(i);

            Amount levelsRange = levelSection.get("level|levels", Amount.class)
                    .requireOrThrow(Requirements.amountBetween(lastLevel + 1, maxLevel));

            int levelsRangeMin = (int) levelsRange.minValue();
            int levelsRangeMax = (int) levelsRange.maxValue();

            if (levelsRangeMin != lastLevel + 1) {
                throw new InvalidConfigException(levelSection, "levels", "Skill configuration cannot skip any levels");
            }

            if (levelsRangeMax <= lastLevel) {
                throw new InvalidConfigException(levelSection, "levels", "Skill level range overlaps with another");
            }

            lastLevel = levelsRangeMax;

            SkillStats levelStatsIncrement = levelSection.get("stats", SkillStats.class)
                    .withDefault(SkillStats.EMPTY);

            List<String> rewards = levelSection.getStringList("rewards", StringColor.FORMATTER)
                    .orEmpty();

            Function onCompletion = levelSection.get("on-completion", Function.class)
                    .withDefault(null);

            Function onRegression = levelSection.get("on-regression", Function.class)
                    .withDefault(null);


            SkillStats accumulatedStats = lastLevelStats;
            for (int level = levelsRangeMin; level <= levelsRangeMax; level++) {
                int exp = (int) expFormula.setVariable("level", level).evaluate();
                accumulatedStats = accumulatedStats.add(levelStatsIncrement);
                skillLevels.set(level - 1, new SkillLevel(exp, accumulatedStats, rewards, onCompletion, onRegression));
            }

            lastLevelStats = accumulatedStats;
        }

        if (lastLevel != maxLevel) {
            throw new InvalidConfigException(settingsSection, "Skills is missing configuration for some levels");
        }

        ItemStack icon = settingsSection.get("icon", ItemStack.class)
                .withDefault(new ItemStack(Material.BEDROCK));

        Function onUnlock = settingsSection.get("on-unlock", Function.class)
                .withDefault(null);

        Function onLock = settingsSection.get("on-lock", Function.class)
                .withDefault(null);

        AmountFunction blockBreakExp = settingsSection.get("block-break-exp", AmountFunction.class)
                .withDefault(null);

        AmountFunction generatorHarvestExp = settingsSection.get("generator-harvest-exp", AmountFunction.class)
                .withDefault(null);

        return new SkillTemplate(name, group,
                icon, skillLevels,
                onUnlock, onLock,
                blockBreakExp, generatorHarvestExp);
    }

}
