package io.github.pigaut.rpg.command.structure;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;
import org.snakeyaml.engine.v2.common.*;

import java.io.*;
import java.util.*;

public class StructureSaveSubCommand extends SubCommand {

    public StructureSaveSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "save");
        withPermission(plugin.getPermission("structure.save"));
        withDescription(plugin.getTranslation("structure-save-command"));
        withParameter(CommandParameters.filePath(plugin, "structures"));
        withPlayerExecution((player, context, args) -> {
            PlayerState playerState = plugin.getPlayerState(player);

            File file = plugin.getFile("structures", args[0]);
            YamlConfig.createFileIfNotExists(file);

            if (!YamlConfig.isYamlFile(file)) {
                plugin.sendMessage(player, context, "not-yaml-file");
                return;
            }

            Location firstSelection = playerState.getFirstSelection();
            Location secondSelection = playerState.getSecondSelection();
            if (firstSelection == null || secondSelection == null) {
                plugin.sendMessage(player, context, "incomplete-region");
                return;
            }

            int centerX = (int) ((firstSelection.getBlockX() + secondSelection.getBlockX()) / 2.0);
            int lowestY = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY());
            int centerZ = (int) ((firstSelection.getBlockZ() + secondSelection.getBlockZ()) / 2.0);

            RootSequence sequence = YamlConfig.createEmptySequence(file, plugin.getConfigurator());
            sequence.setFlowStyle(FlowStyle.AUTO);

            Set<Material> structureBlacklist = plugin.getSettings().getStructureBlacklist();
            for (Location location : CuboidRegion.getAllLocations(player.getWorld(), firstSelection, secondSelection)) {
                Block block = location.getBlock();
                Material blockType = block.getType();

                if (structureBlacklist.contains(blockType)) {
                    continue;
                }

                ConfigSection blockConfig = sequence.addEmptySection();
                blockConfig.map(block);
                blockConfig.set("offset.x", location.getBlockX() - centerX);
                blockConfig.set("offset.y", location.getBlockY() - lowestY);
                blockConfig.set("offset.z", location.getBlockZ() - centerZ);
            }

            if (sequence.size() < 2) {
                plugin.sendMessage(player, context, "structure-minimum-blocks");
                return;
            }

            sequence.save();
            plugin.getStructures().reload();
            plugin.sendMessage(player, context, "saved-structure");
        });
    }

}
