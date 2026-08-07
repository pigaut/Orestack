package io.github.pigaut.rpg.core.menu.paged;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PagedMenuView extends FixedMenuView {

    private final PagedMenu pagedMenu;
    private List<Button> entries;
    private int currentPage = 0;

    public PagedMenuView(@NotNull PagedMenu pagedMenu, @NotNull PlayerState viewer, MenuView previousView, @NotNull Context context) {
        super(pagedMenu, viewer, previousView, context);
        this.pagedMenu = pagedMenu;
    }

    public boolean isFirstPage() {
        return currentPage == 0;
    }

    public boolean isLastPage() {
        return currentPage == this.getTotalPages() - 1;
    }

    public int getTotalPages() {
        final int entriesPerPage = pagedMenu.getEntriesPerPage();
        return (entries.size() + entriesPerPage - 1) / entriesPerPage;
    }

    public void nextPage() {
        this.setCurrentPage(currentPage + 1);
    }

    public void previousPage() {
        this.setCurrentPage(currentPage - 1);
    }

    public void setCurrentPage(int page) {
        if (page < 0 || page >= this.getTotalPages()) {
            return;
        }
        this.currentPage = page;
        this.update();
        pagedMenu.onPageTurn(this);
    }

    @Override
    public void update() {
        buttons = menu.createButtons(context);
        entries = pagedMenu.createEntries(context);
        final int entriesPerPage = pagedMenu.getEntriesPerPage();
        final int startIndex = currentPage * entriesPerPage;
        final int endIndex = Math.min(startIndex + entriesPerPage, entries.size());
        for (int i = startIndex, j = 0; i < endIndex; i++, j++) {
            buttons[pagedMenu.getEntrySlot(j)] = entries.get(i);
        }
        inventory.clear();
        for (int i = 0; i < menu.getSize(); i++) {
            final Button button = buttons[i];
            if (button != null) {
                inventory.setItem(i, button.createIcon(context));
            }
        }
        viewer.updateInventory();
    }

}
