package io.github.pigaut.rpg.command.item;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemGetGroupSubCommand extends SubCommand {

    public ItemGetGroupSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "get-group");
        withPermission(plugin.getPermission("item.get-group"));
        withDescription(plugin.getTranslation("item-get-group-command"));
        withParameter(CommandParameters.itemGroup(plugin));
        withPlayerExecution((player, context, args) -> {
            List<ItemTemplate> groupItems = plugin.getItems().getAll(args[0]);
            if (groupItems.isEmpty()) {
                plugin.sendMessage(player, context, "item-group-not-found");
                return;
            }
            for (ItemTemplate itemTemplate : groupItems) {
                PlayerUtil.giveItemsOrDrop(player, itemTemplate.createItemStack(player));
            }
            plugin.sendMessage(player, context, "received-item-group");
        });
    }

}
