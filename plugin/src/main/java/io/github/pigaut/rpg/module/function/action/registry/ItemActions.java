package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.item.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;
import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class ItemActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        registerAll(plugin, "ITEM");
        registerAll(plugin, "HAND");
        registerAll(plugin, "OFF_HAND");
        registerAll(plugin, "HELMET");
        registerAll(plugin, "CHESTPLATE");
        registerAll(plugin, "LEGGINGS");
        registerAll(plugin, "BOOTS");
    }

    private static void registerAll(@NotNull EnhancedPlugin plugin, @NotNull String slot) {
        ActionRegistry actions = plugin.getActions();

        actions.addLoader("DAMAGE_" + slot, (Line<Action>) line ->
                createAction(slot, new DamageItemDurability(line.get(1, Amount.class).withDefault(Amount.ONE))));

        actions.addLoader("GRANT_" + slot + "_USES", (Line<Action>) line ->
                createAction(slot, new AddItemUses(plugin, line.get(1, Amount.class).withDefault(Amount.ONE))));

        actions.addLoader("RESTORE_" + slot + "_USES", (Line<Action>) line ->
                createAction(slot, new RestoreItemUses(plugin)));

        actions.addLoader("CONSUME_" + slot + "_USES", (Line<Action>) line ->
                createAction(slot, new RemoveItemUses(plugin, line.get(1, Amount.class).withDefault(Amount.ONE))));

        if (slot.equals("HAND")) {
            actions.addAliases("DAMAGE_HAND", "DAMAGE_TOOL");
            actions.addAliases("GRANT_HAND_USES", "GRANT_TOOL_USES");
            actions.addAliases("RESTORE_HAND_USES", "RESTORE_TOOL_USES");
            actions.addAliases("CONSUME_HAND_USES", "CONSUME_TOOL_USES");
        }
    }

    private static Action createAction(@NotNull String slot, @NotNull ItemAction action) {
        return switch (slot) {
            case "ITEM" -> context -> {
                Player player = context.player();
                ItemStack item = context.item();
                if (player == null || item == null) {
                    return new FunctionError("Event that triggered this function does not have an item");
                }
                action.execute(player, item);
                return FunctionResponse.NONE;
            };
            case "HAND" -> context -> {
                Player player = context.player();
                ItemStack tool = context.tool();
                if (player == null || tool == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                action.execute(player, tool);
                return FunctionResponse.NONE;
            };
            case "OFF_HAND" -> context -> {
                Player player = context.player();
                ItemStack offHand = context.offHand();
                if (player == null || offHand == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                action.execute(player, offHand);
                return FunctionResponse.NONE;
            };
            case "HELMET" -> context -> {
                Player player = context.player();
                ItemStack helmet = context.helmet();
                if (player == null || helmet == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                action.execute(player, helmet);
                return FunctionResponse.NONE;
            };
            case "CHESTPLATE" -> context -> {
                Player player = context.player();
                ItemStack chestplate = context.chestplate();
                if (player == null || chestplate == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                action.execute(player, chestplate);
                return FunctionResponse.NONE;
            };
            case "LEGGINGS" -> context -> {
                Player player = context.player();
                ItemStack leggings = context.leggings();
                if (player == null || leggings == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                action.execute(player, leggings);
                return FunctionResponse.NONE;
            };
            case "BOOTS" -> context -> {
                Player player = context.player();
                ItemStack boots = context.boots();
                if (player == null || boots == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                action.execute(player, boots);
                return FunctionResponse.NONE;
            };
            default -> throw new IllegalArgumentException("Unsupported slot: " + slot);
        };

    }

}