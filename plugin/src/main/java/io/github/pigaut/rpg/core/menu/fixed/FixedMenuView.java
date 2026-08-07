package io.github.pigaut.rpg.core.menu.fixed;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FixedMenuView implements MenuView {

    protected final Menu menu;
    protected final PlayerState viewer;
    protected Button[] buttons;
    protected final Inventory inventory;
    private final MenuView previousView;
    private boolean forceClose = false;
    protected final Context context;

    public FixedMenuView(FixedMenu menu, PlayerState viewer, MenuView previousView, Context context) {
        this.menu = menu;
        this.viewer = viewer;
        this.previousView = previousView;
        String parsedTitle = PlaceholderUtil.parseAll(context, menu.getTitle());
        this.inventory = InventoryUtil.createInventory(parsedTitle, InventoryType.CHEST, menu.getSize());
        this.context = context;
    }

    @Override
    public Menu getMenu() {
        return menu;
    }

    @Override
    public PlayerState getViewer() {
        return viewer;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public @Nullable MenuView getPreviousView() {
        return previousView;
    }

    @Override
    public @NotNull MenuView getFirstView() {
        final Set<MenuView> visited = new HashSet<>();
        MenuView current = this;
        while (current.getPreviousView() != null) {
            if (!visited.add(current)) {
                break;
            }
            current = current.getPreviousView();
        }
        return current;
    }

    @Override
    public @NotNull Context getContext() {
        return context;
    }

    @Override
    public boolean isForcedClose() {
        return forceClose;
    }

    @Override
    public boolean isOpen() {
        return viewer.getOpenMenu() == this;
    }

    @Override
    public void open() {
        forceClose = false;
        MenuView openMenu = viewer.getOpenMenu();
        if (openMenu != null) {
            openMenu.getMenu().onClose(openMenu);
        }
        viewer.setOpenMenu(null);
        viewer.openInventory(inventory);
        menu.onOpen(this);
        viewer.setOpenMenu(this);

        update();
    }

    @Override
    public void close() {
        forceClose = true;
        viewer.closeInventory();
    }

    @Override
    public void update() {
        buttons = menu.createButtons(context);
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

    @Override
    public void click(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= menu.getSize()) {
            event.setCancelled(true);
            return;
        }

        Button button = buttons[slot];
        if (button != null) {
            event.setCancelled(button.isLocked());
            button.onClick(this, event);
        }
    }

}
