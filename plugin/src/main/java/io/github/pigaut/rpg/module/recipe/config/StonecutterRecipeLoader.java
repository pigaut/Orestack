package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class StonecutterRecipeLoader implements ConfigLoader<RecipeTemplate> {

    private final EnhancedPlugin plugin;

    public StonecutterRecipeLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid stonecutter recipe";
    }

    @Override
    public @NotNull RecipeTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byRecipeFile(section.getRoot().getFile());
        NamespacedKey key = plugin.getNamespacedKey(name);

        boolean locked = section.getBoolean("locked")
                .require(Requirements.isFalse(), "Stonecutter recipe cannot be locked")
                .withDefault(false);

        boolean discoverAutomatically = section.getBoolean("discover-automatically")
                .require(discoverAuto -> !discoverAuto || !locked, "Recipe must not be locked to discover-automatically")
                .withDefault(false);

        ConfigLine line = section.getRequiredLine("stonecutter", LineStyle.MATH, "<inputs> -> (amount) <result>");
        RecipeChoice input = line.getRequired(0, RecipeChoice.class);

        boolean amountIncluded = line.valueCount() > 3;
        ItemStack result = line.getRequired(amountIncluded ? 3 : 2, ItemStack.class);
        result.setAmount(amountIncluded ? line.getRequiredInteger(2) : 1);

        StonecuttingRecipe stonecuttingRecipe = new StonecuttingRecipe(key, result, input);
        return new RecipeTemplate(key, name, group, RecipeType.STONECUTTER, null, locked, discoverAutomatically, stonecuttingRecipe);
    }

}
