package io.github.pigaut.rpg.command.menu;

import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MenuSubCommand extends SubCommand {

    public MenuSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "menu");
        this.withPermission(plugin.getPermission("menu"));
        this.withDescription(plugin.getTranslation("menu-command"));
        this.addSubCommand(new MenuOpenSubCommand(plugin));
    }

}
