package io.github.pigaut.rpg.module.function.condition.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.block.*;
import io.github.pigaut.rpg.module.function.condition.entity.*;
import io.github.pigaut.rpg.module.function.condition.event.*;
import io.github.pigaut.rpg.module.function.condition.item.*;
import io.github.pigaut.rpg.module.function.condition.item.enchant.*;
import io.github.pigaut.rpg.module.function.condition.menu.*;
import io.github.pigaut.rpg.module.function.condition.mob.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.function.condition.player.ability.*;
import io.github.pigaut.rpg.module.function.condition.player.action.*;
import io.github.pigaut.rpg.module.function.condition.player.state.*;
import io.github.pigaut.rpg.module.function.condition.player.tool.*;
import io.github.pigaut.rpg.module.function.condition.server.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.chance.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ConditionRegistry extends AbstractLoader<Condition> {

    public ConditionRegistry(@NotNull EnhancedPlugin plugin) {

        // Server conditions
        addLoader("CHANCE", (Line<Condition>) line ->
                new TestChance(line.getRequired(1, Chance.class)));

        addLoader("ONLINE_PLAYERS", (Line<Condition>) line ->
                new OnlinePlayersCondition(line.getRequired(1, Amount.class)));

        // Player conditions
        addLoader("PLAYER_HAS_PERMISSION", (Line<Condition>) line ->
                new PlayerHasPermission(line.getRequiredString(1)));

        addLoader("PLAYER_HAS_FLAG", (Line<Condition>) line ->
                new PlayerHasFlag(line.getRequiredString(1)));

        addLoader("PLAYER_HAS_STAT_BOOST", (Line<Condition>) line ->
                new PlayerHasStatBoost(
                        line.getRequired(1, Stat.class),
                        line.getRequiredString("name|id")
            ));

        addLoader("PLAYER_HAS_EXP", (Line<Condition>) line ->
                new PlayerHasExp(line.getRequired(1, Amount.class)));

        addLoader("PLAYER_HAS_EXP_LEVEL", (Line<Condition>) line ->
                new PlayerHasExpLevel(line.getRequired(1, Amount.class)));

        addLoader("PLAYER_HAS_ITEM", (Line<Condition>) line ->
                new PlayerHasItem(line.getRequired(1, ItemStack.class)));

        addLoader("PLAYER_HAS_PLAYED_BEFORE", (Line<Condition>) line ->
                new PlayerHasPlayedBefore());

        addLoader("PLAYER_HAS_FREE_SLOT", (Line<Condition>) line ->
                new PlayerHasFreeSlot());

        addLoader("PLAYER_IS_FLYING", (Line<Condition>) line ->
                new PlayerIsFlying());

        addLoader("PLAYER_IS_SNEAKING", (Line<Condition>) line ->
                new PlayerIsSneaking());

        addLoader("PLAYER_HAS_DISCOVERED_RECIPE", (Line<Condition>) line ->
                new PlayerHasDiscoveredRecipe(plugin.getNamespacedKey(line.getRequiredString(1))));

        EconomyHook economy = Server.getEconomyHook();
        addLoader("PLAYER_HAS_MONEY", (Line<Condition>) line -> {
            if (economy == null) {
                ConfigRoot root = line.getRoot();
                root.collectWarning(new InvalidConfigException(line, "Vault or economy plugin is not installed"));
                return Condition.EMPTY;
            }
            return new PlayerHasMoney(economy, line.getRequired(1, Amount.class));
        });

        addAliases("PLAYER_HAS_PERMISSION", "HAS_PERMISSION");
        addAliases("PLAYER_HAS_FLAG", "HAS_FLAG");
        addAliases("PLAYER_HAS_EXP", "HAS_EXP");
        addAliases("PLAYER_HAS_EXP_LEVEL", "HAS_EXP_LEVEL");
        addAliases("PLAYER_HAS_ITEM", "HAS_ITEM");
        addAliases("PLAYER_HAS_PLAYED_BEFORE", "HAS_PLAYED_BEFORE");
        addAliases("PLAYER_HAS_FREE_SLOT", "HAS_FREE_SLOT");
        addAliases("PLAYER_IS_FLYING", "IS_FLYING");
        addAliases("PLAYER_IS_SNEAKING", "IS_SNEAKING");
        addAliases("PLAYER_HAS_DISCOVERED_RECIPE", "HAS_DISCOVERED_RECIPE");
        addAliases("PLAYER_HAS_MONEY", "HAS_MONEY");

        // Tool conditions
        addLoader("TOOL_ID_EQUALS", (Line<Condition>) line ->
                new PlayerToolIdEquals(plugin, line.getAllRequired(1, String.class)));

        addLoader("TOOL_EQUALS", (Line<Condition>) line ->
                new PlayerToolEquals(line.getAllRequired(1, ItemStack.class)));

        addLoader("TOOL_IS_SIMILAR", (Line<Condition>) line ->
                new PlayerToolIsSimilar(line.getAllRequired(1, ItemStack.class)));

        addLoader("TOOL_TYPE_EQUALS", (Line<Condition>) line ->
                new PlayerToolTypeEquals(line.getAllRequired(1, Material.class)));

        addLoader("TOOL_NAME_EQUALS", (Line<Condition>) line ->
                new PlayerToolNameEquals(line.getRequiredString(1)));

        addLoader("TOOL_LORE_LINE_EQUALS", (Line<Condition>) line -> {
            String lore = line.getRequiredString(1);
            int loreLine = line.getInteger("line|loreLine")
                    .require(Requirements.positive())
                    .orThrow() - 1;
            return new PlayerToolLoreLineEquals(lore, loreLine);
        });

        addLoader("TOOL_LORE_EQUALS", (Line<Condition>) line ->
                new PlayerToolLoreEquals(line.getAllRequired(1, String.class)));

        addLoader("TOOL_HAS_ENCHANT", (Line<Condition>) line ->
                new PlayerToolHasEnchant(
                        line.getRequired(1, Enchantment.class),
                        line.get("level|enchantLevel", Amount.class).withDefault(Amount.ANY)
                ));

        addLoader("TOOL_HAS_CUSTOM_MODEL", (Line<Condition>) line ->
                new PlayerToolHasCustomModel(line.getAllRequired(1, Integer.class)));

        addLoader("TOOL_HAS_USES", (Line<Condition>) line ->
                new PlayerToolHasUses(plugin));

        addLoader("TOOL_USES_EQUALS", (Line<Condition>) line ->
                new PlayerToolUsesEquals(plugin, line.get(1, Amount.class).withDefault(Amount.greaterThanOrEqual(1))));

        addLoader("TOOL_HAS_USES_LEFT", (Line<Condition>) line ->
                new PlayerToolHasUsesLeft(plugin, line.getInteger(1).withDefault(1)));

        addLoader("PLACEHOLDER_EQUALS", (Line<Condition>) line -> {
            String placeholder = line.getRequiredString("id|tag|placeholder|ph");
            Amount amount = line.get(1, Amount.class).orElse(null);
            if (amount != null) {
                return new PlaceholderEqualsAmount(placeholder, amount);
            }

            boolean ignoreCase = line.getBoolean("ignoreCase|ignore-case").withDefault(true);
            return new PlaceholderEqualsString(placeholder, line.getRequiredString(1), ignoreCase);
        });

        addLoader("TOOL_IS_EXECUTABLE_ITEM", (Line<Condition>) line -> {
            if (!Server.isPluginLoaded("ExecutableItems")) {
                throw new InvalidConfigException(line, "Missing ExecutableItems dependency");
            }
            return new PlayerToolIsExecutableItem(line.getRequiredString(1));
        });

        addLoader("TOOL_IS_ECO_ITEM", (Line<Condition>) line -> {
            if (!Server.isPluginLoaded("EcoItems")) {
                throw new InvalidConfigException(line, "Missing EcoItems dependency");
            }
            return new PlayerToolIsEcoItem(line.getRequiredString(1));
        });


        // Block conditions
        addLoader("BLOCK_TYPE_EQUALS", (Line<Condition>) line -> {
            Set<Material> materials = new HashSet<>();
            for (MaterialTag materialTag : line.getAllRequired(1, MaterialTag.class)) {
                materials.addAll(materialTag.getMaterials());
            }
            return new BlockTypeEquals(materials);
        });

        // Event conditions
        addLoader("CLICK_TYPE_EQUALS", (Line<Condition>) line ->
                new ActionEquals(
                        line.getAllRequired(1, InteractType.class),
                        line.getBoolean("shift|sneak|sneaking").withDefault(null)
                ));

        // Entity conditions
        addLoader("ENTITY_IS_MOB", (Line<Condition>) line ->
                new EntityIsMob(plugin));

        addLoader("ENTITY_TYPE_EQUALS", (Line<Condition>) line ->
                new EntityTypeEquals(line.getAllRequired(1, EntityType.class)));

        // Event conditions
        addLoader("DAMAGE_IS_CRITICAL", (Line<Condition>) line ->
                new DamageIsCritical());

        addLoader("DAMAGE_IS_FALLING_CRITICAL", (Line<Condition>) line ->
                new DamageIsFallingCritical());

        addAliases("DAMAGE_IS_CRITICAL", "DAMAGE_IS_CRIT");
        addAliases("DAMAGE_IS_FALLING_CRITICAL", "DAMAGE_IS_FALLING_CRIT");

        // Mob conditions start
        addLoader("MOB_HAS_FLAG", (Line<Condition>) line ->
                new MobHasFlag(line.getRequiredString(1)));

        addLoader("MOB_HEALTH_EQUALS", (Line<Condition>) line ->
                new MobHealthEquals(line.getRequired(1, Amount.class)));

        addLoader("MOB_HEALTH_ABOVE", (Line<Condition>) line ->
                new MobHealthEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        addLoader("MOB_HEALTH_BELOW", (Line<Condition>) line ->
                new MobHealthEquals(Amount.lessThanOrEqual(line.getRequiredInteger(1))));

        addLoader("MOB_IS_FULL_HEALTH", (Line<Condition>) line ->
                new MobIsFullHealth());

        addLoader("MOB_NAME_EQUALS", (Line<Condition>) line ->
                new MobNameEquals(line.getRequiredString(1)));

        addLoader("ATTACKER_COUNT_EQUALS", (Line<Condition>) line ->
                new AttackerCountEquals(line.getRequired(1, Amount.class)));

        addLoader("ATTACKER_COUNT_ABOVE", (Line<Condition>) line ->
                new AttackerCountEquals(Amount.greaterThanOrEqual(line.getRequiredInteger(1))));

        addLoader("ATTACKER_COUNT_BELOW", (Line<Condition>) line ->
                new AttackerCountEquals(Amount.lessThanOrEqual(line.getRequiredInteger(1))));
        // Mob conditions end


        // Menu conditions
        addLoader("MENU_CAN_SCROLL", (Line<Condition>) line ->
                new MenuCanScroll(line.getRequired(ScrollDirection.class),
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_RIGHT", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.RIGHT,
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_DOWN", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.DOWN,
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_LEFT", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.LEFT,
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_UP", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.UP,
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_RIGHT_UP", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.RIGHT_UP,
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_RIGHT_DOWN", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.RIGHT_DOWN,
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_LEFT_DOWN", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.LEFT_DOWN,
                        line.getInteger("amount").withDefault(1)));

        addLoader("MENU_CAN_SCROLL_LEFT_UP", (Line<Condition>) line ->
                new MenuCanScroll(ScrollDirection.LEFT_UP,
                        line.getInteger("amount").withDefault(1)));

        addAliases("MENU_CAN_SCROLL", "CAN_SCROLL");
        addAliases("MENU_CAN_SCROLL_RIGHT", "CAN_SCROLL_RIGHT");
        addAliases("MENU_CAN_SCROLL_DOWN", "CAN_SCROLL_DOWN");
        addAliases("MENU_CAN_SCROLL_LEFT", "CAN_SCROLL_LEFT");
        addAliases("MENU_CAN_SCROLL_UP", "CAN_SCROLL_UP");
        addAliases("MENU_CAN_SCROLL_RIGHT_UP", "CAN_SCROLL_RIGHT_UP");
        addAliases("MENU_CAN_SCROLL_RIGHT_DOWN", "CAN_SCROLL_RIGHT_DOWN");
        addAliases("MENU_CAN_SCROLL_LEFT_DOWN", "CAN_SCROLL_LEFT_DOWN");
        addAliases("MENU_CAN_SCROLL_LEFT_UP", "CAN_SCROLL_LEFT_UP");

        // Item conditions
        addLoader("ADDED_ENCHANT_EQUALS", (Line<Condition>) line ->
                new AddedEnchantEquals(
                        line.getRequired(1, Enchantment.class),
                        line.get("level|lvl", Amount.class).withDefault(Amount.ANY)
                ));

        addLoader("ITEM_IS_POTION", (Line<Condition>) line ->
                new ItemIsPotion());

        addLoader("ITEM_POTION_TYPE_EQUALS", (Line<Condition>) line ->
                new ItemPotionTypeEquals(line.getRequired(1, PotionType.class)));

        addAliases("ADDED_ENCHANT_EQUALS", "ENCHANT_EQUALS");
        addAliases("ITEM_POTION_TYPE_EQUALS", "ITEM_POTION_EQUALS");

        // Ability conditions
        addLoader("PLAYER_HAS_COOLDOWN", (Line<Condition>) line ->
                new PlayerHasCooldown(line.getRequiredString(1)));

        addLoader("PLAYER_HAS_MANA", (Line<Condition>) line ->
                new PlayerHasMana(Amount.greaterThanOrEqual(line.getInteger(1).withDefault(0))));

        addAliases("PLAYER_HAS_COOLDOWN", "HAS_COOLDOWN");
        addAliases("PLAYER_HAS_MANA", "HAS_MANA");

    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid condition";
    }

    @Override
    public @NotNull Condition loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        ConfigLine line = scalar.toLine();
        String conditionName = line.getRequiredString(0);

        ConfigLoader<? extends Condition> loader = getLoader(conditionName);
        if (loader == null) {
            throw new InvalidConfigException(line,
                    "Could not find condition with name: " + CaseFormatter.toCamelCase(conditionName));
        }

        return loader.loadFromScalar(scalar);
    }

    @Override
    public @NotNull Condition loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        if (section.isSet("if")) {
            return section.getRequired("if", Condition.class);
        }
        else if (section.isSet("if-not|if not")) {
            return section.getRequired("if-not|if not", NegativeCondition.class);
        }
        else if (section.isSet("if-any|if any")) {
            return section.getRequired("if-any|if any", DisjunctiveCondition.class);
        }
        else {
            throw new InvalidConfigException(section, "Could not find any valid condition");
        }
    }

    @Override
    public @NotNull Condition loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new MultiCondition(sequence.getAll(Condition.class).orThrow());
    }

}
