package io.github.pigaut.rpg.command.structure;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class StructurePlaceSubCommand extends SubCommand {

    public StructurePlaceSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "place");
        withPermission(plugin.getPermission("structure.place"));
        withDescription(plugin.getTranslation("structure-place-command"));
        withParameter(CommandParameters.structureName(plugin));
        withPlayerExecution((player, context, args) -> {
            StructureTemplate structure = plugin.getStructure(args[0]);
            if (structure == null) {
                plugin.sendMessage(player, context, "structure-not-found");
                return;
            }

            Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, context, "too-far-away");
                return;
            }

            Location location = targetBlock.getLocation();
            structure.place(location, Rotation.NONE);
            plugin.sendMessage(player, context, "placed-structure");
        });
    }

}
