package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.impl.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.impl.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SendTitleToPlayer implements Action {

    private final Message title;

    public SendTitleToPlayer(EnhancedPlugin plugin, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = new TitleMessage(title, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            title.send(player, context);
        }
    }

}
