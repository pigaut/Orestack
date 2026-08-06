package io.github.pigaut.rpg.module.function.condition.generator;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.Condition;
import io.github.pigaut.rpg.module.generator.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public interface GeneratorCondition extends Condition {

    @Nullable
    Boolean evaluate(@NotNull Generator generator);

    @Override
    default @Nullable Boolean evaluate(@NotNull Context context) {
        Block block = context.block();
        if (block == null) {
            return null;
        }

        RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();
        Generator generator = plugin.getGenerator(context.player(), block.getLocation());
        if (generator != null) {
            return evaluate(generator);
        }

        return null;
    }

}
