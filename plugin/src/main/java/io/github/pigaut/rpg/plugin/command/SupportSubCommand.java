package io.github.pigaut.rpg.plugin.command;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SupportSubCommand extends SubCommand {

    public SupportSubCommand(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, "support");
        withPermission(plugin.getPermission("support"));
        withDescription(plugin.getTranslation("support-command"));
        withCommandExecution((sender, context, args) -> {
            if (sender instanceof Player player) {
                plugin.sendMessage(player, context, "player-support");
                return;
            }
            plugin.sendMessage(sender, context, "console-support");
        });
    }

}
