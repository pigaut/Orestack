package io.github.pigaut.rpg.module.function.condition.server;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.jetbrains.annotations.*;

public class PlaceholderEqualsAmount implements Condition.Predicate {

    private final String placeholder;
    private final Amount amount;

    public PlaceholderEqualsAmount(String placeholder, Amount amount) {
        this.placeholder = placeholder;
        this.amount = amount;
    }

    @Override
    public boolean test(@NotNull Context context) {
        String parsedValue = PlaceholderUtil.parseAll(context, placeholder);

        Double parsedAmount = ParseUtil.parseDoubleOrNull(parsedValue);
        if (parsedAmount != null) {
            return amount.match(parsedAmount);
        }

        return false;
    }

}
