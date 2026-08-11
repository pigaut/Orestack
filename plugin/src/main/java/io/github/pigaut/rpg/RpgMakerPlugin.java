package io.github.pigaut.rpg;

import io.github.pigaut.rpg.api.*;
import io.github.pigaut.rpg.listener.block.*;
import io.github.pigaut.rpg.listener.collection.*;
import io.github.pigaut.rpg.listener.gate.*;
import io.github.pigaut.rpg.listener.generator.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.command.*;
import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.core.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.hook.itemsadder.*;
import io.github.pigaut.rpg.hook.plotsquared.*;
import io.github.pigaut.rpg.listener.player.PlayerChunkLoadListener;
import io.github.pigaut.rpg.listener.skill.*;
import io.github.pigaut.rpg.module.collection.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.gate.tool.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.instanced.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.generator.tool.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.settings.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.module.function.foreach.config.*;
import io.github.pigaut.rpg.module.function.foreach.type.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.module.menu.button.dynamic.*;
import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.spawnegg.*;
import io.github.pigaut.rpg.module.mob.spawnpad.tool.*;
import io.github.pigaut.rpg.player.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.boot.*;
import io.github.pigaut.rpg.plugin.boot.phase.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.configurator.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RpgMakerPlugin extends EnhancedJavaPlugin {

    private static RpgMakerPlugin plugin;

    private final RpgSettings settings = new RpgSettings(this);

    private final GeneratorTemplateManager generatorTemplateManager = new GeneratorTemplateManager(this);
    private final GeneratorOptionsManager generatorOptionsManager = new GeneratorOptionsManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);

    private final GateTemplateManager gateTemplateManager = new GateTemplateManager(this);
    private final GateOptionsManager gateOptionsManager = new GateOptionsManager(this);
    private final GateManager gateManager = new GateManager(this);

    private final CollectionTemplateManager collectionTemplateManager = new CollectionTemplateManager(this);
    private final SkillTemplateManager skillTemplateManager = new SkillTemplateManager(this);

    private final RpgPlayerStateManager playerStateManager = new RpgPlayerStateManager(this);
    private final RpgPlayerDataManger playerDataManger = new RpgPlayerDataManger(this);

    public static RpgMakerPlugin getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onBoot() {
        DynamicIconRegistry dynamicIcons = getDynamicIcons();
        dynamicIcons.register("player_head", (item, context) -> {
            Player player = context.player();
            if (player != null) {
                SkullUtil.setSkullTexture(item, player);
            }
        });

        dynamicIcons.register("collection_item", (item, context) -> {
            ItemCollection collection = context.get(ItemCollection.class);
            if (collection != null) {
                item.setType(collection.getItem().getType());
            }
        });

        dynamicIcons.register("skill_icon", (item, context) -> {
            Skill skill = context.get(Skill.class);
            if (skill != null) {
                item.setType(skill.getIcon().getType());
            }
        });

        ForEachSourceRegistry forEachSources = getForEachSources();
        forEachSources.register("online_player", new ForEachOnlinePlayer());
        forEachSources.register("enchant_added", new ForEachEnchantAdded());
        forEachSources.register("mob_attacker", new ForEachMobAttacker());
        forEachSources.register("entity_in_front", new ForEachEntityInFrontLoader());
        forEachSources.register("entity_in_radius", new ForEachEntityInRadiusLoader());
        forEachSources.register("entity_in_range", new ForEachEntityInRangeLoader());
        forEachSources.register("entity_in_ring", new ForEachEntityInRingLoader());
    }

    @Override
    public void onStartup() {
        RpgMaker.setApiInstance(new SimpleRpgMakerAPI(this));

        // Register Placeholders
        DefaultPlaceholders.registerAll(this);
        PlayerPlaceholders.registerAll(this);
        SkillPlaceholders.registerAll(this);
        CollectionPlaceholders.registerAll(this);
        ItemPlaceholders.registerAll(this);
        GeneratorPlaceholders.registerAll(this);
        GatePlaceholders.registerAll(this);
        MobPlaceholders.registerAll(this);

        // Register Tools
        ToolRegistry tools = getTools();
        tools.register("generator", new GeneratorTool(this));
        tools.register("gate", new GateTool(this));
        tools.register("mob_spawn_pad", new MobSpawnPadTool(this));
        tools.register("mob_spawn_egg", new MobSpawnEggTool(this));
    }

    @Override
    public void onReload() {
        // Update placeholders
        PlaceholderRegistry placeholders = getPlaceholders();
        placeholders.clear();
        DefaultPlaceholders.registerAll(this);
        PlayerPlaceholders.registerAll(this);
        SkillPlaceholders.registerAll(this);
        CollectionPlaceholders.registerAll(this);
        ItemPlaceholders.registerAll(this);
        GeneratorPlaceholders.registerAll(this);
        GatePlaceholders.registerAll(this);
        MobPlaceholders.registerAll(this);
    }

    @Override
    public void onShutdown() {

    }

    @Override
    public @NotNull RpgSettings getSettings() {
        return settings;
    }

    @Override
    public @NotNull RpgPlayerStateManager getPlayersState() {
        return playerStateManager;
    }

    @Override
    public @NotNull RpgPlayerState getPlayerState(@NotNull Player player) {
        return playerStateManager.get(player);
    }

    @Override
    public @Nullable RpgPlayerState getPlayerState(@NotNull UUID playerId) {
        return playerStateManager.get(playerId);
    }

    @Override
    public @NotNull RpgPlayerDataManger getPlayerData() {
        return playerDataManger;
    }

    @Override
    public @NotNull RpgPlayerData getPlayerData(@NotNull Player player) {
        return playerDataManger.get(player);
    }

    @Override
    public @Nullable RpgPlayerData getPlayerData(@NotNull UUID playerId) {
        return playerDataManger.get(playerId);
    }

    @Override
    public @NotNull Configurator createConfigurator() {
        return new RpgMakerConfigurator(this);
    }

    @Override
    public boolean isPremium() {
        return true;
    }

    @Override
    public void registerCommands(@NotNull CommandRegistry commands) {
        commands.registerCommand(new RpgMakerCommand(this));
    }

    @Override
    public void registerListeners() {
        registerListener(new BlockEventListener(this));
        registerListener(new CropEventListener(this));

        registerListener(new GeneratorEventListener(this));
        registerListener(new GateEventListener(this));

        if (this.getVirtualStructures().isSupported()) {
            registerListener(new PlayerChunkLoadListener(plugin));
            PacketEventsHook.registerAllPacketListeners(this);
        }

        registerListener(new ItemCollectListener(plugin));
        registerListener(new SkillEventListener(plugin));
    }

    @Override
    public void registerHooks() {
        if (Server.isPluginLoaded("ItemsAdder")) {
            registerListener(new ItemsAdderDropListener());
        }

        if (Server.isPluginLoaded("PlotSquared")) {
            PlotBlockBreakListener listener = new PlotBlockBreakListener(this);
            EventExecutor executor = (l, event) -> {
                if (event instanceof BlockBreakEvent) {
                    ((PlotBlockBreakListener) l).onBreak((BlockBreakEvent) event);
                }
            };
            Server.registerEventAtFirstOfLowestPriority(BlockBreakEvent.getHandlerList(), listener,
                    executor, this, false);
        }
    }

    @Override
    public @Nullable String getDatabaseName() {
        return "data";
    }

    @Override
    public @Nullable String getLogo() {
        return """
                
                
                ┏━┓┏━┓┏━╸   ┏┳┓┏━┓╻┏ ┏━╸┏━┓
                ┣┳┛┣━┛┃╺┓╺━╸┃┃┃┣━┫┣┻┓┣╸ ┣┳┛
                ╹┗╸╹  ┗━┛   ╹ ╹╹ ╹╹ ╹┗━╸╹┗╸""";
    }

    //    @Override
