package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.system.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class SystemActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("RETURN", (Line<Action>) line -> {
            String returnValue = line.getString(1).orElse(null);
            if (returnValue != null) {
                Object parsedValue = ParseUtil.parseAsScalar(returnValue);
                return new YieldAction(parsedValue);
            }
            return new ReturnAction();
        });

        actions.addLoader("CONTINUE", (Line<Action>) line ->
                new ContinueAction());

        actions.addLoader("STOP", (Line<Action>) line ->
                new StopAction());
    }

}