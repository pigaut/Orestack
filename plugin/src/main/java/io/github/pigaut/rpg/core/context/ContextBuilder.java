package io.github.pigaut.rpg.core.context;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ContextBuilder {

    private final EnhancedPlugin plugin;
    private Player player;
    private PlayerState playerState;
    private ItemStack tool;

    private Action action;
    private Block block;

    private Mob mob;
    private LivingEntity enemy;

    private ItemStack item;
    private EnchantLevel enchantAdded;

    private ClickType clickType;
    private CommandNode command;
    private Event event;

    private final Map<Class<?>, Object> instanceByClass = new HashMap<>();
    private final Map<String, Object> placeholders = new HashMap<>();

    private boolean playerProtagonist = true;

    public ContextBuilder(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    public ContextBuilder withPlayer(Player player) {
        this.player = player;
        return this;
    }

    public ContextBuilder withPlayerState(PlayerState playerState) {
        this.playerState = playerState;
        return this;
    }

    public ContextBuilder withAction(Action action) {
        this.action = action;
        return this;
    }

    public ContextBuilder withTool(ItemStack tool) {
        this.tool = tool;
        return this;
    }

    public ContextBuilder withBlock(Block block) {
        this.block = block;
        return this;
    }

    public ContextBuilder withMob(Mob mob) {
        this.mob = mob;
        return this;
    }

    public ContextBuilder withEnemy(LivingEntity enemy) {
        this.enemy = enemy;
        return this;
    }

    public ContextBuilder withItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ContextBuilder withEnchantAdded(EnchantLevel enchantAdded) {
        this.enchantAdded = enchantAdded;
        return this;
    }

    public ContextBuilder withClickType(ClickType clickType) {
        this.clickType = clickType;
        return this;
    }

    public ContextBuilder withCommand(CommandNode command) {
        this.command = command;
        return this;
    }

    public ContextBuilder withPlaceholder(@NotNull String id, Object value) {
        placeholders.put(id, value);
        return this;
    }

    public ContextBuilder withPlaceholders(@NotNull Map<String, Object> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public ContextBuilder withCommandArgs(CommandNode command, String[] args) {
        List<CommandParameter> parameters = command.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            CommandParameter parameter = parameters.get(i);
            String name = parameter.getName();
            String value = args.length > i ? args[i] : parameter.getDefaultValue();
            placeholders.put(name, value);
        }
        return this;
    }

    public ContextBuilder withEvent(Event event) {
        this.event = event;
        return this;
    }

    public <T> ContextBuilder with(@NotNull Class<T> type, @NotNull T instance) {
        instanceByClass.put(type, instance);
        return this;
    }

    public ContextBuilder withPlayerProtagonist() {
        this.playerProtagonist = true;
        return this;
    }

    public ContextBuilder withMobProtagonist() {
        this.playerProtagonist = false;
        return this;
    }

    public Context build() {
        return new Context(plugin, player, playerState, action, tool, block, mob,
                enemy, item, enchantAdded, clickType, command, event, instanceByClass,
                placeholders, playerProtagonist);
    }
}
