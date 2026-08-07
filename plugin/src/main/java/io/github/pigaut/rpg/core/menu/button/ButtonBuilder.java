package io.github.pigaut.rpg.core.menu.button;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class ButtonBuilder extends IconBuilder {

    private boolean updateOnClick = true;

    private BiConsumer<MenuView, PlayerState> leftClick = (view, player) -> {};
    private BiConsumer<MenuView, PlayerState> shiftLeftClick = (view, player) -> {};
    private BiConsumer<MenuView, PlayerState> rightClick = (view, player) -> {};
    private BiConsumer<MenuView, PlayerState> shiftRightClick = (view, player) -> {};

    ButtonBuilder() {
        super(Material.TERRACOTTA);
    }

    ButtonBuilder(Material type) {
        super(type);
    }

    public Button buildButton() {
        ItemStack icon = buildIcon();

        return new Button() {
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
                return updateOnClick;
            }

            @Override
            public void onClick(@NotNull MenuView view, @NotNull InventoryClickEvent event) {
                PlayerState playerState = view.getViewer();
                ClickType click = event.getClick();
                switch (click) {
                    case LEFT -> leftClick.accept(view, playerState);
                    case SHIFT_LEFT -> rightClick.accept(view, playerState);
                    case RIGHT -> shiftLeftClick.accept(view, playerState);
                    case SHIFT_RIGHT -> shiftRightClick.accept(view, playerState);
                }

                if (updateOnClick) {
                    view.update();
                }
            }

        };
    }

    @Override
    public ButtonBuilder type(Material type) {
        return (ButtonBuilder) super.type(type);
    }

    @Override
    public ButtonBuilder amount(int amount) {
        return (ButtonBuilder) super.amount(amount);
    }

    @Override
    public ButtonBuilder name(String display) {
        return (ButtonBuilder) super.name(display);
    }

    @Override
    public ButtonBuilder addEmptyLine() {
        return (ButtonBuilder) super.addEmptyLine();
    }

    @Override
    public ButtonBuilder addLine(@NotNull String line) {
        return (ButtonBuilder) super.addLine(line);
    }

    @Override
    public ButtonBuilder addLines(String... loreLines) {
        return (ButtonBuilder) super.addLines(loreLines);
    }

    @Override
    public ButtonBuilder addLines(List<String> loreLines) {
        return (ButtonBuilder) super.addLines(loreLines);
    }

    @Override
    public ButtonBuilder addLeftClickLine(String action) {
        return (ButtonBuilder) super.addLeftClickLine(action);
    }

    @Override
    public ButtonBuilder addRightClickLine(String action) {
        return (ButtonBuilder) super.addRightClickLine(action);
    }

    @Override
    public ButtonBuilder addShiftLeftClickLine(String action) {
        return (ButtonBuilder) super.addShiftLeftClickLine(action);
    }

    @Override
    public ButtonBuilder addShiftRightClickLine(String action) {
        return (ButtonBuilder) super.addShiftRightClickLine(action);
    }

    @Override
    public ButtonBuilder enchanted(boolean enchanted) {
        return (ButtonBuilder) super.enchanted(enchanted);
    }

    public ButtonBuilder updateOnClick(boolean updateOnClick) {
        this.updateOnClick = updateOnClick;
        return this;
    }

    public ButtonBuilder onLeftClick(BiConsumer<MenuView, PlayerState> action) {
        this.leftClick = action;
        return this;
    }

    public ButtonBuilder onShiftLeftClick(BiConsumer<MenuView, PlayerState> action) {
        this.shiftLeftClick = action;
        return this;
    }

    public ButtonBuilder onRightClick(BiConsumer<MenuView, PlayerState> action) {
        this.rightClick = action;
        return this;
    }

    public ButtonBuilder onShiftRightClick(BiConsumer<MenuView, PlayerState> action) {
        this.shiftRightClick = action;
        return this;
    }

}
