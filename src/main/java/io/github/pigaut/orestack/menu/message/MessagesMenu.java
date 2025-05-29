package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.*;
import io.github.pigaut.voxel.core.message.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

public class MessagesMenu extends GenericGroupsMenu {

    private final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public MessagesMenu(String group) {
        super(StringFormatter.toTitleCase(group) + " Messages");
        for (Message message : plugin.getMessages().getAll(group)) {
            final Button button = Button.builder()
                    .withType(message.getIcon().getType())
                    .withDisplay("&a&o" + StringFormatter.toTitleCase(message.getName()))
                    .addLore("&7Left-Click: &fReceive message")
                    .onLeftClick((menuView, event) -> {
                        menuView.close();
                        final PlayerState viewer = menuView.getViewer();
                        message.send(viewer.asPlayer());
                        viewer.sendMessage(ChatColor.RED + "The menu will reopen in 2 seconds...");
                        plugin.getScheduler().runTaskLater(40L, () -> {
                            viewer.setOpenView(menuView);
                        });
                    })
                    .buildButton();

            this.addEntry(button);
        }
    }

}
