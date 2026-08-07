package io.github.pigaut.rpg.core.command.execution;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.context.*;
import org.bukkit.entity.*;

@FunctionalInterface
public interface PlayerExecution {

    void execute(Player player, Context context, String[] args);

}
