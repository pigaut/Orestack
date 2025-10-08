package io.github.pigaut.orestack;

import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.config.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.listener.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.orestack.options.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.version.*;
import io.github.pigaut.yaml.configurator.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackPlugin extends EnhancedJavaPlugin {

    private static OrestackPlugin plugin;
    private final OrestackOptionsManager optionsManager = new OrestackOptionsManager(this);
    private final GeneratorTemplateManager templateManager = new GeneratorTemplateManager(this);
    private final GeneratorManager generatorManager = new GeneratorManager(this);
    private final OrestackPlayerManager playerManager = new OrestackPlayerManager(this);

    public static OrestackPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public boolean isPremium() {
        return false;
    }

    @Override
    public @Nullable String getLogo() {
        return """
                
                &9 $$$$$$\\                                  $$\\                         $$\\      \s
                &9$$  __$$\\                                 $$ |                        $$ |     \s
                &9$$ /  $$ | $$$$$$\\   $$$$$$\\   $$$$$$$\\ $$$$$$\\    $$$$$$\\   $$$$$$$\\ $$ |  $$\\\s
                &9$$ |  $$ |$$  __$$\\ $$  __$$\\ $$  _____|\\_$$  _|   \\____$$\\ $$  _____|$$ | $$  |
                &9$$ |  $$ |$$ |  \\__|$$$$$$$$ |\\$$$$$$\\    $$ |     $$$$$$$ |$$ /      $$$$$$  /\s
                &9$$ |  $$ |$$ |      $$   ____| \\____$$\\   $$ |$$\\ $$  __$$ |$$ |      $$  _$$< \s
                &9 $$$$$$  |$$ |      \\$$$$$$$\\ $$$$$$$  |  \\$$$$  |\\$$$$$$$ |\\$$$$$$$\\ $$ | \\$$\\\s
                &9 \\______/ \\__|       \\_______|\\_______/    \\____/  \\_______| \\_______|\\__|  \\__|
                """;
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
    public @Nullable String getDonationLink() {
        return "https://www.paypal.com/paypalme/Giovanni335";
    }

    @Override
    public @NotNull List<SpigotVersion> getCompatibleVersions() {
        return SpigotVersion.getVersionsNewerThan(SpigotVersion.V1_16_5);
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
                "effects/particles/special.yml",
                "effects/particles/dust/clouds.yml",
                "effects/particles/dust/spores.yml",
                "effects/particles/dust/fall.yml",
                "effects/particles/dust/rise.yml",
                "effects/sounds/misc.yml",

                "functions/misc.yml",
                "functions/trees.yml",
                "functions/tools/axe.yml",
                "functions/tools/hoe.yml",
                "functions/tools/misc.yml",
                "functions/tools/pickaxe.yml",
                "functions/tools/shovel.yml",

                "generators/example.yml",

                "generators/examples/diamond_node.yml",
                "generators/examples/experience_ore.yml",
                "generators/examples/magic_wheat.yml",
                "generators/examples/great_oak_tree.yml",

                "generators/deposits/stone_deposit.yml",
                "generators/deposits/coal_deposit.yml",
                "generators/deposits/iron_deposit.yml",
                "generators/deposits/gold_deposit.yml",
                "generators/deposits/diamond_deposit.yml",
                "generators/deposits/emerald_deposit.yml",

                "generators/tutorials/drops_tutorial.yml",
                "generators/tutorials/exp_tutorial.yml",
                "generators/tutorials/flag_tutorial.yml",
                "generators/tutorials/function_tutorial.yml",
                "generators/tutorials/hologram_tutorial.yml",
                "generators/tutorials/tools_tutorial.yml",

                "generators/trees/vanilla/oak_tree.yml",
                "generators/trees/vanilla/birch_tree.yml",
                "generators/trees/vanilla/spruce_tree.yml",
                "generators/trees/vanilla/acacia_tree.yml",
                "generators/trees/vanilla/jungle_tree.yml",
                "generators/trees/vanilla/dark_oak_tree.yml",

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

                "structures/deposits/stone/stone_deposit_1.yml",
                "structures/deposits/stone/stone_deposit_2.yml",
                "structures/deposits/stone/stone_deposit_3.yml",
                "structures/deposits/stone/stone_deposit_4.yml",
                "structures/deposits/stone/stone_deposit_5.yml",
                "structures/deposits/stone/stone_deposit_6.yml",
                "structures/deposits/stone/stone_deposit_7.yml",
                "structures/deposits/stone/stone_deposit_8.yml",
                "structures/deposits/stone/stone_deposit_9.yml",
                "structures/deposits/stone/stone_deposit_10.yml",

                "structures/deposits/coal/coal_deposit_1.yml",
                "structures/deposits/coal/coal_deposit_2.yml",
                "structures/deposits/coal/coal_deposit_3.yml",
                "structures/deposits/coal/coal_deposit_4.yml",
                "structures/deposits/coal/coal_deposit_5.yml",
                "structures/deposits/coal/coal_deposit_6.yml",
                "structures/deposits/coal/coal_deposit_7.yml",
                "structures/deposits/coal/coal_deposit_8.yml",
                "structures/deposits/coal/coal_deposit_9.yml",
                "structures/deposits/coal/coal_deposit_10.yml",

                "structures/deposits/iron/iron_deposit_1.yml",
                "structures/deposits/iron/iron_deposit_2.yml",
                "structures/deposits/iron/iron_deposit_3.yml",
                "structures/deposits/iron/iron_deposit_4.yml",
                "structures/deposits/iron/iron_deposit_5.yml",
                "structures/deposits/iron/iron_deposit_6.yml",
                "structures/deposits/iron/iron_deposit_7.yml",
                "structures/deposits/iron/iron_deposit_8.yml",
                "structures/deposits/iron/iron_deposit_9.yml",
                "structures/deposits/iron/iron_deposit_10.yml",

                "structures/deposits/gold/gold_deposit_1.yml",
                "structures/deposits/gold/gold_deposit_2.yml",
                "structures/deposits/gold/gold_deposit_3.yml",
                "structures/deposits/gold/gold_deposit_4.yml",
                "structures/deposits/gold/gold_deposit_5.yml",
                "structures/deposits/gold/gold_deposit_6.yml",
                "structures/deposits/gold/gold_deposit_7.yml",
                "structures/deposits/gold/gold_deposit_8.yml",
                "structures/deposits/gold/gold_deposit_9.yml",
                "structures/deposits/gold/gold_deposit_10.yml",

                "structures/deposits/diamond/diamond_deposit_1.yml",
                "structures/deposits/diamond/diamond_deposit_2.yml",
                "structures/deposits/diamond/diamond_deposit_3.yml",
                "structures/deposits/diamond/diamond_deposit_4.yml",
                "structures/deposits/diamond/diamond_deposit_5.yml",
                "structures/deposits/diamond/diamond_deposit_6.yml",
                "structures/deposits/diamond/diamond_deposit_7.yml",
                "structures/deposits/diamond/diamond_deposit_8.yml",
                "structures/deposits/diamond/diamond_deposit_9.yml",
                "structures/deposits/diamond/diamond_deposit_10.yml",

                "structures/deposits/emerald/emerald_deposit_1.yml",
                "structures/deposits/emerald/emerald_deposit_2.yml",
                "structures/deposits/emerald/emerald_deposit_3.yml",
                "structures/deposits/emerald/emerald_deposit_4.yml",
                "structures/deposits/emerald/emerald_deposit_5.yml",
                "structures/deposits/emerald/emerald_deposit_6.yml",
                "structures/deposits/emerald/emerald_deposit_7.yml",
                "structures/deposits/emerald/emerald_deposit_8.yml",
                "structures/deposits/emerald/emerald_deposit_9.yml",
                "structures/deposits/emerald/emerald_deposit_10.yml",

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
                        "generators/deposits/copper_deposit.yml",
                        "generators/ores/copper.yml",

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
                "AuraSkills", List.of("generators/examples/hooks/auraskills.yml")
        );
    }

    @Override
    public List<EnhancedCommand> getPluginCommands() {
        return List.of(new OrestackCommand(this));
    }

    @Override
    public List<Listener> getPluginListeners() {
        List<Listener> listeners = new ArrayList<>();
        listeners.add(new PlayerInteractListener(plugin));
        listeners.add(new BlockBreakListener(plugin));
        listeners.add(new BlockDestructionListener(plugin));
        listeners.add(new CropChangeListener(plugin));
        listeners.add(new GeneratorEventListener());
        return listeners;
    }

    @Override
    public Map<String, Menu> getPluginMenus() {
        return Map.of(
                "orestack", new OrestackMenu(this)
        );
    }

    @Override
    public @Nullable String getDatabaseName() {
        return "data";
    }

    @Override
    public @NotNull PlayerStateManager<? extends PlayerState> getPlayersState() {
        return playerManager;
    }

    @Override
    public @NotNull OrestackPlayer getPlayerState(@NotNull Player player) {
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
    public @NotNull Configurator createConfigurator() {
        return new OrestackConfigurator(this);
    }

    public OrestackOptionsManager getTools() {
        return optionsManager;
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

    public OrestackOptionsManager getOrestackOptions() {
        return optionsManager;
    }

}
