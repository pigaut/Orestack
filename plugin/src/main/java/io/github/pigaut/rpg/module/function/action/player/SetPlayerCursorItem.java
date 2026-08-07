package io.github.pigaut.rpg.module.function.action.player;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class SetPlayerCursorItem implements PlayerAction {

    private final ItemStack item;

    public SetPlayerCursorItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public void execute(@NotNull Player player) {
        player.setItemOnCursor(item);
    }

}
