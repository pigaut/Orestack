package io.github.pigaut.rpg.core.menu.paged;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.Context;
import io.github.pigaut.rpg.core.menu.button.Button;
import io.github.pigaut.rpg.module.menu.entries.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.module.menu.entries.*;
import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class PagedMenuBuilder {

    private final String name;
    private final String group;

    private String title = "";
    private int size = 9;
    private boolean keepOpen = false;
    private boolean backtrack = false;
    private Function<Context, Button[]> createButtons = context -> new Button[size];
    private MenuEntries entries = (context, buttonTemplate) -> new ArrayList<>();
    private Collection<Integer> entrySlots = Collections.emptyList();

    public PagedMenuBuilder() {
        this(StringUtil.randomName(), null);
    }

    public PagedMenuBuilder(String name, String group) {
        this.name = name;
        this.group = group;
    }

    public PagedMenuBuilder title(String title) {
        this.title = title;
        return this;
    }

    public PagedMenuBuilder size(int size) {
        if (!InventoryUtil.isValidChestSize(size)) {
            throw new IllegalArgumentException(
                    "Chest size is out of bounds. Possible values: 9, 18, 27, 36, 45, 54");
        }
        this.size = size;
        return this;
    }

    public PagedMenuBuilder rows(int rows) {
        return size(rows * 9);
    }

    public PagedMenuBuilder keepOpen(boolean keepOpen) {
        this.keepOpen = keepOpen;
        return this;
    }

    public PagedMenuBuilder backtrack(boolean backtrack) {
        this.backtrack = backtrack;
        return this;
    }

    public PagedMenuBuilder createButtons(Function<Context, Button[]> createButtons) {
        this.createButtons = createButtons;
        return this;
    }

    public PagedMenuBuilder entries(@NotNull MenuEntries entries) {
        this.entries = entries;
        return this;
    }

    public PagedMenuBuilder entrySlots(@NotNull Collection<Integer> entrySlots) {
        this.entrySlots = entrySlots;
        return this;
    }

    public PagedMenu build() {
        Function<Context, Button[]> builtCreateButtons = createButtons;
        return new PagedMenu(name, group, title, size, keepOpen, backtrack, entrySlots) {
            @Override
            public @NotNull Button[] createButtons(@NotNull Context context) {
                return builtCreateButtons.apply(context);
            }
            @Override
            public List<Button> createEntries(@NotNull Context context) {
                return entries.createEntries(context, null);
            }
        };
    }
}