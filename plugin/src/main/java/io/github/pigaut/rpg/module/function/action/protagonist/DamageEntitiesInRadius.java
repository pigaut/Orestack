package io.github.pigaut.rpg.module.function.action.protagonist;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageEntitiesInRadius implements ProtagonistAction {

    private final EnhancedPlugin plugin;
    private final Amount damage;
    private final double radius;
    private final int limit;

    public DamageEntitiesInRadius(EnhancedPlugin plugin, Amount damage, double radius, int limit) {
        this.plugin = plugin;
        this.damage = damage;
        this.radius = radius;
        this.limit = limit;
    }

    @Override
    public void execute(@NotNull LivingEntity protagonist) {
        double damage = this.damage.doubleValue();
        PlayerState playerProtagonist = plugin.getPlayerState(protagonist);
        if (playerProtagonist != null) {
            for (LivingEntity target : EntityUtil.getEntitiesInRadius(protagonist, radius, limit)) {
                playerProtagonist.attack(target, damage);
            }
            return;
        }

        Mob mobProtagonist = plugin.getMob(protagonist);
        if (mobProtagonist != null) {
            for (LivingEntity target : EntityUtil.getEntitiesInRadius(protagonist, radius, limit)) {
                mobProtagonist.attack(target, damage);
            }
            return;
        }

        for (LivingEntity target : EntityUtil.getEntitiesInRadius(protagonist, radius, limit)) {
            EntityUtil.attack(protagonist, target, damage);
        }
    }

}
