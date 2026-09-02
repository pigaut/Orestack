package io.github.pigaut.rpg.module.function.condition.entity;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EntityIsMob implements EntityCondition.Predicate {

    private final EnhancedPlugin plugin;

    public EntityIsMob(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean test(@NotNull Entity entity) {
        return plugin.isMob(entity);
    }

}
