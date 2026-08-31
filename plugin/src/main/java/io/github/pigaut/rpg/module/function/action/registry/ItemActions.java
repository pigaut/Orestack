package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.item.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.jetbrains.annotations.*;
import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class ItemActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("DAMAGE_TOOL", (Line<Action>) line ->
                new DamageTool(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("GRANT_TOOL_USES", (Line<Action>) line ->
                new GrantToolUses(plugin, line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("RESTORE_TOOL_USES", (Line<Action>) line ->
                new RestoreToolUses(plugin));

        actions.addLoader("CONSUME_TOOL_USES", (Line<Action>) line ->
                new ConsumeToolUses(plugin, line.get(1, Amount.class).withDefault(Amount.ONE)));
    }

}