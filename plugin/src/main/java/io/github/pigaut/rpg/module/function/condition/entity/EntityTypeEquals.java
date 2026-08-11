package io.github.pigaut.rpg.module.function.condition.entity;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityTypeEquals implements Condition {

    private final Set<EntityType> entityTypes;

    public EntityTypeEquals(@NotNull Collection<EntityType> entityTypes) {
        this.entityTypes = Set.copyOf(entityTypes);
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Context context) {
        LivingEntity entity = context.enemy();
        if (entity == null) {
            return null;
        }

        return entityTypes.contains(entity.getType());
    }
}
