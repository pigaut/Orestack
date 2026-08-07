package io.github.pigaut.rpg.module.recipe.listener;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

public class RecipeEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public RecipeEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player);
        playerData.runWhenLoaded(() -> {
            for (RecipeTemplate recipeTemplate : plugin.getRecipes().getAllRegistered()) {
                if (!recipeTemplate.isGlobal() && (recipeTemplate.isDiscoverAutomatically()
                        || playerData.hasUnlockedRecipe(recipeTemplate.getKey()))) {
                    player.discoverRecipe(recipeTemplate.getKey());
                }
            }
        });
    }

    @EventHandler
    public void onDiscover(PlayerRecipeDiscoverEvent event) {
        NamespacedKey recipe = event.getRecipe();
        if (!recipe.getNamespace().equals(plugin.getNamespace())) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipe(recipe.getKey());
        if (recipeTemplate == null) {
            return;
        }

        if (recipeTemplate.isGlobal()) {
            return;
        }

        PlayerData playerData = plugin.getPlayerData(event.getPlayer());
        if (!playerData.hasUnlockedRecipe(recipe)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (!(recipe instanceof Keyed)) {
            return;
        }

        NamespacedKey recipeKey = ((Keyed) recipe).getKey();
        if (!recipeKey.getNamespace().equals(plugin.getNamespace())) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipe(recipeKey.getKey());
        if (recipeTemplate == null || recipeTemplate.isGlobal()) {
            return;
        }

        Player player;
        if (Server.getVersion() >= Version.V1_21) {
            player = (Player) event.getView().getPlayer();
        } else {
            player = Reflect.on(event.getView()).call("getPlayer").get();
        }

        PlayerData playerData = plugin.getPlayerData(player);
        if (!playerData.hasUnlockedRecipe(recipeKey)) {
            event.getInventory().setResult(null);
        }
    }

}
