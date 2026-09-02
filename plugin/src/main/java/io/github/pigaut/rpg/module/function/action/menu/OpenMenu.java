package io.github.pigaut.rpg.module.function.action.menu;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class OpenMenu implements PlayerStateAction.Executor {

    private final EnhancedPlugin plugin;
    private final String menuName;

    public OpenMenu(@NotNull EnhancedPlugin plugin, @NotNull String menuName) {
        this.plugin = plugin;
        this.menuName = menuName;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        Menu menu = plugin.getMenu(menuName);
        if (menu != null) {
            playerState.openMenu(menu);
        }
    }

}
