package io.github.pigaut.rpg.module.message.type.chat;

import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.type.*;
import org.jetbrains.annotations.*;

public abstract class ChatMessage extends GenericMessage {

    public ChatMessage(String name, @Nullable String group) {
        super(name, group);
    }

    @Override
    public @NotNull MessageType getType() {
        return MessageType.CHAT;
    }

}
