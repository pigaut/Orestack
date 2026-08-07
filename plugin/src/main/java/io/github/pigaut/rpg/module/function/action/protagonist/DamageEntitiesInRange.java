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

public class DamageEntitiesInRange implements ProtagonistAction {

    private final EnhancedPlugin plugin;
    private final Amount damage;
    private final double range;
    private final int limit;

    public DamageEntitiesInRange(EnhancedPlugin plugin, Amount damage, double range, int limit) {
        this.plugin = plugin;
        this.damage = damage;
        this.range = range;
        this.limit = limit;
    }

    @Override
    public void execute(@NotNull LivingEntity protagonist) {
        double damage = this.damage.doubleValue();
        PlayerState playerProtagonist = plugin.getPlayerState(protagonist);
        if (playerProtagonist != null) {
            for (LivingEntity target : EntityUtil.getEntitiesInRange(protagonist, range, limit)) {
                playerProtagonist.attack(target, damage);
            }
            return;
        }

        Mob mobProtagonist = plugin.getMob(protagonist);
        if (mobProtagonist != null) {
            for (LivingEntity target : EntityUtil.getEntitiesInRange(protagonist, range, limit)) {
                mobProtagonist.attack(target, damage);
            }
            return;
        }

        for (LivingEntity target : EntityUtil.getEntitiesInRange(protagonist, range, limit)) {
            EntityUtil.attack(protagonist, target, damage);
        }
    }

}
