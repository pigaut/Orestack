package io.github.pigaut.orestack.health;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ToolDamage {

    private final Set<Material> blocks;
    private final Map<Material, Amount> damageByTool;

    public ToolDamage(@NotNull Set<Material> blocks, @NotNull Map<Material, Amount> damageByTool) {
        this.blocks = Set.copyOf(blocks);
        this.damageByTool = Map.copyOf(damageByTool);
    }

    public boolean test(@NotNull Material toolType, @NotNull Material blockType) {
        return damageByTool.containsKey(toolType) && blocks.contains(blockType);
    }

    public @NotNull Set<Material> getBlocks() {
        return new HashSet<>(blocks);
    }

    public @Nullable Amount getDamage(@NotNull Material toolType) {
        return damageByTool.get(toolType);
    }

    @Override
    public String toString() {
        return "ToolDamage{" +
                "blocks=" + blocks +
                ", damageByTool=" + damageByTool +
                '}';
    }

}
