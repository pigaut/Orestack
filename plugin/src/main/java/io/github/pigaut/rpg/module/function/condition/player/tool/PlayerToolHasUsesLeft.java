package io.github.pigaut.rpg.module.function.condition.player.tool;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerToolHasUsesLeft implements ToolCondition {

    private final EnhancedPlugin plugin;
    private final int minUses;

    public PlayerToolHasUsesLeft(EnhancedPlugin plugin, int minUses) {
        this.plugin = plugin;
        this.minUses = minUses;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        Integer usesLeft = plugin.getItems().getUsesLeft(tool);
        if (usesLeft == null) {
            return null;
        }

        return usesLeft >= minUses;
    }

}
