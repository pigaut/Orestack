package io.github.pigaut.rpg.module.recipe.detail;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IngredientMatcher {

    private final Ingredient[] ingredients;

    public IngredientMatcher(@NotNull Collection<Ingredient> ingredients) {
        this.ingredients = ingredients.toArray(new Ingredient[0]);
    }

    /**
     * Checks whether every ingredient can be satisfied by the given items,
     * without modifying any of them. Returns as soon as any ingredient
     * cannot be satisfied.
     *
     * @param items the items to check against
     * @return {@code true} if all ingredients are satisfied at least once
     */
    public boolean match(@NotNull List<ItemStack> items) {
        boolean[] used = new boolean[items.size()];

        for (Ingredient ingredient : ingredients) {
            boolean found = false;

            for (int j = 0; j < items.size(); j++) {
                if (used[j]) {
                    continue;
                }

                ItemStack item = items.get(j);
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }

                int required = ingredient.match(item);
                if (required != -1 && item.getAmount() >= required) {
                    used[j] = true;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    /**
     * Attempts to consume one full set of ingredients from the given items.
     * Either all ingredients are satisfied and their required amounts are
     * subtracted from the matched {@link ItemStack}s, or nothing is modified
     * at all.
     *
     * @param items the items to consume from; matched stacks are mutated in place
     * @return {@code true} if all ingredients were satisfied and consumed,
     *         {@code false} if no consumption occurred
     */
    public boolean consumeOne(@NotNull List<ItemStack> items) {
        boolean[] used = new boolean[items.size()];
        int[] chosenIndex = new int[ingredients.length];
        int[] chosenAmount = new int[ingredients.length];

        // Phase 1: find a valid assignment without mutating anything yet
        for (int i = 0; i < ingredients.length; i++) {
            Ingredient ingredient = ingredients[i];
            int foundIndex = -1;
            int foundAmount = -1;

            for (int j = 0; j < items.size(); j++) {
                if (used[j]) continue;
                ItemStack item = items.get(j);
                if (item == null || item.getType() == Material.AIR) continue;

                int required = ingredient.match(item);
                if (required != -1) {
                    foundIndex = j;
                    foundAmount = required;
                    break;
                }
            }

            if (foundIndex == -1) {
                return false; // couldn't satisfy this ingredient; nothing consumed
            }

            used[foundIndex] = true;
            chosenIndex[i] = foundIndex;
            chosenAmount[i] = foundAmount;
        }

        // Phase 2: assignment is valid for every ingredient, now actually consume
        for (int i = 0; i < ingredients.length; i++) {
            ItemStack item = items.get(chosenIndex[i]);
            item.setAmount(item.getAmount() - chosenAmount[i]);
        }

        return true;
    }

    /**
     * Repeatedly consumes full sets of ingredients from the given items for
     * as long as possible.
     *
     * @param items the items to consume from; matched stacks are mutated in place
     * @return the number of times all ingredients were consumed, or {@code -1}
     *         if nothing was consumed at all
     */
    public int consumeAll(@NotNull List<ItemStack> items) {
        int consumedCount = 0;
        while (consumeOne(items)) {
            consumedCount++;
        }
        return consumedCount == 0 ? -1 : consumedCount;
    }

}