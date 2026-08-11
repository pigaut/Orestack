package io.github.pigaut.rpg.module.function.condition.entity;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityIsMob implements Condition {

    private final EnhancedPlugin plugin;

    public EntityIsMob(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        LivingEntity entity = context.enemy();
        if (entity == null) {
            return null;
        }
        return plugin.isMob(entity);
    }

}
