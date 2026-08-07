package io.github.pigaut.rpg.core.menu.button;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class SimpleButton implements Button {

    private final ItemStack icon;

    public SimpleButton(ItemStack icon) {
        this.icon = icon;
    }

    @Override
    public @NotNull ItemStack createIcon(@NotNull Context context) {
        return icon;
    }

    @Override
    public boolean isLocked() {
        return true;
    }

    @Override
    public boolean isUpdateOnClick() {
        return false;
    }

    public void onClick(@NotNull MenuView view, @NotNull InventoryClickEvent event) {
        PlayerState playerState = view.getViewer();
        ClickType click = event.getClick();
        switch (click) {
            case LEFT -> onLeftClick(view, playerState);
            case SHIFT_LEFT -> onShiftLeftClick(view, playerState);
            case RIGHT -> onRightClick(view, playerState);
            case SHIFT_RIGHT -> onShiftRightClick(view, playerState);
        }

        if (isUpdateOnClick()) {
            view.update();
        }
    }

    public void onLeftClick(MenuView view, PlayerState player) {}

    public void onRightClick(MenuView view, PlayerState player) {}

    public void onShiftLeftClick(MenuView view, PlayerState player) {}

    public void onShiftRightClick(MenuView view, PlayerState player) {}


}
