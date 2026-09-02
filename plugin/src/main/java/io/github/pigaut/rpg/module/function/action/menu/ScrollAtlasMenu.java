package io.github.pigaut.rpg.module.function.action.menu;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class ScrollAtlasMenu implements PlayerStateAction.Executor {

    private final ScrollDirection direction;
    private final int amount;

    public ScrollAtlasMenu(ScrollDirection direction, int amount) {
        this.direction = direction;
        this.amount = amount;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        MenuView openMenu = playerState.getOpenMenu();
        if (openMenu instanceof AtlasMenuView atlasMenu) {
            atlasMenu.scroll(direction, amount);
        }
    }
}
