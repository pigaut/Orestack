package io.github.pigaut.rpg.module.function.action.player;

import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class CloseInventory implements PlayerAction {

    @Override
    public void execute(@NotNull Player player) {
        player.closeInventory();
    }

}
