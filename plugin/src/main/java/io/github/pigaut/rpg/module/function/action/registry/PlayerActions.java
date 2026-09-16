package io.github.pigaut.rpg.module.function.action.registry;

import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.action.player.cooldown.*;
import io.github.pigaut.rpg.module.function.action.player.flag.*;
import io.github.pigaut.rpg.module.function.action.player.stat.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.module.function.action.protagonist.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import static io.github.pigaut.yaml.configurator.load.ConfigLoader.Line;

public class PlayerActions {

    public static void registerAll(@NotNull EnhancedPlugin plugin) {
        ActionRegistry actions = plugin.getActions();

        actions.register("DROP_ITEM_AT_PLAYER", (Line<Action>) line ->
                new DropItemAtPlayer(line.getRequired(ItemDrop.class)));

        actions.register("DROP_EXP_AT_PLAYER", (Line<Action>) line ->
                new DropExpAtPlayer(line.getRequired(ExpDrop.class)));

        actions.register("SPAWN_PARTICLE_AT_PLAYER", (Line<Action>) line ->
                new SpawnParticleAtPlayer(line.getRequired(1, ParticleEffect.class)));

        actions.register("PLAY_SOUND_AT_PLAYER", (Line<Action>) line ->
                new PlaySoundOnPlayer(line.getRequired(1, SoundEffect.class)));

        actions.register("ADD_PLAYER_EXP", (Line<Action>) line ->
                new GiveExpToPlayer(line.getRequired(ExpDrop.class)));

        actions.register("ADD_PLAYER_FLAG", (Line<Action>) line -> {
            if (line.hasFlag("duration")) {
                return new AddTemporaryPlayerFlag(
                        line.getRequiredString(1),
                        line.getRequired("duration", Delay.class)
                );
            }
            return new AddPlayerFlag(line.getRequiredString(1));
        });

        actions.register("ADD_TEMPORARY_PLAYER_FLAG", (Line<Action>) line ->
                new AddTemporaryPlayerFlag(
                        line.getRequiredString(1),
                        line.getRequired("duration", Delay.class)
        ));

        actions.register("ADD_PLAYER_STAT_BOOST", (Line<Action>) line ->
                new AddPlayerStatBoost(plugin,
                        line.getRequired(1, Stat.class),
                        line.getRequired("amount", StatModifier.class),
                        line.getRequired("duration", Delay.class),
                        line.getString("name|id").withDefault(null)
                ));

        actions.register("REMOVE_PLAYER_STAT_BOOST", (Line<Action>) line ->
                new RemovePlayerStatBoost(
                        line.getRequired(1, Stat.class),
                        line.getRequiredString("name|id")
                ));

        actions.register("CLEAR_PLAYER_STAT_BOOSTS", (Line<Action>) line ->
                new ClearPlayerStatBoosts(line.getRequired(1, Stat.class)));

        actions.register("ADD_PLAYER_ITEM", (Line<Action>) line ->
                new GiveItemToPlayer(line.getRequired(ItemDrop.class)));

        EconomyHook economy = Server.getEconomyHook();
        actions.register("ADD_PLAYER_MONEY", (Line<Action>) line -> {
            if (economy == null) {
                ConfigRoot root = line.getRoot();
                root.collectWarning(new InvalidConfigException(line, "Vault or economy plugin is not installed"));
                return Action.EMPTY;
            }
            return new GiveMoneyToPlayer(economy, line.getRequired(1, Amount.class));
        });

        actions.register("REMOVE_PLAYER_MONEY", (Line<Action>) line -> {
            if (economy == null) {
                ConfigRoot root = line.getRoot();
                root.collectWarning(new InvalidConfigException(line, "Vault or economy plugin is not installed"));
                return Action.EMPTY;
            }
            return new TakeMoneyFromPlayer(economy, line.getRequired(1, Amount.class));
        });

        actions.register("REMOVE_PLAYER_EXP", (Line<Action>) line ->
                new TakeExpFromPlayer(line.getRequired(1, Amount.class)));

        actions.register("REMOVE_PLAYER_FLAG", (Line<Action>) line ->
                new RemovePlayerFlag(line.getRequiredString(1)));

        actions.register("REMOVE_PLAYER_ITEM", (Line<Action>) line -> {
            ItemStack item = line.getRequired(1, ItemStack.class);
            Amount amount = line.get("amount", Amount.class)
                    .withDefault(Amount.fixed(item.getAmount()));
            return new RemoveItemFromPlayer(item, amount);
        });

        actions.register("SET_PLAYER_EXP", (Line<Action>) line ->
                new SetPlayerExp(line.getRequired(1, Amount.class)));

        actions.register("HEAL_PLAYER", (Line<Action>) line ->
                new HealPlayer(line.get(1, Amount.class).orElse(Amount.fixed(20))));

        actions.register("DAMAGE_PLAYER", (Line<Action>) line ->
                new DamagePlayer(line.get(1, Amount.class).orElse(Amount.fixed(2))));

        actions.register("EXECUTE_COMMAND_AS_PLAYER", (Line<Action>) line ->
                new ExecutePlayerCommand(line.getRequiredString(1)));

        actions.register("SEND_CHAT_TO_PLAYER", (Line<Action>) line ->
                new SendChatToPlayer(line.getRequiredString(1)));

        actions.register("SEND_ACTIONBAR_TO_PLAYER", (Line<Action>) line ->
                new SendActionbarToPlayer(plugin,
                        line.getRequiredString(1),
                        line.get("align", BarAlignment.class).withDefault(plugin.getSettings().getInsertedMessageAlign())
                ));

        actions.register("SEND_TITLE_TO_PLAYER", (Line<Action>) line ->
                new SendTitleToPlayer(plugin,
                        line.getRequiredString(1),
                        line.getString("subtitle").withDefault(""),
                        line.getInteger("fadeIn|fade-in").withDefault(10),
                        line.getInteger("stay").withDefault(70),
                        line.getInteger("fadeOut|fade-out").withDefault(20)
                ));

        actions.register("SEND_HOLOGRAM_TO_PLAYER", (Line<Action>) line ->
                SendHologramToPlayer.create(plugin,
                        line.getRequiredString(1),
                        line.get("duration", Delay.class).withDefault(Delay.fromTicks(40)),
                        line.getDouble("offsetX").withDefault(0d),
                        line.getDouble("offsetY").withDefault(0d),
                        line.getDouble("offsetZ").withDefault(0d),
                        line.getDouble("radiusX|rangeX").withDefault(null),
                        line.getDouble("radiusY|rangeY").withDefault(null),
                        line.getDouble("radiusZ|rangeZ").withDefault(null)
                ));

        actions.register("SEND_MESSAGE_TO_PLAYER", (Line<Action>) line ->
                new SendMessage(line.getRequired(1, Message.class), line.getAllFlags()));

        actions.register("LIGHTNING_AT_PLAYER", (Line<Action>) line ->
                new StrikePlayerWithLightning(line.getBoolean("doDamage|damage").orElse(true)));

        actions.register("STRIKE_PLAYER", (Line<Action>) line ->
                new StrikePlayerWithLightning(line.getBoolean("doDamage|damage").orElse(true)));

        actions.register("SET_PLAYER_FLIGHT", (Line<Action>) line ->
                new SetPlayerFlight(line.getBoolean(1).orElse(true)));

        actions.register("TELEPORT_PLAYER", (Line<Action>) line ->
                new TeleportPlayer(line.getRequired(1, Location.class)));

        actions.register("SET_CURSOR_ITEM", (Line<Action>) line ->
                new SetPlayerCursorItem(line.getRequired(1, ItemStack.class)));

        actions.register("OPEN_PLAYER_ENDERCHEST", (Line<Action>) line ->
                new OpenEnderChest());

        actions.register("CLOSE_PLAYER_INVENTORY", (Line<Action>) line ->
                new CloseInventory());

        actions.register("PLAYER_CACHE", (Line<Action>) line ->
                new CachePlayerValue(
                        line.getRequiredString(1),
                        line.getRequiredString(2)
                ));

        actions.register("ADD_PLAYER_COOLDOWN", (Line<Action>) line ->
                new AddPlayerCooldown(
                        line.getRequiredString(1),
                        line.getRequired("duration", Delay.class)
                ));

        actions.register("REMOVE_PLAYER_COOLDOWN", (Line<Action>) line ->
                new RemovePlayerCooldown(line.getRequiredString(1)));

        actions.register("ADD_PLAYER_MANA", (Line<Action>) line ->
                new AddPlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.register("REMOVE_PLAYER_MANA", (Line<Action>) line ->
                new RemovePlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.register("SET_PLAYER_MANA", (Line<Action>) line ->
                new SetPlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.register("TELEPORT_FORWARD", (Line<Action>) line ->
                new TeleportForward(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.registerAlias("DROP_ITEM_AT_PLAYER", "PLAYER_ITEM_DROP", "DROP_AT_PLAYER");
        actions.registerAlias("DROP_EXP_AT_PLAYER", "PLAYER_EXP_DROP");
        actions.registerAlias("SPAWN_PARTICLE_AT_PLAYER", "PLAYER_PARTICLE");
        actions.registerAlias("PLAY_SOUND_AT_PLAYER", "PLAYER_SOUND");

        actions.registerAlias("ADD_PLAYER_COOLDOWN", "START_PLAYER_COOLDOWN");
        actions.registerAlias("REMOVE_PLAYER_COOLDOWN", "STOP_PLAYER_COOLDOWN");

        actions.registerAlias("ADD_PLAYER_STAT_BOOST", "ADD_STAT_BOOST", "GIVE_PLAYER_STAT_BOOST", "GIVE_STAT_BOOST");
        actions.registerAlias("REMOVE_PLAYER_STAT_BOOST", "REMOVE_STAT_BOOST", "TAKE_PLAYER_STAT_BOOST", "TAKE_STAT_BOOST");

        actions.registerAlias("ADD_PLAYER_MONEY", "ADD_MONEY", "GIVE_PLAYER_MONEY", "GIVE_MONEY");
        actions.registerAlias("REMOVE_PLAYER_MONEY", "REMOVE_MONEY", "TAKE_PLAYER_MONEY", "TAKE_MONEY");

        actions.registerAlias("ADD_PLAYER_FLAG", "ADD_FLAG", "GIVE_PLAYER_FLAG", "GIVE_FLAG");
        actions.registerAlias("ADD_TEMPORARY_PLAYER_FLAG", "ADD_TEMPORARY_FLAG", "GIVE_TEMPORARY_PLAYER_FLAG", "GIVE_TEMPORARY_FLAG");
        actions.registerAlias("REMOVE_PLAYER_FLAG", "REMOVE_FLAG", "TAKE_PLAYER_FLAG", "TAKE_FLAG");

        actions.registerAlias("ADD_PLAYER_ITEM", "ADD_ITEM", "GIVE_PLAYER_ITEM", "GIVE_ITEM");
        actions.registerAlias("REMOVE_PLAYER_ITEM", "REMOVE_ITEM", "TAKE_PLAYER_ITEM", "TAKE_ITEM");

        actions.registerAlias("SET_PLAYER_EXP", "SET_EXP");
        actions.registerAlias("ADD_PLAYER_EXP", "ADD_EXP", "GIVE_PLAYER_EXP", "GIVE_EXP");
        actions.registerAlias("REMOVE_PLAYER_EXP", "REMOVE_EXP", "TAKE_PLAYER_EXP", "TAKE_EXP");

        actions.registerAlias("HEAL_PLAYER", "HEAL");
        actions.registerAlias("DAMAGE_PLAYER", "DAMAGE");

        actions.registerAlias("EXECUTE_COMMAND_AS_PLAYER", "EXECUTE_COMMAND", "COMMAND");

        actions.registerAlias("SEND_MESSAGE_TO_PLAYER", "SEND_MESSAGE", "MESSAGE");
        actions.registerAlias("SEND_CHAT_TO_PLAYER", "SEND_CHAT", "CHAT", "CHAT_MESSAGE");
        actions.registerAlias("SEND_ACTIONBAR_TO_PLAYER", "SEND_ACTION_BAR_TO_PLAYER", "SEND_ACTIONBAR", "SEND_ACTION_BAR", "ACTIONBAR", "ACTION_BAR", "ACTIONBAR_MESSAGE", "ACTION_BAR_MESSAGE");
        actions.registerAlias("SEND_TITLE_TO_PLAYER", "SEND_TITLE", "TITLE", "TITLE_MESSAGE");
        actions.registerAlias("SEND_HOLOGRAM_TO_PLAYER", "SEND_HOLOGRAM", "HOLOGRAM", "HOLOGRAM_MESSAGE");

        actions.registerAlias("SET_PLAYER_FLIGHT", "SET_FLIGHT", "FLIGHT", "FLY");
        actions.registerAlias("TELEPORT_PLAYER", "TELEPORT");
        actions.registerAlias("OPEN_PLAYER_ENDERCHEST", "OPEN_PLAYER_ENDER_CHEST", "OPEN_ENDERCHEST", "OPEN_ENDER_CHEST");
        actions.registerAlias("CLOSE_PLAYER_INVENTORY", "CLOSE_INVENTORY");

        actions.registerAlias("ADD_PLAYER_MANA", "ADD_MANA", "REGEN_PLAYER_MANA", "REGEN_MANA");
        actions.registerAlias("REMOVE_PLAYER_MANA", "REMOVE_MANA", "CONSUME_PLAYER_MANA", "CONSUME_MANA");
    }

}