//    public @Nullable String getLogo() {
//        return """
//
//                ┏━┓┏━┓┏━╸┏━┓╺┳╸┏━┓┏━╸╻┏\s
//                ┃ ┃┣┳┛┣╸ ┗━┓ ┃ ┣━┫┃  ┣┻┓
//                ┗━┛╹┗╸┗━╸┗━┛ ╹ ╹ ╹┗━╸╹ ╹""";
//    }

    @Override
    public @Nullable Integer getMetricsId() {
        return 24502;
    }

    @Override
    public @Nullable Integer getResourceId() {
        return 121905;
    }

    @Override
    public List<BootPhase> getStartupRequirements() {
        return List.of(
                BootPhase.SERVER_LOADED,
                BootPhase.WORLDS_LOADED,
                BootPhase.ITEMSADDER_DATA_LOADED
        );
    }

    public List<StartupTask> getStartupTasks() {
        List<StartupTask> startupTasks = new ArrayList<>();

        if (Server.isPluginLoaded("PlotSquared")) {
            startupTasks.add(StartupTask.create()
                    .require(BootPhase.pluginEnabled("PlotSquared"))
                    .onReady(() -> registerListener(new PlotBlockDamageListener(this))));
        }

        return startupTasks;
    }

    @Override
    public @NotNull List<Integer> getIncompatibleVersions() {
        return Version.getVersionsOlderThan(Version.V1_16_5);
    }

    @Override
    public @NotNull List<String> getCompatiblePlugins() {
        return List.of(
                "Vault",
                "PlaceholderAPI",
                "Multiverse-Core",
                "DecentHolograms",
                "FancyHolograms",
                "AuraSkills",
                "mcMMO",
                "ItemsAdder",
                "Nexo",
                "CraftEngine",
                "PlotSquared",
                "MythicMobs",
                "ExecutableItems",
                "EcoItems",
                "packetevents",
                "LibsDisguises"
        );
    }

    @Override
    public Map<Integer, List<String>> getExamplesByVersion() {
        return Map.of(
                Version.V1_17, List.of(
                        "generators/deposits/copper_deposit.yml",
                        "generators/ores/copper.yml",
                        "generators/crops/glow_berries.yml",

                        "generators/ores/amethyst/amethyst.yml",
                        "generators/ores/amethyst/amethyst_wall.yml",
                        "generators/ores/amethyst/amethyst_ceiling.yml",

                        "structures/deposits/copper/copper_deposit_1.yml",
                        "structures/deposits/copper/copper_deposit_2.yml",
                        "structures/deposits/copper/copper_deposit_3.yml",
                        "structures/deposits/copper/copper_deposit_4.yml",
                        "structures/deposits/copper/copper_deposit_5.yml",
                        "structures/deposits/copper/copper_deposit_6.yml",
                        "structures/deposits/copper/copper_deposit_7.yml",
                        "structures/deposits/copper/copper_deposit_8.yml",
                        "structures/deposits/copper/copper_deposit_9.yml",
                        "structures/deposits/copper/copper_deposit_10.yml"
                ),
                Version.V1_19, List.of(
                        "collections/foraging/mangrove_log.yml"
                ),
                Version.V1_20, List.of(
                        "collections/foraging/cherry_log.yml"
                ),
                Version.V1_21_2, List.of(
                        "collections/foraging/pale_oak_log.yml"
                )
        );
    }

    @Override
    public Map<String, List<String>> getExamplesByPlugin() {
        return Map.of(
                "AuraSkills", List.of("generators/examples/hooks/mana_ore.yml"),
                "ItemsAdder", List.of("generators/examples/hooks/ruby_ore.yml"),
                "CraftEngine", List.of("generators/examples/hooks/topaz_ore.yml")
        );
    }

    public @NotNull GeneratorTemplateManager getGeneratorTemplates() {
        return generatorTemplateManager;
    }

    public @Nullable GeneratorTemplate getGeneratorTemplate(String name) {
        return generatorTemplateManager.get(name);
    }

    public @NotNull List<GeneratorTemplate> getGeneratorTemplates(String group) {
        return generatorTemplateManager.getAll(group);
    }

    public @NotNull GeneratorManager getGenerators() {
        return generatorManager;
    }

    public @Nullable GlobalGenerator getGlobalGenerator(@NotNull Location location) {
        return generatorManager.getGlobalGenerator(location);
    }

    public @Nullable VirtualGenerator getVirtualGenerator(@NotNull Location location) {
        return generatorManager.getVirtualGenerator(location);
    }

    public @Nullable InstancedGenerator getInstancedGenerator(@NotNull Player player, @NotNull Location location) {
        return generatorManager.getPlayerGenerator(player, location);
    }

    public @Nullable Generator getGenerator(@Nullable Player player, @NotNull Location location) {
        return generatorManager.getGenerator(player, location);
    }

    public GeneratorOptionsManager getGeneratorOptions() {
        return generatorOptionsManager;
    }

    public @NotNull GateTemplateManager getGateTemplates() {
        return gateTemplateManager;
    }

    public @Nullable GateTemplate getGateTemplate(String name) {
        return gateTemplateManager.get(name);
    }

    public @NotNull List<GateTemplate> getGateTemplates(String group) {
        return gateTemplateManager.getAll(group);
    }

    public @NotNull GateManager getGates() {
        return gateManager;
    }

    public @Nullable Gate getGate(@NotNull Location location) {
        return gateManager.getGate(location);
    }

    public GateOptionsManager getGateOptions() {
        return gateOptionsManager;
    }

    @NotNull
    public CollectionTemplateManager getCollectionTemplates() {
        return collectionTemplateManager;
    }

    @Nullable
    public CollectionTemplate getCollectionTemplate(@NotNull String name) {
        return collectionTemplateManager.get(name);
    }

    @Nullable
    public CollectionTemplate getCollectionTemplate(@NotNull ItemStack item) {
        return collectionTemplateManager.get(item);
    }

    @NotNull
    public SkillTemplateManager getSkillTemplates() {
        return skillTemplateManager;
    }

    @Nullable
    public SkillTemplate getSkillTemplate(@NotNull String name) {
        return skillTemplateManager.get(name);
    }

}
