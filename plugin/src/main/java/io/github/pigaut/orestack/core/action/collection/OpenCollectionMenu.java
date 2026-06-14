package io.github.pigaut.orestack.core.action.collection;

import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.menu.*;
import io.github.pigaut.voxel.data.function.action.*;
import io.github.pigaut.voxel.data.function.action.player.state.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.voxel.player.state.*;
import io.github.pigaut.voxel.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.orestack.collection.Collection;

import java.util.*;

public class OpenCollectionMenu implements Action {

    private final EnhancedPlugin plugin;
    private final String menuName;
    private final String collectionName;

    public OpenCollectionMenu(@NotNull EnhancedPlugin plugin, @NotNull String menuName, @NotNull String collectionName) {
        this.plugin = plugin;
        this.menuName = menuName;
        this.collectionName = collectionName;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        PlayerState playerState = context.playerState();
        if (player == null || playerState == null) {
            return;
        }

        PlayerData playerData = context.playerData();
        if (!(playerData instanceof RpgPlayerData rpgPlayerData)) {
            return;
        }

        context.addPlaceholder("menu-name", menuName);
        context.addPlaceholder("collection-name", collectionName);

        Menu menu = plugin.getMenu(menuName);
        if (menu == null) {
            plugin.sendMessage(player, context, "menu-not-found");
            return;
        }

        Collection collection = rpgPlayerData.getItemCollection(collectionName);
        if (collection == null) {
            plugin.sendMessage(player, context, "collection-not-found");
            return;
        }

        playerState.openMenu(menu, context.with(Collection.class, collection));
    }

}