package io.github.pigaut.orestack.command.generator;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.math.*;
import com.sk89q.worldedit.regions.*;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.server.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class GeneratorSetAllSubCommand extends SubCommand {

    public GeneratorSetAllSubCommand(@NotNull OrestackPlugin plugin) {
        super("set-all", plugin);
        addParameter("generator", false);
        withDescription(plugin.getLang("GENERATOR_SET_ALL_COMMAND"));
        withPermission("orestack.admin.set-all");
        withPlayerExecution((player, args) -> {
            if (!SpigotServer.isPluginEnabled("WorldEdit")) {
                player.sendMessage(plugin.getLang("MISSING_WORLD_EDIT"));
                return;
            }

            final String generatorName = args[0];
            final Generator generator = plugin.getGenerator(generatorName);
            if (generator == null) {
                player.sendMessage(plugin.getLang("GENERATOR_NOT_FOUND"));
                return;
            }
            final BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
            final Region region;
            try {
                region = WorldEdit.getInstance().getSessionManager().get(bPlayer).getSelection(bPlayer.getWorld());
            } catch (IncompleteRegionException e) {
                player.sendMessage(plugin.getLang("INCOMPLETE_REGION"));
                return;
            }

            final GeneratorStage lastStage = generator.getLastStage();
            for (BlockVector3 point : region) {
                final Location location = new Location(player.getWorld(), point.x(), point.y(), point.z());
                if (lastStage.matchBlock(location.getBlock().getBlockData())) {
                    plugin.getGenerators().addBlockGenerator(generator, location);
                }
            }

            player.sendMessage(plugin.getLang("CREATED_GENERATORS"));
        });
        withPlayerCompletion((player, args) -> plugin.getGenerators().getGeneratorNames());

    }

}
