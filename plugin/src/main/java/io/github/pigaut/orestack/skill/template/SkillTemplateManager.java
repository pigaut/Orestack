package io.github.pigaut.orestack.skill.template;

import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.config.*;
import io.github.pigaut.voxel.plugin.manager.module.Module;

public class SkillTemplateManager extends ConfigBackedManager<SkillTemplate> {

    public SkillTemplateManager(EnhancedJavaPlugin plugin) {
        super(plugin, Module.SKILLS, SkillTemplate.class);
    }

}
