package io.github.pigaut.rpg.module.function.foreach.type;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ForEachEntityInRadius implements ForEachSource<LivingEntity> {

    private final Amount radius;
    private final Amount entityLimit;

    public ForEachEntityInRadius(Amount radius, Amount entityLimit) {
        this.radius = radius;
        this.entityLimit = entityLimit;
    }

    @Override
    public @NotNull Collection<LivingEntity> getElements(@NotNull Context context) {
        LivingEntity protagonist = context.protagonist();
        if (protagonist == null) {
            return List.of();
        }

        double radius = this.radius.doubleValue();
        int entityLimit = this.entityLimit.intValue();
        return EntityUtil.getEntitiesInRadius(protagonist, radius, entityLimit);
    }

}
