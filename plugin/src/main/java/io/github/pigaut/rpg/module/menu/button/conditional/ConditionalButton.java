package io.github.pigaut.rpg.module.menu.button.conditional;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.menu.button.*;
import org.jetbrains.annotations.*;

public class ConditionalButton implements ButtonTemplate {

    private final Condition condition;
    private final Button metButton;
    private final Button unmetButton;

    public ConditionalButton(@NotNull Condition condition,
                             @NotNull Button metButton, @NotNull Button unmetButton) {
        this.condition = condition;
        this.metButton = metButton;
        this.unmetButton = unmetButton;
    }

    @Override
    public @NotNull Button createButton(@NotNull Context context) {
        if (condition.isMet(context)) {
            return metButton;
        }
        return unmetButton;
    }

    public @NotNull Condition getCondition() {
        return condition;
    }

    public @NotNull Button getMetButton() {
        return metButton;
    }

    public @NotNull Button getUnmetButton() {
        return unmetButton;
    }

}
