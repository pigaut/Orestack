package io.github.pigaut.orestack.core.condition.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.module.function.condition.Condition;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.concurrent.locks.*;

public interface GeneratorCondition extends Condition {

    @Nullable
    Boolean evaluate(@NotNull Generator generator);

    @Override
    default @Nullable Boolean evaluate(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return null;
        }

        OrestackPlugin plugin = OrestackPlugin.getInstance();
        Generator generator = plugin.getGenerator(context.player(), block.getLocation());
        if (generator != null) {
            return evaluate(generator);
        }

        return null;
    }

}
