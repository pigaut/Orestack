package io.github.pigaut.rpg.module.recipe.detail;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiIngredient implements Ingredient {

    private final Ingredient[] ingredients;

    public MultiIngredient(@NotNull Collection<Ingredient> ingredients) {
        this.ingredients = ingredients.toArray(new Ingredient[0]);
    }

    public int match(@NotNull ItemStack item) {
        for (Ingredient ingredient : ingredients) {
            int amount = ingredient.match(item);
            if (amount != -1) {
                return amount;
            }
        }
        return -1;
    }

    @Override
    public List<Material> getMaterialChoices() {
        List<Material> materialChoices = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            materialChoices.addAll(ingredient.getMaterialChoices());
        }
        return materialChoices;
    }

}
