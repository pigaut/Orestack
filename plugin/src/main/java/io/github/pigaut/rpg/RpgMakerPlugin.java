package io.github.pigaut.rpg;

import io.github.pigaut.rpg.api.*;
import io.github.pigaut.rpg.hook.auraskill.*;
import io.github.pigaut.rpg.hook.mcmmo.*;
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
import io.github.pigaut.rpg.module.function.action.block.*;
import io.github.pigaut.rpg.module.function.action.event.*;
import io.github.pigaut.rpg.module.function.action.registry.*;
import io.github.pigaut.rpg.module.function.condition.registry.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.tool.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.tool.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.server.version.*;
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
import io.github.pigaut.yaml.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RpgMakerPlugin extends EnhancedJavaPlugin {

    private static RpgMakerPlugin plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    public static @NotNull RpgMakerPlugin getInstance() {
        Preconditions.checkState(plugin != null, "Plugin has not been loaded yet");
        return plugin;
    }

    @Override
    public void onBoot() {

    }

    @Override
    public void onPreStartup() {
        // Register conditions
        ServerConditions.registerAll(this);
        EventConditions.registerAll(this);
        BlockConditions.registerAll(this);
        PlayerConditions.registerAll(this);
        MobConditions.registerAll(this);
        ItemConditions.registerAll(this);
        MenuConditions.registerAll(this);
        GeneratorConditions.registerAll(this);
        SkillConditions.registerAll(this);
        CollectionConditions.registerAll(this);
        AuraSkillsHook.registerAllConditions(this);
        McMMOHook.registerAllConditions(this);

        // Register actions
        SystemActions.registerAll(this);
        ServerActions.registerAll(this);
        EventActions.registerAll(this);
        BlockActions.registerAll(this);
        ProtagonistActions.registerAll(this);
        PlayerActions.registerAll(this);
        MobActions.registerAll(this);
        ItemActions.registerAll(this);
        MenuActions.registerAll(this);
        GeneratorActions.registerAll(this);
        GateActions.registerAll(this);
        CollectionActions.registerAll(this);
        RecipeActions.registerAll(this);
        AuraSkillsHook.registerAllActions(this);
        McMMOHook.registerAllActions(this);

        // Register for-each sources
        ForEachSourceRegistry forEachSources = getForEachSources();
        forEachSources.register("online_player", new ForEachOnlinePlayer());
        forEachSources.register("enchant_added", new ForEachEnchantAdded());
        forEachSources.register("mob_attacker", new ForEachMobAttacker());
        forEachSources.register("entity_in_front", new ForEachEntityInFrontLoader());
        forEachSources.register("entity_in_radius", new ForEachEntityInRadiusLoader());
        forEachSources.register("entity_in_range", new ForEachEntityInRangeLoader());
        forEachSources.register("entity_in_ring", new ForEachEntityInRingLoader());

        // Register dynamic icons
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
    public @NotNull Configurator createConfigurator() {
        return new EnhancedPluginConfigurator(this);
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

}
