package io.github.pigaut.rpg.module.recipe;

import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public record MultiRecipe(@NotNull String name, @NotNull List<RecipeTemplate> recipes) {

    public static final MultiRecipe EMPTY = new MultiRecipe(StringUtil.randomName(), List.of());

    public MultiRecipe(@NotNull RecipeTemplate recipe) {
        this(recipe.getName(), List.of(recipe));
    }

}
