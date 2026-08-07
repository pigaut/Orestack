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

public class ForEachEntityInRange implements ForEachSource<LivingEntity> {

    private final Amount range;
    private final Amount entityLimit;

    public ForEachEntityInRange(Amount range, Amount entityLimit) {
        this.range = range;
        this.entityLimit = entityLimit;
    }

    @Override
    public @NotNull Collection<LivingEntity> getElements(@NotNull Context context) {
        LivingEntity protagonist = context.protagonist();
        if (protagonist == null) {
            return List.of();
        }

        double range = this.range.doubleValue();
        int entityLimit = this.entityLimit.intValue();
        return EntityUtil.getEntitiesInRange(protagonist, range, entityLimit);
    }

}
