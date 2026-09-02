package io.github.pigaut.rpg.module.function.condition.registry;

import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerConditions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ConditionRegistry conditions = plugin.getConditions();

        conditions.addLoader("PLAYER_HAS_PERMISSION", (Line<Condition>) line ->
                new PlayerHasPermission(line.getRequiredString(1)));

        conditions.addLoader("PLAYER_HAS_FLAG", (Line<Condition>) line ->
                new PlayerHasFlag(line.getRequiredString(1)));

        conditions.addLoader("PLAYER_HAS_STAT_BOOST", (Line<Condition>) line ->
                new PlayerHasStatBoost(
                        line.getRequired(1, Stat.class),
                        line.getRequiredString("name|id")
                ));

        conditions.addLoader("PLAYER_HAS_EXP", (Line<Condition>) line ->
                new PlayerHasExp(line.getRequired(1, Amount.class)));

        conditions.addLoader("PLAYER_HAS_EXP_LEVEL", (Line<Condition>) line ->
                new PlayerHasExpLevel(line.getRequired(1, Amount.class)));

        conditions.addLoader("PLAYER_HAS_ITEM", (Line<Condition>) line ->
                new PlayerHasItem(line.getRequired(1, ItemStack.class)));

        conditions.addLoader("PLAYER_HAS_PLAYED_BEFORE", (Line<Condition>) line ->
                new PlayerHasPlayedBefore());

        conditions.addLoader("PLAYER_HAS_FREE_SLOT", (Line<Condition>) line ->
                new PlayerHasFreeSlot());

        conditions.addLoader("PLAYER_IS_FLYING", (Line<Condition>) line ->
                new PlayerIsFlying());

        conditions.addLoader("PLAYER_IS_SNEAKING", (Line<Condition>) line ->
                new PlayerIsSneaking());

        conditions.addLoader("PLAYER_HAS_DISCOVERED_RECIPE", (Line<Condition>) line ->
                new PlayerHasDiscoveredRecipe(plugin.getNamespacedKey(line.getRequiredString(1))));

        EconomyHook economy = Server.getEconomyHook();
        conditions.addLoader("PLAYER_HAS_MONEY", (Line<Condition>) line -> {
            if (economy == null) {
                line.collectWarning(new InvalidConfigException(line, "Vault or economy plugin is not installed"));
                return Condition.ERROR;
            }
            return new PlayerHasMoney(economy, line.getRequired(1, Amount.class));
        });

        conditions.addLoader("PLAYER_HAS_COOLDOWN", (Line<Condition>) line ->
                new PlayerHasCooldown(line.getRequiredString(1)));

        conditions.addLoader("PLAYER_HAS_MANA", (Line<Condition>) line ->
                new PlayerHasMana(Amount.greaterThanOrEqual(line.getInteger(1).withDefault(0))));

        conditions.addAliases("PLAYER_HAS_PERMISSION", "HAS_PERMISSION");
        conditions.addAliases("PLAYER_HAS_FLAG", "HAS_FLAG");
        conditions.addAliases("PLAYER_HAS_EXP", "HAS_EXP");
        conditions.addAliases("PLAYER_HAS_EXP_LEVEL", "HAS_EXP_LEVEL");
        conditions.addAliases("PLAYER_HAS_ITEM", "HAS_ITEM");
        conditions.addAliases("PLAYER_HAS_PLAYED_BEFORE", "HAS_PLAYED_BEFORE");
        conditions.addAliases("PLAYER_HAS_FREE_SLOT", "HAS_FREE_SLOT");
        conditions.addAliases("PLAYER_IS_FLYING", "IS_FLYING");
        conditions.addAliases("PLAYER_IS_SNEAKING", "IS_SNEAKING");
        conditions.addAliases("PLAYER_HAS_DISCOVERED_RECIPE", "HAS_DISCOVERED_RECIPE");
        conditions.addAliases("PLAYER_HAS_MONEY", "HAS_MONEY");
        conditions.addAliases("PLAYER_HAS_COOLDOWN", "HAS_COOLDOWN");
        conditions.addAliases("PLAYER_HAS_MANA", "HAS_MANA");
    }

}
