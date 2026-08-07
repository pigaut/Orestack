package io.github.pigaut.rpg.module.function.action.enemy;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface EnemyAction extends Action {

    void execute(@NotNull LivingEntity enemy);

    @Override
    default void execute(@NotNull Context context) {
        LivingEntity target = context.enemy();
        if (target != null) {
            execute(target);
        }
    }

}
