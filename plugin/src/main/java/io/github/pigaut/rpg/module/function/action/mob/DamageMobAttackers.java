package io.github.pigaut.rpg.module.function.action.mob;

import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageMobAttackers implements MobAction {

    private final Amount amount;

    public DamageMobAttackers(Amount amount) {
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Mob mob) {
        for (LivingEntity attacker : mob.getAttackers()) {
            attacker.damage(amount.doubleValue(), mob.getEntity());
        }
    }

}
