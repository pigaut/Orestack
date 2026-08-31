package io.github.pigaut.rpg.plugin;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.core.buildstation.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.gameplay.brew.*;
import io.github.pigaut.rpg.core.gameplay.chicken.*;
import io.github.pigaut.rpg.core.gameplay.cow.*;
import io.github.pigaut.rpg.core.gameplay.playerblocks.*;
import io.github.pigaut.rpg.core.language.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.command.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.registry.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.config.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.instanced.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.module.menu.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.dynamic.*;
import io.github.pigaut.rpg.module.menu.entries.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.module.structure.virtual.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.task.scheduler.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.section.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class EnhancedJavaPlugin extends JavaPlugin implements EnhancedPlugin, PluginProperties {

    protected final Scheduler scheduler = Server.isFolia() ? new FoliaScheduler(this) : new PluginScheduler(this);
    private final PluginLogger logger = new PluginLogger(this);

    private final TranslationRegistry translationRegistry = new TranslationRegistry(this);
    private final CommandRegistry commandRegistry = new CommandRegistry(this);
    private final PlaceholderRegistry placeholderRegistry = new PlaceholderRegistry(this);
    private final ToolRegistry toolRegistry = new ToolRegistry();
    private final DynamicIconRegistry dynamicIconRegistry = new DynamicIconRegistry();
    private final ForEachSourceRegistry forEachSourceRegistry = new ForEachSourceRegistry();
    private final ConditionRegistry conditionRegistry = new ConditionRegistry(this);
    private final ActionRegistry actionRegistry = new ActionRegistry(this);

    private final StatManager playerStatsManager = new StatManager(this);

    private final ParticleManager particleManager = new ParticleManager(this);
    private final SoundManager soundManager = new SoundManager(this);
    private final MessageManager messageManager = new MessageManager(this);
    private final ItemManager itemManager = new ItemManager(this);
    private final RecipeManager recipeManager = new RecipeManager(this);
    private final GlobalFunctionManager functionManager = new GlobalFunctionManager(this);

    private final MobTemplateManager mobTemplateManager = new MobTemplateManager(this);
    private final MobManager mobManager = new MobManager(this);
    private final MobSpawnPadManager mobSpawnPadManager = new MobSpawnPadManager(this);

    private final GeneratorTemplateManager generatorTemplateManager = new GeneratorTemplateManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);

    private final GateTemplateManager gateTemplateManager = new GateTemplateManager(this);
    private final GateManager gateManager = new GateManager(this);

    private final CollectionTemplateManager collectionTemplateManager = new CollectionTemplateManager(this);
    private final SkillTemplateManager skillTemplateManager = new SkillTemplateManager(this);

    private final PlayerStateManager playerStateManager = new PlayerStateManager(this);
    private final PlayerDataManager playerDataManger = new PlayerDataManager(this);

    private final MenuButtonManager menuButtonManager = new MenuButtonManager(this);
    private final MenuEntriesManager menuEntriesManager = new MenuEntriesManager(this);
    private final MenuManager menuManager = new MenuManager(this);
    private final CustomCommandManager commandManager = new CustomCommandManager(this);

    private final StructureTemplateManager structureManager = new StructureTemplateManager(this);
    private final VirtualStructureManager virtualStructureManager = new VirtualStructureManager(this);
    private final BuildStationManager buildStationManager = new BuildStationManager(this);
    private final PlayerPlacedBlockManager playerPlacedBlockManager = new PlayerPlacedBlockManager(this);
    private final BrewedPotionManager brewedPotionManager = new BrewedPotionManager(this);
    private final ChickenEggManager chickenEggManager = new ChickenEggManager(this);
    private final CowMilkManager cowMilkManager = new CowMilkManager(this);

    private final Settings settings = new Settings(this);

    private final PluginBootstrap bootstrap = new PluginBootstrap(this);
    private final Object readyLock = new Object();
    private final List<Runnable> pendingLoadTasks = new ArrayList<>();
    private final List<Runnable> pendingTasks = new ArrayList<>();
    private final List<Runnable> pendingAsyncTasks = new ArrayList<>();
    private volatile boolean ready = false;
    private boolean reloading = false;

    private String namespace;

    @Override
    public void onDisable() {
        bootstrap.shutdown();
    }

    @Override
    public void onEnable() {
        bootstrap.boot();
    }

    public void onBoot() {

    }

    public void onPreStartup() {

    }

    public void onStartup() {

    }

    public void onReload() {

    }

    public void onShutdown() {

    }

    @Override
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        List<Runnable> loadTasksToRun = null;
        List<Runnable> tasksToRun = null;
        List<Runnable> asyncTasksToRun = null;

        synchronized (readyLock) {
            this.ready = ready;
            if (ready) {
                loadTasksToRun = new ArrayList<>(pendingLoadTasks);
                pendingLoadTasks.clear();

                tasksToRun = new ArrayList<>(pendingTasks);
                pendingTasks.clear();

                asyncTasksToRun = new ArrayList<>(pendingAsyncTasks);
                pendingAsyncTasks.clear();
            }
        }

        if (tasksToRun != null) {
            for (Runnable loadTask : loadTasksToRun) {
                loadTask.run();
            }
            for (Runnable task : tasksToRun) {
                task.run();
            }
            List<Runnable> finalAsyncTasks = asyncTasksToRun;
            scheduler.runTaskAsync(() -> {
                for (Runnable asyncTask : finalAsyncTasks) {
                    asyncTask.run();
                }
            });
        }
    }

    @Override
    public boolean isReloading() {
        return reloading;
    }

    @Override
    public void loadWhenReady(@NotNull Runnable task) {
        synchronized (readyLock) {
            if (ready) {
                task.run();
                return;
            }
            pendingLoadTasks.add(task);
        }
    }

    @Override
    public void runWhenReady(@NotNull Runnable task) {
        synchronized (readyLock) {
            if (ready) {
                task.run();
                return;
            }
            pendingTasks.add(task);
        }
    }

    @Override
    public void runWhenReadyAsync(@NotNull Runnable task) {
        synchronized (readyLock) {
            if (ready) {
                scheduler.runTaskAsync(task);
                return;
            }
            pendingAsyncTasks.add(task);
        }
    }

    @Override
    public String getNamespace() {
        if (namespace == null) {
            namespace = getName().toLowerCase(Locale.ROOT);
        }
        return namespace;
    }

    @Override
    public String getVersion() {
        return this.getDescription().getVersion();
    }

    @Override
    public PluginLogger getColoredLogger() {
        return this.logger;
    }

    @Override
    public PluginBootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public @NotNull Settings getSettings() {
        return settings;
    }

    @Override
    public @Nullable Database getDatabase() {
        return bootstrap.getDatabase();
    }

    @Override
    public @NotNull Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public @NotNull Scheduler getRegionScheduler(@NotNull Location location) {
        if (Server.isFolia()) {
            return new FoliaRegionScheduler(this, location);
        }
        return scheduler;
    }

    @Override
    public @NotNull String getPermission(@NotNull String permission) {
        return getName().toLowerCase() + "." + permission;
    }

    @Override
    public @NotNull NamespacedKey getNamespacedKey(@NotNull String key) {
        return new NamespacedKey(this, key);
    }

    @Override
    public @NotNull Configurator getConfigurator() {
        return bootstrap.getConfigurator();
    }

    @Override
    public @NotNull RootSection getConfiguration() {
        return bootstrap.getConfiguration();
    }

    @Override
    public @NotNull TranslationRegistry getTranslations() {
        return translationRegistry;
    }

    @Override
    public @NotNull String getTranslation(@NotNull String key) {
        return translationRegistry.get(key);
    }

    @Override
    public @NotNull String getTranslationOrDefault(@NotNull String key, @NotNull String defaultMessage) {
        return translationRegistry.getOrDefault(key, defaultMessage);
    }

    @Override
    public @NotNull CommandRegistry getRegisteredCommands() {
        return commandRegistry;
    }

    @Override
    public @Nullable EnhancedCommand getRegisteredCommand(@NotNull String name) {
        return commandRegistry.getCustomCommand(name);
    }

    @Override
    public @NotNull PlaceholderRegistry getPlaceholders() {
        return placeholderRegistry;
    }

    @Override
    public @Nullable String resolvePlaceholder(@NotNull String placeholder, @NotNull Context context) {
        PlaceholderRegistry registry = getPlaceholders();
        return registry.resolvePlaceholder(placeholder, context);
    }

    @Override
    public @NotNull ToolRegistry getTools() {
        return toolRegistry;
    }

    @Override
    public boolean isTool(@NotNull ItemStack item) {
        Tool tool = getTool(item);
        return tool != null;
    }

    @Override
    public @Nullable Tool getTool(@NotNull String name) {
        return toolRegistry.get(name);
    }

    @Override
    public @Nullable Tool getTool(@NotNull ItemStack item) {
        return toolRegistry.get(item);
    }

    @Override
    public @NotNull DynamicIconRegistry getDynamicIcons() {
        return dynamicIconRegistry;
    }

    @Override
    public @Nullable DynamicIcon getDynamicIcon(@NotNull String name) {
        return dynamicIconRegistry.get(name);
    }

    @Override
    public @NotNull ForEachSourceRegistry getForEachSources() {
        return forEachSourceRegistry;
    }

    @Override
    public @NotNull VirtualStructureManager getVirtualStructures() {
        return virtualStructureManager;
    }

    @Override
    public @NotNull PlayerStateManager getPlayerStates() {
        return playerStateManager;
    }

    @Override
    public @NotNull PlayerState getPlayerState(@NotNull Player player) {
        return playerStateManager.get(player);
    }

    @Override
    public @Nullable PlayerState getPlayerState(@NotNull LivingEntity entity) {
        if (entity instanceof Player player) {
            return getPlayerState(player);
        }
        return null;
    }

    @Override
    public @Nullable PlayerState getPlayerState(@NotNull UUID playerId) {
        return playerStateManager.get(playerId);
    }

    @Override
    public @NotNull PlayerDataManager getPlayerData() {
        return playerDataManger;
    }

    @Override
    public @NotNull PlayerData getPlayerData(@NotNull Player player) {
        return playerDataManger.get(player);
    }

    @Override
    public @Nullable PlayerData getPlayerData(@NotNull UUID playerId) {
        return playerDataManger.get(playerId);
    }

    @Override
    public @NotNull ItemManager getItems() {
        return itemManager;
    }

    @Override
    public boolean hasItemTemplate(@NotNull ItemStack item) {
        ItemTemplate itemTemplate = getItemTemplate(item);
        return itemTemplate != null;
    }

    @Override
    public @Nullable ItemTemplate getItemTemplate(@NotNull String name) {
        return itemManager.get(name);
    }

    @Override
    public @Nullable ItemTemplate getItemTemplate(@NotNull ItemStack item) {
        return itemManager.get(item);
    }

    @Override
    public @NotNull List<ItemTemplate> getItems(@NotNull String group) {
        return itemManager.getAll(group);
    }

    @Override
    public @NotNull MessageManager getMessages() {
        return messageManager;
    }

    @Override
    public @Nullable Message getMessage(@NotNull String name) {
        return messageManager.get(name);
    }

    @Override
    public @NotNull ParticleManager getParticles() {
        return particleManager;
    }

    @Override
    public @Nullable ParticleEffect getParticle(@NotNull String name) {
        return particleManager.get(name);
    }

    @Override
    public @NotNull SoundManager getSounds() {
        return soundManager;
    }

    @Override
    public @Nullable SoundEffect getSound(@NotNull String name) {
        return soundManager.get(name);
    }

    @Override
    public @NotNull GlobalFunctionManager getGlobalFunctions() {
        return functionManager;
    }

    @Override
    public @Nullable GlobalFunction getGlobalFunction(@NotNull String name) {
        return functionManager.get(name);
    }

    @Override
    public @NotNull ConditionRegistry getConditions() {
        return conditionRegistry;
    }

    @Override
    public @NotNull ActionRegistry getActions() {
        return actionRegistry;
    }

    @Override
    public @Nullable ConfigLoader<? extends Condition> getCondition(@NotNull String name) {
        return conditionRegistry.getLoader(name);
    }

    @Override
    public @Nullable ConfigLoader<? extends Action> getAction(@NotNull String name) {
        return actionRegistry.getLoader(name);
    }

    @Override
    public @NotNull RecipeManager getRecipes() {
        return recipeManager;
    }

    @Override
    public @Nullable RecipeTemplate getRecipe(@NotNull String name) {
        return recipeManager.get(name);
    }

    @Override
    public @NotNull StatManager getStats() {
        return playerStatsManager;
    }

    @Override
    public @NotNull GeneratorTemplateManager getGeneratorTemplates() {
        return generatorTemplateManager;
    }

    @Override
    public @Nullable GeneratorTemplate getGeneratorTemplate(String name) {
        return generatorTemplateManager.get(name);
    }

    @Override
    public @NotNull List<GeneratorTemplate> getGeneratorTemplates(String group) {
        return generatorTemplateManager.getAll(group);
    }

    @Override
    public @NotNull GeneratorManager getGenerators() {
        return generatorManager;
    }

    @Override
    public @Nullable GlobalGenerator getGlobalGenerator(@NotNull Location location) {
        return generatorManager.getGlobalGenerator(location);
    }

    @Override
    public @Nullable VirtualGenerator getVirtualGenerator(@NotNull Location location) {
        return generatorManager.getVirtualGenerator(location);
    }

    @Override
    public @Nullable InstancedGenerator getInstancedGenerator(@NotNull Player player, @NotNull Location location) {
        return generatorManager.getPlayerGenerator(player, location);
    }

    @Override
    public @Nullable Generator getGenerator(@Nullable Player player, @NotNull Location location) {
        return generatorManager.getGenerator(player, location);
    }

    @Override
    public @NotNull GateTemplateManager getGateTemplates() {
        return gateTemplateManager;
    }

    @Override
    public @Nullable GateTemplate getGateTemplate(String name) {
        return gateTemplateManager.get(name);
    }

    @Override
    public @NotNull List<GateTemplate> getGateTemplates(String group) {
        return gateTemplateManager.getAll(group);
    }

    @Override
    public @NotNull GateManager getGates() {
        return gateManager;
    }

    @Override
    public @Nullable Gate getGate(@NotNull Location location) {
        return gateManager.getGate(location);
    }

    @Override
    public @NotNull CollectionTemplateManager getCollectionTemplates() {
        return collectionTemplateManager;
    }

    @Override
    public @Nullable CollectionTemplate getCollectionTemplate(@NotNull String name) {
        return collectionTemplateManager.get(name);
    }

    @Override
    public @Nullable CollectionTemplate getCollectionTemplate(@NotNull ItemStack item) {
        return collectionTemplateManager.get(item);
    }

    @Override
    public @NotNull SkillTemplateManager getSkillTemplates() {
        return skillTemplateManager;
    }

    @Override
    public @Nullable SkillTemplate getSkillTemplate(@NotNull String name) {
        return skillTemplateManager.get(name);
    }

    @Override
    public @NotNull MobTemplateManager getMobTemplates() {
        return mobTemplateManager;
    }

    @Override
    public @Nullable MobTemplate getMobTemplate(@NotNull String name) {
        return mobTemplateManager.get(name);
    }

    @Override
    public @NotNull MobManager getMobs() {
        return mobManager;
    }

    @Override
    public boolean isMob(@NotNull Entity entity) {
        Mob mob = getMob(entity);
        return mob != null;
    }

    @Override
    public @Nullable Mob getMob(@NotNull UUID entityId) {
        return mobManager.get(entityId);
    }

    @Override
    public @Nullable Mob getMob(@NotNull Entity entity) {
        return mobManager.get(entity);
    }

    @Override
    public @NotNull MobSpawnPadManager getMobSpawnPads() {
        return mobSpawnPadManager;
    }

    @Override
    public @Nullable MobSpawnPad getMobSpawnPad(@NotNull Location location) {
        return mobSpawnPadManager.get(location);
    }

    @Override
    public @NotNull MenuButtonManager getButtons() {
        return menuButtonManager;
    }

    @Override
    public @Nullable ButtonTemplate getButton(@NotNull String name) {
        return menuButtonManager.get(name);
    }

    @Override
    public @NotNull MenuEntriesManager getMenuEntries() {
        return menuEntriesManager;
    }

    @Override
    public @Nullable MenuEntries getMenuEntries(@NotNull String name) {
        return menuEntriesManager.get(name);
    }

    @Override
    public @NotNull MenuManager getMenus() {
        return menuManager;
    }

    @Override
    public @Nullable Menu getMenu(@NotNull String name) {
        return menuManager.get(name);
    }

    @Override
    public @NotNull CustomCommandManager getCustomCommands() {
        return commandManager;
    }

    @Override
    public @Nullable CustomCommand getCustomCommand(@NotNull String name) {
        return commandManager.get(name);
    }

    @Override
    public @NotNull StructureTemplateManager getStructures() {
        return structureManager;
    }

    @Override
    public @Nullable StructureTemplate getStructure(@NotNull String name) {
        return structureManager.get(name);
    }

    @Override
    public @NotNull BuildStationManager getBuildStations() {
        return buildStationManager;
    }

    @Override
    public @Nullable BuildStation getBuildStation(@NotNull Location location) {
        return buildStationManager.getBuildStation(location);
    }

    @Override
    public @NotNull PlayerPlacedBlockManager getPlayerPlacedBlocks() {
        return playerPlacedBlockManager;
    }

    @Override
    public boolean isPlayerPlacedBlock(@NotNull Block block) {
        return playerPlacedBlockManager.isPlayerPlacedBlock(block);
    }

    @Override
    public @NotNull ChickenEggManager getChickenEggs() {
        return chickenEggManager;
    }

    @Override
    public boolean isChickenLaidEgg(@NotNull Item item) {
        return chickenEggManager.contains(item);
    }

    @Override
    public @NotNull CowMilkManager getCowsMilk() {
        return cowMilkManager;
    }

    @Override
    public BrewedPotionManager getBrewedPotions() {
        return brewedPotionManager;
    }

    @Override
    public void sendMessage(@NotNull Player player, @NotNull Context context, @NotNull String messageId) throws TranslationNotFoundException {
        String message = getTranslation(messageId);
        String parsedMessage = PlaceholderUtil.parseAll(context, message);
        PlayerUtil.sendChat(player, parsedMessage);
    }

    @Override
    public void sendMessage(@NotNull CommandSender sender, @NotNull Context context, @NotNull String messageId) throws TranslationNotFoundException {
        String message = getTranslation(messageId);
        String parsedMessage = PlaceholderUtil.parseAll(context, message);
        sender.sendMessage(parsedMessage);
    }

    @Override
    public void registerListener(@NotNull Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void createDirectory() {
        getDataFolder().mkdirs();
    }

    @Override
    public void createDirectory(@NotNull String path) {
        getFile(path).mkdirs();
    }

    @Override
    public void saveResource(@NotNull String path) {
        if (!getFile(path).exists()) {
            saveResource(path, false);
        }
    }

    @Override
    public File getFile(@NotNull String file) {
        return new File(getDataFolder(), file);
    }

    @Override
    public File getFile(@NotNull String parent, @NotNull String child) {
        return new File(getDataFolder(), parent + "/" + child);
    }

    @Override
    public List<File> getFiles(@NotNull String directoryPath) {
        File directory = getFile(directoryPath);
        List<File> yamlFiles = new ArrayList<>();
        collectYamlFiles(directory, yamlFiles);
        return yamlFiles;
    }

    @Override
    public String getFilePath(@NotNull String path) {
        return path.replaceAll("plugins\\\\" + this.getName() + "\\\\", "");
    }

    @Override
    public List<String> getFilePaths(@NotNull String directory) {
        return getFiles(directory).stream()
                .map(file -> file.getPath().replaceAll("plugins\\\\" + this.getName() + "\\\\" + directory + "\\\\", ""))
                .toList();
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
        setReady(!reloading);
    }

    public void reload(@NotNull Consumer<Manager> onManagerReloaded, @NotNull Consumer<ErrorCollector> onReloadComplete) throws PluginReloadInProgressException {
        bootstrap.reload(onManagerReloaded, onReloadComplete);
    }

    public @NotNull List<Manager> getAllManagers() {
        List<Manager> managers = new ArrayList<>();

        managers.add(playerStatsManager);

        managers.add(particleManager);
        managers.add(soundManager);
        managers.add(messageManager);
        managers.add(itemManager);
        managers.add(recipeManager);
        managers.add(functionManager);
        managers.add(structureManager);

        managers.add(mobTemplateManager);
        managers.add(mobSpawnPadManager);
        managers.add(mobManager);

        managers.add(generatorTemplateManager);
        managers.add(generatorManager);

        managers.add(gateTemplateManager);
        managers.add(gateManager);

        managers.add(collectionTemplateManager);
        managers.add(skillTemplateManager);

        for (Field field : getClass().getDeclaredFields()) {
            Manager manager = ReflectionUtil.accessField(field, Manager.class, this);
            if (manager != null) {
                managers.add(manager);
            }
        }

        managers.add(menuButtonManager);
        managers.add(menuManager);
        managers.add(commandManager);

        managers.add(virtualStructureManager);
        managers.add(buildStationManager);
        managers.add(playerPlacedBlockManager);
        managers.add(chickenEggManager);
        managers.add(cowMilkManager);

        return managers;
    }

    public @NotNull Configurator createConfigurator() {
        return new SpigotConfigurator();
    }

    public void saveResources(@NotNull String folder) {
        saveResources(folder, List.of());
    }

    public void saveResources(@NotNull String folder, List<String> ignoredFiles) {
        Preconditions.checkArgument(!folder.startsWith("/"), "Folder should not start with a '/'");

        try {
            URI sourceUri = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            FileSystem fileSystem = null;
            Path rootPath;
            Path baseClassPath;

            if (sourceUri.toString().endsWith(".jar")) {
                URI jarUri = URI.create("jar:" + sourceUri.toString());
                try {
                    fileSystem = FileSystems.getFileSystem(jarUri);
                } catch (FileSystemNotFoundException e) {
                    fileSystem = FileSystems.newFileSystem(jarUri, Collections.emptyMap());
                }

                rootPath = fileSystem.getPath("/" + folder);
                baseClassPath = fileSystem.getPath("/");
            } else {
                URL folderUrl = getClass().getResource("/" + folder);
                if (folderUrl == null) return;

                rootPath = Paths.get(folderUrl.toURI());
                baseClassPath = Paths.get(getClass().getResource("/").toURI());
            }

            try (Stream<Path> walker = Files.walk(rootPath)) {
                walker.filter(Files::isRegularFile).forEach(path -> {

                    // Relativize perfectly from the root of the classpath
                    // Example: transforms "/structures/deposits/copper/a.yml" -> "structures/deposits/copper/a.yml"
                    String relativePath = baseClassPath.relativize(path).toString().replace("\\", "/");

                    if (relativePath.startsWith("/")) {
                        relativePath = relativePath.substring(1);
                    }

                    if (ignoredFiles.contains(relativePath)) {
                        return;
                    }

                    File outFile = new File(getDataFolder(), relativePath);
                    if (!outFile.exists()) {
                        saveResource(relativePath, false);
                    }
                });
            } catch (NoSuchFileException e) {
                // The directory physically doesn't exist in the jar
            }

        } catch (URISyntaxException | IOException e) {
            logger.warning("Failed to save resources in folder '" + folder + "': " + e.getMessage());
        }
    }

    public @Nullable UpdateChecker getUpdateChecker() {
        return bootstrap.getUpdateChecker();
    }

    public @Nullable PluginMetrics getMetrics() {
        return bootstrap.getMetrics();
    }

    private void collectYamlFiles(File directory, List<File> yamlFiles) {
        if (directory == null || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                collectYamlFiles(file, yamlFiles);
            } else if (YamlConfig.isYamlFile(file)) {
                yamlFiles.add(file);
            }
        }
    }

}
