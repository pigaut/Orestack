package io.github.pigaut.rpg.plugin;

import io.github.pigaut.rpg.core.gameplay.brew.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.registry.*;
import io.github.pigaut.rpg.module.function.condition.config.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.instanced.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.player.data.base.*;
import io.github.pigaut.rpg.player.state.base.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.rpg.core.buildstation.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.gameplay.cow.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.gameplay.playerblocks.*;
import io.github.pigaut.rpg.core.gameplay.chicken.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.module.command.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.dynamic.*;
import io.github.pigaut.rpg.module.menu.entries.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.module.mob.spawnpad.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.ItemTemplate;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.core.language.*;
import io.github.pigaut.rpg.module.menu.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.structure.virtual.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.task.scheduler.*;
import io.github.pigaut.yaml.configurator.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.section.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public interface EnhancedPlugin extends Plugin {

    boolean isReady();

    boolean isReloading();

    void loadWhenReady(@NotNull Runnable task);

    void runWhenReady(@NotNull Runnable task);

    void runWhenReadyAsync(@NotNull Runnable task);

    String getNamespace();

    String getVersion();

    PluginLogger getColoredLogger();

    PluginBootstrap getBootstrap();

    @NotNull
    Settings getSettings();

    @Nullable
    Database getDatabase();

    @NotNull
    Scheduler getScheduler();

    @NotNull
    Scheduler getRegionScheduler(@NotNull Location location);

    @NotNull
    String getPermission(@NotNull String permission);

    @NotNull
    NamespacedKey getNamespacedKey(@NotNull String key);

    @NotNull
    Configurator getConfigurator();

    @NotNull
    RootSection getConfiguration();

    @NotNull
    TranslationRegistry getTranslations();

    @NotNull
    String getTranslation(@NotNull String name) throws TranslationNotFoundException;

    @NotNull
    String getTranslationOrDefault(@NotNull String name, @NotNull String def);

    @NotNull
    CommandRegistry getRegisteredCommands();

    @Nullable
    EnhancedCommand getRegisteredCommand(@NotNull String name);

    @NotNull
    PlaceholderRegistry getPlaceholders();

    @Nullable
    String resolvePlaceholder(@NotNull String placeholder, @NotNull Context context);

    @NotNull
    ToolRegistry getTools();

    boolean isTool(@NotNull ItemStack item);

    @Nullable
    Tool getTool(@NotNull String id);

    @Nullable
    Tool getTool(@NotNull ItemStack item);

    @NotNull
    DynamicIconRegistry getDynamicIcons();

    @Nullable
    DynamicIcon getDynamicIcon(@NotNull String name);

    @NotNull ForEachSourceRegistry getForEachSources();

    @NotNull
    VirtualStructureManager getVirtualStructures();

    @NotNull
    EnhancedPlayerStateManager<? extends PlayerState> getPlayerStates();

    @NotNull
    PlayerState getPlayerState(@NotNull Player player);

    @Nullable
    PlayerState getPlayerState(@NotNull LivingEntity entity);

    @Nullable
    PlayerState getPlayerState(@NotNull UUID playerId);

    @NotNull
    EnhancedPlayerDataManager<? extends EnhancedPlayerData> getPlayerData();

    @NotNull
    PlayerData getPlayerData(@NotNull Player player);

    @Nullable
    PlayerData getPlayerData(@NotNull UUID playerId);

    @NotNull
    ItemManager getItems();

    boolean hasItemTemplate(@NotNull ItemStack item);

    @Nullable
    ItemTemplate getItemTemplate(@NotNull String name);

    @Nullable
    ItemTemplate getItemTemplate(@NotNull ItemStack item);

    @NotNull
    List<ItemTemplate> getItems(@NotNull String group);

    @NotNull
    MessageManager getMessages();

    @Nullable
    Message getMessage(@NotNull String name);

    @NotNull
    ParticleManager getParticles();

    @Nullable
    ParticleEffect getParticle(@NotNull String name);

    @NotNull
    SoundManager getSounds();

    @Nullable
    SoundEffect getSound(@NotNull String name);

    @NotNull
    GlobalFunctionManager getGlobalFunctions();

    @Nullable
    Function getGlobalFunction(@NotNull String name);

    @NotNull
    ConditionRegistry getConditions();

    @Nullable
    ConfigLoader<? extends Condition> getCondition(@NotNull String name);

    @NotNull
    ActionRegistry getActions();

    @Nullable
    ConfigLoader<? extends Action> getAction(@NotNull String name);

    @NotNull
    RecipeManager getRecipes();

    @Nullable
    RecipeTemplate getRecipe(@NotNull String name);

    @NotNull
    StatManager getStats();

    @NotNull GeneratorTemplateManager getGeneratorTemplates();

    @Nullable GeneratorTemplate getGeneratorTemplate(String name);

    @NotNull List<GeneratorTemplate> getGeneratorTemplates(String group);

    @NotNull GeneratorManager getGenerators();

    @Nullable GlobalGenerator getGlobalGenerator(@NotNull Location location);

    @Nullable VirtualGenerator getVirtualGenerator(@NotNull Location location);

    @Nullable InstancedGenerator getInstancedGenerator(@NotNull Player player, @NotNull Location location);

    @Nullable Generator getGenerator(@Nullable Player player, @NotNull Location location);

    @NotNull GateTemplateManager getGateTemplates();

    @Nullable GateTemplate getGateTemplate(String name);

    @NotNull List<GateTemplate> getGateTemplates(String group);

    @NotNull GateManager getGates();

    @Nullable Gate getGate(@NotNull Location location);

    @NotNull CollectionTemplateManager getCollectionTemplates();

    @Nullable CollectionTemplate getCollectionTemplate(@NotNull String name);

    @Nullable CollectionTemplate getCollectionTemplate(@NotNull ItemStack item);

    @NotNull SkillTemplateManager getSkillTemplates();

    @Nullable SkillTemplate getSkillTemplate(@NotNull String name);

    @NotNull
    MobTemplateManager getMobTemplates();

    @Nullable
    MobTemplate getMobTemplate(@NotNull String name);

    @NotNull
    MobManager getMobs();

    boolean isMob(@NotNull Entity entity);

    @Nullable
    Mob getMob(@NotNull UUID entityId);

    @Nullable
    Mob getMob(@NotNull Entity entity);

    @NotNull
    MobSpawnPadManager getMobSpawnPads();

    @Nullable
    MobSpawnPad getMobSpawnPad(@NotNull Location location);

    @NotNull
    MenuButtonManager getButtons();

    @Nullable
    ButtonTemplate getButton(@NotNull String name);

    @NotNull
    MenuEntriesManager getMenuEntries();

    @Nullable
    MenuEntries getMenuEntries(@NotNull String name);

    @NotNull
    MenuManager getMenus();

    @Nullable
    Menu getMenu(@NotNull String name);

    @NotNull
    CustomCommandManager getCustomCommands();

    @Nullable
    CustomCommand getCustomCommand(@NotNull String name);

    @NotNull
    StructureTemplateManager getStructures();

    @Nullable
    StructureTemplate getStructure(@NotNull String name);

    @NotNull
    BuildStationManager getBuildStations();

    @Nullable
    BuildStation getBuildStation(@NotNull Location location);

    @NotNull
    PlayerPlacedBlockManager getPlayerPlacedBlocks();

    boolean isPlayerPlacedBlock(@NotNull Block block);

    @NotNull
    ChickenEggManager getChickenEggs();

    boolean isChickenLaidEgg(@NotNull Item item);

    @NotNull CowMilkManager getCowsMilk();

    BrewedPotionManager getBrewedPotions();

    void sendMessage(@NotNull Player player, @NotNull Context context, @NotNull String messageId) throws TranslationNotFoundException;

    void sendMessage(@NotNull CommandSender sender, @NotNull Context context, @NotNull String messageId) throws TranslationNotFoundException;

    void registerListener(@NotNull Listener listener);

    void createDirectory();

    void createDirectory(@NotNull String path);

    void saveResource(@NotNull String path);

    File getFile(@NotNull String file);

    File getFile(@NotNull String parent, @NotNull String child);

    List<File> getFiles(@NotNull String path);

    String getFilePath(@NotNull String path);

    List<String> getFilePaths(@NotNull String directory);

}
