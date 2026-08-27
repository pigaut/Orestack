package io.github.pigaut.rpg.module.menu.button.conditional;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.module.menu.button.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiConditionButton implements ButtonTemplate {

    private final Map<Condition, Button> conditionalButtons;
    private final Button defaultButton;

    public MultiConditionButton(@NotNull Button defaultButton,
                                @NotNull Map<Condition, Button> conditionalButtons) {
        this.defaultButton = defaultButton;
        this.conditionalButtons = conditionalButtons;
    }

    @Override
    public @NotNull Button createButton(@NotNull Context context) {
        for (var entry : conditionalButtons.entrySet()) {
            Condition condition = entry.getKey();
            FunctionResponse response = condition.evaluate(context);
            if (response == FunctionResponse.MET) {
                return entry.getValue();
            }
        }
        return defaultButton;
    }

}
