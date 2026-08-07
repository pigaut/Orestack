package io.github.pigaut.rpg.bukkit;

import org.bukkit.event.block.*;

import java.util.function.*;

public enum InteractType {

    ANY(action -> true),
    RIGHT_CLICK(action -> action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR),
    LEFT_CLICK(action -> action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR),

    RIGHT_CLICK_BLOCK(action -> action == Action.RIGHT_CLICK_BLOCK),
    LEFT_CLICK_BLOCK(action -> action == Action.LEFT_CLICK_BLOCK),
    RIGHT_CLICK_AIR(action -> action == Action.RIGHT_CLICK_AIR),
    LEFT_CLICK_AIR(action -> action == Action.LEFT_CLICK_AIR),
    PHYSICAL(action -> action == Action.PHYSICAL);

    private final Predicate<Action> actionPredicate;

    InteractType(Predicate<Action> actionPredicate) {
        this.actionPredicate = actionPredicate;
    }

    public boolean test(Action action) {
        return actionPredicate.test(action);
    }

}
