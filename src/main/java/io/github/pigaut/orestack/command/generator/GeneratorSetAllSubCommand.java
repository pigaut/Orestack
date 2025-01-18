package io.github.pigaut.orestack.command.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.external.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorSetAllSubCommand extends LangSubCommand {

    private final @Nullable WorldEditHook worldEdit;

    public GeneratorSetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super("set-all-generators", plugin);
        worldEdit = plugin.getWorldEditHook();
        addParameter(new GeneratorNameParameter(plugin));
        withPlayerExecution((player, args, placeholders) -> {
            if (!SpigotServer.isPluginEnabled("WorldEdit") || worldEdit == null) {
                plugin.sendMessage(player, "missing-world-edit", placeholders);
                return;
            }
            final Generator generator = plugin.getGenerator(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "generator-not-found", placeholders);
                return;
            }
            final List<Location> selection = worldEdit.getWorldSelection(player);
            if (selection.isEmpty()) {
                plugin.sendMessage(player, "incomplete-region", placeholders, generator);
                return;
            }
            final GeneratorStage lastStage = generator.getLastStage();
            for (Location location : selection) {
                if (lastStage.matchBlock(location.getBlock().getBlockData())) {
                    plugin.getGenerators().createBlockGenerator(generator, location);
                }
            }
            plugin.sendMessage(player, "created-all-generators", placeholders, generator);
        });
    }

}
