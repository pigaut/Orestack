package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.voxel.core.transform.Rotation;


import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.data.structure.*;
import io.github.pigaut.voxel.util.Server;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GeneratorSetAllSubCommand extends SubCommand {

    public GeneratorSetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super("set-all", plugin);
        withPermission(plugin.getPermission("generator.set-all"));
        withDescription(plugin.getTranslation("generator-set-all-command"));
        withParameter(GeneratorParameters.GENERATOR_NAME);
        withPlayerExecution((player, context, args) -> {
            OrestackPlayer playerState = plugin.getPlayerState(player);

            GeneratorTemplate generator = plugin.getGeneratorTemplate(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, context, "generator-not-found");
                return;
            }

            Location firstSelection = playerState.getFirstSelection();
            Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, context, "incomplete-region");
                return;
            }

            StructureTemplate structure = generator.getLastPhase().getStructureTemplate();
            for (Location location : CuboidRegion.getAllLocations(player.getWorld(), firstSelection, secondSelection)) {
                for (Rotation rotation : Rotation.values()) {
                    if (structure.isPlaced(location, rotation)) {
                        GeneratorPlaceEvent generatorPlaceEvent = new GeneratorPlaceEvent(player, location, generator.getName(), generator.getOccupiedBlocks(location, rotation));
                        Server.callEvent(generatorPlaceEvent);

                        if (generatorPlaceEvent.isCancelled()) {
                            continue;
                        }

                        try {
                            Generator.create(generator, location);
                        } catch (GeneratorOverlapException ignored) {
                            // Ignore if generator overlaps
                        }
                    }
                }
            }

            plugin.sendMessage(player, context, "created-all-generators");
        });
    }

}
