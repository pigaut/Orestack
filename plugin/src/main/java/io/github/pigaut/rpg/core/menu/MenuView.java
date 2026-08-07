package io.github.pigaut.rpg.core.menu;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public interface MenuView {

    Menu getMenu();

    PlayerState getViewer();

    Inventory getInventory();

    @Nullable
    MenuView getPreviousView();

    @NotNull
    MenuView getFirstView();

    @NotNull
    Context getContext();

    boolean isForcedClose();

    boolean isOpen();

    void open();

    void close();

    void update();

    void click(InventoryClickEvent event);

}
