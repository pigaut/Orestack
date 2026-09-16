package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.jetbrains.annotations.*;

public class MultiRecipeLoader implements ConfigLoader<MultiRecipe> {

    private final EnhancedPlugin plugin;

    private final ShapedRecipeLoader shapedRecipeLoader;
    private final ShapelessRecipeLoader shapelessRecipeLoader;
    private final SmithingRecipeLoader smithingRecipeLoader;
    private final StonecutterRecipeLoader stonecutterRecipeLoader;
    private final SmeltingRecipeLoader smeltingRecipeLoader;

    public MultiRecipeLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
        this.shapedRecipeLoader = new ShapedRecipeLoader(plugin);
        this.shapelessRecipeLoader = new ShapelessRecipeLoader(plugin);
        this.smithingRecipeLoader = new SmithingRecipeLoader(plugin);
        this.stonecutterRecipeLoader = new StonecutterRecipeLoader(plugin);
        this.smeltingRecipeLoader = new SmeltingRecipeLoader(plugin);
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid recipe";
    }

    @Override
    public @NotNull MultiRecipe loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (!section.getBoolean("enabled").withDefault(true)) {
            return MultiRecipe.EMPTY;
        }

        if (section.isSet("shape")) {
            return new MultiRecipe(shapedRecipeLoader.loadFromSection(section));
        }

        if (section.isSet("shapeless")) {
            return new MultiRecipe(shapelessRecipeLoader.loadFromSection(section));
        }

        if (section.isSet("forge|smithing-table")) {
            if (!plugin.getRecipes().isSmithingRecipesSupported()) {
                section.collectWarning(section, "Smithing recipes require version 1.20+");
                return MultiRecipe.EMPTY;
            }
            return new MultiRecipe(smithingRecipeLoader.loadFromSection(section));
        }

        if (section.isSet("stonecutter")) {
            return new MultiRecipe(stonecutterRecipeLoader.loadFromSection(section));
        }

        if (section.isSet("smelt|furnace|blast-furnace|blast|smoker|campfire|cooking")) {
            return smeltingRecipeLoader.loadFromSection(section);
        }

        throw new InvalidConfigException(section, "Could not determine recipe type");
    }

}
