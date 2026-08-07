package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class ExecutePlayerCommand implements Action {

    private final String command;

    public ExecutePlayerCommand(String command) {
        this.command = command;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            String parsedCommand = PlaceholderUtil.parseAll(context, command);
            player.performCommand(parsedCommand);
        }
    }

}
