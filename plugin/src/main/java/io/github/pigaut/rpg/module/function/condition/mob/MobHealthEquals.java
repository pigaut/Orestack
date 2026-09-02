package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.function.response.*;
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
    public @NotNull FunctionResponse evaluate(@NotNull Mob mob) {
        LivingEntity entity = mob.getEntity();
        if (entity == null) {
            return new FunctionError("Mob does not have an active entity");
        }
        return FunctionResponse.met(amount.match(entity.getHealth()));
    }

}
