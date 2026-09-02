package io.github.pigaut.rpg.module.function.condition.event;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InteractionTypeEquals implements Condition {

    private final InteractType[] interactTypes;
    private final @Nullable Boolean sneaking;

    public InteractionTypeEquals(@NotNull Collection<InteractType> interactTypes, @Nullable Boolean sneaking) {
        this.interactTypes = interactTypes.toArray(new InteractType[0]);
        this.sneaking = sneaking;
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Context context) {
        if (sneaking != null) {
            Player player = context.player();
            if (player == null) {
                return new FunctionError("Event that triggered this function does not have a player");
            }
            if (player.isSneaking() != sneaking) {
                return FunctionResponse.UNMET;
            }
        }

        Action action = context.action();
        if (action == null) {
            return new FunctionError("Event that triggered this function does not have a click action");
        }

        for (InteractType interactType : interactTypes) {
            if (interactType.test(action)) {
                return FunctionResponse.MET;
            }
        }
        return FunctionResponse.UNMET;
    }
}
