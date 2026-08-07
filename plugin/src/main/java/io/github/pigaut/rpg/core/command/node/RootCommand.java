package io.github.pigaut.rpg.core.command.node;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class RootCommand extends CommandNode {

    public RootCommand(@NotNull EnhancedPlugin plugin, @NotNull String name) {
        super(plugin, name);
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public @NotNull RootCommand getRoot() {
        return this;
    }

    @Override
    public @NotNull CommandNode getParent() {
        throw new UnsupportedOperationException("Root command does not have a parent");
    }
}
