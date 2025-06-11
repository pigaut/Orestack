package io.github.pigaut.orestack.menu.sound;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

import java.util.*;

public class SoundGroupsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public SoundGroupsMenu(EnhancedPlugin plugin) {
        super("Sound Effect Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getSounds().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&3&l" + StringFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all sound effects")
                        .onLeftClick((view, player, event) -> player.openMenu(new SoundsMenu(plugin, group)))
                        .buildButton())
                .toList();
    }
}
