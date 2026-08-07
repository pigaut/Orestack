package io.github.pigaut.rpg.command.recipe;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class RecipeLockAllSubCommand extends SubCommand {

    public RecipeLockAllSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "lock-all");
        withPermission(plugin.getPermission("recipe.lock-all"));
        withDescription(plugin.getTranslation("recipe-lock-all-command"));
        withPlayerExecution((player, context, args) -> {
            for (RecipeTemplate recipe : plugin.getRecipes().getAllRegistered()) {
                if (recipe.isGlobal()) {
                    continue;
                }
                NamespacedKey recipeKey = recipe.getKey();
                PlayerData playerData = plugin.getPlayerData(player);
                if (playerData.hasUnlockedRecipe(recipeKey)) {
                    playerData.removeUnlockedRecipe(recipeKey);
                    player.undiscoverRecipe(recipeKey);
                }
            }
            plugin.sendMessage(player, context, "locked-all-recipes");
        });
    }

}