package io.github.pigaut.rpg.core.menu.template.button;

import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.config.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.plugin.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class PluginReloadButton extends SimpleButton {

    private final EnhancedJavaPlugin plugin;

    public PluginReloadButton(EnhancedJavaPlugin plugin) {
        super(IconBuilder.of(Material.OAK_BUTTON)
                .name("&aReload")
                .enchanted(true)
                .buildIcon());
        this.plugin = plugin;
    }

    @Override
    public void onLeftClick(MenuView view, PlayerState playerState) {
        view.close();
        Player player = playerState.asPlayer();
        player.performCommand("orestack reload");
    }

}
