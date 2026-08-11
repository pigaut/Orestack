package io.github.pigaut.rpg.module.function.action;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.module.function.action.block.*;
import io.github.pigaut.rpg.module.function.action.event.*;
import io.github.pigaut.rpg.module.function.action.menu.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.function.action.mob.flag.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.action.player.ability.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.tool.*;
import io.github.pigaut.rpg.module.function.action.protagonist.*;
import io.github.pigaut.rpg.module.function.action.server.*;
import io.github.pigaut.rpg.module.function.action.system.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.hook.*;
import io.github.pigaut.rpg.module.function.action.block.*;
import io.github.pigaut.rpg.module.function.action.event.*;
import io.github.pigaut.rpg.module.function.action.menu.*;
import io.github.pigaut.rpg.module.function.action.mob.*;
import io.github.pigaut.rpg.module.function.action.mob.flag.*;
import io.github.pigaut.rpg.module.function.action.player.*;
import io.github.pigaut.rpg.module.function.action.player.ability.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.tool.*;
import io.github.pigaut.rpg.module.function.action.protagonist.*;
import io.github.pigaut.rpg.module.function.action.server.*;
import io.github.pigaut.rpg.module.function.action.system.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ActionLoader extends AbstractLoader<DispatchableAction> {

    private final EnhancedPlugin plugin;

    public ActionLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;

        // Server actions start
        addLoader("BROADCAST", (Line<Action>) line ->
                new ServerBroadcast(plugin, line.getRequiredString(1)));

        addLoader("LIGHTNING", (Line<Action>) line ->
                new StrikeLightning(
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z"),
                        line.getBoolean("doDamage|damage").withDefault(true)
                ));

        addLoader("CONSOLE_COMMAND", (Line<Action>) line ->
                new ExecuteConsoleCommand(line.getRequiredString(1)));

        addLoader("DROP_ITEM", (Line<Action>) line -> {
            ItemDrop itemDrop = line.getRequired(ItemDrop.class);

            ItemDropTarget dropTarget = line.get("target", ItemDropTarget.class)
                    .withDefault(plugin.getSettings().getDefaultItemDropTarget());

            return switch (dropTarget) {
                case BLOCK -> new DropItemAtBlock(itemDrop);
                case PLAYER -> new DropItemAtPlayer(itemDrop);
                case COORDS -> new DropItemAtCoords(itemDrop,
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                );
            };
        });

        addLoader("DROP_ITEM_AT_COORDS", (Line<Action>) line ->
                new DropItemAtCoords(
                        line.getRequired(ItemDrop.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        addLoader("DROP_EXP", (Line<Action>) line ->
                new DropExp(plugin,
                        line.getRequired(1, Amount.class),
                        line.get("orbs|orbCount", Amount.class).withDefault(null),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z"),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        addLoader("SPAWN_PARTICLE", (Line<Action>) line ->
                new SpawnParticle(
                        line.getRequired(1, ParticleEffect.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        addLoader("PARTICLE", (Line<Action>) line ->
                new SpawnParticle(
                        line.getRequired(1, ParticleEffect.class),
                        line.get("world", World.class).withDefault(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        addLoader("PLAY_SOUND", (Line<Action>) line ->
                new PlaySound(
                        line.getRequired(1, SoundEffect.class),
                        line.get("world", World.class).orElse(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));

        addLoader("SOUND", (Line<Action>) line ->
                new PlaySound(
                        line.getRequired(1, SoundEffect.class),
                        line.get("world", World.class).orElse(Server.getDefaultWorld()),
                        line.getRequiredDouble("x"),
                        line.getRequiredDouble("y"),
                        line.getRequiredDouble("z")
                ));
        // Server actions end


        // Function actions start
        addLoader("RETURN", (Line<DispatchableAction>) line -> {
            String returnValue = line.getString(1).orElse(null);
            if (returnValue != null) {
                Object parsedValue = ParseUtil.parseAsScalar(returnValue);
                return new ReturnValueAction(parsedValue);
            }
            return new ReturnAction();
        });

        addLoader("STOP", (Line<DispatchableAction>) line ->
                new StopAction());

        addLoader("GOTO", (Line<DispatchableAction>) line ->
                new GotoAction(line.getInteger(1)
                        .require(Requirements.positive(), "Value must be greater than or equal to 1")
                        .orThrow() - 1
                ));


        // Event actions start
        addLoader("CANCEL_EVENT", (Line<Action>) line ->
                new CancelEventAction(true));

        addLoader("CANCEL", (Line<Action>) line ->
                new CancelEventAction(true));

        addLoader("SET_CANCELLED", (Line<Action>) line ->
                new CancelEventAction(line.getRequiredBoolean(1)));


        // Block Actions start
        addLoader("STRIKE_BLOCK", (Line<Action>) line ->
                new StrikeBlockWithLightning(line.getBoolean("doDamage|damage").withDefault(true)));

        addLoader("DROP_ITEM_AT_BLOCK", (Line<Action>) line ->
                new DropItemAtBlock(line.getRequired(ItemDrop.class)));

        addLoader("DROP_EXP_AT_BLOCK", (Line<Action>) line ->
                new DropExpAtBlock(plugin,
                        line.getRequired(1, Amount.class),
                        line.get("orbs|orbCount", Amount.class).withDefault(null),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        addLoader("SPAWN_PARTICLE_AT_BLOCK", (Line<Action>) line ->
                new SpawnParticleAtBlock(line.getRequired(1, ParticleEffect.class)));

        addLoader("PLAY_SOUND_AT_BLOCK", (Line<Action>) line ->
                new PlaySoundAtBlock(line.getRequired(1, SoundEffect.class)));

        addAliases("DROP_ITEM_AT_BLOCK", "BLOCK_ITEM_DROP");
        addAliases("DROP_EXP_AT_BLOCK", "BLOCK_EXP_DROP");
        addAliases("SPAWN_PARTICLE_AT_BLOCK", "BLOCK_PARTICLE");
        addAliases("PLAY_SOUND_AT_BLOCK", "BLOCK_SOUND");

        // Block Actions end

        // Player actions start
        addLoader("DROP_ITEM_AT_PLAYER", (Line<Action>) line ->
                new DropItemAtPlayer(line.getRequired(ItemDrop.class)));

        addLoader("DROP_EXP_AT_PLAYER", (Line<Action>) line ->
                new DropExpAtPlayer(plugin,
                        line.getRequired(1, Amount.class),
                        line.get("orbs|orbCount", Amount.class).withDefault(null),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        addLoader("SPAWN_PARTICLE_AT_PLAYER", (Line<Action>) line ->
                new SpawnParticleAtPlayer(line.getRequired(1, ParticleEffect.class)));

        addLoader("PLAY_SOUND_AT_PLAYER", (Line<Action>) line ->
                new PlaySoundOnPlayer(line.getRequired(1, SoundEffect.class)));

        addLoader("ADD_PLAYER_EXP", (Line<Action>) line ->
                new GiveExpToPlayer(plugin,
                        line.getRequired(1, Amount.class),
                        line.getBoolean("experience").withDefault(plugin.getSettings().isExperience())
                ));

        addLoader("ADD_PLAYER_FLAG", (Line<Action>) line ->
                new AddPlayerFlag(line.getRequiredString(1)));

        addLoader("ADD_TEMPORARY_PLAYER_FLAG", (Line<Action>) line ->
                new AddTemporaryPlayerFlag(
                        line.getRequiredString(1),
                        line.get("duration", Delay.class).mapIfValid(Delay::toTicks).orThrow()
                ));

        addLoader("ADD_PLAYER_STAT_BOOST", (Line<Action>) line ->
                new AddPlayerStatBoost(plugin,
                        line.getRequired(1, Stat.class),
                        line.getRequired("amount", StatModifier.class),
                        line.getRequired("duration", Delay.class),
                        line.getString("name|id").withDefault(null)
                        ));

        addLoader("REMOVE_PLAYER_STAT_BOOST", (Line<Action>) line ->
                new RemovePlayerStatBoost(
                        line.getRequired(1, Stat.class),
                        line.getRequiredString("name|id")
                ));

        addLoader("CLEAR_PLAYER_STAT_BOOSTS", (Line<Action>) line ->
                new ClearPlayerStatBoosts(line.getRequired(1, Stat.class)));

        addAliases("ADD_PLAYER_STAT_BOOST", "ADD_STAT_BOOST", "GIVE_PLAYER_STAT_BOOST", "GIVE_STAT_BOOST");
        addAliases("REMOVE_PLAYER_STAT_BOOST", "REMOVE_STAT_BOOST", "TAKE_PLAYER_STAT_BOOST", "TAKE_STAT_BOOST");
        addAliases("CLEAR_PLAYER_STAT_BOOSTS", "CLEAR_STAT_BOOSTS");

        addLoader("ADD_PLAYER_ITEM", (Line<Action>) line ->
                new GiveItemToPlayer(line.getRequired(ItemDrop.class)));

        EconomyHook economy = Server.getEconomyHook();
        addLoader("ADD_PLAYER_MONEY", (Line<Action>) line -> {
            if (economy == null) {
                ConfigRoot root = line.getRoot();
                root.collectWarning(new InvalidConfigException(line, "Vault or an economy plugin is not installed"));
                return Action.EMPTY;
            }
            return new GiveMoneyToPlayer(economy, line.getRequired(1, Amount.class));
        });

        addLoader("REMOVE_PLAYER_MONEY", (Line<Action>) line -> {
            if (economy == null) {
                ConfigRoot root = line.getRoot();
                root.collectWarning(new InvalidConfigException(line, "Vault or an economy plugin is not installed"));
                return Action.EMPTY;
            }
            return new TakeMoneyFromPlayer(economy, line.getRequired(1, Amount.class));
        });

        addLoader("REMOVE_PLAYER_EXP", (Line<Action>) line ->
                new TakeExpFromPlayer(line.getRequired(1, Amount.class)));

        addLoader("REMOVE_PLAYER_FLAG", (Line<Action>) line ->
                new RemovePlayerFlag(line.getRequiredString(1)));

        addLoader("REMOVE_PLAYER_ITEM", (Line<Action>) line -> {
            ItemStack item = line.getRequired(1, ItemStack.class);
            Amount amount = line.get("amount", Amount.class)
                    .withDefault(Amount.fixed(item.getAmount()));
            return new TakeItemFromPlayer(item, amount);
        });

        addLoader("SET_PLAYER_EXP", (Line<Action>) line ->
                new SetPlayerExp(line.getRequired(1, Amount.class)));

        addLoader("HEAL_PLAYER", (Line<Action>) line ->
                new HealPlayer(line.get(1, Amount.class).orElse(Amount.fixed(20))));

        addLoader("DAMAGE_PLAYER", (Line<Action>) line ->
                new DamagePlayer(line.get(1, Amount.class).orElse(Amount.fixed(2))));

        addLoader("EXECUTE_PLAYER_COMMAND", (Line<Action>) line ->
                new ExecutePlayerCommand(line.getRequiredString(1)));

        addLoader("SEND_PLAYER_CHAT", (Line<Action>) line ->
                new SendChatToPlayer(line.getRequiredString(1)));

        addLoader("SEND_PLAYER_ACTIONBAR", (Line<Action>) line ->
                new SendActionbarToPlayer(plugin,
                        line.getRequiredString(1),
                        line.get("align", BarAlignment.class).withDefault(plugin.getSettings().getInsertedMessageAlign())
                ));

        addLoader("SEND_PLAYER_TITLE", (Line<Action>) line ->
                new SendTitleToPlayer(plugin,
                        line.getRequiredString(1),
                        line.getString("subtitle").withDefault(""),
                        line.getInteger("fadeIn|fade-in").withDefault(10),
                        line.getInteger("stay").withDefault(70),
                        line.getInteger("fadeOut|fade-out").withDefault(20)
                ));

        addLoader("SEND_PLAYER_HOLOGRAM", (Line<Action>) line ->
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

        addLoader("SEND_PLAYER_MESSAGE", (Line<Action>) line ->
                new SendMessage(line.getRequired(1, Message.class), line.getAllFlags()));

        addLoader("LIGHTNING_AT_PLAYER", (Line<Action>) line ->
                new StrikePlayerWithLightning(line.getBoolean("doDamage|damage").orElse(true)));

        addLoader("STRIKE_PLAYER", (Line<Action>) line ->
                new StrikePlayerWithLightning(line.getBoolean("doDamage|damage").orElse(true)));

        addLoader("SET_PLAYER_FLIGHT", (Line<Action>) line ->
                new SetPlayerFlight(line.getBoolean(1).orElse(true)));

        addLoader("TELEPORT_PLAYER", (Line<Action>) line ->
                new TeleportPlayer(line.getRequired(1, Location.class)));

        addLoader("SET_CURSOR_ITEM", (Line<Action>) line ->
                new SetPlayerCursorItem(line.getRequired(1, ItemStack.class)));

        addLoader("OPEN_PLAYER_ENDERCHEST", (Line<Action>) line ->
                new OpenEnderChest());

        addLoader("CLOSE_PLAYER_INVENTORY", (Line<Action>) line ->
                new CloseInventory());

        addLoader("PLAYER_CACHE", (Line<Action>) line ->
                new CachePlayerValue(
                        line.getRequiredString(1),
                        line.getRequiredString(2)
                ));

        addAliases("DROP_ITEM_AT_PLAYER", "PLAYER_ITEM_DROP");
        addAliases("DROP_EXP_AT_PLAYER", "PLAYER_EXP_DROP");
        addAliases("SPAWN_PARTICLE_AT_PLAYER", "PLAYER_PARTICLE");
        addAliases("PLAY_SOUND_AT_PLAYER", "PLAYER_SOUND");

        addAliases("ADD_PLAYER_MONEY", "ADD_MONEY", "GIVE_PLAYER_MONEY", "GIVE_MONEY");
        addAliases("REMOVE_PLAYER_MONEY", "REMOVE_MONEY", "TAKE_PLAYER_MONEY", "TAKE_MONEY");

        addAliases("ADD_PLAYER_FLAG", "ADD_FLAG", "GIVE_PLAYER_FLAG",  "GIVE_FLAG");
        addAliases("ADD_TEMPORARY_PLAYER_FLAG", "ADD_TEMPORARY_FLAG", "GIVE_TEMPORARY_PLAYER_FLAG", "GIVE_TEMPORARY_FLAG");
        addAliases("REMOVE_PLAYER_FLAG", "REMOVE_FLAG", "TAKE_PLAYER_FLAG", "TAKE_FLAG");

        addAliases("ADD_PLAYER_ITEM", "ADD_ITEM", "GIVE_PLAYER_ITEM", "GIVE_ITEM");
        addAliases("REMOVE_PLAYER_ITEM", "REMOVE_ITEM", "TAKE_PLAYER_ITEM", "TAKE_ITEM");

        addAliases("SET_PLAYER_EXP", "SET_EXP");
        addAliases("ADD_PLAYER_EXP", "ADD_EXP", "GIVE_PLAYER_EXP", "GIVE_EXP");
        addAliases("REMOVE_PLAYER_EXP", "REMOVE_EXP", "TAKE_PLAYER_EXP", "TAKE_EXP");

        addAliases("HEAL_PLAYER", "HEAL");
        addAliases("DAMAGE_PLAYER", "DAMAGE");

        addAliases("EXECUTE_PLAYER_COMMAND", "EXECUTE_COMMAND", "COMMAND");

        addAliases("SEND_PLAYER_MESSAGE", "SEND_MESSAGE", "MESSAGE");
        addAliases("SEND_PLAYER_CHAT", "SEND_CHAT", "CHAT", "CHAT_MESSAGE");
        addAliases("SEND_PLAYER_ACTIONBAR", "SEND_PLAYER_ACTION_BAR", "SEND_ACTIONBAR", "SEND_ACTION_BAR", "ACTIONBAR", "ACTION_BAR", "ACTIONBAR_MESSAGE", "ACTION_BAR_MESSAGE");
        addAliases("SEND_PLAYER_TITLE", "SEND_TITLE", "TITLE", "TITLE_MESSAGE");
        addAliases("SEND_PLAYER_HOLOGRAM", "SEND_HOLOGRAM", "HOLOGRAM", "HOLOGRAM_MESSAGE");

        addAliases("SET_PLAYER_FLIGHT", "SET_FLIGHT", "FLIGHT", "FLY");
        addAliases("TELEPORT_PLAYER", "TELEPORT");
        addAliases("OPEN_PLAYER_ENDERCHEST", "OPEN_PLAYER_ENDER_CHEST", "OPEN_ENDERCHEST", "OPEN_ENDER_CHEST");
        addAliases("CLOSE_PLAYER_INVENTORY", "CLOSE_INVENTORY");

        // Item actions start
        addLoader("DAMAGE_TOOL", (Line<Action>) line ->
                new DamageTool(line.get(1, Amount.class).withDefault(Amount.ONE)));

        addLoader("GRANT_TOOL_USES", (Line<Action>) line ->
                new GrantToolUses(plugin, line.get(1, Amount.class).withDefault(Amount.ONE)));

        addLoader("RESTORE_TOOL_USES", (Line<Action>) line ->
                new RestoreToolUses(plugin));

        addLoader("CONSUME_TOOL_USES", (Line<Action>) line ->
                new ConsumeToolUses(plugin, line.get(1, Amount.class).withDefault(Amount.ONE)));
        // Item actions end


        // Menu actions start
        addLoader("OPEN_MENU", (Line<Action>) line -> {
            String menuName = line.getRequiredString(1);
            return new OpenMenu(plugin, menuName);
        });

        addLoader("CLOSE_MENU", (Line<Action>) line ->
                new CloseMenu());

        addLoader("OPEN_PREVIOUS_MENU", (Line<Action>) line ->
                new OpenPreviousMenu());

        addLoader("OPEN_FIRST_MENU", (Line<Action>) line ->
                new OpenFirstMenu());

        addLoader("NEXT_MENU_PAGE", (Line<Action>) line ->
                new NextMenuPage());

        addLoader("PREVIOUS_MENU_PAGE", (Line<Action>) line ->
                new NextMenuPage());

        addLoader("SCROLL_MENU", (Line<Action>) line ->
                new ScrollAtlasMenu(line.getRequired(1, ScrollDirection.class),
                        line.getInteger("amount").withDefault(1)));

        addLoader("SCROLL_MENU_RIGHT", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT, line.getInteger(1).withDefault(1)));

        addLoader("SCROLL_MENU_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.DOWN, line.getInteger(1).withDefault(1)));

        addLoader("SCROLL_MENU_LEFT", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT, line.getInteger(1).withDefault(1)));

        addLoader("SCROLL_MENU_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.UP, line.getInteger(1).withDefault(1)));

        addLoader("SCROLL_MENU_RIGHT_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT_UP, line.getInteger(1).withDefault(1)));

        addLoader("SCROLL_MENU_RIGHT_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.RIGHT_DOWN, line.getInteger(1).withDefault(1)));

        addLoader("SCROLL_MENU_LEFT_DOWN", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT_DOWN, line.getInteger(1).withDefault(1)));

        addLoader("SCROLL_MENU_LEFT_UP", (Line<Action>) line ->
                new ScrollAtlasMenu(ScrollDirection.LEFT_UP, line.getInteger(1).withDefault(1)));
        // Menu actions end


        // Recipe actions start
        addLoader("UNLOCK_RECIPE", (Line<Action>) line -> {
            List<RecipeTemplate> recipes = line.getAll(1, RecipeTemplate.class)
                    .requireEach(RecipeTemplate::isUnlockable, "Cannot unlock global recipes")
                    .orThrow();
            return new UnlockRecipes(recipes);
        });

        addLoader("LOCK_RECIPE", (Line<Action>) line -> {
            List<RecipeTemplate> recipes = line.getAll(1, RecipeTemplate.class)
                    .requireEach(RecipeTemplate::isUnlockable, "Cannot lock global recipes")
                    .orThrow();
            return new LockRecipes(recipes);
        });
        // Recipe actions end


        // Mob actions start
        addLoader("ADD_MOB_FLAG", (Line<Action>) line ->
                new AddMobFlag(line.getRequiredString(1)));

        addLoader("ADD_TEMPORARY_MOB_FLAG", (Line<Action>) line ->
                new AddTemporaryMobFlag(
                        line.getRequiredString(1),
                        line.get("duration", Delay.class).mapIfValid(Delay::toTicks).orThrow()
                ));

        addLoader("REMOVE_MOB_FLAG", (Line<Action>) line ->
                new RemoveMobFlag(line.getRequiredString(1)));

        addLoader("HEAL_MOB", (Line<Action>) line ->
                new HealMob(line.get(1, Amount.class).withDefault(null)));

        addLoader("DAMAGE_MOB_TARGET", (Line<Action>) line ->
                new MobDamageEnemy(line.get(1, Amount.class).withDefault(Amount.ONE)));

        addLoader("DAMAGE_ATTACKERS", (Line<Action>) line ->
                new DamageMobAttackers(line.get(1, Amount.class).withDefault(Amount.ONE)));

        addLoader("DROP_ITEM_AT_MOB", (Line<Action>) line ->
                new DropItemAtMob(line.getRequired(ItemDrop.class)));

        addLoader("DROP_ITEM_AT_LAST_DAMAGER", (Line<Action>) line ->
                new DropItemAtLastDamager(line.getRequired(ItemDrop.class)));

        addLoader("DROP_ITEM_AT_TOP_DAMAGER", (Line<Action>) line ->
                new DropItemAtTopDamager(line.getRequired(ItemDrop.class)));

        addLoader("DROP_ITEM_AT_ATTACKERS", (Line<Action>) line ->
                new DropItemAtDamagers(line.getRequired(ItemDrop.class)));

        addLoader("SPAWN_PARTICLE_AT_MOB", (Line<Action>) line ->
                new SpawnParticleAtMob(line.getRequired(1, ParticleEffect.class)));

        addLoader("PLAY_SOUND_AT_MOB", (Line<Action>) line ->
                new PlaySoundAtMob(line.getRequired(1, SoundEffect.class)));

        // Protagonist
        addLoader("SPAWN_PARTICLE_AT_PROTAGONIST", (Line<Action>) line ->
                new SpawnParticleAtProtagonist(line.getRequired(1, ParticleEffect.class)));

        addLoader("PLAY_SOUND_AT_PROTAGONIST", (Line<Action>) line ->
                new PlaySoundAtProtagonist(line.getRequired(1, SoundEffect.class)));

        addLoader("DAMAGE_ENEMY", (Line<Action>) line ->
                new DamageEnemy(plugin, line.get(1, Amount.class).withDefault(Amount.ONE)));

        addAliases("DAMAGE_ENEMY", "DAMAGE_ENTITY", "DAMAGE_TARGET", "DAMAGE_VICTIM");

        // Cooldowns and abilities
        addLoader("START_COOLDOWN", (Line<Action>) line ->
                new AddCooldown(
                        line.getRequiredString(1),
                        line.getRequired("duration", Delay.class)
                ));

        addLoader("REMOVE_COOLDOWN", (Line<Action>) line ->
                new RemoveCooldown(line.getRequiredString(1)));

        addLoader("CONSUME_MANA", (Line<Action>) line ->
                new ConsumePlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        addLoader("REGEN_MANA", (Line<Action>) line ->
                new RegenPlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        addLoader("SET_MANA", (Line<Action>) line ->
                new SetPlayerMana(line.get(1, Amount.class).withDefault(Amount.ONE)));

        addLoader("TELEPORT_FORWARD", (Line<Action>) line ->
                new TeleportForward(line.get(1, Amount.class).withDefault(Amount.ONE)));

        // Drop action aliases
        addAliases("DROP_ITEM", "DROP");
        addAliases("DROP_ITEM_AT_BLOCK", "DROP_AT_BLOCK");
        addAliases("DROP_ITEM_AT_PLAYER", "DROP_AT_PLAYER");
        addAliases("DROP_ITEM_AT_COORDS", "DROP_AT_COORDS");

        // Recipe action aliases
        addAliases("UNLOCK_RECIPE", "UNLOCK_RECIPES");
        addAliases("LOCK_RECIPE", "LOCK_RECIPES");

        // Mob action aliases
        addAliases("DAMAGE_MOB_TARGET", "DAMAGE_MOB_ENEMY", "DAMAGE_MOB_VICTIM");
        addAliases("DROP_ITEM_AT_MOB", "DROP_AT_MOB");
        addAliases("DROP_ITEM_AT_LAST_DAMAGER", "DROP_AT_LAST_DAMAGER");
        addAliases("DROP_ITEM_AT_TOP_DAMAGER", "DROP_AT_TOP_DAMAGER");
        addAliases("DROP_ITEM_AT_ATTACKERS", "DROP_AT_DAMAGERS");

        // Menu action aliases
        addAliases("SCROLL_MENU", "SCROLL");
        addAliases("SCROLL_MENU_RIGHT", "SCROLL_RIGHT");
        addAliases("SCROLL_MENU_DOWN", "SCROLL_DOWN");
        addAliases("SCROLL_MENU_LEFT", "SCROLL_LEFT");
        addAliases("SCROLL_MENU_UP", "SCROLL_UP");
        addAliases("SCROLL_MENU_RIGHT_UP", "SCROLL_RIGHT_UP");
        addAliases("SCROLL_MENU_RIGHT_DOWN", "SCROLL_RIGHT_DOWN");
        addAliases("SCROLL_MENU_LEFT_DOWN", "SCROLL_LEFT_DOWN");
        addAliases("SCROLL_MENU_LEFT_UP", "SCROLL_LEFT_UP");

    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid action";
    }

    @Override
    public @NotNull DispatchableAction loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        ConfigLine line = scalar.toLine();
        String actionId = line.getRequiredString(0);

        ConfigLoader<? extends DispatchableAction> loader = getLoader(actionId);
        if (loader == null) {
            throw new InvalidConfigException(line,
                    "Could not find action with name: " + CaseFormatter.toCamelCase(actionId));
        }

        DispatchableAction action = loader.loadFromScalar(scalar);

        Integer repetitions = line.getInteger("repeat|repetitions")
                .require(Requirements.positive())
                .withDefault(null);

        Integer interval = line.get("interval|period", Delay.class)
                .check(repetitions != null, "Repetitions must be set to use interval delay")
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (interval != null) {
            action = new PeriodicAction(plugin, action, interval, repetitions);
        } else if (repetitions != null) {
            action = new RepeatedAction(action, repetitions);
        }

        Integer delay = line.get("delay", Delay.class)
                .mapIfValid(Delay::toTicks)
                .withDefault(null);

        if (delay != null) {
            action = new DelayedAction(plugin, action, delay);
        }

        Double chance = line.getDouble("chance")
                .require(Requirements.between(0, 1))
                .withDefault(null);

        if (chance != null) {
            action = new ChanceAction(action, chance);
        }

        return action;
    }

    @Override
    public @NotNull DispatchableAction loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return new MultiAction(sequence.getAll(DispatchableAction.class).orThrow());
    }

}
