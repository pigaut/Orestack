package io.github.pigaut.orestack.menu.sound;

import io.github.pigaut.voxel.core.sound.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.entity.*;

public class SoundsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public SoundsMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Sound Effects", MenuSize.BIG);
        this.plugin = plugin;
        for (SoundEffect sound : plugin.getSounds().getAll(group)) {
            final Button button = Button.builder()
                    .withType(sound.getIcon().getType())
                    .withDisplay("&3&o" + StringFormatter.toTitleCase(sound.getName()))
                    .addLore("")
                    .addLore("&7Left-Click: &fPlay-me sound")
                    .onLeftClick((menuView, event) -> {
                        final Player player = menuView.getViewer().asPlayer();
                        sound.play(player, player.getLocation());
                    })
                    .buildButton();

            this.addEntry(button);
        }
    }

}
