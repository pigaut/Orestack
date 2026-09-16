package io.github.pigaut.rpg.module.recipe.config;

import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.node.line.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiIngredientLoader implements ConfigLoader.Line<MultiIngredient> {

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid ingredient";
    }

    @Override
    public @NotNull LineStyle getLineStyle() {
        return LineStyle.COMMA;
    }

    @Override
    public @NotNull MultiIngredient loadFromLine(@NotNull ConfigLine line) throws InvalidConfigException {
        List<Ingredient> ingredients = line.getAllRequired(Ingredient.class);
        return new MultiIngredient(ingredients);
    }

}
