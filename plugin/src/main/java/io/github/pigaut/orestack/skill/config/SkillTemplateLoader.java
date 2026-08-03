package io.github.pigaut.orestack.skill.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.skill.*;
import io.github.pigaut.orestack.skill.exp.*;
import io.github.pigaut.orestack.skill.level.*;
import io.github.pigaut.orestack.skill.template.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.module.function.*;
import io.github.pigaut.voxel.module.stat.*;
import io.github.pigaut.voxel.module.stat.modifier.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.*;
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
                .require(Requirements.positive())
                .withDefault(plugin.getSettings().getDefaultMaxSkillLevel());

        Expression expFormula = plugin.getSettings().getDefaultExpFormula();
        try {
            String rawFormula = settingsSection.getString("exp-formula").withDefault(null);
            if (rawFormula != null) {
                expFormula = new ExpressionBuilder(rawFormula)
                        .variable("level")
                        .build();
            }
        } catch (Exception e) {
            throw new InvalidConfigException(settingsSection, "exp-formula", "Could not parse exp formula");
        }

        List<SkillLevel> skillLevels = new ArrayList<>(maxLevel);
        for (int i = 0; i < maxLevel; i++) {
            skillLevels.add(new SkillLevel(0, SkillStats.EMPTY, List.of(), null, null));
        }

        int lastLevel = 0;
        long lastLevelExp = 0;
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

            List<String> rewards = levelSection.getStringList("rewards", ColorUtil.FORMATTER)
                    .withDefault(null);

            Function onCompletion = levelSection.get("on-completion", Function.class)
                    .withDefault(null);

            Function onRegression = levelSection.get("on-regression", Function.class)
                    .withDefault(null);

            for (int level = levelsRangeMin; level <= levelsRangeMax; level++) {
                SkillStats.Builder statsBuilder = lastLevelStats.toBuilder();
                for (KeyedField field : levelSection.getSectionOrCreate("stats").getNestedFields()) {
                    Stat stat = field.getKeyAs(Stat.class).orThrow();
                    LeveledStatModifier statModifier = field.getRequired(LeveledStatModifier.class);
                    StatOperation operation = statModifier.getOperation();

                    StatModifier lastLevelModifier = lastLevelStats.get(stat);

                    double currentAmount = statModifier.getAmountAtLevel(level);
                    if (lastLevelModifier != null) {
                        if (operation != lastLevelModifier.getOperation()) {
                            throw new InvalidConfigException(field, "Stat modifier operation does not match ones from previous levels");
                        }
                        currentAmount += lastLevelModifier.getAmount();
                    }

                    StatModifier currentModifier = new StatModifier(currentAmount, operation);
                    statsBuilder.set(stat, currentModifier);
                }

                long expRequirement = Math.round(expFormula.setVariable("level", level).evaluate());
                if (expRequirement <= lastLevelExp) {
                    throw new InvalidConfigException(settingsSection,
                            "exp-formula", "Exp requirement for each level must be greater than the previous level's requirement");
                }

                SkillStats currentLevelStats = statsBuilder.build();
                skillLevels.set(level - 1, new SkillLevel(expRequirement, currentLevelStats, rewards, onCompletion, onRegression));

                if (level == levelsRangeMax) {
                    lastLevelStats = currentLevelStats;
                    lastLevelExp = expRequirement;
                }
            }
        }

        if (lastLevel != maxLevel) {
            throw new InvalidConfigException(settingsSection, "Skills is missing configuration for some levels");
        }

        ItemStack icon = settingsSection.get("icon", ItemStack.class)
                .withDefault(new ItemStack(Material.BEDROCK));

        List<String> description = settingsSection.getStringList("description", ColorUtil.FORMATTER)
                .withDefault(null);

        Function onUnlock = settingsSection.get("on-unlock", Function.class)
                .withDefault(null);

        Function onLock = settingsSection.get("on-lock", Function.class)
                .withDefault(null);

        Function onLevelUp = settingsSection.get("on-level-up", Function.class)
                .withDefault(null);

        Function onLevelDown = settingsSection.get("on-level-down", Function.class)
                .withDefault(null);

        Function onExpEarn = settingsSection.get("on-exp-earn", Function.class)
                .withDefault(plugin.getSettings().getDefaultOnExpEarn());

        ExpYieldFunction blockBreakExp = settingsSection.get("block-break-exp", ExpYieldFunction.class)
                .withDefault(null);

        ExpYieldFunction eggCollectExp = settingsSection.get("egg-collect-exp", ExpYieldFunction.class)
                .withDefault(null);

        ExpYieldFunction milkCowExp = settingsSection.get("milk-cow-exp", ExpYieldFunction.class)
                .withDefault(null);

        ExpYieldFunction shearSheepExp = settingsSection.get("shear-sheep-exp", ExpYieldFunction.class)
                .withDefault(null);

        ExpYieldFunction enchantItemExp = settingsSection.get("enchant-item-exp", ExpYieldFunction.class)
                .withDefault(null);

        ExpYieldFunction brewPotionExp = settingsSection.get("brew-potion-exp", ExpYieldFunction.class)
                .withDefault(null);

        return new SkillTemplate(name, group,
                icon, description,
                skillLevels,
                onUnlock, onLock,
                onLevelUp, onLevelDown,
                onExpEarn,
                blockBreakExp, eggCollectExp,
                milkCowExp, shearSheepExp,
                enchantItemExp, brewPotionExp);
    }

}
