package io.github.pigaut.rpg.module.mob;

import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class MobPlaceholders {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        PlaceholderRegistry placeholders = plugin.getPlaceholders();

        placeholders.register("mob", context -> {
            Mob mob = context.mob();
            return mob != null ? mob.getName() : null;
        });

        placeholders.register("mob_id", context -> {
            Mob mob = context.mob();
            return mob != null ? mob.getEntityId() : null;
        });

        placeholders.register("mob_name", context -> {
            Mob mob = context.mob();
            return mob != null ? (mob.getOptions().getDisplayName() + ChatColor.RESET) : null;
        });

        placeholders.register("mob_max_health", context -> {
            Mob mob = context.mob();
            return mob != null ? mob.getMaxHealth() : null;
        });

        placeholders.register("mob_health", context -> {
            Mob mob = context.mob();
            return mob != null ? mob.getHealth() : null;
        });
    }

}
