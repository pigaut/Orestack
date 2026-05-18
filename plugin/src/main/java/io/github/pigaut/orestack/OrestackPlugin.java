package io.github.pigaut.orestack;

import io.github.pigaut.orestack.api.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.config.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.core.placeholder.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.generator.instanced.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.hook.PacketEventsHook;
import io.github.pigaut.orestack.hook.castlegates.*;
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
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private static OrestackPlugin plugin;
    private final OrestackSettings settings = new OrestackSettings(this);
    private final GeneratorTemplateManager templateManager = new GeneratorTemplateManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final OrestackPlayerStateManager playerStateManager = new OrestackPlayerStateManager(this);
    private final OrestackPlayerDataManger playerDataManger = new OrestackPlayerDataManger(this);
    private final GeneratorOptionsManager generatorOptionsManager = new GeneratorOptionsManager(this);

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
        return true;
    }

    @Override
    public void registerHooks() {
        if (Server.isPluginLoaded("ItemsAdder")) {
            registerListener(new ItemsAdderDropListener());
        }
        if (Server.isPluginLoaded("PlotSquared")) {
            registerListener(new PlotBlockBreakListener(this));
        }
        if (Server.isPluginLoaded("CastleGates")) {
            registerListener(new GateEventListener(this));
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
    public void registerCommands(@NotNull CommandRegistry commands) {
        commands.registerCommand(new OrestackCommand(this));
    }

    @Override
    public void registerPlaceholders(@NotNull PlaceholderRegistry placeholders) {
        OrestackPlaceholders.registerAll(this, placeholders);
    }

    @Override
    public void registerListeners() {
        registerListener(new PlayerEventListener(this));
        registerListener(new BlockEventListener(this));
        registerListener(new CropEventListener(this));

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
    public @NotNull List<String> getDefaultDirectories() {
        return List.of("items", "generators", "messages",
                "languages", "functions", "structures",
                "effects/particles", "effects/sounds",
                "menus", "recipes", "collections");
    }

    @Override
    public List<String> getDefaultResources() {
        return List.of("config.yml", "languages/en.yml");
    }

//    @Override
//    public List<String> getExampleResources() {
//        return List.of(
//                "items/misc.yml",
//                "messages/misc.yml",
//
//                "effects/particles/misc.yml",
//                "effects/particles/flame.yml",
//                "effects/particles/special.yml",
//                "effects/particles/dust/clouds.yml",
//                "effects/particles/dust/spores.yml",
//                "effects/particles/dust/fall.yml",
//                "effects/particles/dust/rise.yml",
//                "effects/sounds/misc.yml",
//
//                "functions/misc.yml",
//                "functions/trees.yml",
//                "functions/tools/axe.yml",
//                "functions/tools/hoe.yml",
//                "functions/tools/misc.yml",
//                "functions/tools/pickaxe.yml",
//                "functions/tools/shovel.yml",
//
//                "menus/buttons.yml",
//                "menus/misc/example.yml",
//                "menus/collections/mining_collections.yml",
//
//                "recipes/misc.yml",
//
//                "collections/dirt.yml",
//
//                "generators/example.yml",
//
//                "generators/examples/diamond_node.yml",
//                "generators/examples/experience_ore.yml",
//                "generators/examples/magic_wheat.yml",
//                "generators/examples/great_oak_tree.yml",
//                "generators/examples/chop_tree.yml",
//                "generators/examples/growth_bar_showcase.yml",
//
//                "generators/deposits/stone_deposit.yml",
//                "generators/deposits/coal_deposit.yml",
//                "generators/deposits/iron_deposit.yml",
//                "generators/deposits/gold_deposit.yml",
//                "generators/deposits/diamond_deposit.yml",
//                "generators/deposits/emerald_deposit.yml",
//
//                "generators/tutorials/drops_tutorial.yml",
//                "generators/tutorials/exp_tutorial.yml",
//                "generators/tutorials/flag_tutorial.yml",
//                "generators/tutorials/function_tutorial.yml",
//                "generators/tutorials/hologram_tutorial.yml",
//                "generators/tutorials/tools_tutorial.yml",
//
//                "generators/trees/vanilla/oak_tree.yml",
//                "generators/trees/vanilla/birch_tree.yml",
//                "generators/trees/vanilla/spruce_tree.yml",
//                "generators/trees/vanilla/acacia_tree.yml",
//                "generators/trees/vanilla/jungle_tree.yml",
//                "generators/trees/vanilla/dark_oak_tree.yml",
//
//                "generators/crops/wheat.yml",
//                "generators/crops/potato.yml",
//                "generators/crops/carrot.yml",
//                "generators/crops/beetroot.yml",
//                "generators/crops/melon.yml",
//                "generators/crops/pumpkin.yml",
//                "generators/crops/cocoa.yml",
//                "generators/crops/sugar_cane.yml",
//                "generators/crops/cactus.yml",
//                "generators/crops/bamboo.yml",
//                "generators/crops/chorus/small_chorus_plant.yml",
//                "generators/crops/chorus/chorus_plant.yml",
//                "generators/crops/sweet_berry_bush.yml",
//
//                "generators/fibers/cotton.yml",
//                "generators/fibers/flax.yml",
//                "generators/fibers/hemp.yml",
//
//                "generators/ores/coal.yml",
//                "generators/ores/iron.yml",
//                "generators/ores/gold.yml",
//                "generators/ores/diamond.yml",
//                "generators/ores/emerald.yml",
//
//                "structures/crops/chorus/chorus_plant_2.yml",
//                "structures/crops/chorus/chorus_plant_3.yml",
//                "structures/crops/chorus/chorus_plant_4.yml",
//                "structures/crops/chorus/chorus_plant_5.yml",
//                "structures/crops/chorus/chorus_plant_6.yml",
//
//                "structures/crops/chorus/small_chorus_plant_2.yml",
//                "structures/crops/chorus/small_chorus_plant_3.yml",
//                "structures/crops/chorus/small_chorus_plant_4.yml",
//
//                "structures/deposits/stone/stone_deposit_1.yml",
//                "structures/deposits/stone/stone_deposit_2.yml",
//                "structures/deposits/stone/stone_deposit_3.yml",
//                "structures/deposits/stone/stone_deposit_4.yml",
//                "structures/deposits/stone/stone_deposit_5.yml",
//                "structures/deposits/stone/stone_deposit_6.yml",
//                "structures/deposits/stone/stone_deposit_7.yml",
//                "structures/deposits/stone/stone_deposit_8.yml",
//                "structures/deposits/stone/stone_deposit_9.yml",
//                "structures/deposits/stone/stone_deposit_10.yml",
//
//                "structures/deposits/coal/coal_deposit_1.yml",
//                "structures/deposits/coal/coal_deposit_2.yml",
//                "structures/deposits/coal/coal_deposit_3.yml",
//                "structures/deposits/coal/coal_deposit_4.yml",
//                "structures/deposits/coal/coal_deposit_5.yml",
//                "structures/deposits/coal/coal_deposit_6.yml",
//                "structures/deposits/coal/coal_deposit_7.yml",
//                "structures/deposits/coal/coal_deposit_8.yml",
//                "structures/deposits/coal/coal_deposit_9.yml",
//                "structures/deposits/coal/coal_deposit_10.yml",
//
//                "structures/deposits/iron/iron_deposit_1.yml",
//                "structures/deposits/iron/iron_deposit_2.yml",
//                "structures/deposits/iron/iron_deposit_3.yml",
//                "structures/deposits/iron/iron_deposit_4.yml",
//                "structures/deposits/iron/iron_deposit_5.yml",
//                "structures/deposits/iron/iron_deposit_6.yml",
//                "structures/deposits/iron/iron_deposit_7.yml",
//                "structures/deposits/iron/iron_deposit_8.yml",
//                "structures/deposits/iron/iron_deposit_9.yml",
//                "structures/deposits/iron/iron_deposit_10.yml",
//
//                "structures/deposits/gold/gold_deposit_1.yml",
//                "structures/deposits/gold/gold_deposit_2.yml",
//                "structures/deposits/gold/gold_deposit_3.yml",
//                "structures/deposits/gold/gold_deposit_4.yml",
//                "structures/deposits/gold/gold_deposit_5.yml",
//                "structures/deposits/gold/gold_deposit_6.yml",
//                "structures/deposits/gold/gold_deposit_7.yml",
//                "structures/deposits/gold/gold_deposit_8.yml",
//                "structures/deposits/gold/gold_deposit_9.yml",
//                "structures/deposits/gold/gold_deposit_10.yml",
//
//                "structures/deposits/diamond/diamond_deposit_1.yml",
//                "structures/deposits/diamond/diamond_deposit_2.yml",
//                "structures/deposits/diamond/diamond_deposit_3.yml",
//                "structures/deposits/diamond/diamond_deposit_4.yml",
//                "structures/deposits/diamond/diamond_deposit_5.yml",
//                "structures/deposits/diamond/diamond_deposit_6.yml",
//                "structures/deposits/diamond/diamond_deposit_7.yml",
//                "structures/deposits/diamond/diamond_deposit_8.yml",
//                "structures/deposits/diamond/diamond_deposit_9.yml",
//                "structures/deposits/diamond/diamond_deposit_10.yml",
//
//                "structures/deposits/emerald/emerald_deposit_1.yml",
//                "structures/deposits/emerald/emerald_deposit_2.yml",
//                "structures/deposits/emerald/emerald_deposit_3.yml",
//                "structures/deposits/emerald/emerald_deposit_4.yml",
//                "structures/deposits/emerald/emerald_deposit_5.yml",
//                "structures/deposits/emerald/emerald_deposit_6.yml",
//                "structures/deposits/emerald/emerald_deposit_7.yml",
//                "structures/deposits/emerald/emerald_deposit_8.yml",
//                "structures/deposits/emerald/emerald_deposit_9.yml",
//                "structures/deposits/emerald/emerald_deposit_10.yml",
//
//                "structures/trees/vanilla/oak/oak_tree_1.yml",
//                "structures/trees/vanilla/oak/oak_tree_2.yml",
//                "structures/trees/vanilla/oak/oak_tree_3.yml",
//                "structures/trees/vanilla/oak/oak_tree_4.yml",
//
//                "structures/trees/vanilla/birch/birch_tree_1.yml",
//                "structures/trees/vanilla/birch/birch_tree_2.yml",
//                "structures/trees/vanilla/birch/birch_tree_3.yml",
//                "structures/trees/vanilla/birch/birch_tree_4.yml",
//                "structures/trees/vanilla/birch/birch_tree_5.yml",
//
//                "structures/trees/vanilla/spruce/spruce_tree_1.yml",
//                "structures/trees/vanilla/spruce/spruce_tree_2.yml",
//                "structures/trees/vanilla/spruce/spruce_tree_3.yml",
//                "structures/trees/vanilla/spruce/spruce_tree_4.yml",
//                "structures/trees/vanilla/spruce/spruce_tree_5.yml",
//
//                "structures/trees/vanilla/acacia/acacia_tree_1.yml",
//                "structures/trees/vanilla/acacia/acacia_tree_2.yml",
//                "structures/trees/vanilla/acacia/acacia_tree_3.yml",
//                "structures/trees/vanilla/acacia/acacia_tree_4.yml",
//                "structures/trees/vanilla/acacia/acacia_tree_5.yml",
//                "structures/trees/vanilla/acacia/acacia_tree_6.yml",
//                "structures/trees/vanilla/acacia/acacia_tree_7.yml",
//
//                "structures/trees/vanilla/jungle/jungle_tree_1.yml",
//                "structures/trees/vanilla/jungle/jungle_tree_2.yml",
//                "structures/trees/vanilla/jungle/jungle_tree_3.yml",
//                "structures/trees/vanilla/jungle/jungle_tree_4.yml",
//                "structures/trees/vanilla/jungle/jungle_tree_5.yml",
//                "structures/trees/vanilla/jungle/jungle_tree_6.yml",
//                "structures/trees/vanilla/jungle/jungle_tree_7.yml",
//                "structures/trees/vanilla/jungle/jungle_tree_8.yml",
//
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_1.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_2.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_3.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_4.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_5.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_6.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_7.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_8.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_9.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_10.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_11.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_12.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_13.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_14.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_15.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_16.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_17.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_18.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_19.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_20.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_21.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_22.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_23.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_24.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_25.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_26.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_27.yml",
//                "structures/trees/vanilla/dark_oak/dark_oak_tree_28.yml",
//
//                "structures/trees/great_oak/great_oak_tree_0.yml",
//                "structures/trees/great_oak/great_oak_tree_1.yml",
//                "structures/trees/great_oak/great_oak_tree_2.yml",
//                "structures/trees/great_oak/great_oak_tree_3.yml",
//                "structures/trees/great_oak/great_oak_tree_4.yml",
//                "structures/trees/great_oak/great_oak_tree_5.yml",
//                "structures/trees/great_oak/great_oak_tree_6.yml",
//                "structures/trees/great_oak/great_oak_tree_silhouette.yml"
//        );
//    }

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

}
