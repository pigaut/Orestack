package io.github.pigaut.rpg.core.menu.atlas;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class AtlasMenuView extends FixedMenuView {

    private final AtlasMenu atlasMenu;
    private final Button[][] atlas;
    private int x = 0;
    private int y = 0;

    public AtlasMenuView(@NotNull AtlasMenu atlasMenu, @NotNull PlayerState viewer, MenuView previousView, @NotNull Context context) {
        super(atlasMenu, viewer, previousView, context);
        this.atlasMenu = atlasMenu;
        this.atlas = atlasMenu.createAtlas(context);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean canScroll(@NotNull ScrollDirection direction) {
        return canScroll(direction, 1);
    }

    public boolean canScroll(@NotNull ScrollDirection direction, int amount) {
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case RIGHT -> dx = amount;
            case LEFT -> dx = -amount;
            case DOWN -> dy = amount;
            case UP -> dy = -amount;
            case RIGHT_UP -> {
                dx = amount;
                dy = -amount;
            }
            case RIGHT_DOWN -> {
                dx = amount;
                dy = amount;
            }
            case LEFT_DOWN -> {
                dx = -amount;
                dy = amount;
            }
            case LEFT_UP -> {
                dx = -amount;
                dy = -amount;
            }
        }

        int maxX = Math.max(0, atlasMenu.getAtlasWidth() - atlasMenu.getViewWidth());
        int maxY = Math.max(0, atlasMenu.getAtlasHeight() - atlasMenu.getViewHeight());
        int targetX = Math.max(0, Math.min(x + dx, maxX));
        int targetY = Math.max(0, Math.min(y + dy, maxY));

        return targetX != x || targetY != y;
    }

    public void scroll(@NotNull ScrollDirection direction, int amount) {
        int dx = 0;
        int dy = 0;
        switch (direction) {
            case RIGHT -> dx = amount;
            case LEFT -> dx = -amount;
            case DOWN -> dy = amount;
            case UP -> dy = -amount;
            case RIGHT_UP -> {
                dx = amount;
                dy = -amount;
            }
            case RIGHT_DOWN -> {
                dx = amount;
                dy = amount;
            }
            case LEFT_DOWN -> {
                dx = -amount;
                dy = amount;
            }
            case LEFT_UP -> {
                dx = -amount;
                dy = -amount;
            }
        }
        setCoordinates(x + dx, y + dy);
    }

    public void setCoordinates(int x, int y) {
        int maxX = Math.max(0, atlasMenu.getAtlasWidth() - atlasMenu.getViewWidth());
        int maxY = Math.max(0, atlasMenu.getAtlasHeight() - atlasMenu.getViewHeight());
        int clampedX = Math.max(0, Math.min(x, maxX));
        int clampedY = Math.max(0, Math.min(y, maxY));

        if (clampedX == this.x && clampedY == this.y) {
            return;
        }

        this.x = clampedX;
        this.y = clampedY;
        this.update();
        atlasMenu.onScroll(this);
    }

    private @Nullable Button getButtonAt(int atlasX, int atlasY) {
        return atlas[atlasX][atlasY];
    }

    @Override
    public void update() {
        buttons = new Button[menu.getSize()];

        int columns = atlasMenu.getViewWidth();
        int rows = atlasMenu.getViewHeight();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Button button = getButtonAt(x + col, y + row);
                if (button != null) {
                    buttons[row * columns + col] = button;
                }
            }
        }

        Button[] fixedButtons = menu.createButtons(context);
        for (int i = 0; i < fixedButtons.length; i++) {
            if (fixedButtons[i] != null) {
                buttons[i] = fixedButtons[i];
            }
        }

        inventory.clear();
        for (int i = 0; i < menu.getSize(); i++) {
            Button button = buttons[i];
            if (button != null) {
                ItemStack parsedItem = PlaceholderUtil.parseAll(context, button.createIcon(context));
                inventory.setItem(i, parsedItem);
            }
        }
        viewer.updateInventory();
    }

}