package io.github.pigaut.rpg.module.mob.template;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class MobTemplateManager extends ConfigBackedManager<MobTemplate> {

    public MobTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.MOBS, MobTemplate.class);
        prefix("Mob");
        ignore("factions.yml");
    }

}
