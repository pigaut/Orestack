package io.github.pigaut.rpg.core.command;

import com.google.common.base.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.command.defaults.*;

import java.util.*;

public class CommandRegistry {

    private final EnhancedJavaPlugin plugin;
    private final Map<String, EnhancedCommand> customCommands = new HashMap<>();
    private boolean initialized = false;

    public CommandRegistry(EnhancedJavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        //Initialize Spigot Command Manager
        plugin.getCommand("");
        initialized = true;
    }

    public EnhancedCommand getCustomCommand(String name) {
        return customCommands.get(name);
    }

    public void registerCommand(EnhancedCommand command) {
        Preconditions.checkArgument(initialized, "Command registry has not been initialized.");
        CommandUtil.registerCommand(command);
        customCommands.put(command.getName(), command);
    }

    public void unregisterCommand(String name) {
        Preconditions.checkArgument(initialized, "Command registry has not been initialized.");
        BukkitCommand command = customCommands.get(name);
        if (command != null) {
            CommandUtil.unregisterCommand(command);
            customCommands.remove(name);
        }
    }

}
