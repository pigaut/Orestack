package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SmithingRecipeLoader implements ConfigLoader<RecipeTemplate> {

    private final EnhancedPlugin plugin;

    public SmithingRecipeLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid smithing recipe";
    }

    @Override
    public @NotNull RecipeTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (!plugin.getRecipes().isSmithingRecipesSupported()) {
            throw new InvalidConfigException(section, "Smithing recipes require version 1.20+");
        }

        String name = section.getKey();
        String group = Group.byRecipeFile(section.getRoot().getFile());
        NamespacedKey key = plugin.getNamespacedKey(name);

        boolean locked = section.getBoolean("locked")
                .withDefault(false);

        boolean discoverAutomatically = section.getBoolean("discover-automatically")
                .require(discoverAuto -> !discoverAuto || !locked, "Recipe must not be locked to discover-automatically")
                .withDefault(false);

        ConfigLine line = section.getRequiredLine("smithing-table|forge", LineStyle.MATH, "<templates> + <bases> + <extras> -> (amount) <result>");

        Ingredient template = line.getRequired(0, MultiIngredient.class);
        Ingredient base = line.getRequired(2, MultiIngredient.class);
        Ingredient addition = line.getRequired(4, MultiIngredient.class);
        IngredientMatcher ingredientMatcher = new IngredientMatcher(List.of(template, base, addition));

        RecipeChoice templateChoice = new RecipeChoice.MaterialChoice(template.getMaterialChoices());
        RecipeChoice baseChoice = new RecipeChoice.MaterialChoice(base.getMaterialChoices());
        RecipeChoice additionChoice = new RecipeChoice.MaterialChoice(addition.getMaterialChoices());

        boolean amountIncluded = line.valueCount() > 7;
        ItemStack result = line.getRequired(amountIncluded ? 7 : 6, ItemStack.class);
        result.setAmount(amountIncluded ? line.getRequiredInteger(6) : 1);

        SmithingTransformRecipe smithingTransformRecipe = new SmithingTransformRecipe(key, result, templateChoice, baseChoice, additionChoice);
        return new RecipeTemplate(key, name, group, RecipeType.SMITHING, ingredientMatcher, locked, discoverAutomatically, smithingTransformRecipe);
    }

}
