package io.github.pigaut.rpg.module.recipe.listener;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.plugin.*;
import io.papermc.paper.event.player.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RecipeEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public RecipeEventListener(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player);
        playerData.runWhenLoaded(() -> {
            for (RecipeTemplate recipeTemplate : plugin.getRecipes().getAllRegistered()) {
                if ((!recipeTemplate.isLocked() && recipeTemplate.isDiscoverAutomatically())
                        || (recipeTemplate.isLocked() && playerData.hasUnlockedRecipe(recipeTemplate.getKey()))) {
                    player.discoverRecipe(recipeTemplate.getKey());
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDiscover(PlayerRecipeDiscoverEvent event) {
        NamespacedKey recipe = event.getRecipe();
        if (!recipe.getNamespace().equals(plugin.getNamespace())) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipe.getKey());
        if (recipeTemplate == null) {
            return;
        }

        if (!recipeTemplate.isLocked()) {
            return;
        }

        PlayerData playerData = plugin.getPlayerData(event.getPlayer());
        if (!playerData.hasUnlockedRecipe(recipe)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack result = inventory.getResult();
        Recipe recipe = event.getRecipe();
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
            List<ItemStack> ingredients = RecipeUtil.getCraftingIngredients(inventory);
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareStonecutter(PlayerStonecutterRecipeSelectEvent event) {
        StonecutterInventory inventory = event.getStonecutterInventory();
        ItemStack result = inventory.getResult();
        Recipe recipe = event.getStonecuttingRecipe();
        if (result == null || recipe == null) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipe);
        if (recipeTemplate == null) {
            return;
        }

        // Check whether recipe ingredients match
        IngredientMatcher ingredientMatcher = recipeTemplate.getIngredientMatcher();
        if (ingredientMatcher != null) {
            List<ItemStack> ingredients = RecipeUtil.getStonecutterIngredients(inventory);
            if (!ingredientMatcher.match(ingredients)) {
                inventory.setResult(null);
                return;
            }
        }

        // Update item creator and placeholders
        Player player = event.getPlayer();
        ItemTemplate itemTemplate = plugin.getItemTemplate(result);
        if (itemTemplate != null) {
            if (plugin.getItemTemplates().getItemCreator(result) == null) {
                plugin.getItemTemplates().setItemCreator(result, player);
            }
            itemTemplate.updateItemMeta(result, player);
            inventory.setResult(result);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOpenInventory(InventoryOpenEvent event) {
        if (!(event.getInventory() instanceof FurnaceInventory inventory) || inventory.getHolder() == null) {
            return;
        }

        ItemStack result = inventory.getResult();
        if (result == null) {
            return;
        }

        // Update item placeholders
        ItemTemplate itemTemplate = plugin.getItemTemplate(result);
        if (itemTemplate != null) {
            Player player = RecipeUtil.getSmeltingPlayer(inventory);
            if (player == null) {
                player = (Player) event.getPlayer();
            }

            if (plugin.getItemTemplates().getItemCreator(result) == null) {
                plugin.getItemTemplates().setItemCreator(result, player);
            }
            itemTemplate.updateItemMeta(result, player);
            inventory.setResult(result);
        }
    }

    private final Map<Location, CookingRecipe<?>> pendingRecipes = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFurnaceStartSmelt(FurnaceStartSmeltEvent event) {
        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(event.getRecipe());
        if (recipeTemplate == null) {
            return;
        }

        IngredientMatcher ingredientMatcher = recipeTemplate.getIngredientMatcher();
        if (ingredientMatcher != null) {
            Location loc = event.getBlock().getLocation();
            pendingRecipes.put(loc, event.getRecipe());

            List<ItemStack> ingredients = List.of(event.getSource());
            if (!ingredientMatcher.match(ingredients)) {
                event.setTotalCookTime(Integer.MAX_VALUE);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClickInventory(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof FurnaceInventory inventory)) {
            return;
        }

        Furnace furnace = inventory.getHolder();
        if (furnace == null) {
            return;
        }

        ItemStack source = inventory.getSmelting();
        if (source == null) {
            return;
        }

        CookingRecipe<?> recipe = pendingRecipes.get(furnace.getLocation());
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

        List<ItemStack> ingredients = List.of(source);
        if (ingredientMatcher.match(ingredients)) {
            furnace.setCookTime((short) 0);
            furnace.setCookTimeTotal(recipe.getCookingTime());
        } else {
            furnace.setCookTime((short) 0);
            furnace.setCookTimeTotal(Integer.MAX_VALUE);
        }

        furnace.update();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSmelt(BlockCookEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe == null) {
            return;
        }

        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipe);
        if (recipeTemplate == null) {
            return;
        }

        Block block = event.getBlock();
        pendingRecipes.remove(block.getLocation());

        BlockState state = block.getState();
        if (!(state instanceof Furnace furnace)) {
            event.setCancelled(true);
            return;
        }

        IngredientMatcher ingredientMatcher = recipeTemplate.getIngredientMatcher();
        if (ingredientMatcher != null) {
            ItemStack source = event.getSource();

            List<ItemStack> ingredients = List.of(source);
            if (!ingredientMatcher.consumeOne(ingredients)) {
                event.setCancelled(true);
                return;
            }

            furnace.getInventory().setSmelting(source.getAmount() >= 1 ? source : null);
        }

        Player player = RecipeUtil.getSmeltingPlayer(furnace);

        // Update item placeholders
        ItemStack result = event.getResult();
        ItemTemplate itemTemplate = plugin.getItemTemplate(result);
        if (itemTemplate != null) {
            if (player != null && plugin.getItemTemplates().getItemCreator(result) == null) {
                plugin.getItemTemplates().setItemCreator(result, player);
            }
            itemTemplate.updateItemMeta(result, player);
            event.setResult(result);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFurnaceBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getState() instanceof Furnace) {
            pendingRecipes.remove(block.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }

        CraftingInventory inventory = event.getInventory();
        Recipe recipe = event.getRecipe();

        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipe);
        if (recipeTemplate == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (recipeTemplate.isLocked()) {
            PlayerData playerData = plugin.getPlayerData(player);
            if (!playerData.hasUnlockedRecipe(recipeTemplate.getKey())) {
                event.setCancelled(true);
                return;
            }
        }

        IngredientMatcher ingredientMatcher = recipeTemplate.getIngredientMatcher();
        if (ingredientMatcher == null) {
            return;
        }

        ItemStack result = inventory.getResult();
        if (result == null) {
            return;
        }

        boolean shiftClick = event.getClick().isShiftClick();

        List<ItemStack> items = RecipeUtil.getCraftingIngredients(inventory);

        if (shiftClick) {
            int craftedCount = ingredientMatcher.consumeAll(items);
            if (craftedCount == -1) {
                event.setCancelled(true);
                return;
            }

            RecipeUtil.setCraftingIngredients(inventory, items);
            event.setCancelled(true);

            ItemStack crafted = result.clone();
            crafted.setAmount(result.getAmount() * craftedCount);
            PlayerUtil.giveItemsOrDrop(player, crafted);

        } else {
            ItemStack cursor = event.getCursor();
            if (cursor.getType() == Material.AIR) {
                boolean consumed = ingredientMatcher.consumeOne(items);
                if (!consumed) {
                    event.setCancelled(true);
                    return;
                }

                event.setCancelled(true);
                event.getView().setCursor(result.clone());
                RecipeUtil.setCraftingIngredients(inventory, items);
            } else if (cursor.isSimilar(result) && cursor.getAmount() + result.getAmount() <= cursor.getMaxStackSize()) {
                boolean consumed = ingredientMatcher.consumeOne(items);
                if (!consumed) {
                    event.setCancelled(true);
                    return;
                }

                event.setCancelled(true);
                ItemStack newCursor = cursor.clone();
                newCursor.setAmount(cursor.getAmount() + result.getAmount());
                event.getView().setCursor(newCursor);
                RecipeUtil.setCraftingIngredients(inventory, items);
            }
        }
    }

}
