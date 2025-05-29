package io.github.pigaut.orestack.menu.sound;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class SoundGroupsMenu extends GenericGroupsMenu {

    public SoundGroupsMenu(OrestackPlugin plugin) {
        super("Sound Effect Groups");

        for (String group : plugin.getSounds().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&3&l" + StringFormatter.toTitleCase(group))
                    .addLore("&7Left-Click: &fView all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new SoundsMenu(group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
