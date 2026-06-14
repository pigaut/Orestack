package io.github.pigaut.orestack.collection.command;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.core.command.node.*;
import io.github.pigaut.voxel.plugin.*;
import org.jetbrains.annotations.*;

public class CollectionSubCommand extends SubCommand {

    public CollectionSubCommand(@NotNull OrestackPlugin plugin) {
        super(plugin, "collection");
        this.withPermission(plugin.getPermission("collection"));
        this.withDescription(plugin.getTranslation("collection-command"));
        this.addSubCommand(new CollectionIncrementSubCommand(plugin));
        this.addSubCommand(new CollectionDecrementSubCommand(plugin));
    }

}
