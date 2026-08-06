package io.github.pigaut.rpg.command.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.node.*;
import org.jetbrains.annotations.*;

public class GeneratorSubCommand extends SubCommand {

    public GeneratorSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "generator");
        withPermission(plugin.getPermission("generator"));
        withDescription(plugin.getTranslation("generator-command"));
        addSubCommand(new GeneratorGetSubCommand(plugin));
        addSubCommand(new GeneratorGetGroupSubCommand(plugin));
        addSubCommand(new GeneratorGetAllSubCommand(plugin));
        addSubCommand(new GeneratorSetSubCommand(plugin));
        addSubCommand(new GeneratorRemoveSubCommand(plugin));
        addSubCommand(new GeneratorSetAllSubCommand(plugin));
        addSubCommand(new GeneratorRemoveAllSubCommand(plugin));
        addSubCommand(new GeneratorHarvestSubCommand(plugin));
    }

}
