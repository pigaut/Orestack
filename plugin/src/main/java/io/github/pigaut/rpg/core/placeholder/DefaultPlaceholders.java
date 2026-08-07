package io.github.pigaut.rpg.core.placeholder;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class DefaultPlaceholders {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        PlaceholderRegistry placeholders = plugin.getPlaceholders();

        // Plugin placeholders
        placeholders.register("plugin", context -> {
            return plugin.getName();
        });

        // Block placeholders
        placeholders.register("block", context -> {
            Block block = context.block();
            return block != null ? CaseFormatter.toTitleCase(block.getType().toString()) : null;
        });

        // Command placeholders
        placeholders.register("command", context -> {
            CommandNode command = context.command();
            return command != null ? command.getFullCommand() : null;
        });

        placeholders.register("command_name", context -> {
            CommandNode command = context.command();
            return command != null ? command.getCommand() : null;
        });

        placeholders.register("command_description", context -> {
            CommandNode command = context.command();
            return command != null ? command.getDescription() : null;
        });

        placeholders.register("command_permission", context -> {
            CommandNode command = context.command();
            return command != null ? command.getPermission() : null;
        });

    }

}
