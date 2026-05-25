package io.github.pigaut.orestack;

import io.github.pigaut.orestack.api.*;
import io.github.pigaut.orestack.advertise.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.core.config.*;
import io.github.pigaut.orestack.core.placeholder.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.generator.instanced.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.hook.PacketEventsHook;
import io.github.pigaut.orestack.hook.itemsadder.*;
import io.github.pigaut.orestack.hook.plotsquared.*;
import io.github.pigaut.orestack.listener.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.voxel.core.command.*;
import io.github.pigaut.voxel.core.placeholder.*;
import io.github.pigaut.voxel.listener.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.boot.*;
import io.github.pigaut.voxel.plugin.boot.phase.*;
import io.github.pigaut.voxel.util.Server;
import io.github.pigaut.voxel.version.*;
import io.github.pigaut.yaml.configurator.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private static OrestackPlugin plugin;
    private final OrestackSettings settings = new OrestackSettings(this);
    private final GeneratorTemplateManager generatorTemplateManager = new GeneratorTemplateManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final GateTemplateManager gateTemplateManager = new GateTemplateManager(this);
    private final GateManager gateManager = new GateManager(this);
    private final OrestackPlayerStateManager playerStateManager = new OrestackPlayerStateManager(this);
    private final OrestackPlayerDataManger playerDataManger = new OrestackPlayerDataManger(this);
    private final GeneratorOptionsManager generatorOptionsManager = new GeneratorOptionsManager(this);
    private final GateOptionsManager gateOptionsManager = new GateOptionsManager(this);
    private final AdvertisementManager advertisementManager = new AdvertisementManager(this);

    public static OrestackPlugin getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onStartup() {
        Orestack.setApiInstance(new SimpleOrestackAPI(this));
    }

    @Override
    public void onShutdown() {

    }

    @Override
    public @NotNull OrestackSettings getSettings() {
        return settings;
    }

    @Override
    public @NotNull Configurator createConfigurator() {
        return new OrestackConfigurator(this);
    }

    @Override
    public @NotNull OrestackPlayerStateManager getPlayersState() {
        return playerStateManager;
    }

    @Override
    public @NotNull OrestackPlayer getPlayerState(@NotNull Player player) {
        return playerStateManager.getPlayerState(player);
    }

    @Override
    public @Nullable OrestackPlayer getPlayerState(@NotNull UUID playerId) {
        return playerStateManager.getPlayerState(playerId);
    }

    @Override
    public @NotNull OrestackPlayerDataManger getPlayersData() {
        return playerDataManger;
    }

    @Override
    public @Nullable PlayerData getPlayerData(@NotNull UUID playerId) {
        return playerDataManger.getPlayerData(playerId);
    }

    @Override
    public @NotNull PlayerData getPlayerData(@NotNull Player player) {
        return playerDataManger.getPlayerData(player);
    }

    @Override
    public boolean isPremium() {
        return false;
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
                
                ┏━┓┏━┓┏━╸┏━┓╺┳╸┏━┓┏━╸╻┏\s
                ┃ ┃┣┳┛┣╸ ┗━┓ ┃ ┣━┫┃  ┣┻┓
                ┗━┛╹┗╸┗━╸┗━┛ ╹ ╹ ╹┗━╸╹ ╹""";
    }

    @Override
    public @Nullable Integer getMetricsId() {
        return 24502;
    }

    @Override
    public @Nullable Integer getResourceId() {
        return 91628;
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
    public void registerCommands(@NotNull CommandRegistry commands) {
        commands.registerCommand(new OrestackCommand(this));
    }

    @Override
    public void registerPlaceholders(@NotNull PlaceholderRegistry placeholders) {
        OrestackPlaceholders.registerAll(this, placeholders);
    }

    @Override
    public void registerListeners() {
        registerListener(new BlockEventListener(this));
        registerListener(new CropEventListener(this));
        registerListener(new AdvertisementListener(this));

        registerListener(new GeneratorEventListener(this));
        registerListener(new GateEventListener(this));

        if (this.getVirtualStructures().isSupported()) {
            registerListener(new PlayerChunkLoadListener(plugin));
            PacketEventsHook.registerAllPacketListeners(this);
        }
    }

    @Override
    public @NotNull List<Integer> getCompatibleVersions() {
        return Version.getVersionsNewerThan(Version.V1_16_5);
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
                "packetevents"
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

}
