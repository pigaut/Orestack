package io.github.pigaut.orestack.command.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class StructurePlaceSubCommand extends LangSubCommand {

    public StructurePlaceSubCommand(@NotNull OrestackPlugin plugin) {
        super("place-structure", plugin);
        addParameter(new StructureNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final BlockStructure structure = plugin.getBlockStructure(args[0]);
            if (structure == null) {
                plugin.sendMessage(player, "structure-not-found", placeholders);
                return;
            }
            final Block targetBlock = player.getTargetBlockExact(6);
            if (targetBlock == null) {
                plugin.sendMessage(player, "too-far-away", placeholders);
                return;
            }
            final Location location = targetBlock.getLocation();
            structure.createBlocks(location);
            plugin.sendMessage(player, "placed-structure", placeholders);
        });
    }

}
