package io.github.pigaut.rpg.plugin.boot;

import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.sql.database.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.UpdateChecker;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.node.section.*;
import org.bstats.charts.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class PluginSetup {

    private PluginSetup() {}

    public static @NotNull RootSection loadConfig(@NotNull EnhancedPlugin plugin, @NotNull String fileName, boolean createIfMissing) {
        File file = plugin.getFile(fileName);
        if (!file.exists() && createIfMissing) {
            plugin.saveResource(fileName);
        }
        Configurator configurator = plugin.getConfigurator();
        RootSection config = YamlConfig.loadSectionOrEmpty(file, configurator);

        boolean keepConfigUpToDate = config.getBoolean("keep-file-up-to-date").orElse(true);
        if (keepConfigUpToDate) {
            RootSection defaultConfig = new RootSection(plugin.getConfigurator());
            try (InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream(fileName)) {
                if (inputStream == null) {
                    plugin.getLogger().warning("Failed to update file " + fileName + ": Internal resource not found");
                    return config;
                }

                defaultConfig.loadFromStream(inputStream);
                config.addDefaults(defaultConfig);
                config.reorderFields(new ArrayList<>(defaultConfig.getKeys()));
                config.save();

            } catch (IOException | ConfigLoadException e) {
                plugin.getLogger().warning("Failed to update file " + fileName + ": " + e.getMessage());
            }
        }

        return config;
    }

    public static void checkServerVersion(EnhancedJavaPlugin plugin) {
        int version = Server.getVersion();
        if (version == Version.UNKNOWN) {
            return;
        }

        if (plugin.getIncompatibleVersions().contains(version)) {
            throw new UnsupportedVersionException(plugin);
        }
    }

    public static @Nullable PluginMetrics createMetrics(EnhancedJavaPlugin plugin) {
        Integer metricsId = plugin.getMetricsId();
        if (metricsId == null) {
            return null;
        }

        PluginMetrics currentMetrics = plugin.getMetrics();
        if (currentMetrics != null) {
            return currentMetrics;
        }

        currentMetrics = new PluginMetrics(plugin, metricsId, plugin.getSettings().isMetrics());
        currentMetrics.addCustomChart(new SimplePie("premium", () ->
                Boolean.toString(plugin.isPremium())));

        String buyerId = "%%__USER__%%";
        currentMetrics.addCustomChart(new SimplePie("distribution", () -> {
            if (buyerId.startsWith("%%") && buyerId.endsWith("%%")) {
                return "internal-stable";
            }
            String encoded = Base64.getEncoder().encodeToString(buyerId.getBytes()).replace("=", "");
            return "dist-" + encoded;
        }));

        return currentMetrics;
    }

    public static @Nullable UpdateChecker createUpdateChecker(EnhancedJavaPlugin plugin) {
        Integer resourceId = plugin.getResourceId();
        if (resourceId == null) {
            return null;
        }

        UpdateChecker currentChecker = plugin.getUpdateChecker();
        if (currentChecker == null) {
            currentChecker = new UpdateChecker(plugin, plugin.getResourceId());
            plugin.registerListener(currentChecker);
        }

        if (!plugin.forceUpdateChecker() && !plugin.getSettings().isCheckForUpdates()) {
            return null;
        }

        currentChecker.checkForUpdates();
        return currentChecker;
    }

    public static void generateDirectoriesAndFiles(EnhancedJavaPlugin plugin) {
        for (Module module : plugin.getSettings().getEnabledModules()) {
            if (module.hasDirectory()) {
                plugin.createDirectory(module.getDirectory());
            }
        }

        for (String resource : plugin.getDefaultResources()) {
            plugin.saveResource(resource);
        }

        if (plugin.getSettings().isGenerateLanguageFiles()) {
            plugin.createDirectory("languages");
            plugin.saveResources("languages");
        }
    }

    public static void generateExampleFiles(EnhancedJavaPlugin plugin) {
        if (!plugin.getSettings().isGenerateExamples()) {
            return;
        }

        Map<Integer, List<String>> examplesByVersion = plugin.getExamplesByVersion();
        Map<String, List<String>> examplesByPlugin = plugin.getExamplesByPlugin();

        // Ignore all examples that require a specific version or plugin installed.
        List<String> ignoredFiles = new ArrayList<>();
        examplesByVersion.forEach((version, files) -> ignoredFiles.addAll(files));
        examplesByPlugin.forEach((pluginName, files) -> ignoredFiles.addAll(files));

        // Save examples under module directories except ones with requirements
        List<Module> disabledModules = new ArrayList<>();
        for (Module module : Module.values()) {
            if (!module.hasDirectory()) {
                continue;
            }

            if (plugin.getSettings().isModuleEnabled(module)) {
                plugin.saveResources(module.getDirectory(), ignoredFiles);
                continue;
            }

            disabledModules.add(module);
        }

        // Save examples by version
        int currentVersion = Server.getVersion();
        for (var entry : examplesByVersion.entrySet()) {
            Integer requiredVersion = entry.getKey();
            if (currentVersion < requiredVersion) {
                continue;
            }

            List<String> versionExamples = entry.getValue();
            for (String resourcePath : versionExamples) {
                boolean shouldSave = true;
                for (Module disabledModule : disabledModules) {
                    if (resourcePath.startsWith(disabledModule.getDirectory())) {
                        shouldSave = false;
                        break;
                    }
                }
                if (shouldSave) {
                    plugin.saveResource(resourcePath);
                }
            }
        }

        // Save examples by plugin
        for (var entry : examplesByPlugin.entrySet()) {
            String pluginName = entry.getKey();
            if (!Server.isPluginEnabled(pluginName)) {
                continue;
            }

            List<String> pluginExamples = entry.getValue();
            for (String resourcePath : pluginExamples) {
                boolean shouldSave = true;
                for (Module disabledModule : disabledModules) {
                    if (resourcePath.startsWith(disabledModule.getDirectory())) {
                        shouldSave = false;
                        break;
                    }
                }
                if (shouldSave) {
                    plugin.saveResource(resourcePath);
                }

                plugin.saveResource(resourcePath);
            }
        }
    }

    public static @Nullable Database createDatabase(EnhancedJavaPlugin plugin) {
        String databaseName = plugin.getDatabaseName();
        if (databaseName == null) {
            return null;
        }

        Database current = plugin.getDatabase();

        if (current != null) {
            current.openConnection();
            return current;
        }

        File file = plugin.getFile(databaseName);
        current = new FileDatabase(file);
        current.openConnection();
        return current;
    }


}
