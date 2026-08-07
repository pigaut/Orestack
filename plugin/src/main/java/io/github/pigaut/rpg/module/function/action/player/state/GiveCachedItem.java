package io.github.pigaut.rpg.module.function.action.player.state;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GiveCachedItem implements PlayerStateAction {

    private final String name;

    public GiveCachedItem(String name) {
        this.name = name;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        ItemStack cachedItem = playerState.getCache(name, ItemStack.class);
        PlayerUtil.giveItemsOrDrop(playerState.asPlayer(), cachedItem);
    }

}
