package io.github.pigaut.rpg.module.message.impl;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PeriodicMessage implements Message {

    private final EnhancedPlugin plugin;
    private final Message message;
    private final int interval;
    private final int repetitions;

    public PeriodicMessage(EnhancedPlugin plugin, Message message, int interval, int repetitions) {
        this.plugin = plugin;
        this.message = message;
        this.interval = interval;
        this.repetitions = repetitions;
    }

    @Override
    public @NotNull String getName() {
        return message.getName();
    }

    @Override
    public @Nullable String getGroup() {
        return message.getGroup();
    }

    @Override
    public @NotNull MessageType getType() {
        return message.getType();
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return message.getIcon();
    }

    @Override
    public void send(@NotNull Player player, @NotNull Context context) {
        message.send(player, context);
        for (int i = 1; i < repetitions; i++) {
            final long delay = (long) interval * i;
            plugin.getScheduler().runTaskLater(delay, () -> message.send(player, context));
        }
    }

}
