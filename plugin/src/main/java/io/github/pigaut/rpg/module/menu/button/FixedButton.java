package io.github.pigaut.rpg.module.menu.button;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class FixedButton implements Button, ButtonTemplate {

    private final ItemStack icon;
    private final boolean updateOnClick;
    private final Function onClick;
    private final Function onLeftClick;
    private final Function onRightClick;
    private final Function onShiftLeftClick;
    private final Function onShiftRightClick;

    public FixedButton(@NotNull ItemStack icon, boolean updateOnClick,
                       Function onClick, Function onLeftClick, Function onRightClick,
                       Function onShiftLeftClick, Function onShiftRightClick) {
        this.icon = icon;
        this.updateOnClick = updateOnClick;
        this.onClick = onClick;
        this.onLeftClick = onLeftClick;
        this.onRightClick = onRightClick;
        this.onShiftLeftClick = onShiftLeftClick;
        this.onShiftRightClick = onShiftRightClick;
    }

    public @NotNull ItemStack getIcon() {
        return icon.clone();
    }

    public @Nullable Function getOnClick() {
        return onClick;
    }

    public @Nullable Function getOnLeftClick() {
        return onLeftClick;
    }

    public @Nullable Function getOnRightClick() {
        return onRightClick;
    }

    public @Nullable Function getOnShiftLeftClick() {
        return onShiftLeftClick;
    }

    public @Nullable Function getOnShiftRightClick() {
        return onShiftRightClick;
    }

    @Override
    public @NotNull ItemStack createIcon(@NotNull Context context) {
        return icon.clone();
    }

    @Override
    public boolean isLocked() {
        return true;
    }

    @Override
    public boolean isUpdateOnClick() {
        return updateOnClick;
    }

    @Override
    public void onClick(@NotNull MenuView view, @NotNull InventoryClickEvent event) {
        PlayerState playerState = view.getViewer();
        ClickType click = event.getClick();
        Context context = Context.builder(playerState.getPlugin())
                .withPlayer(playerState.asPlayer())
                .withPlayerState(playerState)
                .withClickType(click)
                .build();

        if (onClick != null) {
            onClick.run(context);
        }

        switch (click) {
            case LEFT -> {
                if (onLeftClick != null) {
                    onLeftClick.run(context);
                }
            }
            case SHIFT_LEFT -> {
                if (onShiftLeftClick != null) {
                    onShiftLeftClick.run(context);
                }
            }
            case RIGHT -> {
                if (onRightClick != null) {
                    onRightClick.run(context);
                }
            }
            case SHIFT_RIGHT -> {
                if (onShiftRightClick != null) {
                    onShiftRightClick.run(context);
                }
            }
        }

        if (updateOnClick) {
            view.update();
        }
    }

    @Override
    public @NotNull Button createButton(@NotNull Context context) {
        return this;
    }

}
