package io.github.pigaut.orestack;

import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.config.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.listener.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.orestack.options.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.voxel.version.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private static OrestackPlugin plugin;
    private final OptionsManager toolManager = new OptionsManager(this);
    private final GeneratorTemplateManager templateManager = new GeneratorTemplateManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final OptionsManager optionsManager = new OptionsManager(this);
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
    public @NotNull OrestackConfigurator getConfigurator() {
        return new OrestackConfigurator(this);
    }

    @Override
    public @NotNull PlayerStateManager<? extends PlayerState> getPlayersState() {
        return playerManager;
    }

    @Override
    public @NotNull PlayerState getPlayerState(@NotNull Player player) {
        return playerManager.getPlayerState(player);
    }

    @Override
    public @Nullable OrestackPlayer getPlayerState(@NotNull String playerName) {
        return playerManager.getPlayerState(playerName);
    }

    @Override
    public @Nullable OrestackPlayer getPlayerState(@NotNull UUID playerId) {
        return playerManager.getPlayerState(playerId);
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
        return List.of("items", "generators", "messages", "languages", "functions", "effects/particles", "effects/sounds");
    }

    @Override
    public List<String> getPluginResources() {
        return List.of("config.yml",
                "languages/en.yml");
    }

    @Override
    public List<String> getExampleResources() {
        return List.of(
                "items/misc.yml",
                "messages/misc.yml",
                "effects/particles/misc.yml",
                "effects/particles/flame.yml",
                "effects/sounds/misc.yml",
                "functions/misc.yml",
                "functions/tools.yml",
                "functions/trees.yml",

                "generators/example.yml",

                "generators/examples/magic_wheat.yml",
                "generators/examples/diamond_node.yml",

                "generators/rocks/piles/stone_pile.yml",
                "generators/rocks/piles/coal_pile.yml",
                "generators/rocks/piles/iron_pile.yml",
                "generators/rocks/piles/gold_pile.yml",
                "generators/rocks/piles/diamond_pile.yml",
                "generators/rocks/piles/emerald_pile.yml",

                "generators/tutorials/hologram_tutorial.yml",
                "generators/tutorials/function_tutorial.yml",
                "generators/tutorials/flag_tutorial.yml",
                "generators/tutorials/drops_tutorial.yml",

                "generators/trees/vanilla/oak_tree.yml",
                "generators/trees/vanilla/birch_tree.yml",
                "generators/trees/vanilla/spruce_tree.yml",
                "generators/trees/vanilla/acacia_tree.yml",
                "generators/trees/vanilla/jungle_tree.yml",
                "generators/trees/vanilla/dark_oak_tree.yml",
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

                "structures/rocks/piles/stone/stone_pile_1.yml",
                "structures/rocks/piles/stone/stone_pile_2.yml",
                "structures/rocks/piles/stone/stone_pile_3.yml",
                "structures/rocks/piles/stone/stone_pile_4.yml",
                "structures/rocks/piles/stone/stone_pile_5.yml",
                "structures/rocks/piles/stone/stone_pile_6.yml",
                "structures/rocks/piles/stone/stone_pile_7.yml",
                "structures/rocks/piles/stone/stone_pile_8.yml",
                "structures/rocks/piles/stone/stone_pile_9.yml",
                "structures/rocks/piles/stone/stone_pile_10.yml",

                "structures/rocks/piles/coal/coal_pile_1.yml",
                "structures/rocks/piles/coal/coal_pile_2.yml",
                "structures/rocks/piles/coal/coal_pile_3.yml",
                "structures/rocks/piles/coal/coal_pile_4.yml",
                "structures/rocks/piles/coal/coal_pile_5.yml",
                "structures/rocks/piles/coal/coal_pile_6.yml",
                "structures/rocks/piles/coal/coal_pile_7.yml",
                "structures/rocks/piles/coal/coal_pile_8.yml",
                "structures/rocks/piles/coal/coal_pile_9.yml",
                "structures/rocks/piles/coal/coal_pile_10.yml",

                "structures/rocks/piles/iron/iron_pile_1.yml",
                "structures/rocks/piles/iron/iron_pile_2.yml",
                "structures/rocks/piles/iron/iron_pile_3.yml",
                "structures/rocks/piles/iron/iron_pile_4.yml",
                "structures/rocks/piles/iron/iron_pile_5.yml",
                "structures/rocks/piles/iron/iron_pile_6.yml",
                "structures/rocks/piles/iron/iron_pile_7.yml",
                "structures/rocks/piles/iron/iron_pile_8.yml",
                "structures/rocks/piles/iron/iron_pile_9.yml",
                "structures/rocks/piles/iron/iron_pile_10.yml",

                "structures/rocks/piles/gold/gold_pile_1.yml",
                "structures/rocks/piles/gold/gold_pile_2.yml",
                "structures/rocks/piles/gold/gold_pile_3.yml",
                "structures/rocks/piles/gold/gold_pile_4.yml",
                "structures/rocks/piles/gold/gold_pile_5.yml",
                "structures/rocks/piles/gold/gold_pile_6.yml",
                "structures/rocks/piles/gold/gold_pile_7.yml",
                "structures/rocks/piles/gold/gold_pile_8.yml",
                "structures/rocks/piles/gold/gold_pile_9.yml",
                "structures/rocks/piles/gold/gold_pile_10.yml",

                "structures/rocks/piles/diamond/diamond_pile_1.yml",
                "structures/rocks/piles/diamond/diamond_pile_2.yml",
                "structures/rocks/piles/diamond/diamond_pile_3.yml",
                "structures/rocks/piles/diamond/diamond_pile_4.yml",
                "structures/rocks/piles/diamond/diamond_pile_5.yml",
                "structures/rocks/piles/diamond/diamond_pile_6.yml",
                "structures/rocks/piles/diamond/diamond_pile_7.yml",
                "structures/rocks/piles/diamond/diamond_pile_8.yml",
                "structures/rocks/piles/diamond/diamond_pile_9.yml",
                "structures/rocks/piles/diamond/diamond_pile_10.yml",

                "structures/rocks/piles/emerald/emerald_pile_1.yml",
                "structures/rocks/piles/emerald/emerald_pile_2.yml",
                "structures/rocks/piles/emerald/emerald_pile_3.yml",
                "structures/rocks/piles/emerald/emerald_pile_4.yml",
                "structures/rocks/piles/emerald/emerald_pile_5.yml",
                "structures/rocks/piles/emerald/emerald_pile_6.yml",
                "structures/rocks/piles/emerald/emerald_pile_7.yml",
                "structures/rocks/piles/emerald/emerald_pile_8.yml",
                "structures/rocks/piles/emerald/emerald_pile_9.yml",
                "structures/rocks/piles/emerald/emerald_pile_10.yml",

                "structures/trees/vanilla/oak/oak_tree_1.yml",
                "structures/trees/vanilla/oak/oak_tree_2.yml",
                "structures/trees/vanilla/oak/oak_tree_3.yml",
                "structures/trees/vanilla/oak/oak_tree_4.yml",

                "structures/trees/vanilla/birch/birch_tree_1.yml",
                "structures/trees/vanilla/birch/birch_tree_2.yml",
                "structures/trees/vanilla/birch/birch_tree_3.yml",
                "structures/trees/vanilla/birch/birch_tree_4.yml",
                "structures/trees/vanilla/birch/birch_tree_5.yml",

                "structures/trees/vanilla/spruce/spruce_tree_1.yml",
                "structures/trees/vanilla/spruce/spruce_tree_2.yml",
                "structures/trees/vanilla/spruce/spruce_tree_3.yml",
                "structures/trees/vanilla/spruce/spruce_tree_4.yml",
                "structures/trees/vanilla/spruce/spruce_tree_5.yml",

                "structures/trees/vanilla/acacia/acacia_tree_1.yml",
                "structures/trees/vanilla/acacia/acacia_tree_2.yml",
                "structures/trees/vanilla/acacia/acacia_tree_3.yml",
                "structures/trees/vanilla/acacia/acacia_tree_4.yml",
                "structures/trees/vanilla/acacia/acacia_tree_5.yml",
                "structures/trees/vanilla/acacia/acacia_tree_6.yml",
                "structures/trees/vanilla/acacia/acacia_tree_7.yml",

                "structures/trees/vanilla/jungle/jungle_tree_1.yml",
                "structures/trees/vanilla/jungle/jungle_tree_2.yml",
                "structures/trees/vanilla/jungle/jungle_tree_3.yml",
                "structures/trees/vanilla/jungle/jungle_tree_4.yml",
                "structures/trees/vanilla/jungle/jungle_tree_5.yml",
                "structures/trees/vanilla/jungle/jungle_tree_6.yml",
                "structures/trees/vanilla/jungle/jungle_tree_7.yml",
                "structures/trees/vanilla/jungle/jungle_tree_8.yml",

                "structures/trees/vanilla/dark_oak/dark_oak_tree_1.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_2.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_3.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_4.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_5.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_6.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_7.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_8.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_9.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_10.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_11.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_12.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_13.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_14.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_15.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_16.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_17.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_18.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_19.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_20.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_21.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_22.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_23.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_24.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_25.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_26.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_27.yml",
                "structures/trees/vanilla/dark_oak/dark_oak_tree_28.yml",

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
    public Map<SpigotVersion, List<String>> getExamplesByVersion() {
        return Map.of(
                SpigotVersion.V1_17, List.of(
                        "generators/rocks/piles/copper_pile.yml",
                        "generators/ores/copper.yml",

                        "structures/rocks/piles/copper/copper_pile_1.yml",
                        "structures/rocks/piles/copper/copper_pile_2.yml",
                        "structures/rocks/piles/copper/copper_pile_3.yml",
                        "structures/rocks/piles/copper/copper_pile_4.yml",
                        "structures/rocks/piles/copper/copper_pile_5.yml",
                        "structures/rocks/piles/copper/copper_pile_6.yml",
                        "structures/rocks/piles/copper/copper_pile_7.yml",
                        "structures/rocks/piles/copper/copper_pile_8.yml",
                        "structures/rocks/piles/copper/copper_pile_9.yml",
                        "structures/rocks/piles/copper/copper_pile_10.yml"
                )
        );
    }

    @Override
    public List<EnhancedCommand> getPluginCommands() {
        return List.of(new OrestackCommand(this));
    }

    @Override
    public List<Listener> getPluginListeners() {
        final List<Listener> listeners = new ArrayList<>();
        listeners.add(new PlayerInteractListener(plugin));
        listeners.add(new BlockBreakListener(plugin));
        listeners.add(new BlockDestructionListener(plugin));
        listeners.add(new CropChangeListener(plugin));
        listeners.add(new ChunkLoadListener(plugin));
        listeners.add(new GeneratorEventListener());
        if (SpigotServer.isPluginLoaded("WorldGuard")) {
            listeners.add(new WorldGuardListener(this));
        }
        return listeners;
    }

    @Override
    public Map<String, Menu> getPluginMenus() {
        return Map.of(
                "orestack", new OrestackMenu(this)
        );
    }

    public OptionsManager getTools() {
        return toolManager;
    }

    public @NotNull GeneratorTemplateManager getGeneratorTemplates() {
        return templateManager;
    }

    public @Nullable GeneratorTemplate getGeneratorTemplate(String name) {
        return templateManager.get(name);
    }

    public @NotNull List<GeneratorTemplate> getGeneratorTemplates(String group) {
        return templateManager.getAll(group);
    }

    public @NotNull GeneratorManager getGenerators() {
        return generatorManager;
    }

    public @Nullable Generator getGenerator(@NotNull Location location) {
        return generatorManager.getGenerator(location);
    }

    public OptionsManager getOptions() {
        return optionsManager;
    }

    public Database getDatabase() {
        return database;
    }

}
