package io.github.pigaut.rpg.core.command.node;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class SubCommand extends CommandNode {

    private CommandNode parent = null;

    public SubCommand(@NotNull EnhancedPlugin plugin, @NotNull String name) {
        super(plugin, name);
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public @NotNull RootCommand getRoot() {
        return parent.getRoot();
    }

    @Override
    public @NotNull CommandNode getParent() {
        if (parent == null) {
            throw new IllegalStateException("Sub command was not registered under any command");
        }

        return parent;
    }

    protected void setParent(CommandNode parent) {
        this.parent = parent;
    }

}
