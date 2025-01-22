package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GeneratorSetAllSubCommand extends LangSubCommand {

    public GeneratorSetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super("set-all-generators", plugin);
        addParameter(new GeneratorNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            final OrestackPlayer playerState = plugin.getPlayer(player.getUniqueId());
            if (playerState == null) {
                plugin.sendMessage(player, "loading-player-data", placeholders);
                return;
            }
            final Generator generator = plugin.getGenerator(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "generator-not-found", placeholders);
                return;
            }
            final Location firstSelection = playerState.getFirstSelection();
            final Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, "incomplete-region", placeholders, generator);
                return;
            }
            final GeneratorStage lastStage = generator.getLastStage();
            for (Location location : SelectionUtil.getSelectedRegion(player.getWorld(), firstSelection, secondSelection)) {
                if (lastStage.matchBlock(location.getBlock().getBlockData())) {
                    plugin.getGenerators().createBlockGenerator(generator, location);
                }
            }
            plugin.sendMessage(player, "created-all-generators", placeholders, generator);
        });
    }

}
