package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.voxel.util.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

public class GeneratorHarvestSubCommand extends SubCommand {

    public GeneratorHarvestSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("HARVEST_ALL_GENERATORS_COMMAND", "harvest"), plugin);
        addParameter(plugin.getLang("GENERATOR_NAME_PARAMETER", "generator-name"));
        withDescription(plugin.getLang("HARVEST_ALL_GENERATORS_DESCRIPTION"));
        withPermission("orestack.generator.harvest");
        withPlayerCompletion((player, args) -> plugin.getGenerators().getGeneratorNames());
        withPlayerExecution((player, args) -> {
            final Generator generator = plugin.getGenerator(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "GENERATOR_NOT_FOUND", this);
                return;
            }

            for (BlockGenerator blockGenerator : plugin.getGenerators().getAllBlockGenerators()) {
                if (blockGenerator.getGenerator() != generator) {
                    continue;
                }

                final Block block = blockGenerator.getBlock();
                final BlockBreakEvent event = new BlockBreakEvent(block, player);
                SpigotServer.callEvent(event);
            }

            plugin.sendMessage(player, "HARVESTED_ALL_GENERATORS", this, generator);
        });
    }

}
