package io.github.pigaut.rpg.module.function.action.system;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public class GotoAction extends GotoResponse implements DispatchableAction {

    public GotoAction(int gotoLine) {
        super(gotoLine);
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        return this;
    }

}
