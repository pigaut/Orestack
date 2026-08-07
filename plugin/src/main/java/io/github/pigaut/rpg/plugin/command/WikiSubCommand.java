package io.github.pigaut.rpg.plugin.command;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class WikiSubCommand extends SubCommand {

    public WikiSubCommand(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, "wiki");
        withPermission(plugin.getPermission("wiki"));
        withDescription(plugin.getTranslation("wiki-command"));
        withCommandExecution((sender, context, args) -> {
            if (sender instanceof Player player) {
                plugin.sendMessage(player, context, "player-wiki");
                return;
            }
            plugin.sendMessage(sender, context, "console-wiki");
        });
    }

}
