package io.github.pigaut.rpg.core.menu.button;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.*;
import org.bukkit.event.inventory.*;

public interface ViewButton {

    default void onClick(MenuView view, InventoryClickEvent event) {
        final ClickType click = event.getClick();
        switch (click) {
            case LEFT -> onLeftClick(view, event);
            case SHIFT_LEFT -> onShiftLeftClick(view, event);
            case RIGHT -> onRightClick(view, event);
            case SHIFT_RIGHT -> onShiftRightClick(view, event);
        }
    }

    default void onLeftClick(MenuView view, InventoryClickEvent event) {}

    default void onRightClick(MenuView view, InventoryClickEvent event) {}

    default void onShiftLeftClick(MenuView view, InventoryClickEvent event) {}

    default void onShiftRightClick(MenuView view, InventoryClickEvent event) {}

}
