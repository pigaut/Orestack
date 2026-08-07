package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.mob.Mob;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class MobIsFullHealth implements MobCondition {

    @Override
    public Boolean evaluate(@NotNull Mob mob) {
        LivingEntity entity = mob.getEntity();
        if (entity == null) {
            return null;
        }
        return entity.getHealth() == mob.getMaxHealth();
    }

}
