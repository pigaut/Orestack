package io.github.pigaut.rpg.module.function.foreach.type;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ForEachEntityInFront implements ForEachSource<LivingEntity> {

    private final Amount length;
    private final Amount width;
    private final Amount entityLimit;

    public ForEachEntityInFront(Amount length, Amount width, Amount entityLimit) {
        this.length = length;
        this.width = width;
        this.entityLimit = entityLimit;
    }

    @Override
    public @NotNull Collection<LivingEntity> getElements(@NotNull Context context) {
        LivingEntity protagonist = context.protagonist();
        if (protagonist == null) {
            return List.of();
        }

        double length = this.length.doubleValue();
        double width = this.width.doubleValue();
        int entityLimit = this.entityLimit.intValue();
        return EntityUtil.getEntitiesInFront(protagonist, length, width, entityLimit);
    }

}
