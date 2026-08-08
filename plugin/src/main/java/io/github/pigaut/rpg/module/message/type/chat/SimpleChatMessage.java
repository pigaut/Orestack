package io.github.pigaut.rpg.module.message.type.chat;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.message.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleChatMessage extends ChatMessage {

    private final List<String> messages;

    public SimpleChatMessage(String name, @Nullable String group, String message) {
        this(name, group, List.of(message));
    }

    public SimpleChatMessage(String name, @Nullable String group, List<String> messages) {
        super(name, group);
        this.messages = messages;
    }

    @Override
    public @NotNull MessageType getType() {
        return MessageType.CHAT;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return IconBuilder.of(Material.BOOK).buildIcon();
    }

    @Override
    public void send(@NotNull Player player, @NotNull Context context) {
        for (String parsedMessage : PlaceholderUtil.parseAll(context, messages)) {
            PlayerUtil.sendChat(player, parsedMessage);
        }
    }

}
