package io.github.pigaut.rpg.module.function.condition;

import io.github.pigaut.rpg.core.context.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface NegativeCondition extends Condition {

    class Simple implements NegativeCondition {
        private final Condition condition;

        public Simple(@NotNull Condition condition) {
            this.condition = condition;
        }

        @Override
        public @Nullable Boolean isMet(@NotNull Context context) {
            Boolean met = condition.isMet(context);
            if (met == null) {
                return null;
            }
            return !met;
        }
    }

    class Multi implements NegativeCondition {
        private final List<Condition> conditions;

        public Multi(@NotNull List<@NotNull Condition> conditions) {
            this.conditions = conditions;
        }

        @Override
        public @Nullable Boolean isMet(@NotNull Context context) {
            for (Condition condition : conditions) {
                Boolean met = condition.isMet(context);
                if (met == null) {
                    return null;
                }
                if (met) {
                    return false;
                }
            }
            return true;
        }
    }


}
