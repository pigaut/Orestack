package io.github.pigaut.rpg.module.function.action.collection;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.player.data.base.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.rpg.module.collection.ItemCollection;

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

        EnhancedPlayerData playerData = context.playerData();
        if (!(playerData instanceof PlayerData rpgPlayerData)) {
            return;
        }

        context.addPlaceholder("menu-name", menuName);
        context.addPlaceholder("collection-name", collectionName);

        Menu menu = plugin.getMenu(menuName);
        if (menu == null) {
            plugin.sendMessage(player, context, "menu-not-found");
            return;
        }

        ItemCollection collection = rpgPlayerData.getItemCollection(collectionName);
        if (collection == null) {
            plugin.sendMessage(player, context, "collection-not-found");
            return;
        }

        playerState.openMenu(menu, context.with(ItemCollection.class, collection));
    }

}