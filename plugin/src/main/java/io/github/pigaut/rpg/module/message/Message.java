package io.github.pigaut.rpg.module.message;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public interface Message extends Identifiable {

    @NotNull MessageType getType();

    void send(@NotNull Player player, @NotNull Context context);

}
