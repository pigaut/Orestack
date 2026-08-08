package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageEnemy implements Action {

    private final EnhancedPlugin plugin;
    private final Amount damage;

    public DamageEnemy(@NotNull EnhancedPlugin plugin, @NotNull Amount damage) {
        this.plugin = plugin;
        this.damage = damage;
    }

    @Override
    public void execute(@NotNull Context context) {
        LivingEntity protagonist = context.protagonist();
        LivingEntity enemy = context.enemy();
        if (protagonist == null || enemy == null) {
            return;
        }

        double damage = this.damage.doubleValue();
        PlayerState playerProtagonist = plugin.getPlayerState(protagonist);
        if (playerProtagonist != null) {
            playerProtagonist.attack(enemy, damage);
            return;
        }

        Mob mobProtagonist = plugin.getMob(protagonist);
        if (mobProtagonist != null) {
            mobProtagonist.attack(enemy, damage);
            return;
        }

        EntityUtil.attack(protagonist, enemy, damage);
    }
}
