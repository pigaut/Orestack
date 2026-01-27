package io.github.pigaut.orestack.menu.message.editor;

import io.github.pigaut.orestack.menu.message.*;
import io.github.pigaut.voxel.bukkit.*;
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
                    .amount(messageIndex + 1)
                    .enchanted(true)
                    .addEmptyLine()
                    .addLine("&eLeft-Click: &fTo edit message")
                    .addLine("&cRight-Click: &fTo remove message")
                    .onRightClick((menuView, playerState) -> {
                        messageSequence.remove(messageIndex);
                        menuView.update();
                    });

            switch (messageSection.getRequiredString("type", CaseStyle.CONSTANT)) {
                case "CHAT" -> messageButton
                        .type(Material.BOOK)
                        .name(messageSection.getString("message", StringColor.FORMATTER).orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new ChatMessageEditor(messageSection)));

                case "ACTIONBAR" -> messageButton
                        .type(Material.NAME_TAG)
                        .name(messageSection.getString("message", StringColor.FORMATTER).orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new ActionbarEditor(messageSection)));

                case "TITLE" -> messageButton
                        .type(Material.MAP)
                        .name(messageSection.getString("title", StringColor.FORMATTER).orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new TitleEditor(messageSection)));

                case "BOSSBAR" -> messageButton
                        .type(Material.DRAGON_HEAD)
                        .name(messageSection.getString("title", StringColor.FORMATTER).orElse("not set"))
                        .onLeftClick((view, player) -> player.openMenu(new BossbarEditor(messageSection)));

                case "HOLOGRAM" -> messageButton
                            .type(Material.BEACON)
                            .name(messageSection.getSequence("hologram.frames")
                                    .map(frameSequence -> frameSequence.toStringList(StringColor.FORMATTER).orElse(List.of()).stream()
                                            .max(Comparator.comparingInt(String::length)))
                                    .orElse(messageSection.getString("hologram.text", StringColor.FORMATTER).asOptional())
                                    .orElse("not set"))
                            .onLeftClick((view, player) -> player.openMenu(new HologramMessageEditor(messageSection)));
            }

            buttons.add(messageButton.buildButton());
        }

        ButtonBuilder createMessageButton = Button.builder()
                .type(Material.LIME_DYE)
                .enchanted(true)
                .name("&2Create New Message")
                .onLeftClick((view, player) -> {
                    final MessageCreationMenu messageCreationMenu = new MessageCreationMenu(messageSequence.addEmptySection(), false);
                    player.openMenu(messageCreationMenu);
                });

        buttons.add(createMessageButton.buildButton());

        return buttons;
    }

}
