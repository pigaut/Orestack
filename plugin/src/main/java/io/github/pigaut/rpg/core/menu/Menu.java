package io.github.pigaut.rpg.core.menu;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.jetbrains.annotations.*;

public interface Menu extends Identifiable {

    @NotNull String getTitle();

    int getSize();

    @NotNull
    Button[] createButtons(@NotNull Context context);

    boolean keepOpen();

    boolean backtrack();

    void onOpen(MenuView view);

    void onClose(MenuView view);

    @NotNull
    MenuView createView(@NotNull PlayerState player, @Nullable MenuView previousView, @NotNull Context context);

}
