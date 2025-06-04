package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.menu.message.editor.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.section.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class MessagesMenu extends FramedSelectionMenu {

    private final String group;
    private final RootSection config;

    public MessagesMenu(EnhancedPlugin plugin, String group) {
        super(plugin, StringFormatter.toTitleCase(group) + " Messages", MenuSize.BIG);
        this.group = group;
        final File file = PathGroup.getFile(plugin, "messages", group);
        this.config = new RootSection(file, plugin.getConfigurator());
        config.load();
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[39] = new ConfigLoadButton(config);
        buttons[41] = Button.builder()
                .withType(Material.CRAFTING_TABLE)
                .withDisplay("&f&lCreate Message")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new message in the " + group + " group")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(new MessageCreationMenu(plugin, config)))
                .buildButton();

        return buttons;
    }

    @Override
    public List<Button> createEntries() {
        return plugin.getMessages().getAll(group).stream()
                .map(message -> {
                    final String name = message.getName();

                    return Button.builder()
                            .withType(message.getIcon().getType())
                            .withDisplay("&b&o" + StringFormatter.toTitleCase(name))
                            .addLore("")
                            .addLore("&eLeft-Click: &fTo receive message")
                            .addLore("&6Right-Click: &fTo edit message")
                            .onLeftClick((view, event) -> {
                                view.close();
                                final PlayerState viewer = view.getViewer();
                                message.send(viewer.asPlayer());
                                viewer.sendMessage(ChatColor.RED + "The menu will reopen in 3 seconds...");
                                plugin.getScheduler().runTaskLater(60L, () -> {
                                    viewer.setOpenView(view);
                                });
                            })
                            .onRightClick((view, event) -> {
                                final PlayerState viewer = view.getViewer();
                                switch (message.getType()) {
                                    case CHAT -> viewer.openMenu(new ChatMessageEditor(plugin, config, name));
                                    case ACTIONBAR -> viewer.openMenu(new ActionbarEditor(plugin, config, name));
                                    case TITLE -> viewer.openMenu(new TitleEditor(plugin, config, name));
                                    case BOSSBAR -> viewer.openMenu(new BossbarEditor(plugin, config, name));
                                }
                            })
                            .buildButton();

                    })
            .toList();
    }

}
