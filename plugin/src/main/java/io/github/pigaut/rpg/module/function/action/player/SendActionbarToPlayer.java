package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SendActionbarToPlayer implements Action.Executor {

    private final Message actionbar;

    public SendActionbarToPlayer(@NotNull EnhancedPlugin plugin, @NotNull String message, BarAlignment statusBarAlign) {
        this.actionbar = new ActionBarMessage(plugin, message, statusBarAlign);
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player != null) {
            actionbar.send(player, context);
        }
    }

}
