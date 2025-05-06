package io.github.pigaut.orestack.command.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.command.parameter.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;
import org.snakeyaml.engine.v2.common.*;

import java.io.*;

public class StructureSaveSubCommand extends LangSubCommand {

    public StructureSaveSubCommand(@NotNull OrestackPlugin plugin) {
        super("save-structure", plugin);
        addParameter(new FilePathParameter(plugin, "structures"));
        withPlayerExecution((player, args, placeholders) -> {
            final OrestackPlayer playerState = plugin.getPlayer(player.getUniqueId());
            if (playerState == null) {
                plugin.sendMessage(player, "loading-player-data", placeholders);
                return;
            }

            final File file = plugin.getFile("structures", args[0]);
            YamlConfig.createFileIfNotExists(file);

            if (!YamlConfig.isYamlFile(file)) {
                plugin.sendMessage(player, "not-yaml-file", placeholders);
                return;
            }

            final Location firstSelection = playerState.getFirstSelection();
            final Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, "incomplete-region", placeholders);
                return;
            }

            final int centerX = (int) ((firstSelection.getBlockX() + secondSelection.getBlockX()) / 2.0);
            final int lowestY = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY());
            final int centerZ = (int) ((firstSelection.getBlockZ() + secondSelection.getBlockZ()) / 2.0);

            final RootSequence config = plugin.loadConfigSequence(file);
            config.setFlowStyle(FlowStyle.AUTO);
            config.clear();

            for (Location location : GeneratorTools.getSelectedRegion(player.getWorld(), firstSelection, secondSelection)) {
                final Block block = location.getBlock();
                final Material blockType = block.getType();

                if (plugin.getStructures().isBlacklisted(blockType)) {
                    continue;
                }

                final ConfigSection blockConfig = config.addSection();
                blockConfig.map(block);
                blockConfig.set("offset.x", location.getBlockX() - centerX);
                blockConfig.set("offset.y", location.getBlockY() - lowestY);
                blockConfig.set("offset.z", location.getBlockZ() - centerZ);
            }

            if (config.size() < 2) {
                plugin.sendMessage(player, "structure-minimum-blocks", placeholders);
                return;
            }

            config.save();
            plugin.getStructures().reload();
            plugin.sendMessage(player, "saved-structure", placeholders);
        });
    }

}
