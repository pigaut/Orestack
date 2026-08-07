package io.github.pigaut.rpg.core.menu.button;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public interface Button {

    @NotNull
    ItemStack createIcon(@NotNull Context context);

    boolean isLocked();

    boolean isUpdateOnClick();

    default void onClick(@NotNull MenuView view, @NotNull InventoryClickEvent event) {

    }

    static ButtonBuilder builder() {
        return new ButtonBuilder();
    }

}
