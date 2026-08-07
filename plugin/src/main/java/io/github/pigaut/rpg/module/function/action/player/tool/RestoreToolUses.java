package io.github.pigaut.rpg.module.function.action.player.tool;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class RestoreToolUses implements PlayerToolAction {

    private final EnhancedPlugin plugin;

    public RestoreToolUses(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull ItemStack tool) {
        ItemTemplate itemTemplate = plugin.getItemTemplate(tool);
        if (itemTemplate == null) {
            return;
        }

        Integer maxUses = itemTemplate.getMaxUses();
        if (maxUses == null) {
            return;
        }

        PersistentData.setInteger(tool, plugin.getItems().getUsesKey(), maxUses);
        itemTemplate.updateItemMeta(tool, player);
        player.getInventory().setItemInMainHand(tool);
    }

}
