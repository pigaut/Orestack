package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.voxel.core.message.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class MessagesMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;
    private final String group;

    public MessagesMenu(EnhancedPlugin plugin, String group) {
        super(StringFormatter.toTitleCase(group) + " Messages", MenuSize.BIG);
        this.plugin = plugin;
        this.group = group;

        for (Message message : plugin.getMessages().getAll(group)) {
            final Button button = Button.builder()
                    .withType(message.getIcon().getType())
                    .withDisplay("&a&o" + StringFormatter.toTitleCase(message.getName()))
                    .addLore("")
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

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[41] = Button.builder()
                .withType(Material.CRAFTING_TABLE)
                .withDisplay("&f&lCreate Message")
                .addLore("&7Left-Click: &fCreate a new message in the " + group + " group")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(new MessageCreationMenu(plugin, group)))
                .buildButton();

        return buttons;
    }
}
