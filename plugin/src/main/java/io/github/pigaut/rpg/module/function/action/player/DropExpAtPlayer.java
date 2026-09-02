package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropExpAtPlayer implements Action.Executor {

    private final ExpDrop expDrop;

    public DropExpAtPlayer(@NotNull ExpDrop expDrop) {
        this.expDrop = expDrop;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Player player = context.player();
        if (player == null) {
            return new FunctionError("This function trigger does not support player actions");
        }

        expDrop.spawn(player.getLocation(), context.tool());
        return FunctionResponse.NONE;
    }

}
