package io.github.pigaut.rpg.command.recipe;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class RecipeUnlockAllSubCommand extends SubCommand {

    public RecipeUnlockAllSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "unlock-all");
        withPermission(plugin.getPermission("recipe.unlock-all"));
        withDescription(plugin.getTranslation("recipe-unlock-all-command"));
        withPlayerExecution((player, context, args) -> {
            for (RecipeTemplate recipe : plugin.getRecipes().getAllRegistered()) {
                if (recipe.isGlobal()) {
                    continue;
                }
                NamespacedKey recipeKey = recipe.getKey();
                PlayerData playerData = plugin.getPlayerData(player);
                if (!playerData.hasUnlockedRecipe(recipeKey)) {
                    playerData.addUnlockedRecipe(recipeKey);
                    player.discoverRecipe(recipeKey);
                }
            }
            plugin.sendMessage(player, context, "unlocked-all-recipes");
        });
    }
}