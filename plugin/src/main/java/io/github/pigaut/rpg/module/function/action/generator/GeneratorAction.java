package io.github.pigaut.rpg.module.function.action.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.generator.*;
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

        RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();
        Generator generator = plugin.getGenerator(context.player(), block.getLocation());
        if (generator != null) {
            execute(generator);
        }
    }

}
