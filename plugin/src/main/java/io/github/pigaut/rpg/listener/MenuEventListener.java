package io.github.pigaut.rpg.listener;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.input.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.input.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;

public class MenuEventListener implements Listener {

    private final EnhancedPlugin plugin;

    public MenuEventListener(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        PlayerState playerState = plugin.getPlayerState((Player) event.getWhoClicked());
        MenuView openMenu = playerState.getOpenMenu();
        if (openMenu != null) {
            openMenu.click(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.CRAFTING) {
            return;
        }

        PlayerState player = plugin.getPlayerState((Player) event.getPlayer());
        MenuView view = player.getOpenMenu();
        if (view == null) {
            return;
        }

        player.setOpenMenu(null);
        if (player.isAwaitingInput(InputSource.MENU)) {
            player.cancelInputCollection();
            return;
        }

        if (!view.isForcedClose()) {
            Menu menu = view.getMenu();
            if (menu.keepOpen()) {
                plugin.getScheduler().runTaskLater(1, view::open);
                return;
            }
            else if (menu.backtrack()) {
                MenuView previousView = view.getPreviousView();
                if (previousView != null) {
                    plugin.getScheduler().runTaskLater(1, previousView::open);
                }
            }
            view.getMenu().onClose(view);
        }
    }

}
