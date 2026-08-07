package io.github.pigaut.rpg.command.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemGetSubCommand extends SubCommand {

    public ItemGetSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "get");
        withPermission(plugin.getPermission("item.get"));
        withDescription(plugin.getTranslation("item-get-command"));
        withParameter(CommandParameters.itemName(plugin));
        withPlayerExecution((player, context, args) -> {
            ItemStack item = plugin.getItems().createItemStack(args[0], player);
            if (item == null) {
                plugin.sendMessage(player, context, "item-not-found");
                return;
            }
            PlayerUtil.giveItemsOrDrop(player, item);
            plugin.sendMessage(player, context, "received-item");
        });
    }

}
