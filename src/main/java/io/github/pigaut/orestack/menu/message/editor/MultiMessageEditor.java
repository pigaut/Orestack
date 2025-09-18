package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.orestack.menu.message.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.editor.*;
import io.github.pigaut.voxel.util.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.*;

import java.util.*;

public class MultiMessageEditor extends FramedSelectionEditor {

    private final ConfigSequence messageSequence;

    public MultiMessageEditor(ConfigSequence messageSequence) {
        super(messageSequence.getRoot(), "Edit Multi-Message", MenuSize.BIG);
        this.messageSequence = messageSequence;
    }

    @Override
    public Button getFrameButton() {
        return Buttons.LIGHT_BLUE_PANEL;
    }

    @Override
    public List<Button> createEntries() {
        final List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < messageSequence.size(); i++) {
            ConfigSection messageSection = messageSequence.getSectionOrCreate(i);

            if (!messageSection.isSet("type")) {
                messageSequence.remove(i--);
                continue;
            }

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

            switch (messageSection.getRequiredString("type", CaseStyle.CONSTANT)) {
                case "CHAT" -> messageButton
                        .withType(Material.BOOK)
                        .withDisplay(messageSection.getString("message", StringColor.FORMATTER).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new ChatMessageEditor(messageSection)));

                case "ACTIONBAR" -> messageButton
                        .withType(Material.NAME_TAG)
                        .withDisplay(messageSection.getString("message", StringColor.FORMATTER).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new ActionbarEditor(messageSection)));

                case "TITLE" -> messageButton
                        .withType(Material.MAP)
                        .withDisplay(messageSection.getString("title", StringColor.FORMATTER).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new TitleEditor(messageSection)));

                case "BOSSBAR" -> messageButton
                        .withType(Material.DRAGON_HEAD)
                        .withDisplay(messageSection.getString("title", StringColor.FORMATTER).orElse("none"))
                        .onLeftClick((view, player, event) -> player.openMenu(new BossbarEditor(messageSection)));

                case "HOLOGRAM" -> messageButton
                            .withType(Material.BEACON)
                            .withDisplay(messageSection.getSequence("hologram.frames")
                                    .map(frameSequence -> frameSequence.toStringList(StringColor.FORMATTER).stream()
                                            .max(Comparator.comparingInt(String::length)))
                                    .orElse(messageSection.getString("hologram.text", StringColor.FORMATTER).asOptional())
                                    .orElse("none"))
                            .onLeftClick((view, player, event) -> player.openMenu(new HologramMessageEditor(messageSection)));
            }

            buttons.add(messageButton.buildButton());
        }

        ButtonBuilder createMessageButton = Button.builder()
                .withType(Material.LIME_DYE)
                .enchanted(true)
                .withDisplay("&2Create New Message")
                .onLeftClick((view, player, event) -> {
                    final MessageCreationMenu messageCreationMenu = new MessageCreationMenu(messageSequence.addEmptySection(), false);
                    player.openMenu(messageCreationMenu);
                });

        buttons.add(createMessageButton.buildButton());

        return buttons;
    }

}
