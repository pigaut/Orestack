package io.github.pigaut.orestack;

import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.config.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.item.*;
import io.github.pigaut.orestack.listener.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.structure.*;
import io.github.pigaut.voxel.version.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private static OrestackPlugin plugin;
    private final ToolManager toolManager = new ToolManager(this);
    private final StructureManager structureManager = new StructureManager(this);
    private final GeneratorTemplateManager templateManager = new GeneratorTemplateManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final OrestackPlayerManager playerManager = new OrestackPlayerManager(this);
    private final Database database = SQLib.createDatabase(new File(("plugins/Orestack/data")));

    public static OrestackPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        database.closeConnection();
    }

    @Override
    public @NotNull List<SpigotVersion> getCompatibleVersions() {
        return SpigotVersion.getVersionsNewerThan(SpigotVersion.V1_16_5);
    }

    @Override
    public @NotNull PlayerManager<OrestackPlayer> getPlayers() {
        return playerManager;
    }

    @Override
    public @NotNull OrestackConfigurator getConfigurator() {
        return new OrestackConfigurator(this);
    }

    @Override
    public @Nullable OrestackPlayer getPlayer(String playerName) {
        return playerManager.getPlayer(playerName);
    }

    @Override
    public @Nullable OrestackPlayer getPlayer(UUID playerId) {
        return playerManager.getPlayer(playerId);
    }

    @Override
    public @Nullable Integer getMetricsId() {
        return 24502;
    }

    @Override
    public boolean forceMetrics() {
        return true;
    }

    @Override
    public @Nullable Integer getResourceId() {
        return 121905;
    }

    @Override
    public @Nullable String getDonationLink() {
        return "https://www.paypal.com/paypalme/Giovanni335";
    }

    @Override
    public @NotNull List<String> getPluginDirectories() {
        return List.of("items", "generators", "messages", "languages", "effects/particles", "effects/sounds");
    }

    @Override
    public List<String> getPluginResources() {
        return List.of("config.yml",
                "functions.yml",
                "languages/en.yml",
                "languages/fr.yml",
                "languages/zh.yml");
    }

    @Override
    public List<String> getExampleResources() {
        return List.of(
                "items/misc.yml",
                "messages/misc.yml",
                "effects/particles/misc.yml",
                "effects/particles/flame.yml",
                "effects/sounds/misc.yml",

                "generators/example.yml",

                "generators/examples/magic_wheat.yml",
                "generators/examples/diamond_node.yml",

                "generators/tutorials/hologram_tutorial.yml",
                "generators/tutorials/function_tutorial.yml",
                "generators/tutorials/flag_tutorial.yml",
                "generators/tutorials/drops_tutorial.yml",
                "generators/trees/great_oak_tree.yml",

                "generators/crops/wheat.yml",
                "generators/crops/potato.yml",
                "generators/crops/carrot.yml",
                "generators/crops/beetroot.yml",
                "generators/crops/melon.yml",
                "generators/crops/pumpkin.yml",
                "generators/crops/cocoa.yml",
                "generators/crops/sugar_cane.yml",
                "generators/crops/cactus.yml",
                "generators/crops/bamboo.yml",

                "generators/ores/coal.yml",
                "generators/ores/iron.yml",
                "generators/ores/gold.yml",
                "generators/ores/diamond.yml",
                "generators/ores/emerald.yml",

                "structures/trees/great_oak/great_oak_tree_0.yml",
                "structures/trees/great_oak/great_oak_tree_1.yml",
                "structures/trees/great_oak/great_oak_tree_2.yml",
                "structures/trees/great_oak/great_oak_tree_3.yml",
                "structures/trees/great_oak/great_oak_tree_4.yml",
                "structures/trees/great_oak/great_oak_tree_5.yml",
                "structures/trees/great_oak/great_oak_tree_6.yml"
        );
    }

    @Override
    public List<EnhancedCommand> getPluginCommands() {
        return List.of(new OrestackCommand(this));
    }

    @Override
    public List<Listener> getPluginListeners() {
        return List.of(
                new PlayerInteractListener(plugin),
                new BlockBreakListener(plugin),
                new BlockDestructionListener(plugin),
                new CropChangeListener(plugin),
                new ChunkLoadListener(plugin),
                new GeneratorEventListener()
        );
    }

    public ToolManager getTools() {
        return toolManager;
    }

    public @NotNull StructureManager getStructures() {
        return structureManager;
    }

    public @Nullable BlockStructure getBlockStructure(String name) {
        return structureManager.getBlockStructure(name);
    }

    public @NotNull GeneratorTemplateManager getGeneratorTemplates() {
        return templateManager;
    }

    public @Nullable GeneratorTemplate getGeneratorTemplate(String name) {
        return templateManager.getGeneratorTemplate(name);
    }

    public @NotNull GeneratorManager getGenerators() {
        return generatorManager;
    }

    public @Nullable Generator getGenerator(@NotNull Location location) {
        return generatorManager.getGenerator(location);
    }

    public Database getDatabase() {
        return database;
    }

}
