package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SendMessage implements Action {

    private final Message message;
    private final Map<String, Object> customPlaceholders = new HashMap<>();

    public SendMessage(Message message, Map<String, ConfigScalar> flags) {
        this.message = message;
        flags.forEach((key, scalar) -> {
            String id = CaseFormatter.toSnakeCase(key);
            Object value = scalar.getValue();
            if (!(value instanceof Number)) {
                value = scalar.toString();
            }
            customPlaceholders.put(id, value);
        });
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player == null) {
            return;
        }

        if (!customPlaceholders.isEmpty()) {
            context.addPlaceholders(customPlaceholders);
        }

        message.send(player, context);
    }

}
