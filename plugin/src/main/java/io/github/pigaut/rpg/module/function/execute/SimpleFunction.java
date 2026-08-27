package io.github.pigaut.rpg.module.function.execute;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class SimpleFunction implements Function {

    private final Action action;

    public SimpleFunction(@NotNull Action action) {
        this.action = action;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        FunctionResponse response = action.dispatch(context);

        ResponseType type = response.getType();
        if (type == ResponseType.ERROR) {
            EnhancedPlugin plugin = context.plugin();
            Player player = context.player();

            if (response instanceof FunctionError error) {
                plugin.getColoredLogger().severe("Failed to evaluate condition: " + error.getDetails());
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "Failed to evaluate condition: " + error.getDetails());
                }
            }
            else {
                plugin.getColoredLogger().severe("Failed to evaluate condition. Check your configuration.");
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "Failed to evaluate condition. Check your configuration.");
                }
            }
        }

        return response;
    }

}
