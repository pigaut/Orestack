package io.github.pigaut.rpg.module.function.execute;

import com.ssomar.score.features.types.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class ConditionalFunction implements Function {

    private final Condition condition;
    private final Function success;
    private final Function failure;

    public ConditionalFunction(@NotNull Condition condition, @NotNull Function success, @NotNull Function failure) {
        this.condition = condition;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public @NotNull FunctionResponse dispatch(@NotNull Context context) {
        FunctionResponse response = condition.evaluate(context);

        ResponseType type = response.getType();
        if (type == ResponseType.ERROR) {
            EnhancedPlugin plugin = context.plugin();
            Player player = context.player();

            if (response instanceof FunctionError error) {
                plugin.getColoredLogger().severe("Failed to evaluate condition: " + error.getDetails());
                if (player != null) {
                    player.sendMessage(ChatColor.RED + "Failed to evaluate condition: " + error.getDetails());
                }
                return response;
            }

            plugin.getColoredLogger().severe("Failed to evaluate condition. Check your configuration.");
            if (player != null) {
                player.sendMessage(ChatColor.RED + "Failed to evaluate condition. Check your configuration.");
            }
            return response;
        }

        if (type == ResponseType.MET) {
            return success.dispatch(context);
        }

        if (type == ResponseType.UNMET) {
            return failure.dispatch(context);
        }

        return FunctionResponse.STOP;
    }

}
