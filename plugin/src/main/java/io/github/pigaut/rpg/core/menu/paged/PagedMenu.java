package io.github.pigaut.rpg.core.menu.paged;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.fixed.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PagedMenu extends FixedMenu {

    private final List<Integer> entrySlots;

    public PagedMenu(@NotNull String title, int size) {
        this(StringUtil.randomName(), null, title, size, List.of());
    }

    public PagedMenu(@NotNull String name, @Nullable String group,
                     @NotNull String title, int size, @NotNull Collection<Integer> entrySlots) {
        this(name, group, title, size, false, false, entrySlots);
    }

    public PagedMenu(@NotNull String name, @Nullable String group,
                     @NotNull String title, int size, boolean keepOpen, boolean backtrack,
                     @NotNull Collection<Integer> entrySlots) {
        super(name, group, title, size, keepOpen, backtrack);
        this.entrySlots = new ArrayList<>(entrySlots);
    }

    public List<Button> createEntries(@NotNull Context context) {
        return List.of();
    }

    public @NotNull Collection<Integer> getEntrySlots() {
        return new ArrayList<>(entrySlots);
    }

    public Integer getEntrySlot(int index) {
        return entrySlots.get(index);
    }

    public void addEntrySlots(int... slots) {
        for (int slot : slots) {
            if (!entrySlots.contains(slot)) {
                entrySlots.add(slot);
            }
        }
    }

    public void setEntrySlots(@NotNull Collection<Integer> slots) {
        entrySlots.clear();
        entrySlots.addAll(slots);
    }

    public int getEntriesPerPage() {
        return entrySlots.size();
    }

    public void onPageTurn(PagedMenuView menuView) {}

    @Override
    public @NotNull MenuView createView(@NotNull PlayerState player, @NotNull MenuView previousView, @NotNull Context context) {
        return new PagedMenuView(this, player, previousView, context);
    }

}
