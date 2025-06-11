package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.menu.message.editor.*;
import io.github.pigaut.voxel.core.message.*;
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

public class MessageSelectionMenu extends FramedSelectionMenu {

    private final String group;
    private final RootSection config;

    public MessageSelectionMenu(EnhancedPlugin plugin, String group) {
        super(plugin, StringFormatter.toTitleCase(group) + " Messages", MenuSize.BIG);
        this.group = group;
        final File file = PathGroup.getFile(plugin, "messages", group);
        this.config = new RootSection(file, plugin.getConfigurator());
        config.load();
    }

    @Override
    public void onOpen(MenuView view) {
        plugin.getMessages().reload(errorsFound -> {
            view.update();
        });
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[38] = new ConfigLoadButton(config);

        return buttons;
    }

    @Override
    public List<Button> createEntries() {
        final List<Button> entries = new ArrayList<>();

        for (Message message : plugin.getMessages().getAll(group)) {
            final String name = message.getName();
            final Button messageButton = Button.builder()
                    .withType(message.getIcon().getType())
                    .withDisplay("&b&o" + StringFormatter.toTitleCase(name))
                    .addLore("")
                    .addLore("&eLeft-Click: &fTo edit message")
                    .addLore("&6Right-Click: &fTo receive message")
                    .onLeftClick((view, player, event) -> {
                        final PlayerState viewer = view.getViewer();
                        switch (message.getType()) {
                            case CHAT -> viewer.openMenu(new ChatMessageEditor(plugin, config, name));
                            case ACTIONBAR -> viewer.openMenu(new ActionbarEditor(plugin, config, name));
                            case TITLE -> viewer.openMenu(new TitleEditor(plugin, config, name));
                            case BOSSBAR -> viewer.openMenu(new BossbarEditor(plugin, config, name));
                            case HOLOGRAM -> viewer.openMenu(new HologramMessageEditor(plugin, config, name));
                        }
                    })
                    .onRightClick((view, player, event) -> {
                        view.close();
                        message.send(player.asPlayer());
                        player.sendMessage(ChatColor.RED + "The menu will reopen in 3 seconds...");
                        plugin.getScheduler().runTaskLater(60L, view::open);
                    })
                    .buildButton();
            entries.add(messageButton);
        }

        entries.add(Button.builder()
                .withType(Material.LIME_DYE)
                .withDisplay("&f&lAdd New Message")
                .addLore("")
                .addLore("&eLeft-Click: &fTo create a new message in the " + group + " group")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new MessageCreationMenu(plugin, config)))
                .buildButton());

        return entries;
    }

}
