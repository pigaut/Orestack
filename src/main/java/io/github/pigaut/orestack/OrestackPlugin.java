package io.github.pigaut.orestack;

import io.github.pigaut.orestack.config.*;
import io.github.pigaut.orestack.external.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.config.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.voxel.version.*;
import io.github.pigaut.voxel.yaml.configurator.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final PlayerManager<OrestackPlayer> playerManager = new PlayerManager<>(OrestackPlayer::new);
    private final OrestackConfigurator configurator = new OrestackConfigurator(this);
    private final Database database = SQLib.createDatabase(getFile("data.db"));
    private WorldEditHook worldEditHook = null;

    private static OrestackPlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    public static OrestackPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull List<SpigotVersion> getCompatibleVersions() {
        return SpigotVersion.getVersionsNewerThan(SpigotVersion.V1_17);
    }

    @Override
    protected void createHooks() {
        worldEditHook = shouldCreateHook("WorldEdit", "7.3", "7.3.9") ? new WorldEditHook() : null;
    }

    @Override
    public @NotNull List<String> getPluginDirectories() {
        return List.of("items", "generators", "messages", "languages");
    }

    @Override
    public List<String> getExampleResources() {
        return List.of(
                "config.yml",
                "languages/en.yml",
                "generators/example_generator.yml",
                "generators/crops/wheat.yml",
                "generators/crops/potato.yml",
                "generators/crops/carrot.yml",
                "generators/crops/beetroot.yml",
                "generators/crops/melon.yml",
                "generators/crops/pumpkin.yml",
                "generators/crops/cocoa/cocoa_north.yml",
                "generators/ores/coal.yml",
                "generators/ores/iron.yml",
                "generators/ores/gold.yml",
                "generators/ores/diamond.yml",
                "items/example_items.yml",
                "messages/example_messages.yml",
                "flags.yml"
        );
    }

    public @NotNull GeneratorManager getGenerators() {
        return generatorManager;
    }

    public @Nullable Generator getGenerator(String name) {
        return generatorManager.getGenerator(name);
    }

    public @Nullable Generator getGenerator(ItemStack item) {
        final String generatorName = GeneratorItem.getGeneratorFromItem(item);
        return getGenerator(generatorName);
    }

    public @Nullable BlockGenerator getBlockGenerator(@NotNull Location location) {
        return generatorManager.getBlockGenerator(location);
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
        return configurator;
    }

    public Database getDatabase() {
        return database;
    }

    public @Nullable WorldEditHook getWorldEditHook() {
        return worldEditHook;
    }

}
