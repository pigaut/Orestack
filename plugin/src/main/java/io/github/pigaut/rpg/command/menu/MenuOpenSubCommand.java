package io.github.pigaut.rpg.command.menu;

import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.core.command.node.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.jetbrains.annotations.*;

public class MenuOpenSubCommand extends SubCommand {

    public MenuOpenSubCommand(@NotNull EnhancedPlugin plugin) {
        super(plugin, "open");
        withPermission(plugin.getPermission("menu.open-all"));
        withDescription(plugin.getTranslation("menu-open-command"));
        withParameter(CommandParameters.menuName(plugin));
        withPlayerExecution((player, context, args) -> {
            Menu menu = plugin.getMenu(args[0]);
            if (menu == null) {
                plugin.sendMessage(player, context, "menu-not-found");
                return;
            }

            PlayerState playerState = plugin.getPlayerState(player);
            playerState.openMenu(menu);
            plugin.sendMessage(player, context, "opening-menu");
        });
    }

}
