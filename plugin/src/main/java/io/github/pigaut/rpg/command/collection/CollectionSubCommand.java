package io.github.pigaut.rpg.command.collection;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.node.*;
import org.jetbrains.annotations.*;

public class CollectionSubCommand extends SubCommand {

    public CollectionSubCommand(@NotNull RpgMakerPlugin plugin) {
        super(plugin, "collection");
        this.withPermission(plugin.getPermission("collection"));
        this.withDescription(plugin.getTranslation("collection-command"));
        this.addSubCommand(new CollectionIncrementSubCommand(plugin));
        this.addSubCommand(new CollectionDecrementSubCommand(plugin));
    }

}
