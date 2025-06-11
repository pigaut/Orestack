package io.github.pigaut.orestack.menu.sound;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.entity.*;

import java.util.*;

public class SoundsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public SoundsMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Sound Effects", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getSounds().getAll(group).stream()
                .map(sound -> Button.builder()
                        .withType(sound.getIcon().getType())
                        .withDisplay("&3&o" + StringFormatter.toTitleCase(sound.getName()))
                        .addLore("")
                        .addLore("&eLeft-Click: &fPlay-me sound")
                        .onLeftClick((menuView, player, event) -> {
                            sound.play(player.asPlayer(), player.getLocation());
                        })
                        .buildButton())
                .toList();
    }
}
