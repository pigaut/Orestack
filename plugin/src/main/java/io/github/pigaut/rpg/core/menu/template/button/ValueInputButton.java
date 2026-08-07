package io.github.pigaut.rpg.core.menu.template.button;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ValueInputButton extends SimpleButton {

    private final String value;

    public ValueInputButton(@NotNull Material icon, @NotNull String value) {
        super(IconBuilder.of(icon)
                .name(CaseFormatter.toTitleCase(value))
                .addLines("")
                .addLines("&eLeft-Click: &fto select value")
                .buildIcon());
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void onLeftClick(MenuView view, PlayerState player) {
        player.submitInput(value);
    }

}
