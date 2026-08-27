package io.github.pigaut.rpg.module.function.action.event;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class EventActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("CANCEL_EVENT", (Line<Action>) line ->
                new CancelEventAction(true));

        actions.addLoader("CANCEL", (Line<Action>) line ->
                new CancelEventAction(true));

        actions.addLoader("SET_CANCELLED", (Line<Action>) line ->
                new CancelEventAction(line.getRequiredBoolean(1)));
    }

}