package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface ProtagonistAction extends Action {

    void execute(@NotNull LivingEntity protagonist);

    @Override
    default void execute(@NotNull Context context) {
        LivingEntity protagonist = context.protagonist();
        if (protagonist != null) {
            execute(protagonist);
        }
    }

}
