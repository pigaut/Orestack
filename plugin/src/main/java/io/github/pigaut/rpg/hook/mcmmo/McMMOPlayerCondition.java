package io.github.pigaut.rpg.hook.mcmmo;

import com.gmail.nossr50.datatypes.player.*;
import com.gmail.nossr50.util.player.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface McMMOPlayerCondition extends Condition {

    @NotNull
    FunctionResponse evaluate(@NotNull McMMOPlayer mcMMOPlayer);

    @Override
    default @NotNull FunctionResponse evaluate(@NotNull Context context) {
        McMMOPlayer player = UserManager.getPlayer(context.player());
        if (player == null) {
            return new FunctionError("Function trigger does not support McMMO player conditions");
        }
        return evaluate(player);
    }

    @FunctionalInterface
    interface Predicate extends McMMOPlayerCondition {

        boolean test(@NotNull McMMOPlayer player);

        @Override
        default @NotNull FunctionResponse evaluate(@NotNull McMMOPlayer player) {
            return test(player) ? FunctionResponse.MET : FunctionResponse.UNMET;
        }

    }

}
