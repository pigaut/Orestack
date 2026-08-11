package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RecipeTemplateLoader implements ConfigLoader<RecipeTemplate> {

    private final EnhancedPlugin plugin;

    public RecipeTemplateLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid recipe";
    }

    @Override
    public @NotNull RecipeTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String recipeName = scalar.toString();
        RecipeTemplate recipeTemplate = plugin.getRecipe(recipeName);
        if (recipeTemplate == null) {
            throw new InvalidConfigException(scalar, "Could not find recipe with name: " + recipeName);
        }
        return recipeTemplate;
    }

    @Override
    public @NotNull RecipeTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byRecipeFile(section.getRoot().getFile());
        NamespacedKey key = plugin.getNamespacedKey(name);
        RecipeType recipeType = section.get("type", RecipeType.class)
                .withDefault(RecipeType.SHAPED);

        if (recipeType == RecipeType.STONECUTTER) {
            ItemStack result = section.getRequired("result", ItemStack.class);
            RecipeChoice recipeChoice = section.getRequired("input", RecipeChoice.class);
            StonecuttingRecipe stonecuttingRecipe = new StonecuttingRecipe(key, result, recipeChoice);

            boolean global = section.getBoolean("global")
                    .require(Requirements.isTrue(), "Stonecutter recipe must be global")
                    .withDefault(true);
            boolean discoverAutomatically = section.getBoolean("discover-automatically")
                    .withDefault(false);

            return new RecipeTemplate(key, name, group, recipeType, global, discoverAutomatically, stonecuttingRecipe);
        }

        if (recipeType.isSmelting()) {
            RecipeChoice input = section.getRequired("input|source", RecipeChoice.class);
            ItemStack result = section.getRequired("result", ItemStack.class);
            float experience = section.getFloat("exp|experience")
                    .withDefault(0f);
            int cookingTime = section.get("cook-time|cooking-time|smelt-time|smelting-time", Delay.class)
                    .mapIfValid(Delay::toTicks)
                    .orThrow();

            CookingRecipe<?> recipe;
            switch (recipeType) {
                case FURNACE -> recipe = new FurnaceRecipe(key, result, input, experience, cookingTime);
                case BLAST_FURNACE -> recipe = new BlastingRecipe(key, result, input, experience, cookingTime);
                case SMOKER -> recipe = new SmokingRecipe(key, result, input, experience, cookingTime);
                case CAMPFIRE -> recipe = new CampfireRecipe(key, result, input, experience, cookingTime);
                default -> throw new InvalidConfigException(section, "type", "unknown recipe type");
            }

            if (Server.getVersion() >= Version.V1_19_3) {
                CookingBookCategory category = section.get("category|book-category|cooking-book-category", CookingBookCategory.class)
                        .withDefault(CookingBookCategory.MISC);
                recipe.setCategory(category);
            }

            boolean global = section.getBoolean("global")
                    .require(Requirements.isTrue(), "Furnace recipe must be global")
                    .withDefault(true);
            boolean discoverAutomatically = section.getBoolean("discover-automatically")
                    .withDefault(false);

            return new RecipeTemplate(key, name, group, recipeType, global, discoverAutomatically, recipe);
        }

        boolean global = section.getBoolean("global")
                .withDefault(false);

        boolean discoverAutomatically = section.getBoolean("discover-automatically")
                .require(discoverAuto -> !discoverAuto || global, "Recipe must be global to discover-automatically")
                .withDefault(false);

        if (recipeType == RecipeType.SHAPED) {
            ItemStack result = section.getRequired("result", ItemStack.class);
            ShapedRecipe shapedRecipe = new ShapedRecipe(key, result);

            if (Server.getVersion() >= Version.V1_19_3) {
                CraftingBookCategory category = section.get("category|book-category|crafting-book-category", CraftingBookCategory.class)
                        .withDefault(CraftingBookCategory.MISC);
                shapedRecipe.setCategory(category);
            }

            List<String> shape = section.getStringList("shape")
                    .require(list -> list.size() == 3, "Recipe shape must be a 3x3 square")
                    .requireEach(element -> element.length() == 3, "Recipe shape must be a 3x3 square")
                    .orEmpty();
            shapedRecipe.shape(shape.toArray(new String[0]));

            ConfigSection ingredientsSection = section.getSectionOrCreate("ingredients");
            if (ingredientsSection.isEmpty()) {
                throw new InvalidConfigException(section, "ingredients", "Recipe must contain at least one ingredient");
            }

            for (KeyedField field : ingredientsSection.getNestedFields()) {
                char ingredientKey = field.getCharacterKey().orThrow();

                boolean unused = true;
                for (String shapeRow : shape) {
                    if (shapeRow.contains(Character.toString(ingredientKey))) {
                        unused = false;
                        break;
                    }
                }

                if (!unused) {
                    RecipeChoice recipeChoice = field.getRequired(RecipeChoice.class);
                    shapedRecipe.setIngredient(ingredientKey, recipeChoice);
                }
            }

            return new RecipeTemplate(key, name, group, recipeType, global, discoverAutomatically, shapedRecipe);
        }

        if (recipeType == RecipeType.SHAPELESS) {
            ItemStack result = section.getRequired("result", ItemStack.class);
            ShapelessRecipe shapelessRecipe = new ShapelessRecipe(key, result);

            if (Server.getVersion() >= Version.V1_19_3) {
                CraftingBookCategory category = section.get("category|book-category|crafting-book-category", CraftingBookCategory.class)
                        .withDefault(CraftingBookCategory.MISC);
                shapelessRecipe.setCategory(category);
            }

            ConfigSequence ingredientSequence = section.getSequenceOrCreate("ingredients");
            if (ingredientSequence.isEmpty()) {
                throw new InvalidConfigException(section, "ingredients", "Recipe must contain at least one ingredient");
            }

            for (ConfigField field : ingredientSequence.getNestedFields()) {
                RecipeChoice recipeChoice = field.getRequired(RecipeChoice.class);
                shapelessRecipe.addIngredient(recipeChoice);
            }

            return new RecipeTemplate(key, name, group, recipeType, global, discoverAutomatically, shapelessRecipe);
        }

        if (recipeType == RecipeType.SMITHING) {
            if (!plugin.getRecipes().isSmithingRecipesSupported()) {
                throw new InvalidConfigException(section, "Smithing recipes require version 1.19.4+");
            }

            RecipeChoice template = section.getRequired("template", RecipeChoice.class);
            RecipeChoice base = section.getRequired("base|input", RecipeChoice.class);
            RecipeChoice addition = section.getRequired("addition|extra", RecipeChoice.class);
            ItemStack result = section.getRequired("result", ItemStack.class);

            SmithingTransformRecipe smithingTransformRecipe = new SmithingTransformRecipe(key, result, template, base, addition);
            return new RecipeTemplate(key, name, group, recipeType, global, discoverAutomatically, smithingTransformRecipe);
        }

        throw new InvalidConfigException(section, "Could not find recipe type with name: " + recipeType);
    }

}
