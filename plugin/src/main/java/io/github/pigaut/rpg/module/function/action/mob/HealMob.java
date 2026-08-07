package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class HealMob implements MobAction {

    private final @Nullable Amount amount;

    public HealMob(@Nullable Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        if (amount == null) {
            mob.heal();
            return;
        }
        mob.heal(amount.doubleValue());
    }
}
