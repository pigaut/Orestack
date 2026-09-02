package io.github.pigaut.rpg.module.function.action.menu;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class OpenPreviousMenu implements PlayerStateAction.Executor {

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        MenuView openMenu = playerState.getOpenMenu();
        if (openMenu != null) {
            MenuView previousView = openMenu.getPreviousView();
            if (previousView != null) {
                previousView.open();
            }
        }
    }

}
