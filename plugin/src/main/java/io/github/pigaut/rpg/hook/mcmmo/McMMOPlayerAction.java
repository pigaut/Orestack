package io.github.pigaut.rpg.hook.mcmmo;

import com.gmail.nossr50.datatypes.player.*;
import com.gmail.nossr50.util.player.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

@FunctionalInterface
public interface McMMOPlayerAction extends Action {

    @NotNull
    FunctionResponse dispatch(@NotNull McMMOPlayer mcMMOPlayer);

    @Override
    default @NotNull FunctionResponse dispatch(@NotNull Context context) {
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(context.player());
        if (mcMMOPlayer == null) {
            return new FunctionError("Function trigger does not support McMMO player");
        }
        return dispatch(mcMMOPlayer);
    }

    @FunctionalInterface
    interface Executor extends McMMOPlayerAction {

        void execute(@NotNull McMMOPlayer player);

        @Override
        default @NotNull FunctionResponse dispatch(@NotNull McMMOPlayer mcMMOPlayer) {
            execute(mcMMOPlayer);
            return FunctionResponse.NONE;
        }

    }

}
