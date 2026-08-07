package io.github.pigaut.rpg.module.structure;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.*;
import org.jetbrains.annotations.*;

public class StructureTemplateManager extends ConfigBackedManager<StructureTemplate> {

    private final boolean virtualStructuresEnabled;

    public StructureTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.STRUCTURES, StructureTemplate.class);
        virtualStructuresEnabled = Server.isPluginLoaded("packetevents");
    }

    public boolean isVirtualStructuresEnabled() {
        return virtualStructuresEnabled;
    }

}
