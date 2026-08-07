package io.github.pigaut.rpg.command.recipe;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class RecipeUnlockSubCommand extends SubCommand {

    public RecipeUnlockSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "unlock");
        withPermission(plugin.getPermission("recipe.unlock"));
        withDescription(plugin.getTranslation("recipe-unlock-command"));
        withParameter(CommandParameters.unlockableRecipeName(plugin));
        withPlayerExecution((player, context, args) -> {
            RecipeTemplate recipe = plugin.getRecipe(args[0]);
            if (recipe == null) {
                plugin.sendMessage(player, context, "recipe-not-found");
                return;
            }

            if (recipe.isGlobal()) {
                plugin.sendMessage(player, context, "cannot-unlock-global-recipe");
                return;
            }

            NamespacedKey recipeKey = recipe.getKey();
            PlayerData playerData = plugin.getPlayerData(player);
            if (playerData.hasUnlockedRecipe(recipeKey)) {
                plugin.sendMessage(player, context, "already-unlocked-recipe");
                return;
            }

            playerData.addUnlockedRecipe(recipeKey);
            player.discoverRecipe(recipeKey);
            plugin.sendMessage(player, context, "unlocked-recipe");
        });
    }

}
