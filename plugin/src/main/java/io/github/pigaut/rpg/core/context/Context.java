package io.github.pigaut.rpg.core.context;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class Context {

    private static final ItemStack EMPTY_HAND = new ItemStack(Material.AIR);

    private final @NotNull EnhancedPlugin plugin;
    private final @Nullable Player player;
    private final @Nullable PlayerState playerState;
    private final @Nullable ItemStack tool;

    private final @Nullable Action action;
    private final @Nullable Block block;

    private final @Nullable Mob mob;
    private final @Nullable LivingEntity enemy;

    private final @Nullable ItemStack item;
    private final @Nullable EnchantLevel enchantAdded;

    private final @Nullable ClickType clickType;
    private final @Nullable CommandNode command;
    private final @Nullable Event event;

    private final Map<Class<?>, Object> instances;
    private final Map<String, Object> placeholders;

    // Whether the player or mob is the protagonist
    private final boolean playerProtagonist;

    public Context(@NotNull EnhancedPlugin plugin, @Nullable Player player, @Nullable PlayerState playerState, @Nullable Action action,
                   @Nullable ItemStack tool, @Nullable Block block, @Nullable Mob mob, @Nullable LivingEntity enemy,
                   @Nullable ItemStack item, @Nullable EnchantLevel enchantAdded,
                   @Nullable ClickType clickType, @Nullable CommandNode command, @Nullable Event event,
                   @NotNull Map<Class<?>, Object> instances, @NotNull Map<String, Object> placeholders, boolean playerProtagonist) {
        this.plugin = plugin;
        this.player = player;
        this.playerState = playerState;
        this.action = action;
        this.tool = tool == null ? EMPTY_HAND : tool;
        this.block = block;
        this.mob = mob;
        this.enemy = enemy;
        this.item = item;
        this.enchantAdded = enchantAdded;
        this.clickType = clickType;
        this.command = command;
        this.instances = instances;
        this.placeholders = placeholders;
        this.event = event;
        this.playerProtagonist = playerProtagonist;
    }

    public @Nullable String resolvePlaceholder(@NotNull String placeholder) {
        return plugin.resolvePlaceholder(placeholder, this);
    }

    @Nullable
    public Player player() {
        return player;
    }

    public @NotNull Context withPlayer(@Nullable Player player) {
        return new Context(plugin, player, player != null ? plugin.getPlayerState(player) : null,
                action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public PlayerState playerState() {
        return playerState;
    }

    @Nullable
    public PlayerData playerData() {
        return playerState != null ? playerState.getPlayerData() : null;
    }

    public @NotNull Context withPlayerState(@Nullable PlayerState playerState) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public Action action() {
        return action;
    }

    public @NotNull Context withAction(@Nullable Action action) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public ItemStack tool() {
        return tool != null ? tool.clone() : null;
    }

    public @NotNull Context withTool(@Nullable ItemStack tool) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public ItemStack offHand() {
        return player != null ? PlayerUtil.getOffHand(player) : null;
    }

    @Nullable
    public ItemStack helmet() {
        return player != null ? PlayerUtil.getHelmet(player) : null;
    }

    @Nullable
    public ItemStack chestplate() {
        return player != null ? PlayerUtil.getChestplate(player) : null;
    }

    @Nullable
    public ItemStack leggings() {
        return player != null ? PlayerUtil.getLeggings(player) : null;
    }

    @Nullable
    public ItemStack boots() {
        return player != null ? PlayerUtil.getBoots(player) : null;
    }

    @Nullable
    public Block block() {
        return block;
    }

    public @NotNull Context withBlock(@Nullable Block block) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public Mob mob() {
        return mob;
    }

    public @NotNull Context withMob(@Nullable Mob mob) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public LivingEntity enemy() {
        return enemy;
    }

    public @NotNull Context withEnemy(@Nullable LivingEntity enemy) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public ItemStack item() {
        return item != null ? item.clone() : null;
    }

    public @NotNull Context withItem(@Nullable ItemStack item) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public EnchantLevel enchantAdded() {
        return enchantAdded;
    }

    public @NotNull Context withEnchantAdded(@Nullable EnchantLevel enchantAdded) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    public @NotNull Context withForEachElement(@Nullable Object forEachElement) {
        if (forEachElement instanceof Player loopedPlayer) {
            return withPlayer(loopedPlayer);
        }
        else if (forEachElement instanceof EnchantLevel addedEnchant) {
            return withEnchantAdded(addedEnchant);
        }
        return this;
    }

    @Nullable
    public CommandNode command() {
        return command;
    }

    public @NotNull Context withCommand(@Nullable CommandNode command) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public Event event() {
        return event;
    }

    public @NotNull Context withEvent(@Nullable Event event) {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    @Nullable
    public <T> T get(@NotNull Class<T> type) {
        return type.cast(instances.get(type));
    }

    public @NotNull Context with(@NotNull Class<?> type, @NotNull Object value) {
        Map<Class<?>, Object> instances = new HashMap<>(this.instances);
        instances.put(type, value);
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    public boolean containsPlaceholder(@NotNull String placeholder) {
        return placeholders.containsKey(placeholder);
    }

    public @Nullable Object placeholder(@NotNull String id) {
        return placeholders.get(id);
    }

    public Context addPlaceholder(@NotNull String id, Object value) {
        this.placeholders.put(id, value);
        return this;
    }

    public Context addPlaceholders(@NotNull Map<String, Object> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public @NotNull Context withPlaceholder(@NotNull String id, Object value) {
        Map<String, Object> placeholders = new HashMap<>(this.placeholders);
        placeholders.put(id, value);
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    public @NotNull Context copy() {
        return new Context(plugin, player, playerState, action, tool, block, mob, enemy, item, enchantAdded, clickType, command, event, instances, placeholders, playerProtagonist);
    }

    public @Nullable LivingEntity protagonist() {
        return playerProtagonist ? player : mob != null ? mob.getEntity() : null;
    }

    public static ContextBuilder builder(@NotNull EnhancedPlugin plugin) {
        return new ContextBuilder(plugin);
    }

    public static @NotNull Context fromPlugin(@NotNull EnhancedPlugin plugin) {
        return builder(plugin).build();
    }

    public static @NotNull Context fromPlayer(@NotNull EnhancedPlugin plugin, @NotNull Player player) {
        return fromPlayer(plugin, player, plugin.getPlayerState(player));
    }

    public static @NotNull Context fromPlayer(@NotNull EnhancedPlugin plugin, @NotNull PlayerState playerState) {
        return fromPlayer(plugin, playerState.asPlayer(), playerState);
    }

    public static @NotNull Context fromPlayer(@NotNull EnhancedPlugin plugin, @NotNull Player player, @NotNull PlayerState playerState) {
        return builder(plugin)
                .withPlayer(player)
                .withPlayerState(playerState)
                .withTool(PlayerUtil.getTool(player))
                .build();
    }

    public static @NotNull Context fromBlock(@NotNull EnhancedPlugin plugin, @NotNull Block block) {
        return builder(plugin)
                .withBlock(block)
                .build();
    }

    public static @NotNull Context fromPlayerAndBlock(@NotNull EnhancedPlugin plugin, @NotNull Player player, Block block) {
        return fromPlayerAndBlock(plugin, player, block, null, null);
    }

    public static @NotNull Context fromPlayerAndBlock(@NotNull EnhancedPlugin plugin, @NotNull Player player, Block block, Action action) {
        return fromPlayerAndBlock(plugin, player, block, action, null);
    }

    public static @NotNull Context fromPlayerAndBlock(@NotNull EnhancedPlugin plugin, @NotNull Player player, Block block, Event event) {
        return fromPlayerAndBlock(plugin, player, block, null, event);
    }

    public static @NotNull Context fromPlayerAndBlock(@NotNull EnhancedPlugin plugin, @NotNull Player player, Block block, Action action, Event event) {
        return builder(plugin)
                .withPlayer(player)
                .withPlayerState(plugin.getPlayerState(player))
                .withTool(PlayerUtil.getTool(player))
                .withBlock(block)
                .withAction(action)
                .withEvent(event)
                .build();
    }

    public static @NotNull Context fromMob(@NotNull EnhancedPlugin plugin, @NotNull Mob mob) {
        return fromMob(plugin, mob, null);
    }

    public static @NotNull Context fromMob(@NotNull EnhancedPlugin plugin, @NotNull Mob mob, @Nullable Event event) {
        return builder(plugin)
                .withMob(mob)
                .withMobProtagonist()
                .withEvent(event)
                .build();
    }

    public static @NotNull Context fromMobAndEnemy(@NotNull EnhancedPlugin plugin, @NotNull Mob mob, @Nullable Entity enemy) {
        return fromMobAndEnemy(plugin, mob, enemy, null);
    }

    public static @NotNull Context fromMobAndEnemy(@NotNull EnhancedPlugin plugin, @NotNull Mob mob, @Nullable Entity enemy, @Nullable Event event) {
        return builder(plugin)
                .withMob(mob)
                .withMobProtagonist()
                .withEnemy(enemy instanceof LivingEntity livingEnemy ? livingEnemy : null)
                .withPlayer(enemy instanceof Player player ? player : null)
                .withPlayerState(enemy instanceof Player player ? plugin.getPlayerState(player) : null)
                .withTool(enemy instanceof Player player ? PlayerUtil.getTool(player) : null)
                .withEvent(event)
                .build();
    }

    public static @NotNull Context fromMobAndPlayer(@NotNull EnhancedPlugin plugin, @NotNull Mob mob, @Nullable Player player) {
        return builder(plugin)
                .withMob(mob)
                .withMobProtagonist()
                .withPlayer(player)
                .withPlayerState(player != null ? plugin.getPlayerState(player) : null)
                .withTool(player != null ? PlayerUtil.getTool(player) : null)
                .build();
    }

    public static @NotNull Context fromCommand(@NotNull EnhancedPlugin plugin, @NotNull CommandSender sender, @NotNull CommandNode command, @NotNull String[] args) {
        Player player = sender instanceof Player playerSender ? playerSender : null;
        return builder(plugin)
                .withPlayer(player)
                .withPlayerState(player != null ? plugin.getPlayerState(player) : null)
                .withCommand(command)
                .withCommandArgs(command, args)
                .build();
    }

}