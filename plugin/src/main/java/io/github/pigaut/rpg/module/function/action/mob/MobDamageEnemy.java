package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class MobDamageEnemy implements Action {

    private final Amount amount;

    public MobDamageEnemy(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Context context) {
        Mob mob = context.mob();
        LivingEntity enemy = context.enemy();
        if (mob == null || enemy == null) {
            return;
        }

        enemy.damage(amount.doubleValue(), mob.getEntity());
    }

}
