package io.github.pigaut.rpg.module.function.condition.player.tool;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerToolUsesEquals implements ToolCondition {

    private final EnhancedPlugin plugin;
    private final Amount uses;

    public PlayerToolUsesEquals(EnhancedPlugin plugin, Amount uses) {
        this.plugin = plugin;
        this.uses = uses;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        Integer usesLeft = plugin.getItems().getUsesLeft(tool);
        if (usesLeft == null) {
            return null;
        }

        return uses.match(usesLeft);
    }
}
