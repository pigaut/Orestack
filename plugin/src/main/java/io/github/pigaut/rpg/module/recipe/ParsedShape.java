package io.github.pigaut.rpg.module.recipe;

import org.jetbrains.annotations.*;

import java.util.*;

public class ParsedShape {

    private final List<String> shape;
    private final String result;
    private final int amount;

    public ParsedShape(@NotNull List<String> shape, @NotNull String result, int amount) {
        this.shape = List.copyOf(shape);
        this.result = result;
        this.amount = amount;
    }

    public @NotNull String[] getIngredientMatrix() {
        return shape.toArray(new String[0]);
    }

    public @NotNull String getResult() {
        return result;
    }

    public int getResultAmount() {
        return amount;
    }
}
