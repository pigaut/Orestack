package io.github.pigaut.rpg.module.function.action.player.tool;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ConsumeToolUses implements PlayerToolAction {

    private final EnhancedPlugin plugin;
    private final Amount consumeAmount;

    public ConsumeToolUses(EnhancedPlugin plugin, Amount consumeAmount) {
        this.plugin = plugin;
        this.consumeAmount = consumeAmount;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull ItemStack tool) {
        ItemTemplate itemTemplate = plugin.getItemTemplate(tool);
        if (itemTemplate == null || !itemTemplate.hasUses()) {
            return;
        }

        Integer usesLeft = plugin.getItems().getUsesLeft(tool);
        if (usesLeft == null) {
            return;
        }

        int uses = Math.max(0, usesLeft - consumeAmount.intValue());
        PersistentData.setInteger(tool, plugin.getItems().getUsesKey(), uses);

        itemTemplate.updateItemMeta(tool, player);
        PlayerUtil.setTool(player, tool);
    }

}
