package io.github.pigaut.orestack.menu.sound;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.particle.*;
import io.github.pigaut.voxel.core.sound.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.entity.*;

public class SoundsMenu extends GenericGroupsMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public SoundsMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Sound Effects");
        for (SoundEffect sound : plugin.getSounds().getAll(group)) {
            final Button button = Button.builder()
                    .withType(sound.getIcon().getType())
                    .withDisplay("&3&o" + StringFormatter.toTitleCase(sound.getName()))
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
