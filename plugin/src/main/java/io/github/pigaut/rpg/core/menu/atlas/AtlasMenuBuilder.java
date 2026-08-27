package io.github.pigaut.rpg.core.menu.atlas;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.Context;
import io.github.pigaut.rpg.core.menu.button.Button;
import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.*;

import java.util.function.Function;

public class AtlasMenuBuilder {

    private final String name;
    private final String group;

    private String title = "";
    private int size = 9;
    private boolean keepOpen = false;
    private boolean backtrack = false;
    private int atlasWidth = -1;
    private int atlasHeight = -1;
    private Function<Context, Button[]> createButtons = context -> new Button[size];
    private Function<Context, Button[][]> createAtlas = context -> new Button[atlasWidth][atlasHeight];

    public AtlasMenuBuilder() {
        this(StringUtil.generateRandomName(), null);
    }

    public AtlasMenuBuilder(@NotNull String name, @Nullable String group) {
        this.name = name;
        this.group = group;
    }

    public AtlasMenuBuilder title(String title) {
        this.title = title;
        return this;
    }

    public AtlasMenuBuilder size(int size) {
        if (!InventoryUtil.isValidChestSize(size)) {
            throw new IllegalArgumentException(
                    "Chest size is out of bounds. Possible values: 9, 18, 27, 36, 45, 54");
        }
        this.size = size;
        return this;
    }

    public AtlasMenuBuilder rows(int rows) {
        return size(rows * 9);
    }

    public AtlasMenuBuilder keepOpen(boolean keepOpen) {
        this.keepOpen = keepOpen;
        return this;
    }

    public AtlasMenuBuilder backtrack(boolean backtrack) {
        this.backtrack = backtrack;
        return this;
    }

    public AtlasMenuBuilder atlasWidth(int atlasWidth) {
        this.atlasWidth = atlasWidth;
        return this;
    }

    public AtlasMenuBuilder atlasHeight(int atlasHeight) {
        this.atlasHeight = atlasHeight;
        return this;
    }

    public AtlasMenuBuilder createButtons(Function<Context, Button[]> createButtons) {
        this.createButtons = createButtons;
        return this;
    }

    public AtlasMenuBuilder createAtlas(Function<Context, Button[][]> createAtlas) {
        this.createAtlas = createAtlas;
        return this;
    }

    public AtlasMenu build() {
        Function<Context, Button[]> builtCreateButtons = createButtons;
        Function<Context, Button[][]> builtCreateAtlas = createAtlas;

        return new AtlasMenu(name, group, title, size, keepOpen, backtrack, atlasWidth, atlasHeight) {
            @Override
            public @NotNull Button[] createButtons(@NotNull Context context) {
                return builtCreateButtons.apply(context);
            }
            @Override
            public @NotNull Button[][] createAtlas(@NotNull Context context) {
                return builtCreateAtlas.apply(context);
            }
        };
    }
}