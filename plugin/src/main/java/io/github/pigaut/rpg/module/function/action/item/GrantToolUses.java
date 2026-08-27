package io.github.pigaut.rpg.module.function.action.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GrantToolUses implements PlayerToolAction {

    private final EnhancedPlugin plugin;
    private final Amount grantAmount;

    public GrantToolUses(EnhancedPlugin plugin, Amount grantAmount) {
        this.plugin = plugin;
        this.grantAmount = grantAmount;
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

        Integer usesLeft = plugin.getItems().getUsesLeft(tool);
        if (usesLeft == null) {
            return;
        }

        int uses = Math.min(usesLeft + grantAmount.intValue(), maxUses);
        PersistentData.setInteger(tool, plugin.getItems().getUsesKey(), uses);

        itemTemplate.updateItemMeta(tool, player);
        PlayerUtil.setTool(player, tool);
    }

}
