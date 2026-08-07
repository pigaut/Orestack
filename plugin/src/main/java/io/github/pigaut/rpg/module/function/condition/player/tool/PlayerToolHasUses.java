package io.github.pigaut.rpg.module.function.condition.player.tool;

import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerToolHasUses implements ToolCondition {

    private final EnhancedPlugin plugin;

    public PlayerToolHasUses(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        ItemTemplate itemTemplate = plugin.getItemTemplate(tool);
        if (itemTemplate == null) {
            return null;
        }
        return itemTemplate.hasUses();
    }

}
