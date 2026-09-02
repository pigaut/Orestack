package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.function.action.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SendChatToPlayer implements Action.Executor {

    private final String message;

    public SendChatToPlayer(String message) {
        this.message = ColorUtil.translateColors(message);
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            String parsedMessage = PlaceholderUtil.parseAll(context, message);
            PlayerUtil.sendChat(player, parsedMessage);
        }
    }

}
