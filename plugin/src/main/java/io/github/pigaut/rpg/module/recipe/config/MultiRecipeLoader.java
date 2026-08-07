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
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiRecipeLoader implements ConfigLoader<MultiRecipe> {

    private final EnhancedPlugin plugin;

    public MultiRecipeLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid recipe";
    }

    @Override
    public @NotNull MultiRecipe loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        RecipeType recipeType = section.get("type", RecipeType.class)
                .withDefault(RecipeType.SHAPED);

        if (recipeType != RecipeType.SMELT) {
            RecipeTemplate recipeTemplate = section.getRequired(RecipeTemplate.class);
            return new MultiRecipe(section.getKey(), List.of(recipeTemplate));
        }

        String group = Group.byRecipeFile(section.getRoot().getFile());
        boolean global = section.getBoolean("global")
                .require(Requirements.isTrue(), "Smelting recipe must be global")
                .withDefault(true);
        boolean discoverAutomatically = section.getBoolean("discover-automatically")
                .withDefault(false);

        RecipeChoice input = section.getRequired("input|source", RecipeChoice.class);
        ItemStack result = section.getRequired("result", ItemStack.class);
        float experience = section.getFloat("exp|experience")
                .withDefault(0f);
        Integer defaultSmeltTime = section.get("cook-time|cooking-time|smelt-time|smelting-time", Delay.class)
                .map(Delay::toTicks)
                .orElse(null);

        List<RecipeTemplate> smeltRecipes = new ArrayList<>();
        ConfigSection smeltTimeSection = section.getSectionOrCreate("cook-time-by-furnace|cooking-time-by-furnace|smelt-time-by-furnace|smelting-time-by-furnace");

        Integer furnaceSmeltTime = smeltTimeSection.get("furnace", Delay.class)
                .map(Delay::toTicks)
                .orElse(defaultSmeltTime);

        if (furnaceSmeltTime != null) {
            String name = "furnace_" + section.getKey();
            NamespacedKey key = plugin.getNamespacedKey(name);
            FurnaceRecipe furnaceRecipe = new FurnaceRecipe(key, result, input, experience, furnaceSmeltTime);
            if (Server.getVersion() >= Version.V1_19_3) {
                CookingBookCategory category = section.get("category|book-category|cooking-book-category", CookingBookCategory.class)
                        .withDefault(CookingBookCategory.MISC);
                furnaceRecipe.setCategory(category);
            }
            smeltRecipes.add(new RecipeTemplate(key, name, group, RecipeType.FURNACE, global, discoverAutomatically, furnaceRecipe));
        }

        Integer blastingSmeltTime = smeltTimeSection.get("blasting|blast-furnace", Delay.class)
                .map(Delay::toTicks)
                .orElse(defaultSmeltTime);

        if (blastingSmeltTime != null) {
            String name = "blasting_" + section.getKey();
            NamespacedKey key = plugin.getNamespacedKey(name);
            BlastingRecipe blastingRecipe = new BlastingRecipe(key, result, input, experience, blastingSmeltTime);
            if (Server.getVersion() >= Version.V1_19_3) {
                CookingBookCategory category = section.get("category|book-category|cooking-book-category", CookingBookCategory.class)
                        .withDefault(CookingBookCategory.MISC);
                blastingRecipe.setCategory(category);
            }
            smeltRecipes.add(new RecipeTemplate(key, name, group, RecipeType.BLAST_FURNACE, global, discoverAutomatically, blastingRecipe));
        }

        Integer smokingSmeltTime = smeltTimeSection.get("smoking|smoker", Delay.class)
                .map(Delay::toTicks)
                .orElse(defaultSmeltTime);

        if (smokingSmeltTime != null) {
            String name = "smoking_" + section.getKey();
            NamespacedKey key = plugin.getNamespacedKey(name);
            SmokingRecipe smokingRecipe = new SmokingRecipe(key, result, input, experience, smokingSmeltTime);
            if (Server.getVersion() >= Version.V1_19_3) {
                CookingBookCategory category = section.get("category|book-category|cooking-book-category", CookingBookCategory.class)
                        .withDefault(CookingBookCategory.MISC);
                smokingRecipe.setCategory(category);
            }
            smeltRecipes.add(new RecipeTemplate(key, name, group, RecipeType.SMOKER, global, discoverAutomatically, smokingRecipe));
        }

        Integer campfireSmeltTime = smeltTimeSection.get("campfire|cooking", Delay.class)
                .map(Delay::toTicks)
                .orElse(defaultSmeltTime);

        if (campfireSmeltTime != null) {
            String name = "campfire_" + section.getKey();
            NamespacedKey key = plugin.getNamespacedKey(name);
            CampfireRecipe campfireRecipe = new CampfireRecipe(key, result, input, experience, campfireSmeltTime);
            if (Server.getVersion() >= Version.V1_19_3) {
                CookingBookCategory category = section.get("category|book-category|cooking-book-category", CookingBookCategory.class)
                        .withDefault(CookingBookCategory.MISC);
                campfireRecipe.setCategory(category);
            }
            smeltRecipes.add(new RecipeTemplate(key, name, group, RecipeType.CAMPFIRE, global, discoverAutomatically, campfireRecipe));
        }

        if (smeltRecipes.isEmpty()) {
            throw new InvalidConfigException(section, "smelt-time", "At least one smelt-time must be set");
        }

        return new MultiRecipe(section.getKey(), smeltRecipes);
    }

}
