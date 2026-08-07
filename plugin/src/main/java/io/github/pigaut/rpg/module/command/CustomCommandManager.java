package io.github.pigaut.rpg.module.command;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import org.jetbrains.annotations.*;

public class CustomCommandManager extends ConfigBackedManager<CustomCommand> {

    public CustomCommandManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.COMMANDS, CustomCommand.class);
        prefix("Commands");
    }

    @Override
    public void enable() {
        for (CustomCommand command : getAll()) {
            plugin.getRegisteredCommands().registerCommand(command);
        }
    }

    @Override
    public void disable() {
        for (CustomCommand command : getAll()) {
            plugin.getRegisteredCommands().unregisterCommand(command.getName());
        }
    }

}
