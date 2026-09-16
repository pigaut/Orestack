package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.line.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SmeltingRecipeLoader implements ConfigLoader<MultiRecipe> {

    private static final String LINE_FORMAT = "<inputs> -> (amount) <result> (time=5s) (exp=5)";

    private final EnhancedPlugin plugin;

    public SmeltingRecipeLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid smelting recipe";
    }

    @Override
    public @NotNull MultiRecipe loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byRecipeFile(section.getRoot().getFile());

        boolean locked = section.getBoolean("locked")
                .require(Requirements.isFalse(), "Smelting recipe cannot be locked")
                .withDefault(false);

        boolean discoverAutomatically = section.getBoolean("discover-automatically")
                .require(discoverAuto -> !discoverAuto || !locked, "Recipe must not be locked to discover-automatically")
                .withDefault(false);

        ConfigLine smeltLine = section.getLine("smelt", LineStyle.MATH, LINE_FORMAT)
                .withDefaultOrThrow(null);

        List<RecipeTemplate> smeltRecipes = new ArrayList<>();
        addCookingRecipe(section, smeltRecipes, smeltLine, name, group, locked, discoverAutomatically,
                "furnace", "furnace", RecipeType.FURNACE, FurnaceRecipe::new);

        addCookingRecipe(section, smeltRecipes, smeltLine, name, group, locked, discoverAutomatically,
                "blasting|blast-furnace", "blasting", RecipeType.BLAST_FURNACE, BlastingRecipe::new);

        addCookingRecipe(section, smeltRecipes, smeltLine, name, group, locked, discoverAutomatically,
                "smoking|smoker", "smoking", RecipeType.SMOKER, SmokingRecipe::new);

        addCookingRecipe(section, smeltRecipes, smeltLine, name, group, locked, discoverAutomatically,
                "campfire|cooking", "campfire", RecipeType.CAMPFIRE, CampfireRecipe::new);

        if (smeltRecipes.isEmpty()) {
            throw new InvalidConfigException(section, "Expected at least one cooking type (smelt, furnace, blast, smoker, campfire)");
        }

        return new MultiRecipe(name, smeltRecipes);
    }

    private void addCookingRecipe(@NotNull ConfigSection section, @NotNull List<RecipeTemplate> smeltRecipes,
                                  @Nullable ConfigLine smeltLine, @NotNull String name, @Nullable String group,
                                  boolean locked, boolean discoverAutomatically,
                                  @NotNull String configKey, @NotNull String prefix,
                                  @NotNull RecipeType recipeType, @NotNull CookingRecipeFactory factory) throws InvalidConfigException {

        ConfigLine line = section.isSet(configKey) ?
                section.getRequiredLine(configKey, LineStyle.MATH, LINE_FORMAT) : smeltLine;

        if (line == null) {
            return;
        }

        String subName = prefix + "_" + name;
        NamespacedKey key = plugin.getNamespacedKey(subName);

        Ingredient input = line.getRequired(0, MultiIngredient.class);

        boolean amountIncluded = line.valueCount() > 3;
        ItemStack result = line.getRequired(amountIncluded ? 3 : 2, ItemStack.class);
        result.setAmount(amountIncluded ? line.getRequiredInteger(2) : 1);

        Delay smeltTime = line.get("time|smeltTime", Delay.class).withDefault(Delay.fromSeconds(3));
        float exp = line.getFloat("exp|smeltExp").withDefault(0f);

        RecipeChoice materialChoice = new RecipeChoice.MaterialChoice(input.getMaterialChoices());
        CookingRecipe<?> recipe = factory.create(key, result, materialChoice, exp, smeltTime.toTicks());
        if (Server.getVersion() >= Version.V1_19_3) {
            CookingBookCategory category = section.get("category|book-category|cooking-book-category", CookingBookCategory.class)
                    .withDefault(CookingBookCategory.MISC);
            recipe.setCategory(category);
        }

        IngredientMatcher ingredientMatcher = new IngredientMatcher(List.of(input));
        smeltRecipes.add(new RecipeTemplate(key, subName, group, recipeType, ingredientMatcher, locked, discoverAutomatically, recipe));
    }

    @FunctionalInterface
    private interface CookingRecipeFactory {
        CookingRecipe<?> create(NamespacedKey key, ItemStack result, RecipeChoice input, float exp, int cookTime);
    }

}
