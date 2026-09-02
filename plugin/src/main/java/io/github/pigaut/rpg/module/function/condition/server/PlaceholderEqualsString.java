package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.jetbrains.annotations.*;

public class PlaceholderEqualsString implements Condition.Predicate {

    private final String placeholder;
    private final String value;
    private final boolean ignoreCase;

    public PlaceholderEqualsString(@NotNull String placeholder, @NotNull String value, boolean ignoreCase) {
        this.placeholder = placeholder;
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(@NotNull Context context) {
        String parsedValue = PlaceholderUtil.parseAll(context, placeholder);
        if (ignoreCase) {
            return value.equalsIgnoreCase(parsedValue);
        } else {
            return value.equals(parsedValue);
        }
    }

}
