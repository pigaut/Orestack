package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.command.node.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GeneratorRemoveAllSubCommand extends LangSubCommand {

    public GeneratorRemoveAllSubCommand(@NotNull OrestackPlugin plugin) {
        super("remove-all-generators", plugin);
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
                plugin.sendMessage(player, "incomplete-region", placeholders);
                return;
            }
            for (Location point : SelectionUtil.getSelectedRegion(player.getWorld(), firstSelection, secondSelection)) {
                final BlockGenerator blockGenerator = plugin.getBlockGenerator(point);
                if (blockGenerator == null) {
                    continue;
                }
                if (blockGenerator.getGenerator() == generator) {
                    plugin.getGenerators().removeBlockGenerator(point);
                }
            }
            plugin.sendMessage(player, "removed-all-generators", placeholders, generator);
        });
    }

}
