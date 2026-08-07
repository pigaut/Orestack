package io.github.pigaut.rpg.module.function.condition.mob;

import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;

public class AttackerCountEquals implements MobCondition {

    private final Amount amount;

    public AttackerCountEquals(Amount amount) {
        this.amount = amount;
    }

    @Override
    public Boolean evaluate(@NotNull Mob mob) {
        return amount.match(mob.getAttackerCount());
    }

}
