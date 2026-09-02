package io.github.pigaut.rpg.module.function.condition.menu;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.atlas.*;
import io.github.pigaut.rpg.module.function.condition.player.*;
import io.github.pigaut.rpg.module.function.response.*;
import io.github.pigaut.rpg.player.state.*;
import org.jetbrains.annotations.*;

public class MenuCanScroll implements PlayerStateCondition {

    private final ScrollDirection direction;
    private final int amount;

    public MenuCanScroll(ScrollDirection direction, int amount) {
        this.direction = direction;
        this.amount = amount;
    }

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull PlayerState playerState) {
        MenuView menu = playerState.getOpenMenu();
        if (menu instanceof AtlasMenuView atlasMenu) {
            return FunctionResponse.met(atlasMenu.canScroll(direction, amount));
        }
        return new FunctionError("Player does not have an atlas menu open");
    }

}
