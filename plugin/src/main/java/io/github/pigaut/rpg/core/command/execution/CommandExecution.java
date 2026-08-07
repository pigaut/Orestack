package io.github.pigaut.rpg.core.command.execution;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.context.*;
import org.bukkit.command.*;

@FunctionalInterface
public interface CommandExecution {

    void execute(CommandSender sender, Context context, String[] args);

}
