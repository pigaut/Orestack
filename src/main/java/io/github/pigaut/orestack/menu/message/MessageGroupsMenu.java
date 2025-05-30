package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class MessageGroupsMenu extends FramedSelectionMenu {

    public MessageGroupsMenu(OrestackPlugin plugin) {
        super("Message Groups", MenuSize.BIG);

        for (String group : plugin.getMessages().getAllGroups()) {
            final Button button = Button.builder()
                    .withType(Material.CHEST)
                    .withDisplay("&b&l" + StringFormatter.toTitleCase(group))
                    .addLore("")
                    .addLore("&7Left-Click: &fView all")
                    .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new MessagesMenu(plugin, group)))
                    .buildButton();

            this.addEntry(button);
        }
    }

}
