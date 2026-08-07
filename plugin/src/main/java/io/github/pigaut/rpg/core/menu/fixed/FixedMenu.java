package io.github.pigaut.rpg.core.menu.fixed;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.*;
import org.jetbrains.annotations.*;

public class FixedMenu implements Menu {

    private final String name;
    private final String group;

    private final String title;
    private final int size;
    private final boolean keepOpen;
    private final boolean backtrack;

    public FixedMenu(String title, int size) {
        this(YamlConfig.generateRandomKey(), null, title, size);
    }

    public FixedMenu(@NotNull String name, @Nullable String group,
                     @NotNull String title, int size) {
        this(name, group, title, size, false, false);
    }

    public FixedMenu(@NotNull String name, @Nullable String group,
                     @NotNull String title, int size, boolean keepOpen, boolean backtrack) {
        this.name = name;
        this.group = group;
        this.title = title;
        this.size = size;
        this.keepOpen = keepOpen;
        this.backtrack = backtrack;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public @NotNull Button[] createButtons(@NotNull Context context) {
        return new Button[size];
    }

    @Override
    public boolean keepOpen() {
        return keepOpen;
    }

    @Override
    public boolean backtrack() {
        return backtrack;
    }

    @Override
    public void onOpen(MenuView view) {}

    @Override
    public void onClose(MenuView view) {}

    @Override
    public @NotNull MenuView createView(@NotNull PlayerState player, @Nullable MenuView previousView, @NotNull Context context) {
        return new FixedMenuView(this, player, previousView, context);
    }

}
