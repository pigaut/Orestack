package io.github.pigaut.rpg.module.recipe.listener;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.data.base.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.Recipe;

public class PrepareSmithingInventoryListener implements Listener {

    private final EnhancedPlugin plugin;

    public PrepareSmithingInventoryListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSmithingPrepare(PrepareInventoryResultEvent event) {
        ItemStack result = event.getResult();
        if (result == null || result.getType() == Material.AIR) {
            return;
        }

        Inventory inventory = event.getInventory();
        if (!(inventory instanceof SmithingInventory)) {
            return;
        }

        ItemStack template = inventory.getItem(0);
        ItemStack base = inventory.getItem(1);
        ItemStack addition = inventory.getItem(2);
        if (template == null || base == null || addition == null) {
            return;
        }

        SmithingRecipe foundRecipe = null;
        for (Recipe recipe : Bukkit.getRecipesFor(result)) {
            if (!(recipe instanceof SmithingTransformRecipe smithingRecipe)) {
                continue;
            }

            RecipeChoice templateChoice = smithingRecipe.getTemplate();
            RecipeChoice baseChoice = smithingRecipe.getBase();
            RecipeChoice additionChoice = smithingRecipe.getAddition();
            if (templateChoice == null || baseChoice == null || additionChoice == null) {
                continue;
            }

            if (templateChoice.test(template) && baseChoice.test(base) && additionChoice.test(addition)) {
                foundRecipe = smithingRecipe;
                break;
            }
        }

        if (foundRecipe == null) {
            return;
        }

        NamespacedKey recipeKey = foundRecipe.getKey();
        if (!recipeKey.getNamespace().equals(plugin.getNamespace())) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipe(recipeKey.getKey());
        if (recipeTemplate == null) {
            return;
        }

        if (recipeTemplate.isGlobal()) {
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
            event.setResult(null);
        }
    }

}
