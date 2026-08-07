package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class MobHealthEquals implements MobCondition {

    private final Amount amount;

    public MobHealthEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public Boolean evaluate(@NotNull Mob mob) {
        LivingEntity entity = mob.getEntity();
        if (entity == null) {
            return null;
        }
        return amount.match(entity.getHealth());
    }

}
