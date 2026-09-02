package io.github.pigaut.rpg.module.generator.settings;

import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public interface GeneratorSettings {

    boolean isVeinMiner();

    boolean isVeinGenerator(@NotNull Generator generator);

    int getToolMaxVeinSize(@NotNull ItemStack tool);

    @Nullable
    StructureTemplate getVirtualGeneratorBarrierLayout(@NotNull GeneratorTemplate generatorTemplate);

    boolean isDefaultToolDamage();

    Amount getDefaultToolDamage();

    @NotNull
    Delay getGeneratorHitCooldown();

    @NotNull
    Delay getGeneratorClickCooldown();

    @NotNull
    Delay getGeneratorHarvestCooldown();

}
