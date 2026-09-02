package io.github.pigaut.rpg.module.function.action.block;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DropExpAtBlock implements Action {

    private final ExpDrop expDrop;

    public DropExpAtBlock(@NotNull ExpDrop expDrop) {
        this.expDrop = expDrop;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return new FunctionError("This function trigger does not support block actions");
        }

        Location location = LocationUtil.centered(block.getLocation());
        expDrop.spawn(location, context.tool());
        return FunctionResponse.NONE;
    }

}
