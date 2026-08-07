package io.github.pigaut.rpg.module.function.action.menu;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class CloseMenu implements PlayerStateAction {

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        MenuView openMenu = playerState.getOpenMenu();
        if (openMenu != null) {
            openMenu.close();
        }
    }

}
