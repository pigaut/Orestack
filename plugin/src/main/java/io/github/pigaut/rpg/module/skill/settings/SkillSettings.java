package io.github.pigaut.rpg.module.skill.settings;

import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.skill.exp.*;
import net.objecthunter.exp4j.*;
import org.jetbrains.annotations.*;

public interface SkillSettings {

    int getDefaultMaxSkillLevel();

    @NotNull Expression getDefaultExpFormula();

    @Nullable Function getDefaultOnExpEarn();

    @Nullable Function getDefaultOnSkillLevelUp();

    @Nullable
    ExpAmount getExpEarningActivity(@NotNull String name);

    @NotNull
    ProgressBar getSkillProgressBar();

}