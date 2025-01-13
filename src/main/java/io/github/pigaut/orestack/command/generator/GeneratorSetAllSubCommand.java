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

public class GeneratorSetAllSubCommand extends SubCommand {

    private final @Nullable WorldEditHook worldEdit;

    public GeneratorSetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin.getLang("SET_ALL_GENERATORS_COMMAND", "set-all"), plugin);
        worldEdit = plugin.getWorldEditHook();
        addParameter(plugin.getLang("GENERATOR_NAME_PARAMETER", "generator-name"));
        withDescription(plugin.getLang("SET_ALL_GENERATORS_DESCRIPTION"));
        withPermission("orestack.generator.set-all");
        withPlayerCompletion((player, args) -> plugin.getGenerators().getGeneratorNames());
        withPlayerExecution((player, args) -> {
            if (!SpigotServer.isPluginEnabled("WorldEdit") || worldEdit == null) {
                plugin.sendMessage(player, "MISSING_WORLD_EDIT", this);
                return;
            }

            final Generator generator = plugin.getGenerator(args[0]);
            if (generator == null) {
                plugin.sendMessage(player, "GENERATOR_NOT_FOUND", this);
                return;
            }

            final List<Location> selection = worldEdit.getWorldSelection(player);
            if (selection.isEmpty()) {
                plugin.sendMessage(player, "INCOMPLETE_REGION", this, generator);
                return;
            }

            final GeneratorStage lastStage = generator.getLastStage();
            for (Location location : selection) {
                if (lastStage.matchBlock(location.getBlock().getBlockData())) {
                    plugin.getGenerators().createBlockGenerator(generator, location);
                }
            }
            plugin.sendMessage(player, "CREATED_ALL_GENERATORS", this, generator);
        });
    }

}
