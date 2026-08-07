package io.github.pigaut.rpg.player.input;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class MenuSelection<T> extends GenericInputCollector<T> {

    private String description = "Select a value";
    private int size = 45;
    private List<ValueInputButton> valueEntries = new ArrayList<>();

    private MenuView openView;

    public MenuSelection(PlayerState player, Parser<T> parser) {
        super(player, parser);
    }

    @Override
    public @NotNull InputSource getInputSource() {
        return InputSource.MENU;
    }

    @Override
    public void onStart() {
        PlayerState playerState = getPlayerState();
        openView = playerState.openMenu(new ValueSelectionMenu(description, size, valueEntries));
    }

    @Override
    public void onSuccess(@NotNull T input) {
        super.onSuccess(input);
        if (openView != null) {
            MenuView previousView = openView.getPreviousView();
            if (previousView != null) {
                previousView.open();
            }
        }
    }

    @Override
    public void onCancel() {
        super.onCancel();
        if (openView != null) {
            MenuView previousView = openView.getPreviousView();
            if (previousView != null) {
                previousView.open();
            }
        }
    }

    @Override
    public MenuSelection<T> onInput(@NotNull Consumer<T> inputCollector) {
        return (MenuSelection<T>) super.onInput(inputCollector);
    }

    @Override
    public MenuSelection<T> onCancel(@NotNull Runnable cancelAction) {
        return (MenuSelection<T>) super.onCancel(cancelAction);
    }

    public MenuSelection<T> description(@NotNull String description) {
        this.description = description;
        return this;
    }

    public MenuSelection<T> size(int size) {
        Preconditions.checkArgument(MenuSize.isValid(size), "Invalid menu size.");
        this.size = size;
        return this;
    }

    public MenuSelection<T> withValues(@NotNull List<ValueInputButton> entries) {
        Parser<T> parser = getInputParser();
        for (ValueInputButton entry : entries) {
            try {
                parser.parse(entry.getValue());
            } catch (StringParseException e) {
                throw new IllegalStateException("Invalid menu input selection value. " + e.getMessage());
            }
        }
        this.valueEntries = entries;
        return this;
    }

    public MenuSelection<T> addValue(@NotNull Material icon, @NotNull String value) {
        return addValue(new ValueInputButton(icon, value));
    }

    public MenuSelection<T> addValue(@NotNull ValueInputButton entry) {
        Parser<T> parser = getInputParser();
        try {
            parser.parse(entry.getValue());
        } catch (StringParseException e) {
            throw new IllegalStateException("Invalid menu input selection value. " + e.getMessage());
        }
        this.valueEntries.add(entry);
        return this;
    }

}
