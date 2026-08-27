package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

public interface Condition {

    Condition EMPTY = new Condition() {};
    Condition MET = new Condition() {
        @Override
        public Boolean isMet(@NotNull Context context) {
            return true;
        }
    };
    Condition UNMET = new Condition() {
        @Override
        public Boolean isMet(@NotNull Context context) {
            return false;
        }
    };

    @NotNull
    default FunctionResponse evaluate(@NotNull Context context) {
        Boolean result = isMet(context);
        if (result == null) {
            return FunctionResponse.ERROR;
        }
        return result ? FunctionResponse.MET : FunctionResponse.UNMET;
    }

    @Nullable
    default Boolean isMet(@NotNull Context context) {
        return null;
    }

}
