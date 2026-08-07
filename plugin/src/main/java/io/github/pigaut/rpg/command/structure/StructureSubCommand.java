package io.github.pigaut.rpg.command.structure;

import io.github.pigaut.rpg.command.structure.buildstation.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class StructureSubCommand extends SubCommand {

    public StructureSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "structure");
        withPermission(plugin.getPermission("structure"));
        withDescription(plugin.getTranslation("structure-command"));
        addSubCommand(new StructureSaveSubCommand(plugin));
        addSubCommand(new StructurePlaceSubCommand(plugin));
        addSubCommand(new BuildStationSubCommand(plugin));
    }

}
