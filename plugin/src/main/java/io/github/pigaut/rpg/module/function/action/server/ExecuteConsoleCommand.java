package io.github.pigaut.rpg.module.function.action.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ExecuteConsoleCommand implements Action {

    private final String command;

    public ExecuteConsoleCommand(String command) {
        this.command = command;
    }

    @Override
    public void execute(@NotNull Context context) {
        String parsedCommand = PlaceholderUtil.parseAll(context, command);
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), parsedCommand);
    }

}
