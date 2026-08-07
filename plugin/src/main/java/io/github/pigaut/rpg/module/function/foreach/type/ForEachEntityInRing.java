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

public class ForEachEntityInRing implements ForEachSource<LivingEntity> {

    private final Amount diameter;
    private final Amount thickness;
    private final Amount entityLimit;

    public ForEachEntityInRing(Amount diameter, Amount thickness, Amount entityLimit) {
        this.diameter = diameter;
        this.thickness = thickness;
        this.entityLimit = entityLimit;
    }

    @Override
    public @NotNull Collection<LivingEntity> getElements(@NotNull Context context) {
        LivingEntity protagonist = context.protagonist();
        if (protagonist == null) {
            return List.of();
        }

        double diameter = this.diameter.doubleValue();
        double thickness = this.thickness.doubleValue();
        int entityLimit = this.entityLimit.intValue();
        return EntityUtil.getEntitiesInRing(protagonist, diameter, thickness, entityLimit);
    }

}
