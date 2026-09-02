package io.github.pigaut.rpg.module.function.action.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class RestoreItemUses implements ItemAction {

    private final EnhancedPlugin plugin;

    public RestoreItemUses(@NotNull EnhancedPlugin plugin) {
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
