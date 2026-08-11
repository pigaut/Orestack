package io.github.pigaut.rpg.plugin.boot;

import io.github.pigaut.rpg.server.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.language.*;
import io.github.pigaut.rpg.listener.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.PluginLogger;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.task.scheduler.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.node.section.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class PluginBootstrap {

    private final EnhancedJavaPlugin plugin;
    private final PluginLogger logger;

    private final Set<BootPhase> missingStartupRequirements = new HashSet<>();
    private final List<Runnable> startupTasks = new ArrayList<>();
    private final List<ErrorCollector> startupErrors = new ArrayList<>();
    private final List<Manager> loadedManagers = new ArrayList<>();

    private Configurator configurator;
    private RootSection config;
    private @Nullable UpdateChecker updateChecker = null;
    private @Nullable PluginMetrics metrics = null;
    private @Nullable Database database = null;

    private boolean initialized = false;

    public PluginBootstrap(EnhancedJavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getColoredLogger();
    }

    public void boot() {
        Preconditions.checkState(!initialized, "Plugin bootstrap has already been initialized.");
        plugin.setReady(false);

        // Generate and load configuration
        configurator = plugin.createConfigurator();
        config = PluginSetup.loadConfig(plugin, "config.yml", true);

        // Load settings from config.yml
        Settings settings = plugin.getSettings();
        startupErrors.add(settings.loadConfigurationData());

        // Generate directories and files
        PluginSetup.generateDirectoriesAndFiles(plugin);

        // Load language file messages
        TranslationRegistry dictionary = plugin.getTranslations();
        startupErrors.add(dictionary.loadConfigurationData());

        // Initialize command registry and register commands
        CommandRegistry commandRegistry = plugin.getRegisteredCommands();
        commandRegistry.init();
        plugin.registerCommands(commandRegistry);

        // Register listeners
        DefaultListeners.registerAll(plugin);
        plugin.registerListeners();

        initialized = true;
        plugin.onBoot();

        // Register and check already met startup requirements
        List<BootPhase> startupRequirements = new ArrayList<>(plugin.getStartupRequirements());
        plugin.getCompatiblePlugins().stream()
                .map(BootPhase::pluginEnabled)
                .forEach(startupRequirements::add);

        for (BootPhase bootPhase : startupRequirements) {
            if (bootPhase instanceof PluginBootPhase) {
                Plugin plugin = Server.getPlugin(bootPhase.getNamespace());
                if (plugin == null) {
                    continue;
                }

                String pluginPhase = bootPhase.getKey();
                if (pluginPhase == null || (pluginPhase.equals("enabled") && plugin.isEnabled())) {
                    continue;
                }
            }

            missingStartupRequirements.add(bootPhase);
        }

        if (missingStartupRequirements.isEmpty()) {
            startup();
        }
    }

    public void registerStartupTask(@NotNull Runnable startupTask) {
        startupTasks.add(startupTask);
    }

    public void markReady(@NotNull BootPhase bootPhase) {
        missingStartupRequirements.remove(bootPhase);
        if (missingStartupRequirements.isEmpty()) {
            startup();
        }
    }

    public void startup() {
        Preconditions.checkState(missingStartupRequirements.isEmpty(), "Cannot startup plugin because not all startup requirements are met.");

        PluginSetup.checkServerVersion(plugin);
        PluginSetup.generateExampleFiles(plugin);
        metrics = PluginSetup.createMetrics(plugin);
        updateChecker = PluginSetup.createUpdateChecker(plugin);
        configurator = plugin.createConfigurator();
        database = PluginSetup.createDatabase(plugin);
        plugin.registerHooks();

        Settings settings = plugin.getSettings();

        List<Manager> pluginManagers = plugin.getAllManagers();
        plugin.getScheduler().runTaskAsync(() -> {
            for (Manager manager : pluginManagers) {
                if (manager instanceof Toggleable toggleable
                        && !settings.isModuleEnabled(toggleable.getModule())) {
                    continue;
                }

                manager.clear();
                manager.loadData();
                if (manager instanceof ConfigBacked configBackedManager) {
                    startupErrors.add(configBackedManager.loadConfigurationData());
                }

                plugin.getScheduler().runTask(() -> {
                    if (!plugin.isEnabled()) {
                        logger.severe("Attempted to enable manager while plugin is disabled");
                    }
                    manager.enable();
                });
                loadedManagers.add(manager);
            }

            for (Runnable startupTask : startupTasks) {
                startupTask.run();
            }

            LifecycleLog.startup(plugin, startupErrors);
            startupErrors.clear();

            int autoSave = plugin.getSettings().getAutoSave().toTicks();
            if (autoSave > 0) {
                plugin.getScheduler().runTaskTimerAsync(autoSave, () -> {
                    logger.info("Saving data to database...");
                    for (Manager manager : loadedManagers) {
                        manager.saveData();
                    }
                });
            }

            plugin.getScheduler().runTask(() -> {
                plugin.setReady(true);
                plugin.onStartup();
            });
        });
    }

    public void shutdown() {
        missingStartupRequirements.clear();

        for (Manager manager : loadedManagers) {
            manager.disable();
            manager.saveData();
        }

        loadedManagers.clear();

        Database database = plugin.getDatabase();
        if (database != null) {
            database.closeConnection();
        }

        plugin.onShutdown();
        LifecycleLog.shutdown(plugin);
    }

    public void reload(@NotNull Consumer<ErrorCollector> errorConsumer) throws PluginReloadInProgressException {
        if (plugin.isReloading()) {
            throw new PluginReloadInProgressException();
        }

        plugin.setReloading(true);
        ErrorCollector errorCollector = new SimpleErrorCollector();

        // Generate and load configuration
        configurator = plugin.createConfigurator();
        config = PluginSetup.loadConfig(plugin, "config.yml", true);

        // Load settings from config.yml
        Settings settings = plugin.getSettings();
        errorCollector.collectAll(settings.loadConfigurationData());

        // Generate directories and files
        PluginSetup.generateDirectoriesAndFiles(plugin);

        // Load language file messages
        TranslationRegistry dictionary = plugin.getTranslations();
        errorCollector.collectAll(dictionary.loadConfigurationData());

        PluginSetup.generateExampleFiles(plugin);
        metrics = PluginSetup.createMetrics(plugin);
        updateChecker = PluginSetup.createUpdateChecker(plugin);

        Scheduler scheduler = plugin.getScheduler();
        scheduler.runTaskLater(1, () -> {
            reloadManager(scheduler, loadedManagers.iterator(), errorCollector, () -> {
                LifecycleLog.reload(plugin, errorCollector);
                errorConsumer.accept(errorCollector);

                plugin.onReload();
                plugin.setReloading(false);
            });

            for (Runnable startupTask : startupTasks) {
                startupTask.run();
            }
        });
    }

    private void reloadManager(@NotNull Scheduler scheduler, @NotNull Iterator<Manager> managers,
                               @NotNull ErrorCollector errorCollector, @NotNull Runnable onComplete) {
        if (!managers.hasNext()) {
            onComplete.run();
            return;
        }

        Manager manager = managers.next();
        manager.disable();

        scheduler.runTaskAsync(() -> {
            manager.saveData();
            manager.clear();
            manager.loadData();
            if (manager instanceof ConfigBacked configBackedManager) {
                errorCollector.collectAll(configBackedManager.loadConfigurationData());
            }
            scheduler.runTask(() -> {
                manager.enable();
                reloadManager(scheduler, managers, errorCollector, onComplete);
            });
        });
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isMissingStartupRequirements() {
        return !missingStartupRequirements.isEmpty();
    }

    public List<BootPhase> getMissingStartupRequirements() {
        return new ArrayList<>(missingStartupRequirements);
    }

    public Configurator getConfigurator() {
        Preconditions.checkState(configurator != null, "Configurator has not been initialized yet.");
        return configurator;
    }

    public RootSection getConfiguration() {
        Preconditions.checkState(config != null, "Configuration has not been initialized yet.");
        return config;
    }

    public @Nullable UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public @Nullable PluginMetrics getMetrics() {
        return metrics;
    }

    public @Nullable Database getDatabase() {
        return database;
    }

    public List<Manager> getLoadedManagers() {
        return new ArrayList<>(loadedManagers);
    }

}
