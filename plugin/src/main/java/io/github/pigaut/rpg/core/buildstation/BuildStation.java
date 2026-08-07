package io.github.pigaut.rpg.core.buildstation;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.module.structure.global.Structure;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;
import org.snakeyaml.engine.v2.common.*;

import java.io.*;
import java.util.*;

public class BuildStation {

    private final EnhancedPlugin plugin;
    private final Location origin, blueprintOrigin, corner1, corner2;
    private final int xLength, height, zLength;
    private final Location previousButton, saveButton, nextButton, clearButton;

    private List<BlockState> removedBlocks;
    private Structure frameStructure;
    private List<RootSequence> structureBlueprints;
    private int cursor = 0;

    public BuildStation(EnhancedPlugin plugin, Location origin, int xLength, int height, int zLength) {
        this.plugin = plugin;
        this.origin = origin;
        this.xLength = xLength;
        this.height = height;
        this.zLength = zLength;

        int halfX = xLength / 2;
        int halfZ = zLength / 2;

        int frameStartX = -halfX - 2;
        int frameEndX = halfX + 2;

        int frameStartZ = -halfZ - 2;
        int frameEndZ = halfZ + 2;

        blueprintOrigin = origin.clone().add(0, 1, 0);

        corner1 = origin.clone().add(frameEndX - 2, 1, frameEndZ - 2);
        corner2 = origin.clone().add(frameStartX + 2, height, frameStartZ + 2);

        previousButton = origin.clone().add(frameEndZ - 1, 0, frameStartZ - 1);
        saveButton = origin.clone().add(frameEndZ - 2, 0,  frameStartZ - 1);
        nextButton = origin.clone().add(frameEndZ - 3, 0, frameStartZ - 1);
        clearButton = origin.clone().add(frameStartX + 1, 0, frameStartZ - 1);
    }

    public Location getOrigin() {
        return origin;
    }

    public @NotNull Location getPreviousButtonLocation() {
        return previousButton;
    }

    public @NotNull Location getSaveButtonLocation() {
        return saveButton;
    }

    public @NotNull Location getNextButtonLocation() {
        return nextButton;
    }

    public @NotNull Location getClearButtonLocation() {
        return clearButton;
    }

    public void clearBuildArea() {
        int halfX = xLength / 2;
        int halfZ = zLength / 2;

        int frameStartX = -halfX - 2;
        int frameEndX = halfX + 2;
        int frameStartZ = -halfZ - 2;
        int frameEndZ = halfZ + 2;

        for (int x = frameStartX; x < frameEndX; x++) {
            for (int z = frameStartZ; z < frameEndZ; z++) {
                for (int y = 1; y <= height; y++) {
                    Location location = origin.clone().add(x, y, z);
                    location.getBlock().setType(Material.AIR, false);
                }
            }
        }
    }

    public void nextStructure() {
        saveStructure();

        cursor++;
        if (structureBlueprints.size() <= cursor) {
            structureBlueprints.add(new RootSequence(plugin.getConfigurator()));
        }

        StructureTemplate frameTemplate = new BuildStationStructureTemplate(plugin, cursor + 1, xLength, height, zLength);
        frameStructure = frameTemplate.place(origin);

        StructureTemplate structureBlueprint = structureBlueprints.get(cursor).get(StructureTemplate.class).orElse(null);
        if (structureBlueprint != null && structureBlueprint.size() > 0) {
            clearBuildArea();
            structureBlueprint.place(blueprintOrigin);
        }
    }

    public void previousStructure() {
        if (cursor <= 0) {
            return;
        }
        saveStructure();

        cursor--;
        if (structureBlueprints.size() <= cursor) {
            structureBlueprints.add(new RootSequence(plugin.getConfigurator()));
        }

        StructureTemplate frameTemplate = new BuildStationStructureTemplate(plugin, cursor + 1, xLength, height, zLength);
        frameStructure = frameTemplate.place(origin);

        StructureTemplate structureBlueprint = structureBlueprints.get(cursor).get(StructureTemplate.class).orElse(null);
        if (structureBlueprint != null && structureBlueprint.size() > 0) {
            clearBuildArea();
            structureBlueprint.place(blueprintOrigin);
        }
    }

    public @NotNull RootSequence saveStructure() {
        int centerX = (int) ((corner1.getBlockX() + corner2.getBlockX()) / 2.0);
        int lowestY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int centerZ = (int) ((corner1.getBlockZ() + corner2.getBlockZ()) / 2.0);

        RootSequence sequence = structureBlueprints.get(cursor);
        sequence.clear();
        sequence.setFlowStyle(FlowStyle.AUTO);

        Set<Material> structureBlacklist = plugin.getSettings().getStructureBlacklist();
        for (Location location : CuboidRegion.getAllLocations(origin.getWorld(), corner1, corner2)) {
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

        return sequence;
    }

    public void saveStructureToFile(@NotNull Player player) {
        PlayerState playerState = plugin.getPlayerState(player);
        playerState.collectChatInput()
                .description("Enter file path in chat")
                .onInput(filePath -> {
                    RootSequence sequence = saveStructure();
                    String yamlFile = YamlConfig.ensureYamlExtension(filePath);
                    File file = plugin.getFile("structures", yamlFile);
                    sequence.save(file);
                    plugin.getStructures().reload();
                    PlayerUtil.sendChat(player, ColorUtil.parseAll("&aSaved structure to " + yamlFile + " successfully"));
                })
                .start();
    }

    public @NotNull Set<Block> getOccupiedBlocks() {
        StructureTemplate template = getStructureTemplate();
        return template.getOccupiedBlocks(origin);
    }

    public @NotNull StructureTemplate getStructureTemplate() {
        return new BuildStationStructureTemplate(plugin, cursor + 1, xLength, height, zLength);
    }

    public void createStructure() {
        StructureTemplate frameTemplate = getStructureTemplate();
        removedBlocks = new ArrayList<>();
        for (Block block : frameTemplate.getOccupiedBlocks(origin)) {
            removedBlocks.add(block.getState());
        }
        frameStructure = frameTemplate.place(origin);
        clearBuildArea();
    }

    public void create() {
        if (!plugin.getBuildStations().registerBuildStation(this)) {
            return;
        }

        createStructure();
        structureBlueprints = new ArrayList<>();
        structureBlueprints.add(new RootSequence(plugin.getConfigurator()));
    }

    public void removeStructure() {
        clearBuildArea();
        if (frameStructure != null) {
            frameStructure.remove();
        }
        for (BlockState removedBlock : removedBlocks) {
            removedBlock.update(true, false);
        }
    }

    public void remove() {
        plugin.getBuildStations().unregisterBuildStation(this);
        removeStructure();
    }

}
