package io.github.pigaut.rpg.plugin.command;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class HelpSubCommand extends SubCommand {

    public HelpSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "help");
        withPermission(plugin.getPermission("help"));
        withDescription(plugin.getTranslation("help-command"));
        withCommandExecution((sender, context, args) -> {
            plugin.sendMessage(sender, context, "help-header");
            for (SubCommand subCommand : getParent()) {
                if (subCommand.isExecutable()) {
                    Context subCommandContext = Context.fromCommand(plugin, sender, subCommand, args);
                    plugin.sendMessage(sender, subCommandContext, "help-line");
                }
            }
            plugin.sendMessage(sender, context, "help-footer");
        });
    }

}
