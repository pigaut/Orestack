package io.github.pigaut.rpg.module.function.condition.player.action;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.condition.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ActionEquals implements Condition {

    private final List<InteractType> validActions;
    private final @Nullable Boolean sneaking;

    public ActionEquals(List<InteractType> validActions, @Nullable Boolean sneaking) {
        this.validActions = validActions;
        this.sneaking = sneaking;
    }

    @Override
    public @Nullable Boolean isMet(@NotNull Context context) {
        if (sneaking != null) {
            Player player = context.player();
            if (player == null) {
                return null;
            }
            if (player.isSneaking() != sneaking) {
                return false;
            }
        }

        Action action = context.action();
        if (action == null) {
            return null;
        }

        for (InteractType validAction : validActions) {
            if (validAction.test(action)) {
                return true;
            }
        }

        return false;
    }

}
