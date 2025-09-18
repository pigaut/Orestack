package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MessageGroupsMenu extends FramedSelectionMenu {

    public MessageGroupsMenu(@NotNull EnhancedPlugin plugin) {
        super(plugin, "Message Groups", MenuSize.BIG);
    }

    @Override
    public Button getFrameButton() {
        return Buttons.LIGHT_BLUE_PANEL;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getMessages().getAllGroups().stream()
                .map(group -> Button.builder()
                        .withType(Material.CHEST)
                        .withDisplay("&b&l" + CaseFormatter.toTitleCase(group))
                        .addLore("")
                        .addLeftClickLore("To view all messages")
                        .onLeftClick((menuView, player, event) -> player.openMenu(new MessagesMenu(plugin, group)))
                        .buildButton())
                .toList();
    }

}
