package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class OpenEnderChest implements PlayerAction {

    @Override
    public void execute(@NotNull Player player) {
        PlayerUtil.openEnderChest(player);
    }

}
