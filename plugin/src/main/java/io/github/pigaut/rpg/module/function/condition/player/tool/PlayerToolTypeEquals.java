package io.github.pigaut.rpg.module.function.condition.player.tool;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerToolTypeEquals implements ToolCondition {

    private final List<Material> materials;

    public PlayerToolTypeEquals(List<Material> materials) {
        this.materials = materials;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        Material toolType = tool.getType();
        for (Material material : materials) {
            if (material == toolType) {
                return true;
            }
        }
        return false;
    }

}
