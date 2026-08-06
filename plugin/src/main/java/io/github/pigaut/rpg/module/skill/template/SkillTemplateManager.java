package io.github.pigaut.rpg.module.skill.template;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;

public class SkillTemplateManager extends ConfigBackedManager<SkillTemplate> {

    public SkillTemplateManager(EnhancedJavaPlugin plugin) {
        super(plugin, Module.SKILLS, SkillTemplate.class);
    }

}
