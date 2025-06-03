package io.github.pigaut.orestack.menu.sound;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class SoundGroupsMenu extends FramedSelectionMenu {

    public SoundGroupsMenu(EnhancedPlugin plugin) {
        super("Sound Effect Groups", MenuSize.BIG);

        for (String group : plugin.getSounds().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&3&l" + StringFormatter.toTitleCase(group))
                    .addLore("")
                    .addLore("&eLeft-Click: &fView all sound effects")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new SoundsMenu(plugin, group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
