package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.item.*;
import io.github.pigaut.rpg.module.function.condition.item.slot.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class ItemConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        registerAll(plugin, "ITEM");
        registerAll(plugin, "HAND");
        registerAll(plugin, "OFF_HAND");
        registerAll(plugin, "HELMET");
        registerAll(plugin, "CHESTPLATE");
        registerAll(plugin, "LEGGINGS");
        registerAll(plugin, "BOOTS");

        conditions.register("ADDED_ENCHANT_EQUALS", (Line<Condition>) line ->
                new AddedEnchantEquals(
                        line.getRequired(1, Enchantment.class),
                        line.get("level|lvl", Amount.class).withDefault(Amount.ANY)
                ));

        conditions.registerAlias("ADDED_ENCHANT_EQUALS", "ENCHANT_EQUALS");

    }

    private static void registerAll(@NotNull EnhancedPlugin plugin, @NotNull String slot) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.register(slot + "_ID_EQUALS", (Line<Condition>) line ->
                createCondition(slot, new ItemIdEquals(plugin, line.getAllRequired(1, String.class))));

        conditions.register(slot + "_EQUALS", (Line<Condition>) line ->
                createCondition(slot, new ItemEquals(line.getAllRequired(1, ItemStack.class))));

        conditions.register(slot + "_IS_SIMILAR", (Line<Condition>) line ->
                createCondition(slot, new ItemIsSimilar(line.getAllRequired(1, ItemStack.class))));

        conditions.register(slot + "_TYPE_EQUALS", (Line<Condition>) line ->
                createCondition(slot, new ItemTypeEquals(line.getAllRequired(1, Material.class))));

        conditions.register(slot + "_NAME_EQUALS", (Line<Condition>) line ->
                createCondition(slot, new ItemNameEquals(line.getRequiredString(1))));

        conditions.register(slot + "_LORE_LINE_EQUALS", (Line<Condition>) line -> {
            String lore = line.getRequiredString(1);
            int loreLine = line.getInteger("line|loreLine")
                    .require(Requirements.positive())
                    .orThrow() - 1;
            return createCondition(slot, new ItemLoreLineEquals(lore, loreLine));
        });

        conditions.register(slot + "_LORE_EQUALS", (Line<Condition>) line ->
                createCondition(slot, new ItemLoreEquals(line.getAllRequired(1, String.class))));

        conditions.register(slot + "_HAS_ENCHANT", (Line<Condition>) line ->
                createCondition(slot, new ItemHasEnchant(
                        line.getRequired(1, Enchantment.class),
                        line.get("level|enchantLevel", Amount.class).withDefault(Amount.ANY)
                )));

        conditions.register(slot + "_HAS_CUSTOM_MODEL", (Line<Condition>) line ->
                createCondition(slot, new ItemHasCustomModel(line.getAllRequired(1, Integer.class))));

        conditions.register(slot + "_HAS_USES", (Line<Condition>) line ->
                createCondition(slot, new ItemHasUses(plugin)));

        conditions.register(slot + "_USES_EQUALS", (Line<Condition>) line ->
                createCondition(slot, new ItemUsesEquals(plugin, line.get(1, Amount.class).withDefault(Amount.greaterThanOrEqual(1)))));

        conditions.register(slot + "_HAS_USES_LEFT", (Line<Condition>) line ->
                createCondition(slot, new ItemHasUsesLeft(plugin, line.getInteger(1).withDefault(1))));

        conditions.register(slot + "_IS_POTION", (Line<Condition>) line ->
                createCondition(slot, new ItemIsPotion()));

        conditions.register(slot + "_POTION_TYPE_EQUALS", (Line<Condition>) line ->
                createCondition(slot, new ItemPotionTypeEquals(line.getRequired(1, PotionType.class))));

        conditions.registerAlias(slot + "_POTION_TYPE_EQUALS", slot + "_POTION_EQUALS");

        conditions.register(slot + "_IS_EXECUTABLE_ITEM", (Line<Condition>) line -> {
            if (!Server.isPluginLoaded("ExecutableItems")) {
                line.collectWarning(new InvalidConfigException(line, "ExecutableItems plugin is not installed"));
                return Condition.ERROR;
            }
            return createCondition(slot, new ItemIsExecutableItem(line.getRequiredString(1)));
        });

        conditions.register(slot + "_IS_ECO_ITEM", (Line<Condition>) line -> {
            if (!Server.isPluginLoaded("EcoItems")) {
                line.collectWarning(new InvalidConfigException(line, "EcoItems plugin is not installed"));
                return Condition.ERROR;
            }
            return createCondition(slot, new ItemIsEcoItem(line.getRequiredString(1)));
        });

        if (slot.equals("HAND")) {
            conditions.registerAlias("HAND_ID_EQUALS", "TOOL_ID_EQUALS");
            conditions.registerAlias("HAND_EQUALS", "TOOL_EQUALS");
            conditions.registerAlias("HAND_IS_SIMILAR", "TOOL_IS_SIMILAR");
            conditions.registerAlias("HAND_TYPE_EQUALS", "TOOL_TYPE_EQUALS");
            conditions.registerAlias("HAND_NAME_EQUALS", "TOOL_NAME_EQUALS");
            conditions.registerAlias("HAND_LORE_LINE_EQUALS", "TOOL_LORE_LINE_EQUALS");
            conditions.registerAlias("HAND_LORE_EQUALS", "TOOL_LORE_EQUALS");
            conditions.registerAlias("HAND_HAS_ENCHANT", "TOOL_HAS_ENCHANT");
            conditions.registerAlias("HAND_HAS_CUSTOM_MODEL", "TOOL_HAS_CUSTOM_MODEL");
            conditions.registerAlias("HAND_HAS_USES", "TOOL_HAS_USES");
            conditions.registerAlias("HAND_USES_EQUALS", "TOOL_USES_EQUALS");
            conditions.registerAlias("HAND_HAS_USES_LEFT", "TOOL_HAS_USES_LEFT");
            conditions.registerAlias("HAND_IS_EXECUTABLE_ITEM", "TOOL_IS_EXECUTABLE_ITEM");
            conditions.registerAlias("HAND_IS_ECO_ITEM", "TOOL_IS_ECO_ITEM");
            conditions.registerAlias("HAND_IS_POTION", "TOOL_IS_POTION");
            conditions.registerAlias("HAND_POTION_TYPE_EQUALS", "TOOL_POTION_TYPE_EQUALS");
        }
    }

    private static Condition createCondition(@NotNull String slot, @NotNull ItemPredicate condition) {
        return switch (slot) {
            case "ITEM" -> context -> {
                ItemStack item = context.item();
                if (item == null) {
                    return new FunctionError("Event that triggered this function does not have an item");
                }
                return FunctionResponse.met(condition.test(item));
            };
            case "HAND" -> context -> {
                ItemStack tool = context.tool();
                if (tool == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                return FunctionResponse.met(condition.test(tool));
            };
            case "OFF_HAND" -> context -> {
                ItemStack offHand = context.offHand();
                if (offHand == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                return FunctionResponse.met(condition.test(offHand));
            };
            case "HELMET" -> context -> {
                ItemStack helmet = context.helmet();
                if (helmet == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                return FunctionResponse.met(condition.test(helmet));
            };
            case "CHESTPLATE" -> context -> {
                ItemStack chestplate = context.chestplate();
                if (chestplate == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                return FunctionResponse.met(condition.test(chestplate));
            };
            case "LEGGINGS" -> context -> {
                ItemStack leggings = context.leggings();
                if (leggings == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                return FunctionResponse.met(condition.test(leggings));
            };
            case "BOOTS" -> context -> {
                ItemStack boots = context.boots();
                if (boots == null) {
                    return new FunctionError("Event that triggered this function does not have a player");
                }
                return FunctionResponse.met(condition.test(boots));
            };
            default -> throw new IllegalArgumentException("Unsupported slot: " + slot);
        };

    }

}