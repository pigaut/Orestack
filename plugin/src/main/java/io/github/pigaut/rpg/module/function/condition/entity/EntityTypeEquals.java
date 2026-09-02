package io.github.pigaut.rpg.module.function.condition.entity;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityTypeEquals implements EntityCondition.Predicate {

    private final Set<EntityType> entityTypes;

    public EntityTypeEquals(@NotNull Collection<EntityType> entityTypes) {
        this.entityTypes = Set.copyOf(entityTypes);
    }

    @Override
    public boolean test(@NotNull Entity entity) {
        return entityTypes.contains(entity.getType());
    }

}
