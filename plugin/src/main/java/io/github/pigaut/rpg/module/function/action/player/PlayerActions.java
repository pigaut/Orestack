package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.player.ability.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
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

        actions.addLoader("DROP_ITEM_AT_PLAYER", (Line<Action>) line ->
                new DropItemAtPlayer(line.getRequired(ItemDrop.class)));

        actions.addLoader("DROP_EXP_AT_PLAYER", (Line<Action>) line ->
                new DropExpAtPlayer(plugin,
                        line.getRequired(1, Amount.class),
                        line.get("orbs|orbCount", Amount.class).withDefault(null),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        actions.addLoader("SPAWN_PARTICLE_AT_PLAYER", (Line<Action>) line ->
                new SpawnParticleAtPlayer(line.getRequired(1, ParticleEffect.class)));

        actions.addLoader("PLAY_SOUND_AT_PLAYER", (Line<Action>) line ->
                new PlaySoundOnPlayer(line.getRequired(1, SoundEffect.class)));

        actions.addLoader("ADD_PLAYER_EXP", (Line<Action>) line ->
                new GiveExpToPlayer(plugin,
                        line.getRequired(1, Amount.class),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        actions.addLoader("ADD_PLAYER_FLAG", (Line<Action>) line ->
                new AddPlayerFlag(line.getRequiredString(1)));

        actions.addLoader("ADD_TEMPORARY_PLAYER_FLAG", (Line<Action>) line ->
                new AddTemporaryPlayerFlag(
                        line.getRequiredString(1),
                        line.get("duration", Delay.class).mapIfValid(Delay::toTicks).orThrow()
                ));

        actions.addLoader("ADD_PLAYER_STAT_BOOST", (Line<Action>) line ->
                new AddPlayerStatBoost(plugin,
                        line.getRequired(1, Stat.class),
                        line.getRequired("amount", StatModifier.class),
                        line.getRequired("duration", Delay.class),
                        line.getString("name|id").withDefault(null)
                ));

        actions.addLoader("REMOVE_PLAYER_STAT_BOOST", (Line<Action>) line ->
                new RemovePlayerStatBoost(
                        line.getRequired(1, Stat.class),
                        line.getRequiredString("name|id")
                ));

        actions.addLoader("CLEAR_PLAYER_STAT_BOOSTS", (Line<Action>) line ->
                new ClearPlayerStatBoosts(line.getRequired(1, Stat.class)));

        actions.addLoader("ADD_PLAYER_ITEM", (Line<Action>) line ->
                new GiveItemToPlayer(line.getRequired(ItemDrop.class)));

        EconomyHook economy = Server.getEconomyHook();
        actions.addLoader("ADD_PLAYER_MONEY", (Line<Action>) line -> {
            if (economy == null) {
                ConfigRoot root = line.getRoot();
                root.collectWarning(new InvalidConfigException(line, "Vault or economy plugin is not installed"));
                return Action.EMPTY;
            }
            return new GiveMoneyToPlayer(economy, line.getRequired(1, Amount.class));
        });

        actions.addLoader("REMOVE_PLAYER_MONEY", (Line<Action>) line -> {
            if (economy == null) {
                ConfigRoot root = line.getRoot();
                root.collectWarning(new InvalidConfigException(line, "Vault or economy plugin is not installed"));
                return Action.EMPTY;
            }
            return new TakeMoneyFromPlayer(economy, line.getRequired(1, Amount.class));
        });

        actions.addLoader("REMOVE_PLAYER_EXP", (Line<Action>) line ->
                new TakeExpFromPlayer(line.getRequired(1, Amount.class)));

        actions.addLoader("REMOVE_PLAYER_FLAG", (Line<Action>) line ->
                new RemovePlayerFlag(line.getRequiredString(1)));

        actions.addLoader("REMOVE_PLAYER_ITEM", (Line<Action>) line -> {
            ItemStack item = line.getRequired(1, ItemStack.class);
            Amount amount = line.get("amount", Amount.class)
                    .withDefault(Amount.fixed(item.getAmount()));
            return new RemovePlayerItem(item, amount);
        });

        actions.addLoader("SET_PLAYER_EXP", (Line<Action>) line ->
                new SetPlayerExp(line.getRequired(1, Amount.class)));

        actions.addLoader("HEAL_PLAYER", (Line<Action>) line ->
                new HealPlayer(line.get(1, Amount.class).orElse(Amount.fixed(20))));

        actions.addLoader("DAMAGE_PLAYER", (Line<Action>) line ->
                new DamagePlayer(line.get(1, Amount.class).orElse(Amount.fixed(2))));

        actions.addLoader("EXECUTE_COMMAND_AS_PLAYER", (Line<Action>) line ->
                new ExecutePlayerCommand(line.getRequiredString(1)));

        actions.addLoader("SEND_CHAT_TO_PLAYER", (Line<Action>) line ->
                new SendChatToPlayer(line.getRequiredString(1)));

        actions.addLoader("SEND_PLAYER_ACTIONBAR", (Line<Action>) line ->
                new SendActionbarToPlayer(plugin,
                        line.getRequiredString(1),
                        line.get("align", BarAlignment.class).withDefault(plugin.getSettings().getInsertedMessageAlign())
                ));

        actions.addLoader("SEND_TITLE_TO_PLAYER", (Line<Action>) line ->
                new SendTitleToPlayer(plugin,
                        line.getRequiredString(1),
                        line.getString("subtitle").withDefault(""),
                        line.getInteger("fadeIn|fade-in").withDefault(10),
                        line.getInteger("stay").withDefault(70),
                        line.getInteger("fadeOut|fade-out").withDefault(20)
                ));

        actions.addLoader("SEND_HOLOGRAM_TO_PLAYER", (Line<Action>) line ->
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

        actions.addLoader("SEND_MESSAGE_TO_PLAYER", (Line<Action>) line ->
                new SendMessage(line.getRequired(1, Message.class), line.getAllFlags()));

        actions.addLoader("LIGHTNING_AT_PLAYER", (Line<Action>) line ->
                new StrikePlayerWithLightning(line.getBoolean("doDamage|damage").orElse(true)));

        actions.addLoader("STRIKE_PLAYER", (Line<Action>) line ->
                new StrikePlayerWithLightning(line.getBoolean("doDamage|damage").orElse(true)));

        actions.addLoader("SET_PLAYER_FLIGHT", (Line<Action>) line ->
                new SetPlayerFlight(line.getBoolean(1).orElse(true)));

        actions.addLoader("TELEPORT_PLAYER", (Line<Action>) line ->
                new TeleportPlayer(line.getRequired(1, Location.class)));

        actions.addLoader("SET_CURSOR_ITEM", (Line<Action>) line ->
                new SetPlayerCursorItem(line.getRequired(1, ItemStack.class)));

        actions.addLoader("OPEN_PLAYER_ENDERCHEST", (Line<Action>) line ->
                new OpenEnderChest());

        actions.addLoader("CLOSE_PLAYER_INVENTORY", (Line<Action>) line ->
                new CloseInventory());

        actions.addLoader("PLAYER_CACHE", (Line<Action>) line ->
                new CachePlayerValue(
                        line.getRequiredString(1),
                        line.getRequiredString(2)
                ));

        actions.addLoader("START_COOLDOWN", (Line<Action>) line ->
                new StartPlayerCooldown(
                        line.getRequiredString(1),
                        line.getRequired("duration", Delay.class)
                ));

        actions.addLoader("REMOVE_COOLDOWN", (Line<Action>) line ->
                new RemoveCooldown(line.getRequiredString(1)));

        actions.addLoader("CONSUME_MANA", (Line<Action>) line ->
                new ConsumePlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("REGEN_MANA", (Line<Action>) line ->
                new RegenPlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("SET_MANA", (Line<Action>) line ->
                new SetPlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addLoader("TELEPORT_FORWARD", (Line<Action>) line ->
                new TeleportForward(line.get(1, Amount.class).withDefault(Amount.ONE)));

        actions.addAliases("DROP_ITEM_AT_PLAYER", "PLAYER_ITEM_DROP", "DROP_AT_PLAYER");
        actions.addAliases("DROP_EXP_AT_PLAYER", "PLAYER_EXP_DROP");
        actions.addAliases("SPAWN_PARTICLE_AT_PLAYER", "PLAYER_PARTICLE");
        actions.addAliases("PLAY_SOUND_AT_PLAYER", "PLAYER_SOUND");

        actions.addAliases("ADD_PLAYER_MONEY", "ADD_MONEY", "GIVE_PLAYER_MONEY", "GIVE_MONEY");
        actions.addAliases("REMOVE_PLAYER_MONEY", "REMOVE_MONEY", "TAKE_PLAYER_MONEY", "TAKE_MONEY");

        actions.addAliases("ADD_PLAYER_FLAG", "ADD_FLAG", "GIVE_PLAYER_FLAG", "GIVE_FLAG");
        actions.addAliases("ADD_TEMPORARY_PLAYER_FLAG", "ADD_TEMPORARY_FLAG", "GIVE_TEMPORARY_PLAYER_FLAG", "GIVE_TEMPORARY_FLAG");
        actions.addAliases("REMOVE_PLAYER_FLAG", "REMOVE_FLAG", "TAKE_PLAYER_FLAG", "TAKE_FLAG");

        actions.addAliases("ADD_PLAYER_ITEM", "ADD_ITEM", "GIVE_PLAYER_ITEM", "GIVE_ITEM");
        actions.addAliases("REMOVE_PLAYER_ITEM", "REMOVE_ITEM", "TAKE_PLAYER_ITEM", "TAKE_ITEM");

        actions.addAliases("SET_PLAYER_EXP", "SET_EXP");
        actions.addAliases("ADD_PLAYER_EXP", "ADD_EXP", "GIVE_PLAYER_EXP", "GIVE_EXP");
        actions.addAliases("REMOVE_PLAYER_EXP", "REMOVE_EXP", "TAKE_PLAYER_EXP", "TAKE_EXP");

        actions.addAliases("HEAL_PLAYER", "HEAL");
        actions.addAliases("DAMAGE_PLAYER", "DAMAGE");

        actions.addAliases("EXECUTE_COMMAND_AS_PLAYER", "EXECUTE_COMMAND", "COMMAND");

        actions.addAliases("SEND_MESSAGE_TO_PLAYER", "SEND_MESSAGE", "MESSAGE");
        actions.addAliases("SEND_CHAT_TO_PLAYER", "SEND_CHAT", "CHAT", "CHAT_MESSAGE");
        actions.addAliases("SEND_ACTIONBAR_TO_PLAYER", "SEND_PLAYER_ACTION_BAR", "SEND_ACTIONBAR", "SEND_ACTION_BAR", "ACTIONBAR", "ACTION_BAR", "ACTIONBAR_MESSAGE", "ACTION_BAR_MESSAGE");
        actions.addAliases("SEND_TITLE_TO_PLAYER", "SEND_TITLE", "TITLE", "TITLE_MESSAGE");
        actions.addAliases("SEND_HOLOGRAM_TO_PLAYER", "SEND_HOLOGRAM", "HOLOGRAM", "HOLOGRAM_MESSAGE");

        actions.addAliases("SET_PLAYER_FLIGHT", "SET_FLIGHT", "FLIGHT", "FLY");
        actions.addAliases("TELEPORT_PLAYER", "TELEPORT");
        actions.addAliases("OPEN_PLAYER_ENDERCHEST", "OPEN_PLAYER_ENDER_CHEST", "OPEN_ENDERCHEST", "OPEN_ENDER_CHEST");
        actions.addAliases("CLOSE_PLAYER_INVENTORY", "CLOSE_INVENTORY");
    }

}