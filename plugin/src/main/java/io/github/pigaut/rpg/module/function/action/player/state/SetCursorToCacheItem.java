package io.github.pigaut.rpg.module.function.action.player.state;

import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class SetCursorToCacheItem implements PlayerStateAction.Executor {

    private final String name;

    public SetCursorToCacheItem(String name) {
        this.name = name;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        player.setItemOnCursor(playerState.getCache(name, ItemStack.class));
    }

}
