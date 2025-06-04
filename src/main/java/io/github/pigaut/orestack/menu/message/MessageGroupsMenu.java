package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MessageGroupsMenu extends FramedSelectionMenu {

    public MessageGroupsMenu(@NotNull EnhancedPlugin plugin) {
        super(plugin, "Message Groups", MenuSize.BIG);
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getMessages().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&b&l" + StringFormatter.toTitleCase(group))
                        .addLore("")
                        .addLore("&eLeft-Click: &fView all messages")
                        .onLeftClick((menuView, event) -> menuView.getViewer().openMenu(new MessagesMenu(plugin, group)))
                        .buildButton())
                .toList();
    }

}
