package io.github.pigaut.rpg.command.message;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MessageSubCommand extends SubCommand {

    public MessageSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "message");
        withPermission(plugin.getPermission("message"));
        withDescription(plugin.getTranslation("message-command"));
        addSubCommand(new SendMessageSubCommand(plugin));
        addSubCommand(new BroadcastMessageSubCommand(plugin));
    }

}
