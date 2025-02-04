package io.github.pigaut.orestack.command.structure;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.command.node.*;
import io.github.pigaut.voxel.command.parameter.*;
import io.github.pigaut.voxel.yaml.*;
import io.github.pigaut.voxel.yaml.node.sequence.*;
import io.github.pigaut.voxel.yaml.snakeyaml.engine.v2.common.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

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

            if (plugin.getStructures().getAllBlockStructures().size() >= 10) {
                plugin.sendMessage(player, "structure-limit", placeholders);
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

                Integer age = null;
                if (block.getBlockData() instanceof Ageable ageable) {
                    age = ageable.getAge();
                }
                BlockFace direction = null;
                if (block.getBlockData() instanceof Directional directional) {
                    direction = directional.getFacing();
                }
                Axis orientation = null;
                if (block.getBlockData() instanceof Orientable orientable) {
                    orientation = orientable.getAxis();
                }

                final ConfigSection blockConfig = config.addSection();
                blockConfig.set("block", blockType);
                if (age != null) {
                    blockConfig.set("age", age);
                }
                if (direction != null) {
                    blockConfig.set("direction", direction);
                }
                if (orientation != null) {
                    blockConfig.set("orientation", orientation);
                }
                blockConfig.set("offset.x", centerX - location.getBlockX());
                blockConfig.set("offset.y", location.getBlockY() - lowestY);
                blockConfig.set("offset.z", centerZ - location.getBlockZ());
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
