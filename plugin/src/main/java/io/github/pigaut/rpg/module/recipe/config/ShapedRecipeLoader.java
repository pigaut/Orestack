package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShapedRecipeLoader implements ConfigLoader<RecipeTemplate> {

    private final EnhancedPlugin plugin;

    public ShapedRecipeLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid shaped recipe";
    }

    @Override
    public @NotNull RecipeTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byRecipeFile(section.getRoot().getFile());
        NamespacedKey key = plugin.getNamespacedKey(name);

        boolean locked = section.getBoolean("locked")
                .withDefault(false);

        boolean discoverAutomatically = section.getBoolean("discover-automatically")
                .require(discoverAuto -> !discoverAuto || !locked, "Recipe must not be locked to discover-automatically")
                .withDefault(false);

        ParsedShape shape = section.getRequired("shape", ParsedShape.class);

        String resultName = shape.getResult();
        ItemStack result = plugin.getItemTemplates().createItemStack(resultName);
        if (result == null) {
            Material resultMaterial = ParseUtil.parseEnumOrNull(Material.class, resultName);
            if (resultMaterial == null) {
                throw new InvalidConfigException(section, "Could not find recipe result item with name: " + resultName);
            }
            result = new ItemStack(resultMaterial, shape.getResultAmount());
        } else {
            result.setAmount(shape.getResultAmount());
        }

        ShapedRecipe shapedRecipe = new ShapedRecipe(key, result);

        String[] ingredientMatrix = shape.getIngredientMatrix();
        shapedRecipe.shape(ingredientMatrix);

        List<Ingredient> ingredients = new ArrayList<>();
        for (String row : ingredientMatrix) {
            for (char ingredientKey : row.toCharArray()) {
                if (ingredientKey == ' ') {
                    continue;
                }

                Ingredient ingredient = section.getRequired("ingredients." + ingredientKey, MultiIngredient.class);
                ingredients.add(ingredient);

                RecipeChoice materialChoice = new RecipeChoice.MaterialChoice(ingredient.getMaterialChoices());
                shapedRecipe.setIngredient(ingredientKey, materialChoice);
            }
        }

        if (Server.getVersion() >= Version.V1_19_3) {
            CraftingBookCategory category = section.get("category|book-category|crafting-book-category", CraftingBookCategory.class)
                    .withDefault(CraftingBookCategory.MISC);
            shapedRecipe.setCategory(category);
        }

        IngredientMatcher ingredientMatcher = new IngredientMatcher(ingredients);
        return new RecipeTemplate(key, name, group,
                RecipeType.SHAPED, ingredientMatcher,
                locked, discoverAutomatically, shapedRecipe);
    }

}
