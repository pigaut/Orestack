package io.github.pigaut.rpg.core.menu.fixed;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.Context;
import io.github.pigaut.rpg.core.menu.button.Button;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.*;

import java.util.function.Function;

public class FixedMenuBuilder {

    private final String name;
    private final String group;

    private String title = "";
    private int size = 9;
    private boolean keepOpen = false;
    private boolean backtrack = false;
    private Function<Context, Button[]> createButtons = context -> new Button[size];

    public FixedMenuBuilder() {
        this(StringUtil.randomName(), null);
    }

    public FixedMenuBuilder(@NotNull String name, @Nullable String group) {
        this.name = name;
        this.group = group;
    }

    public FixedMenuBuilder title(String title) {
        this.title = title;
        return this;
    }

    public FixedMenuBuilder size(int size) {
        if (!InventoryUtil.isValidChestSize(size)) {
            throw new IllegalArgumentException(
                    "Chest size is out of bounds. Possible values: 9, 18, 27, 36, 45, 54");
        }
        this.size = size;
        return this;
    }

    public FixedMenuBuilder rows(int rows) {
        return size(rows * 9);
    }

    public FixedMenuBuilder keepOpen(boolean keepOpen) {
        this.keepOpen = keepOpen;
        return this;
    }

    public FixedMenuBuilder backtrack(boolean backtrack) {
        this.backtrack = backtrack;
        return this;
    }

    public FixedMenuBuilder createButtons(@NotNull Function<Context, Button[]> createButtons) {
        this.createButtons = createButtons;
        return this;
    }

    public FixedMenu build() {
        Function<Context, Button[]> builtCreateButtons = createButtons;
        return new FixedMenu(name, group, title, size, keepOpen, backtrack) {
            @Override
            public @NotNull Button[] createButtons(@NotNull Context context) {
                return builtCreateButtons.apply(context);
            }
        };
    }
}