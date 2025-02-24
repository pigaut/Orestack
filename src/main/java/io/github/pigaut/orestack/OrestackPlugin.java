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
import io.github.pigaut.voxel.version.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private final ToolManager toolManager = new ToolManager(this);
    private final StructureManager structureManager = new StructureManager(this);
    private final GeneratorTemplateManager templateManager = new GeneratorTemplateManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final OrestackPlayerManager playerManager = new OrestackPlayerManager(this);
    private final Database database = SQLib.createDatabase(new File(("plugins/Orestack/data")));

    private static OrestackPlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

//    @Override
//    public void onDisable() {
//        super.onDisable();
//        database.closeConnection();
//    }

    public static OrestackPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull List<SpigotVersion> getCompatibleVersions() {
        return SpigotVersion.getVersionsNewerThan(SpigotVersion.V1_16_5);
    }

    @Override
    public @Nullable Integer getMetricsId() {
        return 24502;
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
    public List<String> getPluginResources() {
        return List.of("config.yml",
                "flags.yml",
                "languages/en.yml",
                "languages/fr.yml",
                "languages/zh.yml");
    }

    @Override
    public @NotNull List<String> getPluginDirectories() {
        return List.of("items", "generators", "messages", "languages", "structures", "effects/particles", "effects/sounds");
    }

    @Override
    public List<String> getExampleResources() {
        return List.of(
                "items/items.yml",
                "messages/messages.yml",
                "effects/particles/particles.yml",
                "effects/sounds/sounds.yml",

                "generators/example.yml",
                "generators/diamond_node.yml",
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

                "structures/diamond_node.yml",
                "structures/trees/oak_tree.yml",
                "structures/trees/great_oak/great_oak_tree_1.yml",
                "structures/trees/great_oak/great_oak_tree_2.yml",
                "structures/trees/great_oak/great_oak_tree_3.yml",
                "structures/trees/great_oak/great_oak_tree_4.yml",
                "structures/trees/great_oak/great_oak_tree_5.yml",
                "structures/trees/great_oak/great_oak_tree_6.yml",
                "structures/trees/great_oak/great_oak_tree_7.yml"
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

    @Override
    public @NotNull PlayerManager<OrestackPlayer> getPlayers() {
        return playerManager;
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
    public @NotNull OrestackConfigurator getConfigurator() {
        return new OrestackConfigurator(this);
    }

    public Database getDatabase() {
        return database;
    }

}
