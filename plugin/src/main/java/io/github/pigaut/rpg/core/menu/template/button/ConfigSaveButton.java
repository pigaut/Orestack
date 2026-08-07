package io.github.pigaut.rpg.core.menu.template.button;

import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class ConfigSaveButton extends SimpleButton {

    public static final ItemStack icon = IconBuilder.of(Material.WRITABLE_BOOK)
            .name("&fSave To File")
            .enchanted(true)
            .buildIcon();

    private final ConfigRoot root;

    public ConfigSaveButton(ConfigRoot root) {
        super(icon);
        this.root = root;
    }

    @Override
    public void onLeftClick(MenuView view, PlayerState player) {
        root.save();
    }

}
