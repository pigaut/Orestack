package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.menu.message.editor.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.node.section.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.function.*;

public class MessageCreationMenu extends FramedMenu {

    private final RootSection config;

    public MessageCreationMenu(EnhancedPlugin plugin, String group) {
        super("Message Creation", MenuSize.SMALL);

        final File file = PathGroup.getFile(plugin, "messages", group);
        this.config = new RootSection(file, plugin.getConfigurator());
        config.load();
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();
        buttons[10] = Button.builder()
                .withType(Material.BOOK)
                .withDisplay("&a&lChat")
                .addLore("")
                .addLore("&7Left-Click: &fCreate a new chat message")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    final Consumer<String> inputCollector = input -> {
                        player.openMenu(new ChatMessageEditor(config, input), view);
                    };
                    final Runnable onCancel = () -> player.setOpenView(view);
                    player.collectChatInput("name", inputCollector, onCancel);
                })
                .buildButton();

        buttons[11] = Button.builder()
                .withType(Material.MAP)
                .withDisplay("&a&lAction Bar")
                .addLore("")
                .addLore("&7Left-Click: &fCreate a new action bar message")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    final Consumer<String> inputCollector = input -> {
                        player.openMenu(new ActionbarEditor(config, input), view);
                    };
                    final Runnable onCancel = () -> player.setOpenView(view);
                    player.collectChatInput("name", inputCollector, onCancel);
                })
                .buildButton();

        buttons[12] = Button.builder()
                .withType(Material.NAME_TAG)
                .withDisplay("&a&lTitle")
                .addLore("")
                .addLore("&7Left-Click: &fCreate a new title message")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    final PlayerState player = view.getViewer();
                    final Consumer<String> inputCollector = input -> {
                        player.openMenu(new TitleEditor(config, input), view);
                    };
                    final Runnable onCancel = () -> player.setOpenView(view);
                    player.collectChatInput("name", inputCollector, onCancel);
                })
                .buildButton();

        buttons[14] = Button.builder()
                .withType(Material.DRAGON_HEAD)
                .withDisplay("&a&lBoss Bar &c(Coming soon)")
                .addLore("")
                .addLore("&7Left-Click: &fCreate a new boss bar message")
                .enchanted(true)
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.BEACON)
                .withDisplay("&a&lHologram &c(Coming soon)")
                .addLore("")
                .addLore("&7Left-Click: &fCreate a new hologram message")
                .enchanted(true)
                .buildButton();

        buttons[16] = Button.builder()
                .withType(Material.BOOKSHELF)
                .withDisplay("&a&lMulti Message &c(Coming soon)")
                .addLore("")
                .addLore("&7Left-Click: &fCreate a new multi message")
                .enchanted(true)
                .buildButton();

        return buttons;
    }
}
