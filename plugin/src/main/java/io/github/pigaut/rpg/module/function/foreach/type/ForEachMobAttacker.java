package io.github.pigaut.rpg.module.function.foreach.type;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.module.mob.Mob;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ForEachMobAttacker implements ForEachSource<Player> {

    @Override
    public @NotNull Collection<Player> getElements(@NotNull Context context) {
        Mob mob = context.mob();
        if (mob == null) {
            return List.of();
        }

        return mob.getAttackers();
    }

}
