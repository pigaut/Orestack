package io.github.pigaut.orestack.core.action;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.action.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface GeneratorAction extends Action {

    void execute(@NotNull Generator generator);

    @Override
    default void execute(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return;
        }

        OrestackPlugin plugin = OrestackPlugin.getInstance();
        Generator generator = plugin.getGenerator(context.player(), block.getLocation());
        if (generator != null) {
            execute(generator);
        }
    }

}
