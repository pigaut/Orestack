package io.github.pigaut.rpg.module.message.type;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiMessage implements Message {

    private final String name;
    private final String group;
    private final List<Message> messages;

    public MultiMessage(String name, String group, @NotNull List<@NotNull Message> messages) {
        this.name = name;
        this.group = group;
        this.messages = messages;
    }

    @Override
    public @NotNull MessageType getType() {
        return MessageType.MULTI;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return IconBuilder.of(Material.BOOKSHELF).buildIcon();
    }

    @Override
    public void send(@NotNull Player player, @NotNull Context context) {
        for (Message message : messages) {
            message.send(player, context);
        }
    }

}
