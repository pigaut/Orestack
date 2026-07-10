package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.core.menu.*;
import io.github.pigaut.voxel.core.menu.button.*;
import io.github.pigaut.voxel.core.menu.template.button.*;
import io.github.pigaut.voxel.core.menu.template.menu.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MessageGroupsMenu extends FramedSelectionMenu {

    private final EnhancedPlugin plugin;

    public MessageGroupsMenu(@NotNull EnhancedPlugin plugin) {
        super("Message Groups", MenuSize.BIG);
        this.plugin = plugin;
    }

    @Override
    public Button getFrameButton() {
        return Buttons.LIGHT_BLUE_PANEL;
    }

    @Override
    public List<Button> createEntries(@NotNull Context context) {
        return plugin.getMessages().getAllGroups().stream()
                .map(group -> Button.builder()
                        .type(Material.CHEST)
                        .name("&b&l" + CaseFormatter.toTitleCase(group))
                        .addEmptyLine()
                        .addLeftClickLine("To view all messages")
                        .onLeftClick((menuView, player) -> player.openMenu(new MessagesMenu(plugin, group)))
                        .buildButton())
                .toList();
    }

}
