package io.github.pigaut.rpg.command.structure.buildstation;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class BuildStationSubCommand extends SubCommand {

    public BuildStationSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "build-station");
        withPermission(plugin.getPermission("buildstation"));
        withDescription(plugin.getTranslation("buildstation-command"));
        addSubCommand(new BuildStationCreateSubCommand(plugin));
        addSubCommand(new BuildStationRemoveSubCommand(plugin));
    }

}
