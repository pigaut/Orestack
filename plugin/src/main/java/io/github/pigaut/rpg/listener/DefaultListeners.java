package io.github.pigaut.rpg.listener;

import io.github.pigaut.rpg.core.gameplay.chicken.*;
import io.github.pigaut.rpg.core.playerblocks.*;
import io.github.pigaut.rpg.hook.itemsadder.*;
import io.github.pigaut.rpg.listener.gameplay.GameplayEventListener;
import io.github.pigaut.rpg.listener.item.ItemEventListener;
import io.github.pigaut.rpg.listener.mob.EntityEventListener;
import io.github.pigaut.rpg.listener.mob.EntityPaperEventListener;
import io.github.pigaut.rpg.listener.mob.MobEventListener;
import io.github.pigaut.rpg.listener.phase.PluginPhaseListener;
import io.github.pigaut.rpg.listener.phase.ServerPhaseListener;
import io.github.pigaut.rpg.listener.player.PlayerEquipmentChangeListener;
import io.github.pigaut.rpg.listener.player.PlayerInputListener;
import io.github.pigaut.rpg.listener.player.PlayerLifecycleListener;
import io.github.pigaut.rpg.listener.player.PlayerStatListener;
import io.github.pigaut.rpg.core.playerblocks.*;
import io.github.pigaut.rpg.core.gameplay.chicken.*;
import io.github.pigaut.rpg.module.recipe.listener.*;
import io.github.pigaut.rpg.hook.itemsadder.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.module.recipe.listener.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.*;
import org.jetbrains.annotations.*;

public class DefaultListeners {

    public static void registerAll(@NotNull EnhancedJavaPlugin plugin) {
        plugin.registerListener(new PluginPhaseListener(plugin));
        plugin.registerListener(new ServerPhaseListener(plugin));
        plugin.registerListener(new ItemEventListener(plugin));

        plugin.registerListener(new PlayerLifecycleListener(plugin));
        plugin.registerListener(new PlayerInputListener(plugin));
        plugin.registerListener(new PlayerEquipmentChangeListener(plugin));
        plugin.registerListener(new PlayerStatListener(plugin));

        plugin.registerListener(new ToolEventListener(plugin));
        plugin.registerListener(new MenuEventListener(plugin));
        plugin.registerListener(new GameplayEventListener(plugin));

        plugin.registerListener(new StructureWandListener(plugin));
        plugin.registerListener(new BuildStationEventListener(plugin));

        // Recipe Listeners
        plugin.registerListener(new RecipeEventListener(plugin));
        if (plugin.getRecipes().isSmithingRecipesSupported()) {
            plugin.registerListener(new PrepareSmithingInventoryListener(plugin));
        }

        // Mob listeners
        plugin.registerListener(new MobEventListener(plugin));
        plugin.registerListener(new EntityEventListener(plugin));
        if (Server.isPaper()) {
            plugin.registerListener(new EntityPaperEventListener(plugin));
        }

        // Player placed blocks and dropped items listener
        plugin.registerListener(new PlayerPlacedBlockListener(plugin));
        plugin.registerListener(new ChickenLayEggListener(plugin));

        if (Server.isPluginLoaded("ItemsAdder")) {
            plugin.registerListener(new ItemsAdderPhaseListener(plugin));
        }
    }

}
