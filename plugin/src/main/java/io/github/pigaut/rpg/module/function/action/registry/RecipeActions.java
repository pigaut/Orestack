package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.recipe.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;
import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class RecipeActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("UNLOCK_RECIPE", (Line<Action>) line -> {
            List<RecipeTemplate> recipes = line.getAll(1, RecipeTemplate.class)
                    .requireEach(RecipeTemplate::isUnlockable, "Cannot unlock global recipes")
                    .orThrow();
            return new UnlockRecipes(recipes);
        });

        actions.addLoader("LOCK_RECIPE", (Line<Action>) line -> {
            List<RecipeTemplate> recipes = line.getAll(1, RecipeTemplate.class)
                    .requireEach(RecipeTemplate::isUnlockable, "Cannot lock global recipes")
                    .orThrow();
            return new LockRecipes(recipes);
        });

        actions.addAliases("UNLOCK_RECIPE", "UNLOCK_RECIPES");
        actions.addAliases("LOCK_RECIPE", "LOCK_RECIPES");
    }

}