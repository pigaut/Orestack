package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.menu.message.editor.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.voxel.player.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.node.section.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class MessageCreationMenu extends FramedMenu {

    private final RootSection config;

    public MessageCreationMenu(@NotNull EnhancedPlugin plugin, @NotNull RootSection config) {
        super(plugin, "Message Creation", MenuSize.SMALL);
        this.config = config;
    }

    @Override
    public boolean backOnClose() {
        return true;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[10] = Button.builder()
                .withType(Material.BOOK)
                .withDisplay("Chat")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new chat message")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter message name in chat")
                            .onInput(input -> {
                                player.openMenu(new ChatMessageEditor(plugin, config, input), view.getPreviousView());
                            })
                            .collect();
                })
                .buildButton();

        buttons[11] = Button.builder()
                .withType(Material.MAP)
                .withDisplay("Action Bar")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new action bar message")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter message name in chat")
                            .onInput(input -> {
                                player.openMenu(new ActionbarEditor(plugin, config, input), view.getPreviousView());
                            })
                            .collect();
                })
                .buildButton();

        buttons[12] = Button.builder()
                .withType(Material.NAME_TAG)
                .withDisplay("Title")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new title message")
                .enchanted(true)
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter message name in chat")
                            .onInput(input -> {
                                player.openMenu(new TitleEditor(plugin, config, input), view.getPreviousView());
                            })
                            .collect();
                })
                .buildButton();

        buttons[14] = Button.builder()
                .withType(Material.DRAGON_HEAD)
                .withDisplay("Boss Bar")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new boss bar message")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter message name in chat")
                            .onInput(input -> {
                                player.openMenu(new BossbarEditor(plugin, config, input), view.getPreviousView());
                            })
                            .collect();
                })
                .enchanted(true)
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.BEACON)
                .enchanted(true)
                .withDisplay("Hologram")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new hologram message")
                .onLeftClick((view, player, event) -> {
                    player.createChatInput()
                            .withDescription("Enter message name in chat")
                            .onInput(input -> {
                                player.openMenu(new HologramMessageEditor(plugin, config, input), view.getPreviousView());
                            })
                            .collect();
                })
                .buildButton();

        buttons[16] = Button.builder()
                .withType(Material.BOOKSHELF)
                .withDisplay("Multi Message &c(Coming soon)")
                .addLore("")
                .addLore("&eLeft-Click: &fCreate a new multi message")
                .enchanted(true)
                .buildButton();

        return buttons;
    }
}
