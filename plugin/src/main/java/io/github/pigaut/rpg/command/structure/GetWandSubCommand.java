package io.github.pigaut.rpg.command.structure;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class GetWandSubCommand extends SubCommand {

    public GetWandSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "wand");
        withPermission(plugin.getPermission("structure.wand"));
        withDescription(plugin.getTranslation("wand-command"));
        withPlayerExecution((player, context, args) -> {
            PlayerUtil.giveItemsOrDrop(player, plugin.getSettings().getStructureWand());
            plugin.sendMessage(player, context, "received-wand");
        });
    }

}
