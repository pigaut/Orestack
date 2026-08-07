package io.github.pigaut.rpg.command.message;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SendMessageSubCommand extends SubCommand {

    public SendMessageSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "send");
        withPermission(plugin.getPermission("message.send"));
        withDescription(plugin.getTranslation("message-send-command"));
        withParameter(CommandParameters.ONLINE_PLAYER);
        withParameter(CommandParameters.messageName(plugin));
        withCommandExecution((sender, context, args) -> {
            Player receiver = Bukkit.getPlayer(args[0]);
            if (receiver == null) {
                plugin.sendMessage(sender, context, "player-not-online");
                return;
            }
            Message message = plugin.getMessage(args[1]);
            if (message == null) {
                plugin.sendMessage(sender, context, "message-not-found");
                return;
            }

            Context receiverContext = Context.fromPlayer(plugin, receiver);
            message.send(receiver, receiverContext);

            plugin.sendMessage(sender, context, "sent-message-to-player");
        });
    }

}
