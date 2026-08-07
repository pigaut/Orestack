package io.github.pigaut.rpg.command.message;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class BroadcastMessageSubCommand extends SubCommand {

    public BroadcastMessageSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "broadcast");
        withPermission(plugin.getPermission("message.broadcast"));
        withDescription(plugin.getTranslation("message-broadcast-command"));
        withParameter(CommandParameters.messageName(plugin));
        withCommandExecution((sender, context, args) -> {
            Message message = plugin.getMessage(args[0]);
            if (message == null) {
                plugin.sendMessage(sender, context, "message-not-found");
                return;
            }
            for (Player receiver : Bukkit.getOnlinePlayers()) {
                Context receiverContext = Context.fromPlayer(plugin, receiver);
                message.send(receiver, receiverContext);
            }
            plugin.sendMessage(sender, context, "sent-message-to-all");
        });
    }

}
