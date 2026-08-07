package io.github.pigaut.rpg.core.menu.atlas;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.event.inventory.*;
import org.jetbrains.annotations.*;

public class AtlasMenu extends FixedMenu {

    private final int viewWidth;
    private final int viewHeight;

    private final int atlasWidth;
    private final int atlasHeight;

    public AtlasMenu(@NotNull String name, @Nullable String group,
                     @NotNull String title, int size, boolean keepOpen, boolean backtrack,
                     int atlasWidth, int atlasHeight) {
        super(name, group, title, size, keepOpen, backtrack);
        this.viewWidth = InventoryUtil.getInventoryLength(InventoryType.CHEST, size);
        this.viewHeight = InventoryUtil.getInventoryHeight(InventoryType.CHEST, size);
        this.atlasWidth = Math.max(atlasWidth, viewWidth);
        this.atlasHeight = Math.max(atlasHeight, viewHeight);
    }

    public @NotNull Button[][] createAtlas(@NotNull Context context) {
        return new Button[atlasWidth][atlasHeight];
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public int getAtlasWidth() {
        return atlasWidth;
    }

    public int getAtlasHeight() {
        return atlasHeight;
    }

    public void onScroll(AtlasMenuView menuView) {}

    @Override
    public @NotNull MenuView createView(@NotNull PlayerState player, @Nullable MenuView previousView, @NotNull Context context) {
        return new AtlasMenuView(this, player, previousView, context);
    }

}