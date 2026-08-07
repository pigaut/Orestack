package io.github.pigaut.rpg.module.command;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.jetbrains.annotations.*;

public class CustomCommand extends EnhancedCommand implements Identifiable {

    private final String group;

    public CustomCommand(@NotNull EnhancedPlugin plugin, @NotNull String name, @Nullable String group) {
        super(plugin, name);
        this.group = group;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

}
