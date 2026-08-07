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

public class DamageEntitiesInRing implements ProtagonistAction {

    private final EnhancedPlugin plugin;
    private final Amount damage;
    private final double diameter;
    private final double thickness;
    private final int limit;

    public DamageEntitiesInRing(EnhancedPlugin plugin, Amount damage, double diameter, double thickness, int limit) {
        this.plugin = plugin;
        this.damage = damage;
        this.diameter = diameter;
        this.thickness = thickness;
        this.limit = limit;
    }

    @Override
    public void execute(@NotNull LivingEntity protagonist) {
        double damage = this.damage.doubleValue();
        PlayerState playerProtagonist = plugin.getPlayerState(protagonist);
        if (playerProtagonist != null) {
            for (LivingEntity target : EntityUtil.getEntitiesInRing(protagonist, diameter, thickness, limit)) {
                playerProtagonist.attack(target, damage);
            }
            return;
        }

        Mob mobProtagonist = plugin.getMob(protagonist);
        if (mobProtagonist != null) {
            for (LivingEntity target : EntityUtil.getEntitiesInRing(protagonist, diameter, thickness, limit)) {
                mobProtagonist.attack(target, damage);
            }
            return;
        }

        for (LivingEntity target : EntityUtil.getEntitiesInRing(protagonist, diameter, thickness, limit)) {
            EntityUtil.attack(protagonist, target, damage);
        }
    }

}
