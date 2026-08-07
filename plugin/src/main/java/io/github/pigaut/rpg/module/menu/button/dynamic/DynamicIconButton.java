package io.github.pigaut.rpg.module.menu.button.dynamic;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.menu.button.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class DynamicIconButton extends FixedButton {

    private final DynamicIcon dynamicIcon;

    public DynamicIconButton(@NotNull ItemStack icon, boolean updateOnClick,
                             Function onClick, Function onLeftClick, Function onRightClick,
                             Function onShiftLeftClick, Function onShiftRightClick, DynamicIcon dynamicIcon) {
        super(icon, updateOnClick, onClick, onLeftClick, onRightClick, onShiftLeftClick, onShiftRightClick);
        this.dynamicIcon = dynamicIcon;
    }

    @Override
    public @NotNull ItemStack createIcon(@NotNull Context context) {
        ItemStack icon = super.createIcon(context);
        dynamicIcon.apply(icon, context);
        return icon;
    }
}
