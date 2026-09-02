package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.mob.Mob;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class MobIsFullHealth implements MobCondition {

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Mob mob) {
        LivingEntity entity = mob.getEntity();
        if (entity == null) {
            return new FunctionError("Mob does not have an active entity");
        }
        return FunctionResponse.met(entity.getHealth() == mob.getMaxHealth());
    }

}
