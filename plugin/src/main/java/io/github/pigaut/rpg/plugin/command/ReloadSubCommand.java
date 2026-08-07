package io.github.pigaut.rpg.plugin.command;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, "reload");
        withPermission(plugin.getPermission("reload"));
        withDescription(plugin.getTranslation("reload-command"));
        withCommandExecution((sender, context, args) -> {
            plugin.sendMessage(sender, context, "reloading");
            try {
                plugin.reload(errorCollector -> {
                    if (sender instanceof Player player) {
                        if (!errorCollector.hasErrors() && !errorCollector.hasWarnings()) {
                            plugin.sendMessage(player, context, "reload-completed");
                            return;
                        }

                        context.addPlaceholder("error_count", errorCollector.getErrorCount());
                        context.addPlaceholder("warning_count", errorCollector.getWarningCount());
                        context.addPlaceholder("issue_count", errorCollector.getErrorCount() + errorCollector.getWarningCount());

                        if (!plugin.getSettings().isShowReloadErrors()) {
                            plugin.sendMessage(player, context, "reload-completed-check-console");
                            return;
                        }

                        plugin.sendMessage(player, context, "reload-completed-with-errors");
                        ConfigErrorUtil.sendAll(plugin, player, errorCollector.getErrors(), errorCollector.getWarnings());
                    }
                });
            }
            catch (PluginReloadInProgressException e) {
                plugin.sendMessage(sender, context, "already-reloading");
            }
        });
    }


}
