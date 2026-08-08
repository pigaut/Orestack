package io.github.pigaut.rpg.module.message.option;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DelayedMessage implements Message {

    private final EnhancedPlugin plugin;
    private final Message message;
    private final int delay;

    public DelayedMessage(EnhancedPlugin plugin, Message message, int delay) {
        this.plugin = plugin;
        this.message = message;
        this.delay = delay;
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
        plugin.getScheduler().runTaskLater(delay, () -> {
            message.send(player, context);
        });
    }

}
