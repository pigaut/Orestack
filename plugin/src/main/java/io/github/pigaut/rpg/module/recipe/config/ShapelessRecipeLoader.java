package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShapelessRecipeLoader implements ConfigLoader<RecipeTemplate> {

    private final EnhancedPlugin plugin;

    public ShapelessRecipeLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid shapeless recipe";
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

        ConfigLine line = section.getRequiredLine("shapeless", LineStyle.MATH,
                "<ingredient> [+ <ingredient>] -> (amount) <result>");

        int resultIndex = line.indexOf("->") + 1;
        if (resultIndex <= 0 || resultIndex > 18) {
            throw new InvalidConfigException(line, "Crafting recipe must have between 1 and 9 ingredients");
        }

        boolean amountIncluded = line.valueCount() > resultIndex + 1;
        ItemStack result = line.getRequired(amountIncluded ? resultIndex + 1 : resultIndex, ItemStack.class);
        result.setAmount(amountIncluded ? line.getRequiredInteger(resultIndex) : 1);

        List<Ingredient> ingredients = new ArrayList<>();
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(key, result);
        for (int i = 0; i < resultIndex; i++) {
            if (i % 2 != 0) {
                continue;
            }

            Ingredient ingredient = line.getRequired(i, MultiIngredient.class);
            ingredients.add(ingredient);

            shapelessRecipe.addIngredient(new RecipeChoice.MaterialChoice(ingredient.getMaterialChoices()));
        }

        IngredientMatcher ingredientMatcher = new IngredientMatcher(ingredients);

        if (Server.getVersion() >= Version.V1_19_3) {
            CraftingBookCategory category = section.get("category|book-category|crafting-book-category", CraftingBookCategory.class)
                    .withDefault(CraftingBookCategory.MISC);
            shapelessRecipe.setCategory(category);
        }

        return new RecipeTemplate(key, name, group, RecipeType.SHAPELESS, ingredientMatcher, locked, discoverAutomatically, shapelessRecipe);
    }

}
