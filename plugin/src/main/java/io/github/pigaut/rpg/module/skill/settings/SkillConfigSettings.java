package io.github.pigaut.rpg.module.skill.settings;

import io.github.pigaut.rpg.module.skill.exp.*;
import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.scalar.*;
import net.objecthunter.exp4j.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SkillConfigSettings implements SkillSettings {

    private final EnhancedPlugin plugin;

    private int defaultMaxLevel;
    private Expression defaultExpFormula;
    private Function defaultOnExpEarn;
    private Function defaultOnSkillLevelUp;
    private final Map<String, ExpAmount> expEarningActivities = new HashMap<>();
    private ProgressBar skillProgressBar;

    public SkillConfigSettings(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfiguration(@NotNull ConfigSection config) {
        defaultMaxLevel = config.getInteger("default-skill-settings.max-level")
                .require(Requirements.positive())
                .withDefault(100);

        try {
            String rawFormula = config.getString("default-skill-settings.exp-formula")
                    .withDefault(null);
            defaultExpFormula = new ExpressionBuilder(Objects.requireNonNullElse(rawFormula, "(level/0.1)^2"))
                    .variable("level")
                    .build();
        } catch (Exception e) {
            config.collectError(new InvalidConfigException(config, "default-skill-settings.exp-formula", "Could not parse exp formula"));
            defaultExpFormula = new ExpressionBuilder("(level/0.1)^2")
                    .variable("level")
                    .build();
        }

        plugin.loadWhenReady(() -> {
            defaultOnExpEarn = config.get("default-skill-settings.on-exp-earn", Function.class)
                    .withDefault(null);

            defaultOnSkillLevelUp = config.get("default-skill-settings.on-level-up", Function.class)
                    .withDefault(null);
        });

        expEarningActivities.clear();
        for (KeyedScalar scalar : config.getSectionOrEmpty("exp-earning-activities").getNestedScalars()) {
            ConfigLine line = scalar.toLine();
            String name = scalar.getKey();
            ExpAmount expAmount = line.get(ExpAmount.class)
                    .withDefault(null);
            expEarningActivities.put(name, expAmount);
        }

        skillProgressBar = config.get("skill-progress-bar", ProgressBar.class)
                .withDefault(ProgressBar.EMPTY);
    }

    @Override
    public int getDefaultMaxSkillLevel() {
        return defaultMaxLevel;
    }

    @Override
    public @NotNull Expression getDefaultExpFormula() {
        return defaultExpFormula;
    }

    @Override
    public @Nullable Function getDefaultOnExpEarn() {
        return defaultOnExpEarn;
    }

    @Override
    public @Nullable Function getDefaultOnSkillLevelUp() {
        return defaultOnSkillLevelUp;
    }

    @Override
    public @Nullable ExpAmount getExpEarningActivity(@NotNull String name) {
        return expEarningActivities.get(name);
    }

    @Override
    public @NotNull ProgressBar getSkillProgressBar() {
        return skillProgressBar;
    }

}