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

        actions.register("UNLOCK_RECIPE", (Line<Action>) line -> {
            List<RecipeTemplate> recipes = line.getAll(1, RecipeTemplate.class)
                    .requireEach(RecipeTemplate::isUnlockable, "Cannot unlock global recipes")
                    .orThrow();
            return new UnlockRecipes(recipes);
        });

        actions.register("LOCK_RECIPE", (Line<Action>) line -> {
            List<RecipeTemplate> recipes = line.getAll(1, RecipeTemplate.class)
                    .requireEach(RecipeTemplate::isUnlockable, "Cannot lock global recipes")
                    .orThrow();
            return new LockRecipes(recipes);
        });

        actions.registerAlias("UNLOCK_RECIPE", "UNLOCK_RECIPES");
        actions.registerAlias("LOCK_RECIPE", "LOCK_RECIPES");
    }

}