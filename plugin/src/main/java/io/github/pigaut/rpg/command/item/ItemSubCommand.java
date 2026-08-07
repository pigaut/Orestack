package io.github.pigaut.rpg.command.item;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class ItemSubCommand extends SubCommand {

    public ItemSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "item");
        this.withPermission(plugin.getPermission("item"));
        this.withDescription(plugin.getTranslation("item-command"));
        this.addSubCommand(new ItemSaveSubCommand(plugin));
        this.addSubCommand(new ItemGetSubCommand(plugin));
        this.addSubCommand(new ItemGetGroupSubCommand(plugin));
    }

}
