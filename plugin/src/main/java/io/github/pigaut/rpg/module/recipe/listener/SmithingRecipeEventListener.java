package io.github.pigaut.rpg.module.recipe.listener;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.player.data.*;
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
import org.jetbrains.annotations.*;

import java.util.*;

public class SmithingRecipeEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public SmithingRecipeEventListener(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

//    @EventHandler
//    public void onSmithingPrepare(PrepareInventoryResultEvent event) {
//        ItemStack result = event.getResult();
//        if (result == null || result.getType() == Material.AIR) {
//            return;
//        }
//
//        Inventory inventory = event.getInventory();
//        if (!(inventory instanceof SmithingInventory)) {
//            return;
//        }
//
//        ItemStack template = inventory.getItem(0);
//        ItemStack base = inventory.getItem(1);
//        ItemStack addition = inventory.getItem(2);
//        if (template == null || base == null || addition == null) {
//            return;
//        }
//
//        SmithingRecipe foundRecipe = null;
//        for (Recipe recipe : Bukkit.getRecipesFor(result)) {
//            if (!(recipe instanceof SmithingTransformRecipe smithingRecipe)) {
//                continue;
//            }
//
//            RecipeChoice templateChoice = smithingRecipe.getTemplate();
//            RecipeChoice baseChoice = smithingRecipe.getBase();
//            RecipeChoice additionChoice = smithingRecipe.getAddition();
//            if (templateChoice == null || baseChoice == null || additionChoice == null) {
//                continue;
//            }
//
//            if (templateChoice.test(template) && baseChoice.test(base) && additionChoice.test(addition)) {
//                foundRecipe = smithingRecipe;
//                break;
//            }
//        }
//
//        if (foundRecipe == null) {
//            return;
//        }
//
//        NamespacedKey recipeKey = foundRecipe.getKey();
//        if (!recipeKey.getNamespace().equals(plugin.getNamespace())) {
//            return;
//        }
//
//        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipeKey.getKey());
//        if (recipeTemplate == null) {
//            return;
//        }
//
//        if (!recipeTemplate.isLocked()) {
//            return;
//        }
//
//        Player player;
//        if (Server.getVersion() >= Version.V1_21) {
//            player = (Player) event.getView().getPlayer();
//        } else {
//            player = Reflect.on(event.getView()).call("getPlayer").get();
//        }
//
//        PlayerData playerData = plugin.getPlayerData(player);
//        if (!playerData.hasUnlockedRecipe(recipeKey)) {
//            event.setResult(null);
//        }
//    }

    @EventHandler
    public void onPrepareSmithing(PrepareSmithingEvent event) {
        SmithingInventory inventory = event.getInventory();
        ItemStack result = event.getResult();
        Recipe recipe = inventory.getRecipe();
        if (result == null || recipe == null) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipe);
        if (recipeTemplate == null) {
            return;
        }

        Player player = (Player) event.getView().getPlayer();

        // Check whether player has unlocked recipe
        PlayerData playerData = plugin.getPlayerData(player);
        if (recipeTemplate.isLocked()) {
            if (!playerData.hasUnlockedRecipe(recipeTemplate.getKey())) {
                inventory.setResult(null);
                return;
            }
        }

        // Check whether recipe ingredients match
        IngredientMatcher ingredientMatcher = recipeTemplate.getIngredientMatcher();
        if (ingredientMatcher != null) {
            List<ItemStack> ingredients = RecipeUtil.getSmithingIngredients(inventory);
            if (!ingredientMatcher.match(ingredients)) {
                inventory.setResult(null);
                return;
            }
        }

        // Update item creator and placeholders
        ItemTemplate itemTemplate = plugin.getItemTemplate(result);
        if (itemTemplate != null) {
            if (plugin.getItemTemplates().getItemCreator(result) == null) {
                plugin.getItemTemplates().setItemCreator(result, player);
            }
            itemTemplate.updateItemMeta(result, player);
            inventory.setResult(result);
        }
    }

    @EventHandler
    public void onSmithItem(SmithItemEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }

        SmithingInventory inventory = event.getInventory();
        Recipe recipe = inventory.getRecipe();
        if (recipe == null) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipe);
        if (recipeTemplate == null) {
            return;
        }

        IngredientMatcher ingredientMatcher = recipeTemplate.getIngredientMatcher();
        if (ingredientMatcher == null) {
            return;
        }

        ItemStack result = inventory.getResult();
        if (result == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        List<ItemStack> items = RecipeUtil.getSmithingIngredients(inventory);

        boolean consumed = ingredientMatcher.consumeOne(items);
        if (!consumed) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        if (event.getClick().isShiftClick()) {
            RecipeUtil.setSmithingIngredients(inventory, items);
            PlayerUtil.giveItemsOrDrop(player, result.clone());
            return;
        }

        ItemStack cursor = event.getCursor();
        if (cursor.getType() == Material.AIR) {
            event.getView().setCursor(result.clone());
            RecipeUtil.setSmithingIngredients(inventory, items);
        }
        else if (cursor.isSimilar(result) && cursor.getAmount() + result.getAmount() <= cursor.getMaxStackSize()) {
            ItemStack newCursor = cursor.clone();
            newCursor.setAmount(cursor.getAmount() + result.getAmount());
            event.getView().setCursor(newCursor);
            RecipeUtil.setSmithingIngredients(inventory, items);
        }
    }

}
