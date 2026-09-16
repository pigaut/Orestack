package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class RecipeTemplateLoader implements ConfigLoader<RecipeTemplate> {

    private final EnhancedPlugin plugin;

    public RecipeTemplateLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid recipe";
    }

    @Override
    public @NotNull RecipeTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String recipeName = scalar.toString();
        RecipeTemplate recipeTemplate = plugin.getRecipeTemplate(recipeName);
        if (recipeTemplate == null) {
            throw new InvalidConfigException(scalar, "Could not find recipe with name: " + recipeName);
        }
        return recipeTemplate;
    }

}
