package io.github.pigaut.rpg.command.stat;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class StatSubCommand extends SubCommand {

    public StatSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "stat");
        withPermission(plugin.getPermission("stat"));
        withDescription(plugin.getTranslation("stat-command"));
        addSubCommand(new StatGiveManaSubCommand(plugin));
        addSubCommand(new StatRemoveManaSubCommand(plugin));
        addSubCommand(new StatGiveHealthSubCommand(plugin));
        addSubCommand(new StatRemoveHealthSubCommand(plugin));
    }

}
