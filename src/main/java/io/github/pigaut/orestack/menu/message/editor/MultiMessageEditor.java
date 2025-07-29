package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.orestack.menu.message.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.parser.*;
import org.bukkit.*;

import java.util.*;

public class MultiMessageEditor extends FramedSelectionMenu {

    private final ConfigSequence messageSequence;

    public MultiMessageEditor(ConfigSequence messageSequence) {
        super("Edit Multi-Message", MenuSize.BIG);
        this.messageSequence = messageSequence;
    }

    @Override
    public List<Button> createEntries() {
        final List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < messageSequence.size(); i++) {
            ConfigSection hologramSection = messageSequence.getSectionOrCreate(i);

            final int messageIndex = i;
            ButtonBuilder messageButton = Button.builder()
                    .withAmount(messageIndex + 1)
                    .enchanted(true)
                    .addLore("")
                    .addLore("&eLeft-Click: &fTo edit message")
                    .addLore("&cRight-Click: &fTo remove message")
                    .onRightClick((view, playerState, event) -> {
                        messageSequence.remove(messageIndex);
                        view.update();
                    });

            switch (hologramSection.getString("type", StringStyle.CONSTANT)) {
                case "CHAT" -> messageButton
                        .withType(Material.BOOK)
                        .onLeftClick((view, player, event) -> player.openMenu(new ChatMessageEditor(hologramSection)));

                case "ACTIONBAR" -> messageButton
                        .withType(Material.NAME_TAG)
                        .onLeftClick((view, player, event) -> player.openMenu(new ActionbarEditor(hologramSection)));

                case "TITLE" -> messageButton
                        .withType(Material.MAP)
                        .onLeftClick((view, player, event) -> player.openMenu(new TitleEditor(hologramSection)));

                case "BOSSBAR" -> messageButton
                        .withType(Material.DRAGON_HEAD)
                        .onLeftClick((view, player, event) -> player.openMenu(new BossbarEditor(hologramSection)));

                case "HOLOGRAM" -> messageButton
                        .withType(Material.BEACON)
                        .onLeftClick((view, player, event) -> player.openMenu(new HologramMessageEditor(hologramSection)));
            }

            buttons.add(messageButton.buildButton());
        }

        ButtonBuilder createMessageButton = Button.builder()
                .withType(Material.LIME_DYE)
                .enchanted(true)
                .withDisplay("&a&lCreate New Message")
                .addLore("")
                .addLore("&eLeft-Click: &fTo add a new message")
                .onLeftClick((view, player, event) -> {
                    final MessageCreationMenu messageCreationMenu = new MessageCreationMenu(messageSequence.addSection(), false);
                    player.openMenu(messageCreationMenu);
                });

        buttons.add(createMessageButton.buildButton());

        return buttons;
    }

}
