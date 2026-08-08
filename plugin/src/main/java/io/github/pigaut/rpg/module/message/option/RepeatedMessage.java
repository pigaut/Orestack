package io.github.pigaut.rpg.module.message.option;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class RepeatedMessage implements Message {

    private final Message message;
    private final int repetitions;

    public RepeatedMessage(Message message, int repetitions) {
        this.message = message;
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
        for (int i = 0; i < repetitions; i++) {
            message.send(player, context);
        }
    }

}
