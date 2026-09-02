package io.github.pigaut.rpg.module.function.action.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class AddItemUses implements ItemAction {

    private final EnhancedPlugin plugin;
    private final Amount grantAmount;

    public AddItemUses(@NotNull EnhancedPlugin plugin, @NotNull Amount grantAmount) {
        this.plugin = plugin;
        this.grantAmount = grantAmount;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull ItemStack item) {
        ItemTemplate itemTemplate = plugin.getItemTemplate(item);
        if (itemTemplate == null) {
            return;
        }

        Integer maxUses = itemTemplate.getMaxUses();
        if (maxUses == null) {
            return;
        }

        Integer usesLeft = plugin.getItems().getUsesLeft(item);
        if (usesLeft == null) {
            return;
        }

        int uses = Math.min(usesLeft + grantAmount.intValue(), maxUses);
        PersistentData.setInteger(item, plugin.getItems().getUsesKey(), uses);

        itemTemplate.updateItemMeta(item, player);
        PlayerUtil.setTool(player, item);
    }

}
