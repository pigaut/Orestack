package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.block.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

import java.util.*;

public class BlockConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("BLOCK_TYPE_EQUALS", (Line<Condition>) line -> {
            Set<Material> materials = new HashSet<>();
            for (MaterialTag materialTag : line.getAllRequired(1, MaterialTag.class)) {
                materials.addAll(materialTag.getMaterials());
            }
            return new BlockTypeEquals(materials);
        });
    }

}