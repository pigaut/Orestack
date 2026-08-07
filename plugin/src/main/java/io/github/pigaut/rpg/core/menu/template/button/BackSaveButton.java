package io.github.pigaut.rpg.core.menu.template.button;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;

public class BackSaveButton extends SimpleButton {

    private final ConfigRoot config;

    public BackSaveButton(ConfigRoot config) {
        super(IconBuilder.of(Material.SPRUCE_DOOR)
                .name("&cSave & Back")
                .enchanted(true)
                .buildIcon());
        this.config = config;
    }

    @Override
    public void onLeftClick(MenuView view, PlayerState player) {
        MenuView previousView = view.getPreviousView();
        if (previousView != null) {
            previousView.open();
        }
    }

}
